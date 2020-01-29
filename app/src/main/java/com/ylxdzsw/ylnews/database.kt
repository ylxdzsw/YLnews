package com.ylxdzsw.ylnews

import android.content.Context
import java.security.MessageDigest

// a simple text format for saving news. Files are named as the hex of md5 of the corresponding url of the news.
// each file consists of either 3 or at least 4 lines, the first three lines are url, title and time
// the fourth line is thumb, which could be empty. Starts from the fifth line is the parsed content
class DataBase(val ctx: Context) {
    fun listInfo() = ctx.fileList().map { loadInfo(it) }.sortedByDescending { it.time }

    // save the url, title and time of a news
    fun saveInfo(news: News) {
        ctx.openFileOutput(news.url.md5(), Context.MODE_PRIVATE).bufferedWriter().apply {
            write(news.url)
            newLine()
            write(news.title)
            newLine()
            write(news.time)
            newLine()
        }.close()
    }

    // load the url, title and time of a news
    fun loadInfo(hash: String): News {
        ctx.openFileInput(hash).bufferedReader().use {
            val url = it.readLine().trim()
            val title = it.readLine().trim()
            val time = it.readLine().trim()
            return News(url, title, time, null, null)
        }
    }

    // save the thumb (could be empty) and content of a news
    fun saveDetail(news: News, detail: String) {
        ctx.openFileOutput(news.url.md5(), Context.MODE_APPEND).writer().use {
            it.write(detail)
        }
    }

    // load the thumb (could be empty) and content of a news
    fun loadDetail(news: News): String {
        ctx.openFileInput(news.url.md5()).bufferedReader().use {
            // discard the first 4 lines
            for (i in 0..3) {
                it.readLine()
            }
            return it.readText()
        }
    }

    // remove news to keep only [n] latest
    fun vacuum(n: Int) {
        val latest = ctx.fileList().sortedByDescending { loadInfo(it).time }.take(n).toSet()
        ctx.fileList().filter { it in latest }.forEach { ctx.deleteFile(it) }
    }
}

fun String.md5(): String {
    val bytes = this.toByteArray()
    val digest = MessageDigest.getInstance("MD5").apply { update(bytes) }.digest()

    return StringBuffer().apply {
        for (i in digest) {
            append(Integer.toHexString(i.toInt()))
        }
    }.toString()
}
