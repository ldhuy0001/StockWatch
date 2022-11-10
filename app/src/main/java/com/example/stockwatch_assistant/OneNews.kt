package com.example.stockwatch_assistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.stockwatch_assistant.databinding.ActivityOneNewsBinding

class OneNews : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_news)

        val oneNewsBinding = ActivityOneNewsBinding.inflate(layoutInflater)
        setContentView(oneNewsBinding.root)

        setSupportActionBar(oneNewsBinding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)



        val newsURL: String = intent.getStringExtra("stockURL").toString()

        supportActionBar!!.title = "yow"

        oneNewsBinding.webview.loadUrl(newsURL)


    }

//    override fun onBackPressed() {
//        if (binding.)
//    }
//
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}