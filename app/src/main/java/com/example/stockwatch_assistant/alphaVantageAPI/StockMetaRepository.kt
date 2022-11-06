package com.example.stockwatch_assistant.alphaVantageAPI

import android.util.Log
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
