package com.example.stockwatch_assistant

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stockwatch_assistant.alphaVantageAPI.AlphaVantageAPI
import com.example.stockwatch_assistant.alphaVantageAPI.News
import com.example.stockwatch_assistant.alphaVantageAPI.NewsRepository
import com.example.stockwatch_assistant.databinding.NewsRowBinding
import java.time.Month

//unused
class NewsOneStockAdapter(private val viewModel: MainViewModel, private val context: Context) :
    ListAdapter<News, NewsOneStockAdapter.ViewHolder>(StockDiff()) {

    private val alphaVantageAPIForJSON = AlphaVantageAPI.createURLForJSON()
    private val newsRepository = NewsRepository(alphaVantageAPIForJSON)

    inner class ViewHolder(val newsBinding: NewsRowBinding) :
        RecyclerView.ViewHolder(newsBinding.root) {
        init {
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val newsBinding = NewsRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(newsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val newsBinding = holder.newsBinding

    }


    class StockDiff : DiffUtil.ItemCallback<News>() {
        //item identity
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        // Item contents are the same, but the object might have changed
        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.title == newItem.title
                    && oldItem.url == newItem.url
        }
    }
}
