package com.example.stockwatch_assistant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta


import com.example.stockwatch_assistant.databinding.FragmentRvBinding
import com.example.stockwatch_assistant.databinding.SongRowBinding

class StockRowAdapter(private val viewModel: MainViewModel, private val context: Context) :
    ListAdapter<StockMeta, StockRowAdapter.ViewHolder>(StockDiff()) {


    //added
    private var _binding: FragmentRvBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!


    // ViewHolder pattern holds row binding
    inner class ViewHolder(val songRowBinding : SongRowBinding)
        : RecyclerView.ViewHolder(songRowBinding.root) {
        init {
            //XXX Write me.

            //set on-click listener
            songRowBinding.root.setOnClickListener {
//            val selected = "You selected $position ${getItem(position).name}"
//            Snackbar.make(it, selected, Snackbar.LENGTH_LONG).show()
                clickListener(songRowBinding.root, getItem(bindingAdapterPosition).name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //XXX Write me.
        val songBinding = SongRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(songBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //XXX Write me.

        //get the item position
        val item = getItem(position)
        val songBinding = holder.songRowBinding
        // change the text and time
        songBinding.stockRow_name.text = item.name
        songBinding.stockRow_symbol.text = item.symbol
        songBinding.stockRow_exchange.text = item.exchange
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