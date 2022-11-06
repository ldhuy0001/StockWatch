package com.example.stockwatch_assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import android.content.Context
import android.util.Log
import com.example.stockwatch_assistant.alphaVantageAPI.AlphaVantageAPI
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta
import com.example.stockwatch_assistant.alphaVantageAPI.StockMetaRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel(){

    private lateinit var stockListFetchedFromAPI: List<StockMeta>

    private val alphaVantageApi = AlphaVantageAPI.create()
    private val stockMetaRepository = StockMetaRepository(alphaVantageApi)

    private var username = MutableLiveData("Empty!")



    private var stockMetaList: List<StockMeta> = emptyList()
    private var stockMeta = MutableLiveData<List<StockMeta>>()


    fun observeUserName(): LiveData<String>{
        return username
    }

    fun updateTest(){
        username.postValue(FirebaseAuth.getInstance().currentUser?.displayName)
    }

    fun netPosts() = viewModelScope.launch(
        context = viewModelScope.coroutineContext
            + Dispatchers.IO)  {

            stockListFetchedFromAPI = stockMetaRepository.getStocks()
//            stockMetaRepository.getStocks()
        Log.d("ck","here is stock list \n $stockListFetchedFromAPI")
    }


    fun setSearchTerm(s: String) {

        //example
//        var filteredList: List<RedditPost> = redditPostsList.filter {
//            it.searchFor(s)
//        }
//        Log.d("XXX", "$s search size: " + filteredList.size)
//        redditPosts.postValue(filteredList)

        var filteredList: List<StockMeta> =   stockMetaList.filter {
            it.searchFor(s)
        }
        Log.d("XXX", "$s search size: " + filteredList.size)
        stockMeta.postValue(filteredList)

    }



}