package com.example.stockwatch_assistant.alphaVantageAPI

import android.text.SpannableString
import android.util.Log
import com.google.gson.GsonBuilder
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.InputStreamReader

class StockMetaRepository(private val alphaVantageAPI: AlphaVantageAPI) {

   suspend fun getStocks(): List<StockMeta> {
       val response = alphaVantageAPI.getAllActiveListingStocks()
       Log.d("ck","here is response $response")
       Log.d("ck","here is response in byteStream \n" +
               " ${response.byteStream()}")
       return unpackPosts(response)
    }

    private suspend fun unpackPosts(responseBody: ResponseBody): List<StockMeta>{
        val csvReader = CSVReader(InputStreamReader(responseBody.byteStream()))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val symbol = line.getOrNull(0)
                    val name = line.getOrNull(1)
                    val exchange = line.getOrNull(2)
                    StockMeta(
                        name = name ?: return@mapNotNull null,
                        symbol = symbol ?: return@mapNotNull null,
                        exchange = exchange ?: return@mapNotNull null
                    )
                }
                .also {
                    csvReader.close()
                }
        }
    }

}

//class RedditPostRepository(private val redditApi: RedditApi) {
//    // NB: This is for our testing.
//    val gson = GsonBuilder().registerTypeAdapter(
//        SpannableString::class.java, RedditApi.SpannableDeserializer()
//    ).create()
//
//    private fun unpackPosts(response: RedditApi.ListingResponse): List<RedditPost> {
//        // TODO XXX Write me.
//        val postResponse = response.data.children.map {
//            it.data
//        }
////        Log.d("ck","postResponse $postResponse")
//        return postResponse
//    }
//
//    suspend fun getPosts(subreddit: String): List<RedditPost> {
//        if (MainActivity.globalDebug) {
//            val response = gson.fromJson(
//                MainActivity.jsonAww100,
//                RedditApi.ListingResponse::class.java)
//            return unpackPosts(response)
//        }
//        else {
//            // TODO XXX Write me.
//            val reddit = redditApi.get100Posts(subreddit)
//            return unpackPosts(reddit)
//        }
//    }
//
//    suspend fun getSubreddits(): List<RedditPost> {
//        if (MainActivity.globalDebug) {
//            val response = gson.fromJson(
//                MainActivity.subreddit1,
//                RedditApi.ListingResponse::class.java)
//            return unpackPosts(response)
//        } else {
//            // TODO XXX Write me.
//            val reddit = redditApi.getSubReddit()
//            return unpackPosts(reddit)
//        }
//    }
//}
