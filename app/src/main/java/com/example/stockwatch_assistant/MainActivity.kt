package com.example.stockwatch_assistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.example.stockwatch_assistant.databinding.ActivityMainBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StockRowAdapter

    private val homeFragment = HomeFragment()
    private val allStocksFragment = AllStocksFragment()
    private val newsFragment = NewsFragment()

    fun replaceFragment(fragment: Fragment) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
    }


//Set up signInLauncher
    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()){
        }

//add simple divider
    fun initRecyclerViewDividers(rv: RecyclerView) {
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

        //enable darkmode - still has error
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AuthInit(viewModel, signInLauncher)
        viewModel.updateTest()
//        viewModel.observeUserName().observe(this){
//            binding.hello.text = "Hello $it! Welcome to StockWatch-Assistant!"
//        }
//
////        var adapter = StockRowAdapter(viewModel, requireContext())
//        adapter = StockRowAdapter(viewModel = viewModel, context = this)
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = adapter
//
//        initRecyclerViewDividers(binding.recyclerView)
//
//        viewModel.stockMetaListLiveData.observe(this){
//            list -> adapter.submitList(list)
//            adapter.notifyDataSetChanged()
//        }
//
//        binding.searchBar.addTextChangedListener(){
//
//            if (it.toString().isEmpty())
//            {
//                hideKeyboard()
//            }
//
//            viewModel.searchStock(it.toString())
//        }


    viewModel.netPosts() //Fetch data all stock from Alpha Vantage API
//    viewModel.netGeneralNews() //Fetch General News

//Test
    viewModel.netNewsWithCategory("technology")

    replaceFragment(homeFragment)

    //youtube method
    binding.bottomNavigation.setOnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.ic_home -> replaceFragment(homeFragment)
            R.id.ic_list -> replaceFragment(allStocksFragment)
            R.id.ic_news -> replaceFragment(newsFragment)
        }
        true
    }


    }
}

//Example API call for symbol=TSLA and interval=5min
//https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=TSLA&interval=5min&apikey=CUZFO32ID30TEUX6

//Example API call for all active stock listing. Unfortunately, api return CSV file instead of JSON
//https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=demo