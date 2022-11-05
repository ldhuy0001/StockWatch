package com.example.stockwatch_assistant

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.example.stockwatch_assistant.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

//Set up signInLauncher
    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()){

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


    }
}

//Example API call for symbol=TSLA and interval=5min
//https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=TSLA&interval=5min&apikey=CUZFO32ID30TEUX6

//Example API call for all active stock listing. Unfortunately, api return CSV file instead of JSON
//https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=demo