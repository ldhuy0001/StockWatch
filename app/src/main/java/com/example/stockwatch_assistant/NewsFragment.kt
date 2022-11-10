package com.example.stockwatch_assistant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockwatch_assistant.databinding.FragmentNewsBinding

class NewsFragment: Fragment(R.layout.fragment_news) {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: NewsAdapter

    private var _binding: FragmentNewsBinding? = null
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
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NewsAdapter(viewModel = viewModel, context = requireContext())
        binding.rvNews.layoutManager = LinearLayoutManager(binding.rvNews.context)
        binding.rvNews.adapter = adapter

        initRecyclerViewDividers(binding.rvNews)

        viewModel.generalNewsLiveData.observe(requireActivity()){
                list -> adapter.submitList(list)
            Log.d("generalNews","Here is list in generalNews \n $list")
            adapter.notifyDataSetChanged()
        }

    }
}