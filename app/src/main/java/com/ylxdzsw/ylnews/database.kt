package com.ylxdzsw.ylnews

import android.content.Context
import java.security.MessageDigest

// a simple text format for saving news. Files are named as the hex of md5 of the corresponding url of the news.
// each file consists of either 3 or at least 4 lines, the first three lines are url, title and time
// the fourth line is thumb, which could be empty. Starts from the fifth line is the parsed content
class DataBase(val ctx: Context) {
    fun listInfo() = ctx.fileList().map { loadInfo(it) }.sortedByDescending { it.date }

    fun save(news: News) {
        ctx.openFileOutput(news.url.md5(), Context.MODE_PRIVATE).bufferedWriter().apply {
            write(news.url)
            newLine()
            write(news.title)
            newLine()
            write(news.date)
            newLine()
            write(news.thumb ?: "")
            newLine()
            if (news.content != null) {
                write(news.content)
            }
        }.close()
    }

    // load a news without content
    fun loadInfo(hash: String): News {
        ctx.openFileInput(hash).bufferedReader().use {
            val url = it.readLine().trim()
            val title = it.readLine().trim()
            val time = it.readLine().trim()
            val thumb = it.readLine().trim().run { if (isEmpty()) null else this }
            return News(url, title, time, thumb, null)
        }
    }

    // load the content of a news
    fun loadDetail(news: News): String? {
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
        val latest = ctx.fileList().sortedByDescending { loadInfo(it).date }.take(n).toSet()
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
