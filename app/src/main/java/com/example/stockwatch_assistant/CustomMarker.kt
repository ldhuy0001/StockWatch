package com.example.stockwatch_assistant

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarker(context: Context,
                   layoutResource: Int,
                   private val priceDataToDisplay: MutableList<String>,
                   private val volumeDataToDisplay: MutableList<String>,
                   private val dateDataToDisplay: MutableList<String>
):  MarkerView(context, layoutResource) {

    private var priceMarker: TextView? = null
    private var volumeMarker: TextView? = null
    private var dateMarker: TextView? = null

    init {
        dateMarker = findViewById(R.id.dateMarker)
        priceMarker = findViewById(R.id.priceMarker)
        volumeMarker = findViewById(R.id.volumeMarker)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        try {
            val xAxis = e?.x?.toInt() ?: 0
            dateMarker?.text = "Date: " + dateDataToDisplay[xAxis].drop(5)
            priceMarker?.text = "Price: $" + priceDataToDisplay[xAxis]
            volumeMarker?.text = "Volume: " + volumeDataToDisplay[xAxis]
        } catch (e: IndexOutOfBoundsException) { }

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}