package com.ylxdzsw.ylnews

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.URL

data class News(val url: String, val title: String, val date: String,
                var thumb: String? = null, var content: String? = null) {
    fun hasDetail(): Boolean = content != null
}

class YLNewsActivity : AppCompatActivity() {
    lateinit var newsCache: DataBase

    private lateinit var appBarConfiguration: AppBarConfiguration

    var currentNews: News? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newsCache = DataBase(applicationContext)

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

class HomeFragment : Fragment() {
    private lateinit var newsListView: RecyclerView

    private val newsList = ArrayList<News>()
    private val imageCache = HashMap<String, Bitmap>()

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        updateInBackground()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        newsListView = root.findViewById(R.id.news_list)
        newsListView.layoutManager = LinearLayoutManager(context)
        newsListView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val v = LayoutInflater.from(context).inflate(R.layout.news_list_item, parent, false)
                return object : RecyclerView.ViewHolder(v) {}
            }

            override fun getItemCount(): Int = newsList.size

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                // TODO: inherit ViewHolder and save those things in properties?
                val v: TextView = holder.itemView.findViewById(R.id.news_list_item_title)
                val news = newsList[position]
                v.text = news.title

                val imageView = holder.itemView.findViewById<ImageView>(R.id.news_list_item_thumb)
                imageView.visibility = View.GONE

                news.thumb?.let { url ->
                    val bitmap = imageCache[url]
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap)
                        imageView.visibility = View.VISIBLE
                    } else {
                        doInBackground(this, {
                            try {
                                BitmapFactory.decodeStream(URL(news.thumb).openStream())
                            } catch (e: Throwable) { null }
                        }, {
                            // TODO: check if it is already bound to another news
                            if (it != null) {
                                imageCache[url] = it
                                imageView.setImageBitmap(it)
                                imageView.visibility = View.VISIBLE
                            }
                        })
                    }
                }

                holder.itemView.setOnClickListener {
                    (activity as YLNewsActivity).currentNews = newsList[position]
                    findNavController().navigate(R.id.action_show_detail)
                }
            }
        }

        return root
    }

    private fun updateInBackground() {
        // TODO: render at once
        for (source in Source.sources) {
            doInBackground(this, { source.fetch() }, { news ->
                news?.forEach { newsList.add(it) }
                newsList.sortByDescending { it.date } // TODO: proper binary searched insertion
                render()
            })
        }
    }

    private fun render() = newsListView.adapter!!.notifyDataSetChanged()
}

class DetailFragment : Fragment() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_detail, container, false)

        val webView: WebView = root.findViewById(R.id.webview_detail)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true

        val news = (activity as YLNewsActivity).currentNews!!
        // TODO: this bunch of code is ugly
        if (news.hasDetail()) {
            webView.loadData(news.content, "text/html", "utf8")
        } else {
            val newsCache = (activity as YLNewsActivity).newsCache
            try {
                newsCache.loadDetail(news)
            } catch (e: Throwable) {}
            if (news.hasDetail()) {
                webView.loadData(news.content, "text/html", "utf8")
            } else {
                doInBackground(this, {
                    Parser.parsers.find { it.match(news.url) }?.parse(news)
                    if (news.hasDetail()) {// found
                        newsCache.save(news)
                    }
                }, {
                    if (news.hasDetail()) {
                        webView.loadData(news.content, "text/html", "utf8")
                    } else {
                        webView.loadUrl(news.url)
                    }
                })
            }
        }

        (activity as YLNewsActivity).supportActionBar?.title = news.title

        return root
    }
}
