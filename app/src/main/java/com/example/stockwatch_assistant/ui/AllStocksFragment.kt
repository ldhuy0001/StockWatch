package com.example.stockwatch_assistant.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockwatch_assistant.MainViewModel
import com.example.stockwatch_assistant.R
import com.example.stockwatch_assistant.ui.adapter.StockRowAdapter
import com.example.stockwatch_assistant.databinding.FragmentAllStocksBinding
import com.google.android.material.snackbar.Snackbar

//import com.example.stockwatch_assistant.ui.MainActivity.hideKeyboard

//import package com.example.stockwatch_assistant.*

class AllStocksFragment: Fragment(R.layout.fragment_all_stocks) {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: StockRowAdapter

    private var _binding: FragmentAllStocksBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun initRecyclerViewDividers(rv: RecyclerView) {
        // Let's have dividers between list items
        val dividerItemDecoration = DividerItemDecoration(
            rv.context, LinearLayoutManager.VERTICAL )
        rv.addItemDecoration(dividerItemDecoration)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllStocksBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = StockRowAdapter(viewModel = viewModel, context = requireContext(), false)
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.recyclerView.context)
        binding.recyclerView.adapter = adapter

        initRecyclerViewDividers(binding.recyclerView)

        viewModel.stockMetaListLiveData.observe(viewLifecycleOwner){

//            if (!it.isNullOrEmpty()) adapter.submitList(it)
            adapter.submitList(it)
            if(it.isNullOrEmpty())
                Snackbar.make(binding.searchBar,"No stock found",500).show()
//                list -> adapter.submitList(list)
            //no need
//            adapter.notifyDataSetChanged()
            Log.d("XXX", "data changed")
            if (it.isNotEmpty()) {
                binding.indeterminateBar.visibility = View.GONE
            }
        }

        binding.searchBar.queryHint = "Search Stocks"

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

//                (activity as MainActivity).hideBottomNavBar()

                binding.searchBar.clearFocus()
                return viewModel.searchStock(query.toString())

//                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

//                (activity as MainActivity).hideBottomNavBar()

                if (newText.toString().isEmpty())
            {
                (activity as MainActivity).hideKeyboard()
            }
                return viewModel.searchStock(newText.toString())
//                return false
            }
        })
    }
}