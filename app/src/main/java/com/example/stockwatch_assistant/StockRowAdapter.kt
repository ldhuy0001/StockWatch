package com.example.stockwatch_assistant

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stockwatch_assistant.alphaVantageAPI.AlphaVantageAPI
import com.example.stockwatch_assistant.alphaVantageAPI.StockDetails
import com.example.stockwatch_assistant.alphaVantageAPI.StockDetailsRepository
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta


import com.example.stockwatch_assistant.databinding.StockRowBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class StockRowAdapter(private val viewModel: MainViewModel, private val context: Context) :
    ListAdapter<StockMeta, StockRowAdapter.ViewHolder>(StockDiff()) {

    private val alphaVantageAPIForJSON = AlphaVantageAPI.createURLForJSON()
    private val stockDetailsRepository = StockDetailsRepository(alphaVantageAPIForJSON)
    private val FavoriteCollectionRef = Firebase.firestore.collection("Favorites")

//    private val reference = FirebaseFirestore.getInstance().getReference()
//    private lateinit var database: DatabaseReference
//    val database = Firebase.database.reference

    var db = FirebaseFirestore.getInstance()

    private fun getPos(holder: ViewHolder): Int {
        val position = holder.adapterPosition
        // notifyDataSetChanged was called, so position is not known
        if (position == RecyclerView.NO_POSITION) {
            return holder.layoutPosition
        }
        return position
    }


    // ViewHolder pattern holds row binding
    inner class ViewHolder(val stockRowBinding : StockRowBinding)
        : RecyclerView.ViewHolder(stockRowBinding.root) {
        init {
        }

        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val stockRowBinding = StockRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)

        return ViewHolder(stockRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val stockRowBinding = holder.stockRowBinding
        var intent = Intent(context, OnePost::class.java)

        stockRowBinding.stockRowName.text = item.name
        stockRowBinding.stockRowSymbol.text = item.symbol
        stockRowBinding.stockRowExchange.text = item.exchange

        stockRowBinding.stockRoot.setOnClickListener {

            intent.putExtra("stockSymbol", item.symbol)
            intent.putExtra("stockName", item.name)
            context.startActivity(intent)

        }

        if (FirebaseAuth.getInstance().currentUser == null) {
            stockRowBinding.rowFav.visibility = View.INVISIBLE
        }

        stockRowBinding.rowFav.setOnClickListener {

//            val item = getItem(position)
//            FirebaseAuth.AuthStateListener {
//                Log.d("FirebaseAuth","it ==========\n $it")
//                Log.d("FirebaseAuth","it.curUser ==========\n ${it.currentUser}")
//            }
            Log.d("FirebaseAuth","cur User ==========\n ${FirebaseAuth.getInstance().currentUser}")
            if (FirebaseAuth.getInstance().currentUser != null) {
                item.let {
                    Log.d("isFav", "before click: ${viewModel.isFavorite(it)}")
                    if (viewModel.isFavorite(it)) {
                        viewModel.removeFavorite(it)
//                    stockRowBinding.rowFav.setImageResource(R.drawable.ic_baseline_check)
                        Log.d("isFav", "removeItem")


                        db.collection("Favorites")
                            .whereEqualTo("stockExchange", item.exchange)
                            .whereEqualTo("stockName", item.name)
                            .whereEqualTo("stockSymbol", item.symbol)
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    Log.d("query data", "${document.id} => ${document.data}")

                                    db.collection("Favorites")
                                        .document(document.id)
                                        .delete()
                                        .addOnSuccessListener {
                                            Log.d("delete", "fav delete success")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.d("delete", "fav deleting FAILED ")
                                        }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w("query data", "Error query data.", exception)
                            }
                    } else {

//                    stockRowBinding.rowFav.setImageResource(R.drawable.ic_baseline_add)
                        Log.d("isFav", "addItem")

                        if (!viewModel.isFavorite(item)) {
                            stockRowBinding.rowFav.setImageResource(R.drawable.ic_baseline_check)
                        } else {
                            stockRowBinding.rowFav.setImageResource(R.drawable.ic_baseline_add)
                        }
                        viewModel.addFavorite(it)

                        val uid = FirebaseAuth.getInstance().currentUser!!.uid
                        Log.d("isFav", "uid: $uid")

                        val docid = db.collection("Favorites").document().id
                        Log.d("isFav", "docid: $docid")

                        val user: MutableMap<String, Any> = HashMap()
                        user["userName"] = viewModel.observeUserName().value!!
                        user["userId"] = uid
                        user["docId"] = docid
                        user["stockName"] = item.name
                        user["stockSymbol"] = item.symbol
                        user["stockExchange"] = item.exchange

                        db.collection("Favorites")
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    "add",
                                    "DocumentSnapshot added with ID: " + documentReference.id
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w("add", "Error adding document", e)
                            }
                    }
                    notifyItemChanged(position)
                    Log.d("isFav", "after click: ${viewModel.isFavorite(it)}")
                }
            } else {
                Snackbar.make(stockRowBinding.stockRoot,"Please Log in to add Favorite Stock", 1000).show()
            }
        }
        if (viewModel.isFavorite(item)) {
            stockRowBinding.rowFav.setImageResource(R.drawable.ic_baseline_check)
        } else {
            stockRowBinding.rowFav.setImageResource(R.drawable.ic_baseline_add)
        }
    }



    class StockDiff : DiffUtil.ItemCallback<StockMeta>() {
        //item identity
        override fun areItemsTheSame(oldItem: StockMeta, newItem: StockMeta): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        // Item contents are the same, but the object might have changed
        override fun areContentsTheSame(oldItem: StockMeta, newItem: StockMeta): Boolean {
            return return oldItem == newItem
            return oldItem.symbol == newItem.symbol
                    && oldItem.name == newItem.name
                    && oldItem.exchange == newItem.exchange
        }
    }
}