package com.example.stockwatch_assistant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.stockwatch_assistant.alphaVantageAPI.AlphaVantageAPI
import com.example.stockwatch_assistant.alphaVantageAPI.News
import com.example.stockwatch_assistant.alphaVantageAPI.NewsRepository
import com.example.stockwatch_assistant.databinding.NewsBinding

class NewsAdpater(private val viewModel: MainViewModel, private val context: Context) :
    ListAdapter<News, NewsAdpater.ViewHolder>(StockDiff()) {

    private val alphaVantageAPIForJSON = AlphaVantageAPI.createURLForJSON()
    private val newsRepository = NewsRepository(alphaVantageAPIForJSON)

    inner class ViewHolder(val newsBinding: NewsBinding)
        : RecyclerView.ViewHolder(newsBinding.root) {
        init {
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val newsBinding = NewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(newsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val newsBinding = holder.newsBinding

        newsBinding.title.text = item.title
//        newsBinding.author.text = item.authors[0]
        newsBinding.source.text = item.source
        newsBinding.timePublished.text = item.time_published
//        newsBinding.bannerImage
        newsBinding.summary.text = item.summary
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
                    && oldItem.time_published == newItem.time_published
                    && oldItem.authors == newItem.authors
                    && oldItem.summary == newItem.summary
                    && oldItem.banner_image == newItem.banner_image
                    && oldItem.source == newItem.source
        }
    }

}