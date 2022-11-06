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
}