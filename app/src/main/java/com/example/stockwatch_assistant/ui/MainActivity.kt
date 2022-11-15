package com.example.stockwatch_assistant.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockwatch_assistant.MainViewModel
import com.example.stockwatch_assistant.R
import com.example.stockwatch_assistant.ui.adapter.StockRowAdapter
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta
import com.example.stockwatch_assistant.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StockRowAdapter

    val db = Firebase.firestore

    private val homeFragment = HomeFragment()
    private val allStocksFragment = AllStocksFragment()
    private val newsFragment = NewsFragment()


    var canGoBack = false

    fun replaceFragment(fragment: Fragment) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
    }


//Set up signInLauncher
    val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.updateUser()
            viewModel.setUserLoggedIn(true)
            val user = FirebaseAuth.getInstance().currentUser!!.displayName
            viewModel.updateUserName(user!!)

//            db.collection("Favorites")
//                .get()
//                .addOnSuccessListener { result ->
//                    for (document in result) {
//                        Log.d("read", "${document.id} => ${document.data}, ${document.data["stockName"]}")
//                        val stock: StockMeta = StockMeta(
//                            symbol = document.data["stockSymbol"].toString(),
//                            name = document.data["stockName"].toString(),
//                            exchange = document.data["stockExchange"].toString()
//                        )
//                        if(document.data["userId"] == FirebaseAuth.getInstance().currentUser!!.uid){
//                            viewModel.addFavorite(stock)
//
//                            Log.d("XXX", "add from siginInLauncher")
//                        }
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.w("read", "Error getting documents.", exception)
//                }
            getStocks()



        } else {
            // Sign in failed
            Log.d("MainActivity", "sign in failed ${result}")
        }
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

    fun hideBottomNavBar() {
        binding.bottomNavigation.visibility = View.INVISIBLE
//        window.decorView.apply {
//            systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        }

        window.decorView.apply {
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//            WindowInsetsController.OnControllableInsetsChangedListener()
        }

    }

//onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {

//        val splashScreen = installSplashScreen()
//        splashScreen.setKeepOnScreenCondition{
//        viewModel.isLoading.value
//        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //enable dark-mode
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

//        AuthInit(viewModel, signInLauncher)
        viewModel.updateTest()
        viewModel.netPosts() //Fetch data all stock from Alpha Vantage API
        viewModel.netGeneralNews() //Fetch General News
        viewModel.netPortfolio("2022-10") //Fetch Default Porto
        replaceFragment(homeFragment)

//Test
//    viewModel.netNewsWithCategory("technology")



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
    }



//    override fun onBackPressed() {
//        if(viewModel.observeUserName().value.isNullOrEmpty())
//            super.onBackPressed()
//    }

}

//Example API call for symbol=TSLA and interval=5min
//https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=TSLA&interval=5min&apikey=CUZFO32ID30TEUX6
//Example API call for all active stock listing. Unfortunately, api return CSV file instead of JSON
//https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=demo