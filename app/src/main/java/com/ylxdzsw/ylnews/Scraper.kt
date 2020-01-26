package com.ylxdzsw.ylnews

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object Scraper {
    const val SITE = "http://www.hnyanling.gov.cn"

    fun onSite(url: String) = url.startsWith(SITE)

    fun fetchPage(url: String) = Jsoup.connect(url).get()

    fun fetchNewsList() {

    }
}