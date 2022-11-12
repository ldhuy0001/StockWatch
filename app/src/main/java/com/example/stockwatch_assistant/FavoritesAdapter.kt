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
import com.example.stockwatch_assistant.alphaVantageAPI.Portfolio
import com.example.stockwatch_assistant.alphaVantageAPI.StockDetailsRepository
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta
import com.example.stockwatch_assistant.databinding.StockRowBinding


import com.example.stockwatch_assistant.databinding.PortoRowBinding

//unused
class PortofolioAdapter(private val viewModel: MainViewModel, private val context: Context) :
    ListAdapter<Portfolio, PortofolioAdapter.ViewHolder>(PortoDiff()) {

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
    inner class ViewHolder(val portoRowBinding : PortoRowBinding)
        : RecyclerView.ViewHolder(portoRowBinding.root) {
        init {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val portoRowBinding = PortoRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(portoRowBinding)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val portoRowBinding = holder.portoRowBinding

//        portoRowBinding.stockRowName.text = item.portfolio
//

    }


    class PortoDiff : DiffUtil.ItemCallback<Portfolio>() {
        override fun areItemsTheSame(oldItem: Portfolio, newItem: Portfolio): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Portfolio, newItem: Portfolio): Boolean {
            return oldItem== newItem
        }
    }


}