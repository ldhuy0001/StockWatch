package com.example.stockwatch_assistant.alphaVantageAPI

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.reflect.Type

interface AlphaVantageAPI {
//Example API call for all active stock listing. Unfortunately, api return CSV file instead of JSON
//https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=demo

    @GET("/query?function=LISTING_STATUS")
    suspend fun getAllActiveListingStocks (
        @Query("apikey") apikey: String = API_KEY
    ) : ResponseBody

    companion object{
        const val API_KEY = "CUZFO32ID30TEUX6"
        const val BASE_URL = "https://alphavantage.co"

        var httpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("www.alphavantage.co")
            .build()

        fun create(): AlphaVantageAPI = create(httpUrl)

        private fun create(httpUrl: HttpUrl): AlphaVantageAPI{
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .build()
                .create(AlphaVantageAPI::class.java)
        }
    }
}
