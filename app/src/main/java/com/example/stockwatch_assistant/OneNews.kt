package com.example.stockwatch_assistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient

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



        val newsURL: String = intent.getStringExtra("stockNewsURL").toString()
        val newsTitle: String = intent.getStringExtra("stockNewsTitle").toString()

        supportActionBar!!.title = newsTitle

//        oneNewsBinding.webview.webChromeClient = WebChromeClient()
        oneNewsBinding.webview.webViewClient = WebViewClient()
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