package com.example.stockwatch_assistant.alphaVantageAPI

data class Portfolio (
    val portfolio: List<Symbol>,
    val percent_gain: Float
)
class Symbol (
    val symbol: String
)
