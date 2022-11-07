package com.example.stockwatch_assistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.stockwatch_assistant.databinding.ActivityOnePostBinding

class OnePost : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_post)

        //naming title
        val actionBar = supportActionBar



        val onePostBinding = ActivityOnePostBinding.inflate(layoutInflater)
        setContentView(onePostBinding.root)

        setSupportActionBar(onePostBinding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        supportActionBar!!.title = "One Post"

//        if (actionBar!=null){
//            actionBar.title = "One Post"
//        }


        val stockName: String = intent.getStringExtra("stockName").toString()
        val stockSymbol: String = intent.getStringExtra("stockSymbol").toString()


        onePostBinding.stockName.text = stockName
        onePostBinding.stockSymbol.text = "stockSymbol: " + stockSymbol




    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}