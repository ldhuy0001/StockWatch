package com.example.stockwatch_assistant.alphaVantageAPI

import android.util.Log
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import org.json.JSONTokener
import java.io.InputStreamReader

class StockDetailsRepository(private val alphaVantageAPI: AlphaVantageAPI) {

     suspend fun getStockDetails(symbol: String) : StockDetails {
        var response : StockDetails = alphaVantageAPI.getStockDetailsFromAPI(symbol)
//        Log.d("ck","here is alphaVantageAPI.getStockDetailsFromAPI() \n ${alphaVantageAPI.getStockDetailsFromAPI()}")
        Log.d("ck","here is response \n $response")
//        delay(5000L)
        return response
    }
}

//https://www.alphavantage.co/query?function=OVERVIEW&symbol=IBM&apikey=CUZFO32ID30TEUX6
//https://www.alphavantage.co/query?function=OVERVIEW&symbol=AAAU&apikey=CUZFO32ID30TEUX6