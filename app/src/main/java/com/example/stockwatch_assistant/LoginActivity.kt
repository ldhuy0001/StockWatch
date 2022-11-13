package com.example.stockwatch_assistant

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta
import com.example.stockwatch_assistant.databinding.ActivityLoginBinding
import com.example.stockwatch_assistant.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    val db = Firebase.firestore
    val bool = false


    private lateinit var binding: ActivityLoginBinding

    val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            viewModel.updateUser()
            viewModel.setUserLoggedIn(true)
            val user = FirebaseAuth.getInstance().currentUser!!.displayName
            viewModel.updateUserName(user!!)
            getStocks()

            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()

        } else {
            // Sign in failed
            Log.d("MainActivity", "sign in failed ${result}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //fetch-data
        viewModel.updateTest()
        viewModel.netPosts() //Fetch data all stock from Alpha Vantage API
        viewModel.netGeneralNews() //Fetch General News
        viewModel.netPortfolio("2022-10") //Fetch Default Porto

        binding.button2.setOnClickListener {
            AuthInit(viewModel, signInLauncher)
        }

        val bool = FirebaseAuth.getInstance().currentUser
        Log.d("MMM","this called1,user $bool")

//        if (bool==null) {
//            Log.d("MMM","this called2")
//            AuthInit(viewModel, signInLauncher)
//        }
//        else{
//            Log.d("MMM","this called3")
//            val i = Intent(this, MainActivity::class.java)
//            startActivity(i)
//            finish()
//        }

        if (bool!=null) {
            Log.d("MMM","this called2")
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
        else{
            Log.d("MMM","this called3")
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

    override fun onBackPressed() {
        if(bool)
            super.onBackPressed()
    }



}