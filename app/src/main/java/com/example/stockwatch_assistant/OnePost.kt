package com.example.stockwatch_assistant

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.stockwatch_assistant.databinding.ActivityOnePostBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import java.math.RoundingMode

class OnePost : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: NewsAdapter

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
        
        onePostBinding.stockRelatedNews.text = "$stockName Related News"


//        val dialog = progressDialog(message = "Please wait a bit…", title = "Fetching data")
//        dialog.show()
////....
//        dialog.dismiss()

        viewModel.netStockDetails(stockSymbol)
        viewModel.netStockPrice(stockSymbol)
        viewModel.netStockNews(stockSymbol)

        Log.d("testchart","ck0")
//        supportActionBar!!.title = "One Post"
        supportActionBar!!.title = stockSymbol

        var count = 0
        viewModel.stockPriceListLiveData.observe(this){
            val entries = ArrayList<Entry>()
            Log.d("testchart","ck1")
            for (i in it){
                count++
                entries.add(Entry(count.toFloat(),i.low.toFloat()))
//                Log.d("testchart","ck2 low === ${i.low.toFloat()} || high === ${i.high.toFloat()}")
            }

//            TODO
            val lineDataSet = LineDataSet(entries,"$stockName ( $stockSymbol )")


            lineDataSet.setDrawValues(true)
            lineDataSet.setDrawFilled(false)

            lineDataSet.lineWidth = 2f
            if (it.isNotEmpty()){
                if (it[0].low < it[it.size-1].low ) {
    //                lineDataSet.fillColor = Color.GREEN
                    lineDataSet.color = Color.GREEN
                    lineDataSet.setCircleColor(Color.GREEN)
                } else {
    //                lineDataSet.fillColor = Color.RED
                    lineDataSet.color = Color.RED
                    lineDataSet.setCircleColor(Color.RED)
                }

                onePostBinding.openValue.text = roundOffDecimal(number= it[0].open)
                onePostBinding.highValue.text = roundOffDecimal(number= it[0].high)
                onePostBinding.lowValue.text = roundOffDecimal(number= it[0].low)
                onePostBinding.volumeValue.text = roundOffDecimal(number= it[0].volume)
            }


            onePostBinding.lineChart.xAxis.labelRotationAngle = 0f
            onePostBinding.lineChart.data = LineData(lineDataSet)

            onePostBinding.lineChart.axisRight.isEnabled = false
//            onePostBinding.lineChart.xAxis.axisMaximum = j+0.1f
//            onePostBinding.lineChart.xAxis.axisMaximum = 0.1f

            onePostBinding.lineChart.setTouchEnabled(true)
            onePostBinding.lineChart.setPinchZoom(true)

            onePostBinding.lineChart.description.text = "Minute"

            onePostBinding.lineChart.animateX(1800, Easing.EaseInExpo)

//            val markerView = CustomMarker(this@OnePost, R.layout.marker_view)
//            onePostBinding.lineChart.marker = markerView

//            val df = DecimalFormat("%.2f")
//            df.roundingMode = RoundingMode.DOWN

            val number = 0.0449999
            val rounded = number.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()




            Log.d("XXX", "$rounded")



        }
        Log.d("testchart","ck10")


        viewModel.stockDetailsLiveData.observe(this) {
            if (it.isNotEmpty()) {
                onePostBinding.stockName.text = stockName
                Log.d("okhttp.OkHttpClient","time ${System.currentTimeMillis()}\n" +
                        "time in second ${System.currentTimeMillis()/1000%60/15}")

                onePostBinding.stockSector.text =
                    Html.fromHtml("<b>" + "Sector: " + "</b>" + it.sector)
                onePostBinding.stockIndustry.text =
                    Html.fromHtml("<b>" + "Industry: " + "</b>" + it.industry)

//                if (onePostBinding.stockName.text == stockName) {
//                    onePostBinding.indeterminateBar.visibility = View.INVISIBLE
//                    onePostBinding.indeterminateBarBackground.visibility = View.INVISIBLE
//                }

                if (it.description == "None" || it.description.isEmpty()) {
                    onePostBinding.stockDescription.text =
                        Html.fromHtml("<b>" + "Description: " + "</b>" + "N/A")
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


        adapter = NewsAdapter(viewModel = viewModel, context = this)
        onePostBinding.recyclerView.layoutManager = LinearLayoutManager(onePostBinding.recyclerView.context)
        onePostBinding.recyclerView.adapter = adapter



//        initRecyclerViewDividers(binding.recyclerView)

//        viewModel.observe(viewLifecycleOwner){
//                list -> adapter.submitList(list)
//            adapter.notifyDataSetChanged()
//        }

        viewModel.stockNewsLiveData.observe(this){

                list -> adapter.submitList(list)
            Log.d("stockNews","Here is list in stockNews \n $list")
            adapter.notifyDataSetChanged()

            if (!list.isNullOrEmpty()) onePostBinding.noNews.visibility = View.INVISIBLE

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun roundOffDecimal(number: String): String{
        return number.toBigDecimal().setScale(2, RoundingMode.UP).toString()
    }
}