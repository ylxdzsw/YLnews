package com.ylxdzsw.ylnews

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException
import java.util.concurrent.Executors

fun fetchDocument(url: String, retry: Int = 2, timeout: Int = 10000): Document? = if (retry <= 0) {
    null
} else {
    try {
        Jsoup.connect(url).timeout(timeout).get()
    } catch (e: SocketTimeoutException) {
        fetchDocument(url, retry - 1, timeout * 2)
    } catch (e: Throwable) {
        null
    }
}

fun String.absoluteURL(base: String) = when {
    startsWith("http") -> ""
    startsWith("//") -> "http:"
    else -> base
} + this

val executor = Executors.newCachedThreadPool()!!
fun<T, R> doInBackground(receiver: T, task: () -> R, callback: T.(R) -> Unit) {
    object : AsyncTask<Unit, Unit, R>() {
        private val ref = WeakReference<T>(receiver)
        override fun doInBackground(vararg params: Unit?): R = task()
        override fun onPostExecute(result: R) = ref.get()?.run { callback(result) } ?: Unit
    }.executeOnExecutor(executor)
}
