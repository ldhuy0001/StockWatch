package com.example.stockwatch_assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import com.example.stockwatch_assistant.alphaVantageAPI.*
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel(){

    private lateinit var stockListFetchedFromAPI: List<StockMeta>

//All CSV list here
    private val alphaVantageApiForCSV = AlphaVantageAPI.createURLForCSV()
    private val stockMetaRepository = StockMetaRepository(alphaVantageApiForCSV)
    private val stockPriceRepository = StockPriceRepository(alphaVantageApiForCSV)

//    private lateinit var stockDetailFetchedFromAPI: LiveData<StockDetails>
    private val alphaVantageAPIForJSON = AlphaVantageAPI.createURLForJSON()
    private val stockDetailsRepository = StockDetailsRepository(alphaVantageAPIForJSON)
    private val stockNewsRepository = NewsRepository(alphaVantageAPIForJSON)


    private var username = MutableLiveData("Empty!")

//Create LiveData for stockRow
    private var stockMetaList = MediatorLiveData<List<StockMeta>>()
    val stockMetaListLiveData: LiveData<List<StockMeta>>
        get() = stockMetaList

//Create LiveData for stockDetail
    private var stockDetails = MutableLiveData<StockDetails>()
    val stockDetailsLiveData : LiveData<StockDetails>
        get() = stockDetails

//Create LiveData for stockRow
    private var stockPriceList = MediatorLiveData<List<StockPrice>>()
    val stockPriceListLiveData: LiveData<List<StockPrice>>
        get() = stockPriceList

//Create LiveData for GeneralNews
    private var generalNews = MutableLiveData<List<News>>()
    val generalNewsLiveData : LiveData<List<News>>
        get() = generalNews

//Create LiveData for StockNews
    private var stockNews = MutableLiveData<List<News>>()
    val stockNewsLiveData : LiveData<List<News>>
        get() = stockNews

//favorites
    private var fList: MutableList<StockMeta> = mutableListOf()
    private var favoritesListMutableLiveData = MutableLiveData<List<StockMeta>>()
    val favoritesListLiveData: LiveData<List<StockMeta>>
        get() = favoritesListMutableLiveData

    //firebase
    private val dbHelp = ViewModelDBHelper()

    private var firebaseAuthLiveData = FirestoreAuthLiveData()

    fun fetchStockMeta() {
//        dbHelp.fetchInitialStocks(favoritesListMutableLiveData)
    }

    fun updateUserName(string:String){
        username.value = string
    }


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

//Fetch Stock Details for OnePost
    fun netStockDetails(symbol : String) = viewModelScope.launch (
        context = viewModelScope.coroutineContext
                + Dispatchers.IO ) {
        stockDetails.postValue(stockDetailsRepository.getStockDetails(symbol))
//        delay(10000L)
        Log.d("ck","here is stock details =========== \n $stockDetails")
        Log.d("ck", "here is stock details value =========== \n ${stockDetails.value}")
    }

//Fetch Stock Price for graph
    fun netStockPrice(symbol: String) = viewModelScope.launch (
        context = viewModelScope.coroutineContext
            + Dispatchers.IO){
        stockPriceList.postValue(stockPriceRepository.getStockPrice(symbol))
    Log.d("stockPrice","here is stock details =========== \n $stockPriceList")
    Log.d("testchart", "here is stock details value =========== \n ${stockPriceList.value}")
    }

//Fetch General News
    fun netGeneralNews() = viewModelScope.launch (
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        generalNews.postValue(stockNewsRepository.getGeneralNews())
    }

//Fetch News With Category
    fun netNewsWithCategory(category: String) = viewModelScope.launch (
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        generalNews.postValue(stockNewsRepository.getNewsWithCategory(category))
    }

//Fetch Stock News
    fun netStockNews(symbol: String) = viewModelScope.launch (
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        stockNews.postValue(stockNewsRepository.getStockNews(symbol))

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



    fun emptyFavorite() {
        fList.clear()
        favoritesListMutableLiveData.postValue(fList)


    }


    fun updateUser() {
        firebaseAuthLiveData.updateUser()
    }


}