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
import com.example.stockwatch_assistant.alphaVantageAPI.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class MainViewModel : ViewModel(){

    private lateinit var stockListFetchedFromAPI: List<StockMeta>
    private val alphaVantageApiForCSV = AlphaVantageAPI.createURLForCSV()
    private val stockMetaRepository = StockMetaRepository(alphaVantageApiForCSV)

//    private lateinit var stockDetailFetchedFromAPI: LiveData<StockDetails>
    private val alphaVantageAPIForJSON = AlphaVantageAPI.createURLForJSON()
    private val stockDetailsRepository = StockDetailsRepository(alphaVantageAPIForJSON)


    private var username = MutableLiveData("Empty!")

//Create LiveData for stockRow
    private var stockMetaList = MediatorLiveData<List<StockMeta>>()
    val stockMetaListLiveData: LiveData<List<StockMeta>>
        get() = stockMetaList

//Create LiveData for stockDetail
    private var stockDetails = MutableLiveData<StockDetails>()
    val stockDetailsLiveData : LiveData<StockDetails>
        get() = stockDetails


    private var fList: MutableList<StockMeta> = mutableListOf()
    private var favoritesListMutableLiveData = MutableLiveData<List<StockMeta>>()
    val favoritesListLiveData: LiveData<List<StockMeta>>
        get() = favoritesListMutableLiveData

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

//Fetch stock details from API
//    fun netStockDetails()  = viewModelScope.launch (
//        context = viewModelScope.coroutineContext
//                + Dispatchers.IO ) : StockDetails {
//        stockDetails.postValue(stockDetailsRepository.getStockDetails())
//        Log.d("ck","here is stock details =========== \n $stockDetails")
//        Log.d("ck", "here is stock details value =========== \n ${stockDetails.value}")
//    return stockDetails.value
//    }

    fun netStockDetails(symbol : String) = viewModelScope.launch (
        context = viewModelScope.coroutineContext
                + Dispatchers.IO ) {
        stockDetails.postValue(stockDetailsRepository.getStockDetails(symbol))
//        delay(10000L)
        Log.d("ck","here is stock details =========== \n $stockDetails")
        Log.d("ck", "here is stock details value =========== \n ${stockDetails.value}")
    }


    //favorites stuff
    fun isFavorite(item: StockMeta): Boolean {
        return fList.contains(item)
    }

    fun addFavorite(item: StockMeta) {
        fList.add(item)
        favoritesListMutableLiveData.postValue(fList)
    }

    fun removeFavorite(item: StockMeta) {
        fList.remove(item)
        favoritesListMutableLiveData.postValue(fList)
    }

    fun getFavoriteItem(position: Int): StockMeta {
        return fList[position]
    }


}