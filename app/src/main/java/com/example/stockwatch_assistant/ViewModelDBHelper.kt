package com.example.stockwatch_assistant

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

import com.example.stockwatch_assistant.model.Stock

//unused
class ViewModelDBHelper() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionRoot = "allNotes"


    fun fetchInitialStocks(notesList: MutableLiveData<List<Stock>>) {
        dbFetchStocks(notesList)
    }

    private fun dbFetchStocks(stocksList: MutableLiveData<List<Stock>>) {
        db.collection(collectionRoot)
            .orderBy("timeStamp")//, Query.Direction.DESCENDING)
            .limit(100)
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "allNotes fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                stocksList.postValue(result.documents.mapNotNull {
                    it.toObject(Stock::class.java)
                })
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "allNotes fetch FAILED ", it)
            }
    }




}