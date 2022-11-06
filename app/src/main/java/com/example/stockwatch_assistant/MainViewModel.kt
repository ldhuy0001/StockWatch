package com.example.stockwatch_assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import android.content.Context
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import com.example.stockwatch_assistant.alphaVantageAPI.AlphaVantageAPI
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta
import com.example.stockwatch_assistant.alphaVantageAPI.StockMetaRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel(){

    private lateinit var stockListFetchedFromAPI: List<StockMeta>

    private val alphaVantageApi = AlphaVantageAPI.create()
    private val stockMetaRepository = StockMetaRepository(alphaVantageApi)

    private var username = MutableLiveData("Empty!")

//Create LiveData for stockRow
    private var stockMetaList = MediatorLiveData<List<StockMeta>>()
    val stockMetaListLiveData: LiveData<List<StockMeta>>
        get() = stockMetaList


    fun observeUserName(): LiveData<String>{
        return username
    }

    fun updateTest(){
        username.postValue(FirebaseAuth.getInstance().currentUser?.displayName)
    }

//Fetch data all stock from Alpha Vantage API
    fun netPosts() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
            + Dispatchers.IO)  {
            stockListFetchedFromAPI = stockMetaRepository.getStocks()
        stockMetaList.postValue(stockListFetchedFromAPI)
        Log.d("ck","here is stock list \n $stockListFetchedFromAPI")
    }

//Need to store list of all stocks in local storage

//searchStock
    fun searchStock(searchTerm : String):Boolean {
        var searchListResult: List<StockMeta> = stockListFetchedFromAPI.filter{
            it.searchFor(searchTerm)
        }
        stockMetaList.postValue(searchListResult)
        return searchListResult.isEmpty()
    }

}