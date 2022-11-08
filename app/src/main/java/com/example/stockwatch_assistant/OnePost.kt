package com.example.stockwatch_assistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.activity.viewModels

import com.example.stockwatch_assistant.databinding.ActivityOnePostBinding
import com.google.android.material.snackbar.Snackbar

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


//        val dialog = progressDialog(message = "Please wait a bit…", title = "Fetching data")
//        dialog.show()
////....
//        dialog.dismiss()


        viewModel.netStockDetails(stockSymbol)

//        supportActionBar!!.title = "One Post"
        supportActionBar!!.title = stockSymbol



        viewModel.stockDetailsLiveData.observe(this) {
            if (it.isNotEmpty()) {
                onePostBinding.stockName.text = stockName
                Log.d("okhttp.OkHttpClient","time ${System.currentTimeMillis()}\n" +
                        "time in second ${System.currentTimeMillis()/1000%60/15}")

                onePostBinding.stockSector.text =
                    Html.fromHtml("<b>" + "Sector: " + "</b>" + it.sector)
                onePostBinding.stockIndustry.text =
                    Html.fromHtml("<b>" + "Industry: " + "</b>" + it.industry)

                if (onePostBinding.stockName.text == stockName) {
                    onePostBinding.indeterminateBar.visibility = View.INVISIBLE
                    onePostBinding.indeterminateBarBackground.visibility = View.INVISIBLE
                }

                if (it.description == "None" || it.description.isEmpty()) {
                    onePostBinding.stockDescription.text = "N/A"
                } else onePostBinding.stockDescription.text =
                    Html.fromHtml("<b>" + "Description: " + "</b>" + it.description)

                Log.d(
                    "onePost",
                    "${it.dividendYield} ${it.peRatio} ${it.weekHigh52} ${it.weekLow52}"
                )

                if (it.dividendYield == "None" || it.dividendYield == "0") {
                    onePostBinding.divYieldValue.text = "N/A"
                } else onePostBinding.divYieldValue.text = it.dividendYield

                if (it.peRatio == "None" || it.peRatio == "0") {
                    onePostBinding.peValue.text = "N/A"
                } else onePostBinding.peValue.text = it.peRatio

                if (it.weekHigh52 == "None" || it.weekHigh52 == "0") {
                    onePostBinding.wkHigh52Value.text = "N/A"
                } else onePostBinding.wkHigh52Value.text = it.weekHigh52

                if (it.weekLow52 == "None" || it.weekLow52 == "0") {
                    onePostBinding.wkLow52Value.text = "N/A"
                } else onePostBinding.wkLow52Value.text = it.weekLow52
            } else {
                Snackbar.make(onePostBinding.root
                    ,"API call reachs limitation. \nPlease try again in next minute!!"
                    ,Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}