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
        val stockName: String = intent.getStringExtra("stockName").toString()
        Log.d("ck","symbol $stockSymbol")
        viewModel.netStockDetails(stockSymbol)

//        supportActionBar!!.title = "One Post"
        supportActionBar!!.title = stockSymbol

        viewModel.stockDetailsLiveData.observe(this){
            onePostBinding.stockName.text = stockName

//            onePostBinding.stockSymbol.text = "Symbol: " + it.symbol
            onePostBinding.stockSector.text = Html.fromHtml("<b>" +"Sector: "+"</b>"+it.sector)

            onePostBinding.stockIndustry.text = Html.fromHtml("<b>" +"Industry: "+"</b>"+it.industry)

            if (it.description == "None" || it.description.isEmpty()) {
                onePostBinding.stockDescription.text = "N/A"
            }
            else onePostBinding.stockDescription.text = Html.fromHtml("<b>" +"Description: "+"</b>"+it.description)

//            onePostBinding.stockDescription.text = Html.fromHtml("<b>" +"Description: "+"</b>"+it.description)

            Log.d("onePost", "${it.dividendYield} ${it.peRatio} ${it.weekHigh52} ${it.weekLow52}")

            if (it.dividendYield == "None" || it.dividendYield == "0") {
                onePostBinding.divYieldValue.text = "N/A"
            }
            else onePostBinding.divYieldValue.text = it.dividendYield

            if (it.peRatio == "None" || it.peRatio== "0") {
                onePostBinding.peValue.text = "N/A"
            }
            else onePostBinding.peValue.text = it.peRatio

            if (it.weekHigh52 == "None" || it.weekHigh52== "0") {
                onePostBinding.wkHigh52Value.text = "N/A"
            }
            else onePostBinding.wkHigh52Value.text = it.weekHigh52

            if (it.weekLow52 == "None" || it.weekLow52 == "0") {
                onePostBinding.wkLow52Value.text = "N/A"
            }
            else onePostBinding.wkLow52Value.text = it.weekLow52
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}