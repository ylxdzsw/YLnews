package com.ylxdzsw.ylnews

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
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
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class News(val url: String, val title: String, val date: String,
                var thumb: String? = null, var content: String? = null) {
    fun hasDetail(): Boolean = content != null
}

class YLNewsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

class HomeFragment : Fragment() {
    private lateinit var newsListView: RecyclerView

    private val newsList = ArrayList<News>()

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
                v.text = newsList[position].title
                holder.itemView.setOnClickListener {
                    findNavController().navigate(R.id.action_show_detail)
                }
            }
        }

        if (newsList.isEmpty()) {
            updateInBackground()
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.textview_home_second).text = "fuck"
    }
}
