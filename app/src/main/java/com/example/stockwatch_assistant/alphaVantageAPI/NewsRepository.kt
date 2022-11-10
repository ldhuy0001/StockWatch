package com.example.stockwatch_assistant.alphaVantageAPI

import android.util.Log

class NewsRepository(private val alphaVantageAPI: AlphaVantageAPI) {

    suspend fun getGeneralNews(): List<News>{
        val response = alphaVantageAPI.getGeneralNews()

        Log.d("ck","here is response on getGeneralNews \n $response")
        Log.d("ck","here is response on getGeneralNews \n ${response.feed}")

        return response.feed
    }

}