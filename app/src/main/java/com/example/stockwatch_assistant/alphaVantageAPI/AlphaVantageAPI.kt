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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.Type

interface AlphaVantageAPI {
//Example API call for all active stock listing. Unfortunately, api return CSV file instead of JSON
//https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=demo

    @GET("/query?function=LISTING_STATUS")
    suspend fun getAllActiveListingStocks (
        @Query("apikey") apikey: String = API_KEY_FOR_ALL_STOCKS
    ) : ResponseBody

//https://www.alphavantage.co/query?function=OVERVIEW&symbol=IBM&apikey=demo
    @GET("/query?function=OVERVIEW")
    suspend fun getStockDetailsFromAPI(
        @Query("symbol") symbol : String,
        @Query("apikey") apikey: String
        = when((System.currentTimeMillis()/1000%60/15).toString()){
            "0" -> API_KEY_FORINFO_0
            "1" -> API_KEY_FORINFO_1
            "2" -> API_KEY_FORINFO_2
            "3" -> API_KEY_FORINFO_3
            else -> ({}).toString()
        }
    ) : StockDetails

//https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY
    // &symbol=TSLA&datatype=csv&interval=5min&apikey=CUZFO32ID30TEUX6
    @GET("query?function=TIME_SERIES_INTRADAY")
//    @GET("query?function=TIME_SERIES_DAILY_ADJUSTED")
    suspend fun getStockGraphInfo(
        @Query("symbol") symbol: String,
        @Query("datatype") datatype : String = "csv",
        @Query("interval") interval : String = "5min",
        @Query("apikey") apikey: String
        = when((System.currentTimeMillis()/1000%60/15).toString()){
            "0" -> API_KEY_FORGRAPH_0
            "1" -> API_KEY_FORGRAPH_1
            "2" -> API_KEY_FORGRAPH_2
            "3" -> API_KEY_FORGRAPH_3
            else -> ({}).toString()
        }
    ) : ResponseBody

//https://www.alphavantage.co/query?function=NEWS_SENTIMENT&apikey=MY7UOXNLMVAJOBL6
    @GET("query?function=NEWS_SENTIMENT")
    suspend fun getGeneralNews (
        @Query("apikey") apikey: String = API_KEY_FOR_ALL_STOCKS
    ) : ListingFeed

//https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=AAPL&limit=10&apikey=MY7UOXNLMVAJOBL6
    @GET("query?function=NEWS_SENTIMENT")
    suspend fun getNewsForStock (
        @Query("tickers") tickers : String,
        @Query("limit") limit : String = "10",
        @Query("apikey") apikey: String
        = when((System.currentTimeMillis()/1000%60/15).toString()){
            "0" -> API_KEY_FORNEWS_0
            "1" -> API_KEY_FORNEWS_1
            "2" -> API_KEY_FORNEWS_2
            "3" -> API_KEY_FORNEWS_3
            else -> ({}).toString()
        }
    ) : ListingFeed

//https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=AAPL&limit=10&apikey=MY7UOXNLMVAJOBL6
    @GET("query?function=NEWS_SENTIMENT")
    suspend fun getNewsWithCategory (
        @Query("topics") topics : String,
        @Query("apikey") apikey: String = API_KEY_FOR_CATEGORY_NEWS
    ) : ListingFeed

    class ListingFeed(
        val feed: List<News>
    )

    companion object {
        const val API_KEY_FORINFO_0 = "GPO2P34U9Y7A7HZX"
        const val API_KEY_FORGRAPH_0 = "4MZVP7YPQKC4UCXM"
        const val API_KEY_FORNEWS_0 = "MY7UOXNLMVAJOBL6"

        const val API_KEY_FORINFO_1 = "CUZFO32ID30TEUX6"
        const val API_KEY_FORGRAPH_1 = "K5EMMM6BVQCN3JU5"
        const val API_KEY_FORNEWS_1 = "E55T5S14GV1NN2I3"

        const val API_KEY_FORINFO_2 = "QWWI53Q0A5XH00RC"
        const val API_KEY_FORGRAPH_2 = "5FG1ZTL7HT3NSBN9"
        const val API_KEY_FORNEWS_2 = "I6MIN9Q7N3UD1L8K"

        const val API_KEY_FORINFO_3 = "IMAOA9LVMDPN229X"
        const val API_KEY_FORGRAPH_3 = "8R2XN1FJNE462AU4"
        const val API_KEY_FORNEWS_3 = "C4VZ3V3IA7D3WXVI"

        const val API_KEY_FOR_ALL_STOCKS = "9UF22PMWEV9BPYJ9"
        const val API_KEY_FOR_CATEGORY_NEWS = "Z7B9H271PNLIOWHP"

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

