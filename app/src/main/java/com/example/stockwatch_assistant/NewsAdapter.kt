package com.example.stockwatch_assistant

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.stockwatch_assistant.alphaVantageAPI.AlphaVantageAPI
import com.example.stockwatch_assistant.alphaVantageAPI.News
import com.example.stockwatch_assistant.alphaVantageAPI.NewsRepository
import com.example.stockwatch_assistant.databinding.NewsRowBinding
import java.time.Month

class NewsAdapter(private val viewModel: MainViewModel, private val context: Context) :
    ListAdapter<News, NewsAdapter.ViewHolder>(NewsDiff()) {

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

        newsBinding.title.text = item.title
        newsBinding.source.text = item.source

        var originalTime = item.time_published

        originalTime!!.substring(0, 8)

        val year = originalTime.substring(0, 4)
        val month =
            Month.of(originalTime.substring(4, 6).toInt()).toString().lowercase().capitalize()
        val date = originalTime.substring(6, 8)

        newsBinding.timePublished.text = "$month $date, $year"
        newsBinding.summary.text = item.summary

        val url = item.banner_image

        if (!url.isNullOrEmpty()) {
            Glide.with(context)
                .load(url)
                .into(newsBinding.bannerImage)
        } else {
            newsBinding.bannerImage.visibility = View.GONE

            val param = newsBinding.title.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(32, 8, 8, 0)
            newsBinding.title.layoutParams = param
        }

        newsBinding.root.setOnClickListener {
            val intent = Intent(context, OneNews::class.java)

            intent.putExtra("stockNewsURL", item.url)
            intent.putExtra("stockNewsTitle", item.title)

            context.startActivity(intent)
        }
    }

    class NewsDiff : DiffUtil.ItemCallback<News>() {
        //item identity
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        // Item contents are the same, but the object might have changed
        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
//            return oldItem.title == newItem.title
//                    && oldItem.url == newItem.url
//                    && oldItem.time_published == newItem.time_published
//                    && oldItem.authors == newItem.authors
//                    && oldItem.summary == newItem.summary
//                    && oldItem.banner_image == newItem.banner_image
//                    && oldItem.source == newItem.source
        }
    }
}
