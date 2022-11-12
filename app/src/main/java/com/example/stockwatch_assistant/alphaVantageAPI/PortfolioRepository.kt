package com.example.stockwatch_assistant.alphaVantageAPI

import android.util.Log

class PortfolioRepository(private val alphaVantageAPI: AlphaVantageAPI) {
    suspend fun getPorfolio(season: String) : List<Portfolio>{
        val response = alphaVantageAPI.getTournamentPortfolio(season)
        Log.d("Porfolio","here is response on getPorfolio \n $response")
        Log.d("Porfolio","here is response on getPorfolio \n ${response.ranking}")

        return response.ranking
    }
}