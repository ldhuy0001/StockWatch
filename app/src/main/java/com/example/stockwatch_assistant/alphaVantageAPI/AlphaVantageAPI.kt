package com.example.stockwatch_assistant.alphaVantageAPI

import android.text.SpannableString
import android.util.Log
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
        @Query("apikey") apikey: String = findAPIKeyAt(count++%numberOfAPIKEY)
    ) : ResponseBody

//https://www.alphavantage.co/query?function=OVERVIEW&symbol=IBM&apikey=demo
    @GET("/query?function=OVERVIEW")
    suspend fun getStockDetailsFromAPI(
        @Query("symbol") symbol : String,
        @Query("apikey") apikey: String = findAPIKeyAt(count++%numberOfAPIKEY)
    ) : StockDetails

//https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY
    // &symbol=TSLA&datatype=csv&interval=5min&apikey=CUZFO32ID30TEUX6
    @GET("query?function=TIME_SERIES_INTRADAY")
//    @GET("query?function=TIME_SERIES_DAILY_ADJUSTED")
    suspend fun getStockGraphInfo(
        @Query("symbol") symbol: String,
        @Query("datatype") datatype : String = "csv",
        @Query("interval") interval : String = "5min",
        @Query("apikey") apikey: String = findAPIKeyAt(count++%numberOfAPIKEY)
    ) : ResponseBody

//https://www.alphavantage.co/query?function=NEWS_SENTIMENT&apikey=MY7UOXNLMVAJOBL6
    @GET("query?function=NEWS_SENTIMENT")
    suspend fun getGeneralNews (
        @Query("apikey") apikey: String = findAPIKeyAt(count++%numberOfAPIKEY)
    ) : ListingFeed

//https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=AAPL&limit=10&apikey=MY7UOXNLMVAJOBL6
    @GET("query?function=NEWS_SENTIMENT")
    suspend fun getNewsForStock (
        @Query("tickers") tickers : String,
        @Query("limit") limit : String = "10",
        @Query("apikey") apikey: String = findAPIKeyAt(count++%numberOfAPIKEY)
    ) : ListingFeed

//https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=AAPL&limit=10&apikey=MY7UOXNLMVAJOBL6
    @GET("query?function=NEWS_SENTIMENT")
    suspend fun getNewsWithCategory (
        @Query("topics") topics : String,
        @Query("apikey") apikey: String = findAPIKeyAt(count++%numberOfAPIKEY)
//            API_KEY_FOR_CATEGORY_NEWS
    ) : ListingFeed

    class ListingFeed(
        val feed: List<News>
    )

    companion object {
//        const val API_KEY_FORINFO_0 = "GPO2P34U9Y7A7HZX"
//        const val API_KEY_FORGRAPH_0 = "4MZVP7YPQKC4UCXM"
//        const val API_KEY_FORNEWS_0 = "MY7UOXNLMVAJOBL6"
//
//        const val API_KEY_FORINFO_1 = "CUZFO32ID30TEUX6"
//        const val API_KEY_FORGRAPH_1 = "K5EMMM6BVQCN3JU5"
//        const val API_KEY_FORNEWS_1 = "E55T5S14GV1NN2I3"
//
//        const val API_KEY_FORINFO_2 = "QWWI53Q0A5XH00RC"
//        const val API_KEY_FORGRAPH_2 = "5FG1ZTL7HT3NSBN9"
//        const val API_KEY_FORNEWS_2 = "I6MIN9Q7N3UD1L8K"
//
//        const val API_KEY_FORINFO_3 = "IMAOA9LVMDPN229X"
//        const val API_KEY_FORGRAPH_3 = "8R2XN1FJNE462AU4"
//        const val API_KEY_FORNEWS_3 = "C4VZ3V3IA7D3WXVI"
//
//        const val API_KEY_FOR_ALL_STOCKS = "9UF22PMWEV9BPYJ9"
//        const val API_KEY_FOR_CATEGORY_NEWS = "Z7B9H271PNLIOWHP"

        var count = (0..34).random()
        var numberOfAPIKEY = 35

        const val BASE_URL = "https://alphavantage.co"

        var httpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("www.alphavantage.co")
            .build()

        fun findAPIKeyAt(count: Int) :String{
            Log.d("count","count ================ $count")
            when (count) {
                //old
//                0 -> return "GPO2P34U9Y7A7HZX"
//                1 -> return "4MZVP7YPQKC4UCXM"
//                2 -> return "MY7UOXNLMVAJOBL6"
//                3 -> return "CUZFO32ID30TEUX6"
//                4 -> return "K5EMMM6BVQCN3JU5"
//                5 -> return "E55T5S14GV1NN2I3"
//                6 -> return "QWWI53Q0A5XH00RC"
//                7 -> return "5FG1ZTL7HT3NSBN9"
//                8 -> return "I6MIN9Q7N3UD1L8K"
//                9 -> return "IMAOA9LVMDPN229X"
//                10-> return "8R2XN1FJNE462AU4"
//                11-> return "C4VZ3V3IA7D3WXVI"
//                12-> return "9UF22PMWEV9BPYJ9"
//                13-> return "Z7B9H271PNLIOWHP"
//                14-> return "YO8GWZOX68E9XL6V"

                //new
//                0 -> return "AO48IFCXLA3BX1O9"
//                1 -> return "WA7BKM5HXI56RLF6"
//                2 -> return "THYSGY8VEFDN4N57"
//                3 -> return "7II03ZCFLUAXQ3I9"
//                4 -> return "ZNN7KWS78SORDNHP"
//                5 -> return "46SD6DKCB9GS2DRX"
////                66SHOES29PMPXSH8
//                6 -> return "J3O650830XSJ5OPD"
//                7 -> return "5FG1ZTL7HT3NSBN9"
//                8 -> return "PCPMGEIC3XN6PRBG"
//                9 -> return "LNIVEQWGJNJN7GXL"
//                10-> return "M1PNUXBG56P9VBOV"
//                11-> return "LPFYE1HD2ULBRY3T"
//                12-> return "F7P66V0VOQ5TFH8N"
//                13-> return "EZSFOHAVM93TJZVJ"
//                14-> return "HQ1XJSI9C7T9ZQGK"
//                15-> return "28OX6WFDGCY93HBH"
//                16-> return "2GM3WMNE96JNFKK6"
//                17-> return "XTV5KHDB5AL1MAQV"
//                18-> return "ZLWW2UUXX1M2LSHE"

                0 -> return "AO48IFCXLA3BX1O9"
                1 -> return "WA7BKM5HXI56RLF6"
                2 -> return "THYSGY8VEFDN4N57"
                3 -> return "7II03ZCFLUAXQ3I9"
                4 -> return "ZNN7KWS78SORDNHP"
                5 -> return "46SD6DKCB9GS2DRX"
                6 -> return "J3O650830XSJ5OPD"
                7 -> return "5FG1ZTL7HT3NSBN9"
                8 -> return "PCPMGEIC3XN6PRBG"
                9 -> return "LNIVEQWGJNJN7GXL"
                10-> return "M1PNUXBG56P9VBOV"
                11-> return "LPFYE1HD2ULBRY3T"
                12-> return "F7P66V0VOQ5TFH8N"
                13-> return "EZSFOHAVM93TJZVJ"
                14-> return "HQ1XJSI9C7T9ZQGK"
                15-> return "28OX6WFDGCY93HBH"
                16-> return "2GM3WMNE96JNFKK6"
                17-> return "XTV5KHDB5AL1MAQV"
                18-> return "ZLWW2UUXX1M2LSHE"
                19-> return "66SHOES29PMPXSH8"
                20-> return "GPO2P34U9Y7A7HZX"
                21-> return "4MZVP7YPQKC4UCXM"
                22-> return "MY7UOXNLMVAJOBL6"
                23-> return "CUZFO32ID30TEUX6"
                24-> return "K5EMMM6BVQCN3JU5"
                25-> return "E55T5S14GV1NN2I3"
                26-> return "QWWI53Q0A5XH00RC"
                27-> return "5FG1ZTL7HT3NSBN9"
                28-> return "I6MIN9Q7N3UD1L8K"
                29-> return "IMAOA9LVMDPN229X"
                30-> return "8R2XN1FJNE462AU4"
                31-> return "C4VZ3V3IA7D3WXVI"
                32-> return "9UF22PMWEV9BPYJ9"
                33-> return "Z7B9H271PNLIOWHP"
                34-> return "YO8GWZOX68E9XL6V"

                else -> return ""
            }

        }



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

