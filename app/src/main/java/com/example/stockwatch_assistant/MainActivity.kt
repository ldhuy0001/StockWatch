package com.example.stockwatch_assistant

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.example.stockwatch_assistant.databinding.ActivityMainBinding
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StockRowAdapter

//Set up signInLauncher
    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()){

        }

//add simple divider
    private fun initRecyclerViewDividers(rv: RecyclerView) {
        // Let's have dividers between list items
        val dividerItemDecoration = DividerItemDecoration(
            rv.context, LinearLayoutManager.VERTICAL )
        rv.addItemDecoration(dividerItemDecoration)
    }

//hide keyboard when search text is empty
    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

//onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AuthInit(viewModel, signInLauncher)
        viewModel.updateTest()
        viewModel.observeUserName().observe(this){
            binding.hello.text = "Hello $it! Welcome to StockWatch-Assistant!"
        }

//        var adapter = StockRowAdapter(viewModel, requireContext())
        adapter = StockRowAdapter(viewModel = viewModel, context = this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        initRecyclerViewDividers(binding.recyclerView)

        viewModel.stockMetaListLiveData.observe(this){
            list -> adapter.submitList(list)
            adapter.notifyDataSetChanged()
        }

        binding.searchBar.addTextChangedListener(){

            if (it.toString().isEmpty())
            {
                hideKeyboard()
            }

            viewModel.searchStock(it.toString())
        }
        viewModel.netPosts()
    }
}

//Example API call for symbol=TSLA and interval=5min
//https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=TSLA&interval=5min&apikey=CUZFO32ID30TEUX6

//Example API call for all active stock listing. Unfortunately, api return CSV file instead of JSON
//https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=demo