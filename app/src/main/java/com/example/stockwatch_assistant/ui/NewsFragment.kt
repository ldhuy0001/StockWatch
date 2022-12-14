package com.example.stockwatch_assistant.ui

import android.graphics.Color
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
import com.example.stockwatch_assistant.ui.adapter.NewsAdapter
import com.example.stockwatch_assistant.R
import com.example.stockwatch_assistant.databinding.FragmentNewsBinding
import com.google.android.material.snackbar.Snackbar

class NewsFragment: Fragment(R.layout.fragment_news) {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: NewsAdapter

    private var _binding: FragmentNewsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


//    private var actionBarBinding: ActionBarBinding? = null

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
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NewsAdapter(viewModel = viewModel, context = requireContext())
        binding.rvNews.layoutManager = LinearLayoutManager(binding.rvNews.context)
        binding.rvNews.adapter = adapter

//        initRecyclerViewDividers(binding.rvNews)

        viewModel.generalNewsLiveData.observe(viewLifecycleOwner){
//            if (!it.isNullOrEmpty()) {
                adapter.submitList(it)
                binding.indeterminateBar.visibility = View.GONE
                if(it.isNullOrEmpty())
                    Snackbar.make(binding.searchBar,"No news found",500).show()
//            }
//                list -> adapter.submitList(list)
            Log.d("generalNews","observe called, Here is list in generalNews \n $it")
//            Log.d("generalNews","news list size: ${list.size}")
//            adapter.notifyDataSetChanged()
        }






        binding.btnTechnology.setOnClickListener {
            viewModel.netNewsWithCategory("technology")
            binding.btnTechnology.setBackgroundColor(Color.parseColor("#808080"))
            binding.btnLifeSciences.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnFinance.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnManufacturing.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnEconomy.setBackgroundColor(Color.parseColor("#FF6200EE"))
        }
        binding.btnLifeSciences.setOnClickListener {
            viewModel.netNewsWithCategory("life_sciences")
            binding.btnTechnology.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnLifeSciences.setBackgroundColor(Color.parseColor("#808080"))
            binding.btnFinance.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnManufacturing.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnEconomy.setBackgroundColor(Color.parseColor("#FF6200EE"))
        }
        binding.btnFinance.setOnClickListener {
            viewModel.netNewsWithCategory("finance")
            binding.btnTechnology.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnLifeSciences.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnFinance.setBackgroundColor(Color.parseColor("#808080"))
            binding.btnManufacturing.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnEconomy.setBackgroundColor(Color.parseColor("#FF6200EE"))
        }
        binding.btnManufacturing.setOnClickListener {
            viewModel.netNewsWithCategory("manufacturing")
            binding.btnTechnology.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnLifeSciences.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnFinance.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnManufacturing.setBackgroundColor(Color.parseColor("#808080"))
            binding.btnEconomy.setBackgroundColor(Color.parseColor("#FF6200EE"))
        }
        binding.btnEconomy.setOnClickListener {
            viewModel.netNewsWithCategory("economy_fiscal")
            binding.btnTechnology.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnLifeSciences.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnFinance.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnManufacturing.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.btnEconomy.setBackgroundColor(Color.parseColor("#808080"))
        }

        binding.searchBar.queryHint = "Search News"

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchBar.clearFocus()
                viewModel.searchNews(query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.toString().isEmpty())
                {
                    (activity as MainActivity).hideKeyboard()
//                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//                    actionBar?.hide()
//                    val actionBar: ActionBar = getSupportActionBar() //Get action bar reference
//                    actionBar.hide()

//                    (activity as MainActivity?)?.actionBar?.hide()

                }
                viewModel.searchNews(newText.toString())
                return false
            }
        })
    }
}