package com.example.stockwatch_assistant

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stockwatch_assistant.alphaVantageAPI.AlphaVantageAPI
import com.example.stockwatch_assistant.alphaVantageAPI.StockDetailsRepository
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta
import com.example.stockwatch_assistant.databinding.StockRowBinding

class FavoritesAdapter(private val viewModel: MainViewModel, private val context: Context) :
    ListAdapter<StockMeta, FavoritesAdapter.ViewHolder>(StockDiff()) {

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
            stockRowBinding.rowFav.setOnClickListener {
                val position = getPos(this)
                val item = viewModel.getFavoriteItem(position)
                viewModel.removeFavorite(item)
                stockRowBinding.rowFav.setImageResource(R.drawable.ic_baseline_check)
                notifyItemRemoved(position)
            }
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
            context.startActivity(intent)
        }
        stockRowBinding.rowFav.setImageResource(R.drawable.ic_baseline_check)
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


        }
    }


}