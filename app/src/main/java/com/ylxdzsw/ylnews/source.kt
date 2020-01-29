package com.ylxdzsw.ylnews

import android.os.AsyncTask
import java.lang.ref.WeakReference

abstract class Source {
    companion object {
        val sources = arrayOf(
            object : Source() {
                override fun fetch(): Iterable<News>? = fetchGovText("http://www.hnyanling.gov.cn/c14904")
            },
            object : Source() {
                override fun fetch(): Iterable<News>? = fetchGovText("http://www.hnyanling.gov.cn/c14905")
            },
            object : Source() {
                override fun fetch(): Iterable<News>? = fetchGovText("http://www.hnyanling.gov.cn/c14907")
            },
            object : Source() {
                override fun fetch(): Iterable<News>? = fetchGovVideo("http://www.hnyanling.gov.cn/c15108")
            },
            object : Source() {
                override fun fetch(): Iterable<News>? = fetchGovText("http://www.hnyanling.gov.cn/c14909")
            }
        )
    }
    abstract fun fetch(): Iterable<News>?
}

private fun fetchGovText(url: String): Iterable<News>? {
    val doc = fetchDocument(url) ?: return null

    return doc.selectFirst(".c > ul").children().map {
        News(
            url = it.attr("href").absoluteURL("http://www.hnyanling.gov.cn"),
            title = it.selectFirst("i").text(),
            date = it.selectFirst("em").text()
        )
    }
}

private fun fetchGovVideo(url: String): Iterable<News>? {
    val doc = fetchDocument(url) ?: return null

    return doc.selectFirst(".video-c > ul").children().map {
        News(
            url = it.attr("href").absoluteURL("http://www.hnyanling.gov.cn"),
            title = it.selectFirst("i").text(),
            date = it.selectFirst("em").text(),
            thumb = it.selectFirst("img").attr("src").absoluteURL("http://www.hnyanling.gov.cn")
        )
    }
}

fun<T> getFromAllSourceAsync(receiver: T, callback: T.(Iterable<News>) -> Unit ) {
    object : AsyncTask<String, Unit, Iterable<News>>() {
        private val ref = WeakReference<T>(receiver)
        override fun doInBackground(vararg params: String?): Iterable<News> {
            val result = ArrayList<News>()
            for (source in Source.sources) { // TODO: parallel
                source.fetch()?.forEach { result.add(it) }
            }
            return result
        }

        override fun onPostExecute(result: Iterable<News>) {
            super.onPostExecute(result)
            ref.get()?.apply { callback(result) }
        }
    }.execute()
}
