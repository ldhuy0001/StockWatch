package com.example.stockwatch_assistant.alphaVantageAPI

import android.util.Log

class NewsRepository(private val alphaVantageAPI: AlphaVantageAPI) {

    suspend fun getGeneralNews(): List<News>{
        val response = alphaVantageAPI.getGeneralNews()

        Log.d("ck","here is response on getGeneralNews \n $response")
        Log.d("ck","here is response on getGeneralNews \n ${response.feed}")

        return response.feed
    }

    suspend fun getStockNews(symbol: String): List<News>{
        val response = alphaVantageAPI.getNewsForStock(symbol)

        Log.d("ck","here is response on getStockNews \n $response")
        Log.d("ck","here is response on getStockNews \n ${response.feed}")

        return response.feed
    }

    suspend fun getNewsWithCategory(category: String): List<News>{
        val response = alphaVantageAPI.getNewsWithCategory(category)

        Log.d("getNewsWithCategory","here is response on getNewsWithCategory \n $response")
        Log.d("getNewsWithCategory","here is response on getNewsWithCategory \n ${response.feed}")

        return response.feed
    }

}