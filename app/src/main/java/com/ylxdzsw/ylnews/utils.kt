package com.ylxdzsw.ylnews

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.SocketTimeoutException

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
