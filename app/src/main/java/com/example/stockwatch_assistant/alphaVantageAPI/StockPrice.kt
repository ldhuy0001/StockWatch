package com.example.stockwatch_assistant.alphaVantageAPI

data class StockPrice (
    val timeStamp: String,
    val open: String,
    val high: String,
    val low: String,
    val close: String,
    val volume: String
)