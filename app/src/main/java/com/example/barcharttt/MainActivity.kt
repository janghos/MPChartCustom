package com.example.barcharttt

import CustomBarChartRender
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import com.example.barcharttt.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

private lateinit var binding: ActivityMainBinding
private lateinit var barDataSet : BarDataSet
private var customMarkerView : CustomMarkerView ?= null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        showBarChart()

        // 커스텀 Renderer 설정
        val barRender = CustomBarChartRender(binding.barChart, binding.barChart.animator, binding.barChart.viewPortHandler)
        barRender.setRadius(100)
        binding.barChart.renderer = barRender
        customMarkerView = CustomMarkerView(this, R.layout.marker_view, null, null)
        customMarkerView?.let {
            it.chartView = binding.barChart
            binding.barChart.marker = it
        }

        binding.btn.setOnClickListener {
            var intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }

    private fun showBarChart() {
        val valueList = ArrayList<Double>()
        val entries = ArrayList<BarEntry>()
        val title = "Title"

        //input data
        for (i in 0..5) {
            valueList.add(i * 100.1)
        }

        //fit the data into a bar
        for (i in valueList.indices) {
            val barEntry = BarEntry(i.toFloat(), valueList[i].toFloat())
            entries.add(barEntry)
        }
        barDataSet = BarDataSet(entries, title)
        val data = BarData(barDataSet)
        binding.barChart.data = data
        initBarDataSet(barDataSet)

        // 인덱스 라벨 설정
        val xAxis = binding.barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("0", "1", "2", "3", "4", "5"))
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        // 세로 격자선 제거, 가로 격자선 유지
        xAxis.setDrawGridLines(false)
        binding.barChart.axisLeft.setDrawGridLines(true)
        binding.barChart.axisRight.setDrawGridLines(false)
        val yAxis = binding.barChart.axisLeft
        yAxis.axisMinimum = 0f
        // X축 라벨 아래쪽 마진 조정
//        xAxis.yOffset = 10f

        // X축의 최소값을 설정하여 그래프의 시작을 0에 붙임
        xAxis.axisMinimum = 0f
        binding.barChart.setVisibleXRangeMinimum(1f)
        binding.barChart.setFitBars(true)
        binding.barChart.extraLeftOffset = 0f
        binding.barChart.extraRightOffset = 0f

        binding.barChart.invalidate()
        binding.barChart.isDoubleTapToZoomEnabled = false
        binding.barChart.isFullyZoomedOut
    }

    private fun initBarDataSet(barDataSet: BarDataSet) {
        //Changing the color of the bar
        barDataSet.color = Color.RED
        //Removing the bar shadow
        barDataSet.barShadowColor = Color.TRANSPARENT
        //Setting the size of the form in the legend
        barDataSet.formSize = 15f
        //Showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false)
        //Setting the text size of the value of the bar
        barDataSet.valueTextSize = 12f
        //Setting highlight color to blue
        barDataSet.highLightColor = Color.BLUE
        //Setting highlight alpha to fully opaque
        barDataSet.highLightAlpha = 255
    }
}
