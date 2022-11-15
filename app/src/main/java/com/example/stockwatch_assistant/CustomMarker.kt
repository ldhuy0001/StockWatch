package com.example.stockwatch_assistant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarker(context: Context, layoutResource: Int, private val dataToDisplay: MutableList<String>):  MarkerView(context, layoutResource) {

//    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
//        val value = entry?.y?.toDouble() ?: 0.0
//        var resText = ""
//        if(value.toString().length > 8){
//            resText = "Val: " + value.toString().substring(0,7)
//        }
//        else{
//            resText = "Val: " + value.toString()
//        }
//        tvPrice.text = resText
//        super.refreshContent(entry, highlight)
//    }
//
//    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
//        return MPPointF(-width / 2f, -height - 10f)
//    }

    private var txtViewData: TextView? = null

    init {
        txtViewData = findViewById(R.id.txtViewData)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        try {
            val xAxis = e?.x?.toInt() ?: 0
            txtViewData?.text = dataToDisplay[xAxis].toString()
        } catch (e: IndexOutOfBoundsException) { }

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}