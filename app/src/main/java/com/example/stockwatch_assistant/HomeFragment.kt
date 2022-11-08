package com.example.stockwatch_assistant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta


import com.example.stockwatch_assistant.databinding.FragmentHomeBinding
import com.example.stockwatch_assistant.model.Stock
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment: Fragment(R.layout.fragment_home) {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: StockRowAdapter

    val db = Firebase.firestore

//    private var _bindingHome: FragmentHomeBinding? = null
//    private val bindingHome get() = _bindingHome!!




    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        viewModel.observeUserName().observe(viewLifecycleOwner){
            binding.hello.text = "Hello $it! Welcome to StockWatch-Assistant!"
        }

        adapter = StockRowAdapter(viewModel, requireContext())
        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(binding.recyclerViewFavorite.context)
        binding.recyclerViewFavorite.adapter = adapter

        (activity as MainActivity).initRecyclerViewDividers(binding.recyclerViewFavorite)

        viewModel.favoritesListLiveData.observe(requireActivity()) {
                list -> adapter.submitList(list)
            Log.d("XXX", "list size: " + list.size)
            Log.d("XXX", "list: " + list)
            adapter.notifyDataSetChanged()
        }


        db.collection("Favorites")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("read", "${document.id} => ${document.data}, ${document.data["stockName"]}")



                    var test:StockMeta = StockMeta(symbol = document.data["stockSymbol"].toString(),
                        name = document.data["stockName"].toString(), exchange = document.data["stockExchange"].toString())

                    viewModel.addFavorite(test)


                }
            }
            .addOnFailureListener { exception ->
                Log.w("read", "Error getting documents.", exception)
            }



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        rowSubredditBinding.rowFav.setOnClickListener {
//            val position = getPos(this)
//            val item = viewModel.getFavoriteItem(position)
//            viewModel.removeFavorite(item)
//            rowSubredditBinding.rowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
//            notifyItemRemoved(position)
//        }






    }


}