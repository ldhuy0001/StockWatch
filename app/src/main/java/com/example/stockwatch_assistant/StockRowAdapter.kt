package com.example.stockwatch_assistant

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
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
import kotlinx.coroutines.delay

class StockRowAdapter(private val viewModel: MainViewModel, private val context: Context) :
    ListAdapter<StockMeta, StockRowAdapter.ViewHolder>(StockDiff()) {

    private val alphaVantageAPIForJSON = AlphaVantageAPI.createURLForJSON()
    private val stockDetailsRepository = StockDetailsRepository(alphaVantageAPIForJSON)

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
//            stockRowBinding.root.setOnClickListener {
//                viewModel.stockDetailsLiveData.observe(LifecycleOwner(), Observer {
//
//                })
//
//
//
//                var intent = Intent(context, OnePost::class.java)
//                var position = getPos(this)
////                var item = viewModel.redditPostsLiveData.value!![position]
////                var item = viewModel.stockMetaListLiveData.value!![position]
//                intent.putExtra("stockName", item.)
//                intent.putExtra("stockSymbol", item.symbol.toString())
////                intent.putExtra("thumbnail", item.thumbnailURL)
////                intent.putExtra("link", item.imageURL)
//                context.startActivity(intent)

//            stockRowBinding.root.setOnClickListener {
//                viewModel.stockDetailsLiveData.observe(holder.itemView.context as LifecycleOwner) {
//                    stockDetails = it
//
//
//                    var intent = Intent(context, OnePost::class.java)
////                var item = viewModel.redditPostsLiveData.value!![position]
////                var item = viewModel.stockMetaListLiveData.value!![position]
//                    intent.putExtra("stockName", stockDetails.name)
//                    intent.putExtra("stockSymbol", stockDetails.symbol)
////                intent.putExtra("thumbnail", item.thumbnailURL)
////                intent.putExtra("link", item.imageURL)
//                    context.startActivity(intent)


                }
//            }



//            }
        }
//    }

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
//            viewModel.netStockDetails(item.symbol)

            intent.putExtra("stockSymbol", item.symbol)
            context.startActivity(intent)

        }

        stockRowBinding.rowFav.setOnClickListener {

            val item = getItem(position)

//            val position = getPos(this)
//            val item = viewModel.redditPostsLiveData.value!![position]

            item.let {
                if (viewModel.isFavorite(it)) {
                    viewModel.removeFavorite(item)
                    stockRowBinding.rowFav.setImageResource(R.drawable.ic_baseline_add)
                } else {
                    viewModel.addFavorite(it)
                    stockRowBinding.rowFav.setImageResource(R.drawable.ic_baseline_check)
                }
                notifyItemChanged(position)
            }



        }

    }



    class StockDiff : DiffUtil.ItemCallback<StockMeta>() {
        //item identity
        override fun areItemsTheSame(oldItem: StockMeta, newItem: StockMeta): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        // Item contents are the same, but the object might have changed
        override fun areContentsTheSame(oldItem: StockMeta, newItem: StockMeta): Boolean {
            return oldItem.symbol == newItem.symbol
                    && oldItem.name == newItem.name
                    && oldItem.exchange == newItem.exchange


//            return StockMeta.spannableStringsEqual(oldItem.name, newItem.name) &&
//                    StockMeta.spannableStringsEqual(oldItem.exchange, newItem.exchange) &&
//                    StockMeta.spannableStringsEqual(oldItem.symbol, newItem.symbol)

        }
    }
}