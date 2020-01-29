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
                    val source = bc[1].children()[0].text().apply { drop(indexOf('：')) }
                    val date = bc[1].children()[1].text().apply { drop(indexOf('：')) }
                    val video = bc[2].select("video").map { it.attr("src").absoluteURL(site) }
                    val image = bc[2].select("img").map { it.attr("src").absoluteURL(site) }
                    val text = bc[2].select("p").map { it.text() }.filter { it.length > 1 }.dropLast(1)

                    if (image.isNotEmpty()) {
                        news.thumb = image[0]
                    }

                    news.content = genPage(title, "$source $date", image, video, text)
                }
            }
        )
    }
    abstract fun match(url: String): Boolean
    abstract fun parse(news: News)
}

private fun genPage(title: String, info: String, image: Iterable<String>, video: Iterable<String>, text: Iterable<String>) = """
    <title>$title</title>
    ${ video.joinToString("\n") { """<video src="$it" controls></video>""" } }
    ${ image.joinToString("\n") { """<img src="$it"></img>""" } }
    ${ text.joinToString("\n") { "<p>$it</p>" } }
""".trimIndent()
