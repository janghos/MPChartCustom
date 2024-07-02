package com.example.barcharttt

import android.content.Context
import android.util.Log
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarkerView(context: Context, layoutResource: Int, lineEntry : ArrayList<Entry>?, barEntry: ArrayList<BarEntry>?) : MarkerView(context, layoutResource) {

    private val tvContent: TextView = findViewById(R.id.tvContent)
    private var lineEntries = lineEntry
    private var barEntries = barEntry
    private var markerViewIsHorizontal = false


    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        var barValue = ""
        var lineValue = ""

        // Find corresponding entries in both data sets
        val barEntry = e

        // Set values if not null
        barValue = barEntry?.y?.toString() ?: barValue


        // Format the marker content
        tvContent.text = "매출액: $barValue"
        Log.d("rrrLine", lineEntries.toString())
        Log.d("rrrBar", barEntries.toString())
        Log.d("rrreee", e.toString())

        if(lineEntries != null && barEntries != null) {

            lineValue = lineEntries!![e!!.x.toInt()].y.toInt().toString()

            tvContent.text = "매출액: $barValue 만원\n 판매 수:${lineValue}"
        }

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        if(markerViewIsHorizontal) {
            return MPPointF(-(width * 1.2).toFloat(), -height / 20.toFloat())
//            return MPPointF(-(width / 2).toFloat(), - height - 20f)
        }else {
            return MPPointF(-(width / 2).toFloat(), - height - 20f)
        }

    }

    fun isHorizontal(boolean : Boolean) {
        markerViewIsHorizontal = boolean
    }
}
