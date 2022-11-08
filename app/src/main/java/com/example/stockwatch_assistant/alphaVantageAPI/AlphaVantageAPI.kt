package com.example.stockwatch_assistant.alphaVantageAPI

import android.text.SpannableString
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
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

    //https://www.alphavantage.co/query?function=OVERVIEW&symbol=IBM&apikey=demo
    @GET("/query?function=OVERVIEW")
    suspend fun getStockDetailsFromAPI(
        @Query("symbol") symbol : String,
        @Query("apikey") apikey: String = API_KEY
    ) : StockDetails

    companion object {
//        const val API_KEY = "CUZFO32ID30TEUX6"
        const val API_KEY = "K5EMMM6BVQCN3JU5"
        
        const val BASE_URL = "https://alphavantage.co"

        var httpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("www.alphavantage.co")
            .build()


        //create unique url for csv file
        fun createURLForCSV(): AlphaVantageAPI = createURLForCSV(httpUrl)

        private fun createURLForCSV(httpUrl: HttpUrl): AlphaVantageAPI {
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


        //create unique url for JSON
        fun createURLForJSON(): AlphaVantageAPI = createURLForJSON(httpUrl)
        private fun createURLForJSON(httpUrl: HttpUrl): AlphaVantageAPI {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    // Enable basic HTTP logging to help with debugging.
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(buildGsonConverterFactory())
                .build()
                .create(AlphaVantageAPI::class.java)
        }

        // Tell Gson to use our SpannableString deserializer
        private fun buildGsonConverterFactory(): GsonConverterFactory {
            val gsonBuilder = GsonBuilder().registerTypeAdapter(
                SpannableString::class.java, SpannableDeserializer()
            )
            return GsonConverterFactory.create(gsonBuilder.create())
        }

        // This class allows Retrofit to parse items in our model of type
        // SpannableString.  Note, given the amount of "work" we do to
        // enable this behavior, one can argue that Retrofit is a bit...."simple."
        class SpannableDeserializer : JsonDeserializer<SpannableString> {
            // @Throws(JsonParseException::class)
            override fun deserialize(
                json: JsonElement,
                typeOfT: Type,
                context: JsonDeserializationContext
            ): SpannableString {
                return SpannableString(json.asString)
            }
        }
    }
}

