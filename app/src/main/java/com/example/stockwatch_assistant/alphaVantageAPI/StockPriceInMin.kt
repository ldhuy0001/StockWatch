package com.example.stockwatch_assistant.alphaVantageAPI

data class StockPriceInMin(
//    val date: String,
//    val time: Int,
    val timestamp: String,
    val midPrice: String
)

data class StockPriceInMinTransfer(
    val date: String,
    val time: Int,
    val midPrice: Int
)