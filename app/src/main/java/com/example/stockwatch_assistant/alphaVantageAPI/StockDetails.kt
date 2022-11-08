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
    @SerializedName("Industry")
    val industry: String
)