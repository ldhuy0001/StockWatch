package com.example.stockwatch_assistant

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta


import com.example.stockwatch_assistant.databinding.FragmentHomeBinding
import com.example.stockwatch_assistant.model.Stock
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
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
    private lateinit var auth: FirebaseAuth

    private var initialFetch = true
    private val changeNameFragment = ChangeNameFragment()

//    private val signInLauncher =
//        registerForActivityResult(FirebaseAuthUIActivityResultContract()){
//        }




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


        val signInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.updateUser()

                val user = FirebaseAuth.getInstance().currentUser!!.displayName
                viewModel.updateUserName(user!!)

                db.collection("Favorites")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d("read", "${document.id} => ${document.data}, ${document.data["stockName"]}")
                            val stock: StockMeta = StockMeta(
                                symbol = document.data["stockSymbol"].toString(),
                                name = document.data["stockName"].toString(),
                                exchange = document.data["stockExchange"].toString()
                            )
                            if(document.data["userId"] == FirebaseAuth.getInstance().currentUser!!.uid){
                                viewModel.addFavorite(stock)

                                Log.d("XXX", "add from siginInLauncher")
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("read", "Error getting documents.", exception)
                    }


            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.d("MainActivity", "sign in failed ${result}")
            }
        }


        fun showPopup(view: View) {
            val popup = PopupMenu(requireContext(), view)
            popup.inflate(R.menu.header_menu)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

                when (item!!.itemId) {
                    R.id.header1 -> {
                        Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
                        (activity as MainActivity).replaceFragment(changeNameFragment)

                    }
                    R.id.header2 -> {
                        Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.header3 -> {
                        Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()
                        AuthInit(viewModel, signInLauncher)
                        viewModel.emptyFavorite()
                    }
                }
                true
            })
            popup.show()

        }




        binding.logoutBut.setOnClickListener {
            showPopup(binding.logoutBut)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeUserName().observe(viewLifecycleOwner){
            binding.hello.text = "Hello $it! Welcome to StockWatch-Assistant!"
            Log.d("XXX", "userName: $it")

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

        Log.d("XXX", "$initialFetch")

        if (initialFetch) {
            db.collection("Favorites")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("read", "${document.id} => ${document.data}, ${document.data["stockName"]}")
                        val stock: StockMeta = StockMeta(
                            symbol = document.data["stockSymbol"].toString(),
                            name = document.data["stockName"].toString(),
                            exchange = document.data["stockExchange"].toString()
                        )
                        if(document.data["userId"] == FirebaseAuth.getInstance().currentUser!!.uid){
                            if(!viewModel.isFavorite(stock)){
                                viewModel.addFavorite(stock)
                                Log.d("XXX", "add from initial fetch")
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("read", "Error getting documents.", exception)
                }
        }
        initialFetch = false


    }




}