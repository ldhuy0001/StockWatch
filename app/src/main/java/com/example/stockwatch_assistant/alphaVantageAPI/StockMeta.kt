package com.example.stockwatch_assistant.alphaVantageAPI

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
    }
}