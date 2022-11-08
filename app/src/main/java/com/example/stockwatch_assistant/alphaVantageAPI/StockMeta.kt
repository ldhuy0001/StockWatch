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
    companion object {
        private fun searchStockInfo(searchTerm: String, fulltext: String): Int{
            return fulltext.indexOf(searchTerm, ignoreCase = true)
        }
    }

    fun searchFor(searchTerm: String):Boolean {
        val searchName = searchStockInfo(searchTerm,name)
        val searchSymbols = searchStockInfo(searchTerm,symbol)
        return searchName != -1 || searchSymbols != -1
    }
}