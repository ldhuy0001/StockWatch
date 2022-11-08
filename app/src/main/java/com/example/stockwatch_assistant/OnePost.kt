package com.example.stockwatch_assistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.activity.viewModels

import com.example.stockwatch_assistant.databinding.ActivityOnePostBinding

class OnePost : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_post)

        val onePostBinding = ActivityOnePostBinding.inflate(layoutInflater)
        setContentView(onePostBinding.root)

        setSupportActionBar(onePostBinding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)



//        if (actionBar!=null){
//            actionBar.title = "One Post"
//        }

        val stockSymbol: String = intent.getStringExtra("stockSymbol").toString()
        Log.d("ck","symbol $stockSymbol")
        viewModel.netStockDetails(stockSymbol)

//        supportActionBar!!.title = "One Post"

        supportActionBar!!.title = stockSymbol



        viewModel.stockDetailsLiveData.observe(this){
            onePostBinding.stockName.text = it.name

//            onePostBinding.stockSymbol.text = "Symbol: " + it.symbol
            onePostBinding.stockSector.text = Html.fromHtml("<b>" +"Sector: "+"</b>"+it.sector)

            onePostBinding.stockIndustry.text = Html.fromHtml("<b>" +"Industry: "+"</b>"+it.industry)

//            onePostBinding.stockDescription.text = "Description: " + it.description
            onePostBinding.stockDescription.text = Html.fromHtml("<b>" +"Description: "+"</b>"+it.description)

            onePostBinding.divYieldValue.text = it.dividendYield
            onePostBinding.peValue.text = it.peRatio
            onePostBinding.wkHigh52Value.text = it.weekHigh52
            onePostBinding.wkLow52Value.text = it.weekLow52
        }
//        onePostBinding.stockName.text = stockName
//        onePostBinding.stockSymbol.text = "stockSymbol: " + stockSymbol

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}