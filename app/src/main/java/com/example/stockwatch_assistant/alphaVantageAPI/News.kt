package com.example.stockwatch_assistant.alphaVantageAPI

import com.google.gson.annotations.SerializedName

data class News(
//    @SerializedName("title")
    val title: String,
//    @SerializedName("url")
    val url: String,
//    @SerializedName("time_published")
    val time_published: String,
//    @SerializedName("authors")
    val authors: List<String>,
//    @SerializedName("summary")
    val summary: String,
//    @SerializedName("banner_image")
    val banner_image: String,
//    @SerializedName("source")
    val source: String,
)
{
    companion object {
        private fun searchNewsInfo(searchTerm: String, fulltext: String): Int{
            return fulltext.indexOf(searchTerm, ignoreCase = true)
        }
    }

    fun searchFor(searchTerm: String):Boolean {
        val searchTitle = searchNewsInfo(searchTerm,title)
        val searchSummary = searchNewsInfo(searchTerm,summary)
        return searchTitle != -1 || searchSummary != -1
    }
}
