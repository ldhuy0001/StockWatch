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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel(){

    private lateinit var stockListFetchedFromAPI: MutableList<StockMeta>
    private var stockListOnlyNASDAQandNYSE: MutableList<StockMeta> = mutableListOf()

    //for search
    private  var stockNewsList: List<News> = listOf()

//All CSV list here
    private val alphaVantageApiForCSV = AlphaVantageAPI.createURLForCSV()
    private val stockMetaRepository = StockMetaRepository(alphaVantageApiForCSV)
    private val stockPriceRepository = StockPriceRepository(alphaVantageApiForCSV)

//    private lateinit var stockDetailFetchedFromAPI: LiveData<StockDetails>
    private val alphaVantageAPIForJSON = AlphaVantageAPI.createURLForJSON()
    private val stockDetailsRepository = StockDetailsRepository(alphaVantageAPIForJSON)
    private val stockNewsRepository = NewsRepository(alphaVantageAPIForJSON)
    private val portfolioRepository = PortfolioRepository(alphaVantageAPIForJSON)

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

//Create LiveData for Portfolio
    private var portfolio = MutableLiveData<List<Portfolio>>()
    val portfolioLiveData : LiveData<List<Portfolio>>
        get() = portfolio

//Create LiveData for isUserLoggedIn
    private var userLoggedInLiveData = MutableLiveData<Boolean>()

    fun setUserLoggedIn(s: Boolean){
        userLoggedInLiveData.value = s
    }

    fun observeUserLoggedIn(): LiveData<Boolean>{
        return userLoggedInLiveData
    }

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
            stockListFetchedFromAPI = stockMetaRepository.getStocks().toMutableList()

            for (i in stockListFetchedFromAPI){
                if (i.exchange == "NASDAQ" || i.exchange == "NYSE")
//                    stockListFetchedFromAPI.remove(i)
//                Log.d("netPosts","here is stock detail \n $i")
                  stockListOnlyNASDAQandNYSE.add(i)
            }

        stockMetaList.postValue(stockListOnlyNASDAQandNYSE)
        Log.d("ck","here is stock list \n $stockListOnlyNASDAQandNYSE")
    }

//Need to store list of all stocks in local storage

//searchStock
    fun searchStock(searchTerm : String):Boolean {
        var searchListResult: List<StockMeta> = stockListOnlyNASDAQandNYSE.filter{
            it.searchFor(searchTerm)
        }
        Log.d("searchStock", "searchListResult ===== \n$searchListResult")
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
        stockNewsList = stockNewsRepository.getGeneralNews()
        generalNews.postValue(stockNewsList)
    }

//Fetch News With Category
    fun netNewsWithCategory(category: String) = viewModelScope.launch (
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        stockNewsList = stockNewsRepository.getNewsWithCategory(category)
        generalNews.postValue(stockNewsList)
    }

//Fetch Stock News
    fun netStockNews(symbol: String) = viewModelScope.launch (
        context = viewModelScope.coroutineContext
                + Dispatchers.IO) {
        stockNews.postValue(stockNewsRepository.getStockNews(symbol))
    }

//searchNews
    fun searchNews(searchTerm : String):Boolean {
        if(!stockNewsList.isNullOrEmpty()) {
            var searchListResult: List<News> = stockNewsList.filter {
                it.searchFor(searchTerm)
            }
            generalNews.postValue(searchListResult)
            return searchListResult.isEmpty()
        }
        return false
    }

//Fetch Portfolio
    fun netPortfolio(season: String) = viewModelScope.launch (
    context = viewModelScope.coroutineContext
            + Dispatchers.IO) {
        portfolio.postValue(portfolioRepository.getPorfolio(season))
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