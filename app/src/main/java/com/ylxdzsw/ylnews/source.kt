package com.ylxdzsw.ylnews

import org.jsoup.Jsoup

abstract class Source {
    companion object {
        val sources = arrayOf(
            object : Source() {
                override fun fetch(): Iterable<News>? = fetch_hnyanling("http://www.hnyanling.gov.cn/c14904")
            },
            object : Source() {
                override fun fetch(): Iterable<News>? = fetch_hnyanling("http://www.hnyanling.gov.cn/c14905")
            },
            object : Source() {
                override fun fetch(): Iterable<News>? = fetch_hnyanling("http://www.hnyanling.gov.cn/c14907")
            },
            object : Source() {
                override fun fetch(): Iterable<News>? = fetch_hnyanling("http://www.hnyanling.gov.cn/c14908")
            },
            object : Source() {
                override fun fetch(): Iterable<News>? = fetch_hnyanling("http://www.hnyanling.gov.cn/c14909")
            }
        )
    }
    abstract fun fetch(): Iterable<News>?
}

private fun fetch_hnyanling(url: String): Iterable<News>? {
    TODO()
}
