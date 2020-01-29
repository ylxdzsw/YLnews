package com.ylxdzsw.ylnews

abstract class Parser {
    companion object {
        val parsers = arrayOf<Parser>(
            object : Parser() {
                override fun match(url: String): Boolean = url.startsWith("http://www.hnyanling.gov.cn/")
                override fun parse(news: News) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
        )
    }
    abstract fun match(url: String): Boolean
    abstract fun parse(news: News)
}
