package com.example.stockwatch_assistant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
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
import androidx.appcompat.app.AppCompatActivity
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

    private var initialFetch = true
    private val changeNameFragment = ChangeNameFragment()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.placeholder.visibility = View.INVISIBLE
        binding.noNews.visibility = View.INVISIBLE

        val signInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.updateUser()
                viewModel.setUserLoggedIn(true)
                val user = FirebaseAuth.getInstance().currentUser!!.displayName
                viewModel.updateUserName(user!!)
                viewModel.emptyFavorite()

//                getStocks()
            } else {
                // Sign in failed
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
//                        Toast.makeText(requireContext(), "log out" Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()
                        viewModel.setUserLoggedIn(false)
//                        AuthInit(viewModel, signInLauncher)

                        activity?.finish()

                        val i = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(i)

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

        viewModel.observeUserLoggedIn().observe(viewLifecycleOwner){
            if(it){
                viewModel.observeUserName().observe(viewLifecycleOwner) {
                    binding.hello.text = "Hello $it"

                    Log.d("XXX", "userName: $it")
                    Log.d("YYY", "${FirebaseAuth.getInstance().currentUser}")
                    Log.d("YYY", "${FirebaseAuth.getInstance().currentUser?.displayName}")

                }
//                binding.logInBut.visibility = View.INVISIBLE
                binding.logoutBut.visibility = View.VISIBLE
            }else{
//                binding.logInBut.visibility = View.VISIBLE
                binding.hello.text = ""
                binding.logoutBut.visibility = View.INVISIBLE
            }
        }

        adapter = StockRowAdapter(viewModel, requireContext())
        binding.recyclerViewFavorite.layoutManager =
            LinearLayoutManager(binding.recyclerViewFavorite.context)
        binding.recyclerViewFavorite.adapter = adapter

        (activity as MainActivity).initRecyclerViewDividers(binding.recyclerViewFavorite)

        getStocks()

        viewModel.favoritesListLiveData.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                adapter.submitList(it)
                binding.noNews.visibility = View.INVISIBLE
            }
            else {
                binding.noNews.text = "No stocks found, add watchlist stocks from Stocks tab"
                binding.noNews.visibility = View.VISIBLE
            }
            Log.d("XXX", "fav list size: " + it.size)
            Log.d("XXX", "fav list: " + it)
            adapter.notifyDataSetChanged()
        }

        Log.d("XXX", "$initialFetch")



        viewModel.portfolioLiveData.observe(viewLifecycleOwner){

            Log.d("XXX", "porto list: " + it)

            if (!it.isNullOrEmpty()) {

                binding.indeterminateBar.visibility = View.GONE
                binding.placeholder.visibility = View.VISIBLE

                binding.rank1?.text = "Rank 1: \n" + createStrForPortfolio(it[0])
                binding.rank1Gain?.text = String.format("Gain: %.2f", it[0].percent_gain) + "%"

                binding.rank2?.text = "Rank 2: \n" + createStrForPortfolio(it[1])
                binding.rank2Gain?.text = String.format("Gain: %.2f", it[1].percent_gain) + "%"

                binding.rank3?.text = "Rank 3: \n" + createStrForPortfolio(it[2])
                binding.rank3Gain?.text = String.format("Gain: %.2f", it[2].percent_gain) + "%"

                Log.d("XXX", "porto list called")
            }
        }

        //porto folio stuff
        var selectedItemIndex = 0
        fun showConfirmationDialog() {
            val choice =
                arrayOf("2022-10", "2022-09", "2022-08", "2022-07", "2022-06", "2022-05","2022-04",
                    "2022-03","2022-02","2022-01")
            var selectedChoice = choice[selectedItemIndex]

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Choose Date")
                .setSingleChoiceItems(choice, selectedItemIndex) { dialog, which ->
                    selectedItemIndex = which
                    selectedChoice = choice[which]
                }
                .setPositiveButton("OK") { dialog, which ->
                    Toast.makeText(requireContext(), selectedChoice, Toast.LENGTH_SHORT).show()
                    binding.currentYearDate.text = selectedChoice

                    when (selectedChoice) {
                        "2022-10" ->  viewModel.netPortfolio("2022-10")
                        "2022-09" -> viewModel.netPortfolio("2022-09")
                        "2022-08" -> viewModel.netPortfolio("2022-08")
                        "2022-07"-> viewModel.netPortfolio("2022-07")
                        "2022-06" -> viewModel.netPortfolio("2022-06")
                        "2022-05"-> viewModel.netPortfolio("2022-05")
                        "2022-04" -> viewModel.netPortfolio("2022-04")
                        "2022-03"-> viewModel.netPortfolio("2022-03")
                        "2022-02" -> viewModel.netPortfolio("2022-02")
                        "2022-01" ->  viewModel.netPortfolio("2022-01")
                    }
                }
                .setNeutralButton("Cancel") { dialog, which ->
                }
                .show()
        }

        binding.change.setOnClickListener {
            showConfirmationDialog()
        }
        binding.currentYearDate.setOnClickListener {
            showConfirmationDialog()
        }

    }

    private fun createStrForPortfolio(list: Portfolio): String {
        var str = ""
        for (i in list.portfolio) {
            str += " + " + i.symbol + "\n"
        }
        return str.dropLast(2)
    }

    private fun getStocks() {
        db.collection("Favorites")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(
                        "read",
                        "${document.id} => ${document.data}, ${document.data["stockName"]}"
                    )
                    val stock: StockMeta = StockMeta(
                        symbol = document.data["stockSymbol"].toString(),
                        name = document.data["stockName"].toString(),
                        exchange = document.data["stockExchange"].toString()
                    )

                    if (document.data["userId"] == FirebaseAuth.getInstance().currentUser!!.uid) {
                        if (!viewModel.isFavorite(stock)) {
                            viewModel.addFavorite(stock)
                            Log.d("XXX", "add from initial fetch")
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("read", "Error getting documents.", exception)
            }
        binding.indeterminateBar2.visibility = View.GONE
        binding.noNews.text = "No stocks found, add watchlist stocks from Stocks tab"
        binding.noNews.visibility = View.VISIBLE
    }
}



