package com.example.stockwatch_assistant.alphaVantageAPI

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.text.clearSpans
//import com.google.gson.annotations.SerializedName

data class StockMeta(
//    symbol,name,exchange,assetType,ipoDate,delistingDate,status
    val symbol: String,
    val name: String,
    val exchange: String,
//    val assetType: String,
//    val ipoDate: String,
//    val delistingDate: String,
//    val status: String,
){
    companion object {
        private fun searchStockInfo(searchTerm: String, fulltext: String): Boolean {
            return fulltext.contains(searchTerm)
        }
    }

    fun searchFor(searchTerm: String):Boolean {
        val searchName = searchStockInfo(searchTerm,name)
        val searchSymbols = searchStockInfo(searchTerm,symbol)
        return searchName || searchSymbols
    }
}