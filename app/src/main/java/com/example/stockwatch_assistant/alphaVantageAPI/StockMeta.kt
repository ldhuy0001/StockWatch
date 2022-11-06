package com.example.stockwatch_assistant.alphaVantageAPI

import android.text.SpannableString

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
    companion object{
        fun searchFor(searchTerm: String):Boolean{
//            search function work here
            return true
        }

        fun spannableStringsEqual(a: SpannableString?, b: SpannableString?): Boolean {
            if(a == null && b == null) return true
            if(a == null && b != null) return false
            if(a != null && b == null) return false
            val spA = a!!.getSpans(0, a.length, Any::class.java)
            val spB = b!!.getSpans(0, b.length, Any::class.java)
            return a.toString() == b.toString()
                    &&
                    spA.size == spB.size && spA.equals(spB)

        }
    }
}