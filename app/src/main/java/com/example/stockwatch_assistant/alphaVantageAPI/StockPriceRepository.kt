package com.example.stockwatch_assistant.alphaVantageAPI

import android.util.Log
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.InputStreamReader

class StockPriceRepository(private val alphaVantageAPI: AlphaVantageAPI) {

    suspend fun getStockPriceInMin(symbol: String): List<StockPriceInMin>{
        val response = alphaVantageAPI.getStockPriceInMin(symbol)
        Log.d(
            "priceInMin", "here is response in byteStream \n" +
                    " ${response.byteStream()}"
        )
        return unpackStockPriceInMin(response)
    }

    suspend fun unpackStockPriceInMin(response: ResponseBody) : List<StockPriceInMin>{
        val csvReader = CSVReader(InputStreamReader(response.byteStream()))
        return withContext(Dispatchers.IO){
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line->
                    val timestamp = line.getOrNull(0)
                    val midPrice = line.getOrNull(1)
                    StockPriceInMin(
                        timestamp = timestamp?: return@mapNotNull null,
                        midPrice = midPrice?: return@mapNotNull null
                    )
                }
                .also {
                    csvReader.close()
                }
        }
    }

    suspend fun getStockPrice(symbol : String) : List<StockPrice> {
        val response = alphaVantageAPI.getStockGraphInfo(symbol)
        Log.d("ck", "here is response $response")
        Log.d(
            "ck", "here is response in byteStream \n" +
                    " ${response.byteStream()}"
        )
        return unpackPosts(response)
    }

    private suspend fun unpackPosts(response: ResponseBody): List<StockPrice>{
        val csvReader = CSVReader(InputStreamReader(response.byteStream()))
        return withContext(Dispatchers.IO){
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val timeStamp = line.getOrNull(0)
                    val open = line.getOrNull(1)
                    val high = line.getOrNull(2)
                    val low = line.getOrNull(3)
                    val close = line.getOrNull(4)
                    val volume = line.getOrNull(6)
                    StockPrice(
                        timeStamp = timeStamp ?: return@mapNotNull null,
                        open =  open ?: return@mapNotNull null,
                        high = high ?: return@mapNotNull null,
                        low = low ?: return@mapNotNull null,
                        close = close ?: return@mapNotNull null,
                        volume = volume ?: return@mapNotNull null
                    )
                }
                .also {
                    csvReader.close()
                }
        }
    }
}