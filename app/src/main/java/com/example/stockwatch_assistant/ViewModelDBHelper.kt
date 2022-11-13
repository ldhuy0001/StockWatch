package com.example.stockwatch_assistant

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta
import com.google.firebase.firestore.FirebaseFirestore

import com.example.stockwatch_assistant.model.Stock
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//unused
class ViewModelDBHelper() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionRoot = "Favorites"

    private val db2: FirebaseFirestore= Firebase.firestore

//    private val viewModel: MainViewModel by viewModels()


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


//    private fun getStocks(){
//        db2.collection(collectionRoot)
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    Log.d(
//                        "read",
//                        "${document.id} => ${document.data}, ${document.data["stockName"]}"
//                    )
//                    val stock: StockMeta = StockMeta(
//                        symbol = document.data["stockSymbol"].toString(),
//                        name = document.data["stockName"].toString(),
//                        exchange = document.data["stockExchange"].toString()
//                    )
//
//                    if (document.data["userId"] == FirebaseAuth.getInstance().currentUser!!.uid) {
//                        if (!viewModel.isFavorite(stock)) {
//                            viewModel.addFavorite(stock)
//                            Log.d("XXX", "add from initial fetch")
//                        }
//                    }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w("read", "Error getting documents.", exception)
//            }
//
//    }

}