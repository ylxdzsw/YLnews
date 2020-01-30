package com.ylxdzsw.ylnews

abstract class Parser {
    companion object {
        val parsers = arrayOf<Parser>(
            object : Parser() {
                val site = "http://www.hnyanling.gov.cn/"

                override fun match(url: String): Boolean = url.startsWith(site)
                override fun parse(news: News) {
                    val doc = fetchDocument(news.url) ?: return

                    val bc = doc.selectFirst(".b").children()
                    val title = bc[0].text()
                    val info = bc[1].children()
                        .map { it.text() }
                        .filter { it.contains("来源") || it.contains("作者") || it.contains("时间") }
                        .joinToString(" ")
                    val video = bc[2].select("video").map { it.attr("src").absoluteURL(site) }
                    val image = bc[2].select("img").map { it.attr("src").absoluteURL(site) }
                    val text = bc[2].select("p").map { it.text() }.filter { it.length > 1 }.dropLast(1)

                    if (image.isNotEmpty()) {
                        news.thumb = image[0]
                    }

                    news.content = genPage(title, info, video, image, text)
                }
            }
        )
    }
    abstract fun match(url: String): Boolean
    abstract fun parse(news: News)
}

private fun genPage(title: String, info: String, video: Iterable<String>, image: Iterable<String>, text: Iterable<String>) = """
    <title>$title</title>
    <style>
        html { width: calc(100vw - 4px); margin: 0 0 0 0; padding: 0 2px; overflow-x: hidden }
        #title { font-size: 22px }
        #info { color: #444; font-size: 14px }
        p { font-size: 16px }
        img,p,video { max-width: 100%; margin: .5em 0 }
    </style>
    <h1 id="title">$title</h1> 
    <p id="info">$info</p>
    ${ video.joinToString("\n") { """<video src="$it" controls></video>""" } }
    ${ image.joinToString("\n") { """<img src="$it"></img>""" } }
    ${ text.joinToString("\n") { "<p>$it</p>" } }
""".trimIndent()
