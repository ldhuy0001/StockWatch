package com.example.stockwatch_assistant

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stockwatch_assistant.alphaVantageAPI.Portfolio
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta


import com.example.stockwatch_assistant.databinding.FragmentHomeBinding
import com.example.stockwatch_assistant.model.Stock
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch

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
    private val changeThemeFragment = ChangeThemeFragment()

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.portfolioLiveData.observe(this){
            if (it.isNotEmpty()) {
                binding.rank1?.text = "Rank 1: \n" + createStrForPortfolio(it[0])
                binding.rank1Gain?.text = String.format("Gain: %.2f", it[0].percent_gain) + "%"

                binding.rank2?.text = "Rank 2: \n" + createStrForPortfolio(it[1])
                binding.rank2Gain?.text = String.format("Gain: %.2f", it[1].percent_gain) + "%"

                binding.rank3?.text = "Rank 3: \n" + createStrForPortfolio(it[2])
                binding.rank3Gain?.text = String.format("Gain: %.2f", it[2].percent_gain) + "%"
            }
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
//                        Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
                        (activity as MainActivity).replaceFragment(changeNameFragment)
                    }
                    R.id.header2 -> {
//                        Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
                        var selectedItemIndex = 0

                        fun showConfirmationDialog(){
                            val choice = arrayOf("Light","Dark")
                            var selectedChoice = choice[selectedItemIndex]

                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Choose Theme")
                                .setSingleChoiceItems(choice,selectedItemIndex) {dialog, which ->
                                    selectedItemIndex = which
                                    selectedChoice = choice[which]
                                }

                                .setPositiveButton("OK"){dialog, which ->
                                    Toast.makeText(requireContext(), selectedChoice, Toast.LENGTH_SHORT).show()
                                    if(selectedChoice=="Light")AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                    else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                }
                                .setNeutralButton("Cancel"){dialog, which ->

                                }
                                .show()
                        }
                        showConfirmationDialog()
                    }
                    R.id.header3 -> {
//                        Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
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

//        viewModel.favoritesListLiveData.observe(viewLifecycleOwner) {
//                list -> adapter.submitList(list)
//
//            adapter.notifyDataSetChanged()
//        }

        viewModel.favoritesListLiveData.observe(viewLifecycleOwner){
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){
                adapter.submitList(it)
                Log.d("XXX", "list size: " + it.size)
                Log.d("XXX", "list: " +it)
                adapter.notifyDataSetChanged()
            }
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

        binding.monthPickerBtn.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.month ,android.R.layout.simple_spinner_item)
        binding.monthPickerBtn?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var str = when(position){
                    0 -> "2022-10"
                    1 -> "2022-09"
                    2 -> "2022-08"
                    3 -> "2022-07"
                    4 -> "2022-06"
                    5 -> "2022-05"
                    6 -> "2022-04"
                    7 -> "2022-03"
                    8 -> "2022-02"
                    9 -> "2022-01"
                    else -> "2022-10"
                }
                viewModel.netPortfolio(str)
            }
        }

        viewModel.netPortfolio("2022-10")


    }

    private fun createStrForPortfolio(list: Portfolio): String {
        var str = ""
        for (i in list.portfolio) {
            str += " + " + i.symbol + "\n"
        }
        return str.dropLast(2)
    }
}

