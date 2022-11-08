package com.example.stockwatch_assistant.alphaVantageAPI

import com.google.gson.annotations.SerializedName

data class StockDetails(
    @SerializedName("Symbol")
    val symbol: String,
    @SerializedName("Description")
    val description: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Country")
    val country: String,
    @SerializedName("Sector")
    val sector: String,
    @SerializedName("Industry")
    val industry: String,

    @SerializedName("PERatio")
    val peRatio: String,
    @SerializedName("DividendYield")
    val dividendYield: String,
    @SerializedName("52WeekHigh")
    val weekHigh52: String,
    @SerializedName("52WeekLow")
    val weekLow52: String,
)