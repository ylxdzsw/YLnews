package com.ylxdzsw.ylnews

import android.content.Context
import java.security.MessageDigest
import java.time.Instant

data class NewsInfo(val url: String, val title: String, val time: Long, val thumb: String)
fun NewsInfo(url: String, title: String) = NewsInfo(url, title, Instant.now().epochSecond, "")

class DataBase(val ctx: Context) {
    fun listInfo() = ctx.fileList().map { loadInfo(it) }.sortedByDescending { it.time }

    fun saveInfo(news: NewsInfo) {
        ctx.openFileOutput(news.url.md5(), Context.MODE_PRIVATE).bufferedWriter().apply {
            write(news.url)
            newLine()
            write(news.title)
            newLine()
            write(news.time.toString())
            newLine()
            write(news.thumb)
            newLine()
        }.close()
    }

    fun loadInfo(hash: String): NewsInfo {
        ctx.openFileInput(hash).bufferedReader().use {
            val url = it.readLine().trim()
            val title = it.readLine().trim()
            val time = it.readLine().trim().toLong()
            val thumb = it.readLine().trim()
            return NewsInfo(url, title, time, thumb)
        }
    }

    fun saveDetail(news: NewsInfo, detail: String) {
        ctx.openFileOutput(news.url.md5(), Context.MODE_APPEND).writer().use {
            it.write(detail)
        }
    }

    fun loadDetail(news: NewsInfo): String {
        ctx.openFileInput(news.url.md5()).bufferedReader().use {
            // discard the first 4 lines
            for (i in 0..3) {
                it.readLine()
            }
            return it.readText()
        }
    }

    fun vacuum() {
        TODO()
    }

    fun clear() {
        ctx.fileList().forEach { ctx.deleteFile(it) }
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
