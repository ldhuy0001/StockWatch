package com.example.stockwatch_assistant.alphaVantageAPI

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.text.clearSpans
import androidx.core.text.toSpanned
import com.google.gson.annotations.SerializedName

data class StockMeta(
//    symbol,name,exchange,assetType,ipoDate,delistingDate,status
//    val symbol: String,
//    val name: String,
//    val exchange: String,

    val symbol: SpannableString?,
//    @SerializedName("name")
    val name: SpannableString?,
    val exchange: SpannableString?,


//    var symbol_span = SpannableString(symbol)




//    val assetType: String,
//    val ipoDate: String,
//    val delistingDate: String,
//    val status: String,
){
    companion object {
//        fun searchFor(searchTerm: String):Boolean{
////            search function work here
//            return true
//        }


        private fun findAndSetSpan(fulltext: SpannableString, subtext: String): Boolean {
            if (subtext.isEmpty()) return true
            val i = fulltext.indexOf(subtext, ignoreCase = true)
            if (i == -1) return false
            fulltext.setSpan(
                ForegroundColorSpan(Color.CYAN), i, i + subtext.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return true
        }


        fun spannableStringsEqual(a: SpannableString?, b: SpannableString?): Boolean {
            if (a == null && b == null) return true
            if (a == null && b != null) return false
            if (a != null && b == null) return false
            val spA = a!!.getSpans(0, a.length, Any::class.java)
            val spB = b!!.getSpans(0, b.length, Any::class.java)
            return a.toString() == b.toString()
                    &&
                    spA.size == spB.size && spA.equals(spB)

        }
    }

        private fun clearSpan(str: SpannableString?) {
            str?.clearSpans()
            str?.setSpan(
                ForegroundColorSpan(Color.GRAY), 0, 0,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        // clearSpans does not invalidate the textview
        // We have to assign a span to make sure text gets redrawn, so assign
        // a span that does nothing
        fun removeAllCurrentSpans(){
            // Erase all spans
            clearSpan(name)
            clearSpan(symbol)
        }

        // Given a search string, look for it in the RedditPost.  If found,
        // highlight it and return true, otherwise return false.
        fun searchFor(searchTerm: String): Boolean {
            // XXX Write me, search both regular posts and subreddit listings

            removeAllCurrentSpans()
            val spanSymbol  = symbol!!.let { findAndSetSpan(it, searchTerm) }

            val spanName = name!!.let { findAndSetSpan(it, searchTerm) }


//            val spanSymbol = findAndSetSpan(symbol, searchTerm)
//            val spanName = findAndSetSpan(name, searchTerm)

            return spanName || spanSymbol
        }


}