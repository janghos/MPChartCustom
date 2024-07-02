package com.example.barcharttt

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.icu.lang.UCharacter.IndicPositionalCategory.BOTTOM
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Gravity.BOTTOM
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.barcharttt.databinding.ActivityMain2Binding
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.*
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private var customMarkerView : CustomMarkerView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCombinedChart()
        setupHorizontalBarChart()
    }

    private fun setupCombinedChart() {
        val barEntries = ArrayList<BarEntry>()
        val lineEntries = ArrayList<Entry>()
        val labels = listOf("01/01", "02/01", "03/01", "04/01", "05/01", "06/01", "07/01")

        barEntries.add(BarEntry(0f, 200f))
        barEntries.add(BarEntry(1f, 300f))
        barEntries.add(BarEntry(2f, 250f))
        barEntries.add(BarEntry(3f, 400f))
        barEntries.add(BarEntry(4f, 350f))
        barEntries.add(BarEntry(5f, 280f))
        barEntries.add(BarEntry(6f, 220f))

        lineEntries.add(Entry(0f, 30f))
        lineEntries.add(Entry(1f, 50f))
        lineEntries.add(Entry(2f, 40f))
        lineEntries.add(Entry(3f, 60f))
        lineEntries.add(Entry(4f, 55f))
        lineEntries.add(Entry(5f, 45f))
        lineEntries.add(Entry(6f, 35f))

        val barDataSet = BarDataSet(barEntries, "매출액")
        barDataSet.color = Color.GRAY
        barDataSet.highLightColor = Color.RED
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.barBorderWidth = 0.3f // 폭을 줄임
        barDataSet.valueTextColor = Color.TRANSPARENT

        val lineDataSet = LineDataSet(lineEntries, "판매 수")
        lineDataSet.color = Color.RED
        lineDataSet.setCircleColor(Color.RED)
        lineDataSet.valueTextColor = Color.BLACK
        lineDataSet.lineWidth = 2f
        lineDataSet.circleRadius = 4f
        lineDataSet.setDrawValues(true)
        lineDataSet.axisDependency = YAxis.AxisDependency.RIGHT
        lineDataSet.isHighlightEnabled = false
        lineDataSet.valueTextColor = Color.TRANSPARENT

        val barData = BarData(barDataSet)
        val lineData = LineData(lineDataSet)


        val combinedData = CombinedData()
        combinedData.setData(barData)
        combinedData.setData(lineData)

        val xAxis = binding.combinedChart.xAxis
        val typeface = ResourcesCompat.getFont(this, R.font.suit_bold)
        xAxis.typeface = typeface
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.gridColor = Color.LTGRAY
        xAxis.setAxisMinimum(-0.5f) // Add padding to the left
        xAxis.setAxisMaximum(labels.size - 0.5f) // Add padding to the right
        xAxis.gridColor = Color.WHITE

        val yAxisLeft = binding.combinedChart.axisLeft
        yAxisLeft.typeface = typeface
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.granularity = 100f
        yAxisLeft.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return value.toInt().toString()
            }
        }

        yAxisLeft.setDrawGridLines(false)


        val yAxisRight = binding.combinedChart.axisRight
        yAxisRight.typeface = typeface
        yAxisRight.axisMinimum = 0f
        yAxisRight.granularity = 10f
        yAxisRight.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return value.toInt().toString()
            }
        }
        yAxisRight.gridColor = Color.LTGRAY

        Log.d("line", lineEntries.toString())
        Log.d("bar", barEntries.toString())
        customMarkerView = CustomMarkerView(this, R.layout.marker_view, lineEntries, barEntries)

        customMarkerView?.let {
            it.chartView = binding.combinedChart
            binding.combinedChart.marker = it
        }

        binding.combinedChart.description.isEnabled = false
        binding.combinedChart.legend.isEnabled = true
        binding.combinedChart.setScaleEnabled(false)
        binding.combinedChart.isDoubleTapToZoomEnabled = false
        binding.combinedChart.setPinchZoom(false)
        binding.combinedChart.data = combinedData

        // 커스텀 Renderer 설정
        val barRenderer = CustomBarChartRenderer(binding.combinedChart, binding.combinedChart.animator, binding.combinedChart.viewPortHandler)
        barRenderer.setRadius(0) // 둥글기 반경 설정
        val lineRenderer = LineChartRenderer(binding.combinedChart, binding.combinedChart.animator, binding.combinedChart.viewPortHandler)

        binding.combinedChart.renderer = CombinedChartRenderer(binding.combinedChart, binding.combinedChart.animator, binding.combinedChart.viewPortHandler).apply {
            subRenderers.add(barRenderer)
            subRenderers.add(lineRenderer)
        }

        binding.combinedChart.invalidate()
    }

    private fun setupHorizontalBarChart() {
        val barEntries = ArrayList<BarEntry>()
        val labels = listOf("커피", "빵", "케이크", "주스", "샐러드")

        barEntries.add(BarEntry(0f, 230000f))
        barEntries.add(BarEntry(1f, 800000f))
        barEntries.add(BarEntry(2f, 450000f))
        barEntries.add(BarEntry(3f, 600000f))
        barEntries.add(BarEntry(4f, 200000f))

        val barDataSet = BarDataSet(barEntries, "매출액")
        barDataSet.color = Color.GRAY
        barDataSet.highLightColor = Color.RED
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 12f
        barDataSet.setDrawValues(true)

        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f

        binding.horizontalBarChart.data = barData

        val xAxis = binding.horizontalBarChart.xAxis
        val typeface = ResourcesCompat.getFont(this, R.font.suit_bold)
        xAxis.typeface = typeface
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.setDrawGridLines(false) // 세로 격자선 제거

        val yAxisLeft = binding.horizontalBarChart.axisLeft
        val yAxisRight = binding.horizontalBarChart.axisRight

// 왼쪽 y축을 비활성화합니다.
        yAxisRight.isEnabled = true
        yAxisRight.axisMinimum = 0f
        yAxisRight.gridColor = Color.LTGRAY
// 오른쪽 y축을 활성화하고 설정을 맞춥니다.
        yAxisLeft.isEnabled = false
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.granularity = 100000f // 200,000 단위로 쪼개기
        yAxisLeft.typeface = typeface
        yAxisLeft.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return value.toInt().toString()
            }
        }
        yAxisLeft.gridColor = Color.LTGRAY

// x축 설정
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)


// 차트 기타 설정
        binding.horizontalBarChart.description.isEnabled = false
        binding.horizontalBarChart.legend.isEnabled = false
        binding.horizontalBarChart.setScaleEnabled(false)
        binding.horizontalBarChart.isDoubleTapToZoomEnabled = false
        binding.horizontalBarChart.setPinchZoom(false)
        binding.horizontalBarChart.setFitBars(true)

        binding.horizontalBarChart.invalidate()
        // 커스텀 Renderer 설정
        val barRenderer = CustomHorizontalBarChartRenderer(binding.horizontalBarChart, binding.horizontalBarChart.animator, binding.horizontalBarChart.viewPortHandler)
        barRenderer.setRadius(0) // 둥글기 반경 설정
        binding.horizontalBarChart.renderer = barRenderer

        // ValueFormatter를 사용하여 바 안에 값을 표시
        barDataSet.valueFormatter = BarValueFormatter()

        customMarkerView = CustomMarkerView(this, R.layout.marker_view,null,null)
        customMarkerView?.let {
            it.isHorizontal(true)
            it.chartView = binding.horizontalBarChart
            binding.horizontalBarChart.marker = it
        }
        binding.horizontalBarChart.invalidate()
    }

    class CustomBarChartRenderer(
        chart: CombinedChart,
        animator: ChartAnimator,
        viewPortHandler: ViewPortHandler
    ) : BarChartRenderer(chart as BarDataProvider, animator, viewPortHandler) {

        private var mRadius = 5f
        private val mBarShadowRectBuffer = RectF()

        fun setRadius(radius: Int) {
            this.mRadius = radius.toFloat()
        }

        override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
            val buffer = mBarBuffers[index]

            val trans = mChart.getTransformer(dataSet.axisDependency)
            mRenderPaint.color = dataSet.color
            mRenderPaint.style = Paint.Style.FILL

            val phaseX = mAnimator.phaseX
            val phaseY = mAnimator.phaseY

            // draw the bar shadow before the values
            if (mChart.isDrawBarShadowEnabled) {
                mShadowPaint.color = dataSet.barShadowColor
                val barData = mChart.barData
                val barWidth = barData.barWidth
                val barWidthHalf = barWidth / 2.0f
                var x: Float
                var i = 0
                val count = Math.min(Math.ceil((dataSet.entryCount.toFloat() * phaseX).toDouble()).toInt(), dataSet.entryCount)
                while (i < count) {
                    val e = dataSet.getEntryForIndex(i)
                    x = e.x
                    mBarShadowRectBuffer.left = x - barWidthHalf
                    mBarShadowRectBuffer.right = x + barWidthHalf
                    trans.rectValueToPixel(mBarShadowRectBuffer)
                    if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                        i++
                        continue
                    }
                    if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) break
                    mBarShadowRectBuffer.top = mViewPortHandler.contentTop()
                    mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom()
                    c.drawRoundRect(mBarShadowRectBuffer, mRadius, mRadius, mShadowPaint)
                    i++
                }
            }

            // initialize the buffer
            buffer.setPhases(phaseX, phaseY)
            buffer.setDataSet(index)
            buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
            buffer.setBarWidth(mChart.barData.barWidth)
            buffer.feed(dataSet)

            trans.pointValuesToPixel(buffer.buffer)

            val isSingleColor = dataSet.colors.size == 1

            if (isSingleColor) {
                mRenderPaint.color = dataSet.color
            }

            var j = 0
            while (j < buffer.size()) {
                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                    j += 4
                    continue
                }

                if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break

                if (!isSingleColor) {
                    mRenderPaint.color = dataSet.getColor(j / 4)
                }

                val path = Path()
                path.addRoundRect(RectF(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3]),
                    floatArrayOf(mRadius, mRadius, mRadius, mRadius, 0f, 0f, 0f, 0f), Path.Direction.CW)
                c.drawPath(path, mRenderPaint)

                j += 4
            }
        }

        override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {
            val barData = mChart.barData

            for (high in indices) {
                val set = barData.getDataSetByIndex(high.dataSetIndex)
                if (set == null || !set.isHighlightEnabled)
                    continue

                val e = set.getEntryForXValue(high.x, high.y) ?: continue

                val transformer = mChart.getTransformer(set.axisDependency)

                mHighlightPaint.color = Color.RED // highlight color
                mHighlightPaint.alpha = 255 // fully opaque

                val isStack = high.stackIndex >= 0 && e.isStacked

                val y1: Float
                val y2: Float

                if (isStack) {
                    if (mChart.isHighlightFullBarEnabled) {
                        y1 = e.positiveSum
                        y2 = -e.negativeSum
                    } else {
                        val range = e.ranges[high.stackIndex]
                        y1 = range.from
                        y2 = range.to
                    }
                } else {
                    y1 = e.y
                    y2 = 0f
                }

                prepareBarHighlight(e.x, y1, y2, barData.barWidth / 2f, transformer)

                setHighlightDrawPos(high, mBarRect)

                val path = Path()
                path.addRoundRect(mBarRect, floatArrayOf(mRadius, mRadius, mRadius, mRadius, 0f, 0f, 0f, 0f), Path.Direction.CW)
                c.drawPath(path, mHighlightPaint)
            }
        }
    }

    class CustomHorizontalBarChartRenderer(
        chart: HorizontalBarChart,
        animator: ChartAnimator,
        viewPortHandler: ViewPortHandler
    ) : HorizontalBarChartRenderer(chart, animator, viewPortHandler) {

        private var mRadius = 0f
        private val mBarShadowRectBuffer = RectF()
        private val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val highlightedEntries = mutableListOf<BarEntry>()

        fun setRadius(radius: Int) {
            this.mRadius = 0f
        }

        init {
            valuePaint.color = Color.BLACK
            valuePaint.textSize = 20f
            // 커스텀 폰트를 설정합니다. res/font 폴더에 custom_font.ttf 파일을 추가하세요.
            val typeface = ResourcesCompat.getFont(chart.context, R.font.suit_bold)
            valuePaint.typeface = typeface
        }

        override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
            val buffer = mBarBuffers[index]

            val trans = mChart.getTransformer(dataSet.axisDependency)
            mRenderPaint.color = dataSet.color
            mRenderPaint.style = Paint.Style.FILL

            val phaseX = mAnimator.phaseX
            val phaseY = mAnimator.phaseY

            // draw the bar shadow before the values
            if (mChart.isDrawBarShadowEnabled) {
                mShadowPaint.color = dataSet.barShadowColor
                val barData = mChart.barData
                val barWidth = barData.barWidth
                val barWidthHalf = barWidth / 2.0f
                var x: Float
                var i = 0
                val count = Math.min(Math.ceil((dataSet.entryCount.toFloat() * phaseX).toDouble()).toInt(), dataSet.entryCount)
                while (i < count) {
                    val e = dataSet.getEntryForIndex(i)
                    x = e.x
                    mBarShadowRectBuffer.left = x - barWidthHalf
                    mBarShadowRectBuffer.right = x + barWidthHalf
                    trans.rectValueToPixel(mBarShadowRectBuffer)
                    if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                        i++
                        continue
                    }
                    if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) break
                    mBarShadowRectBuffer.top = mViewPortHandler.contentTop()
                    mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom()
                    c.drawRoundRect(mBarShadowRectBuffer, mRadius, mRadius, mShadowPaint)
                    i++
                }
            }

            // initialize the buffer
            buffer.setPhases(phaseX, phaseY)
            buffer.setDataSet(index)
            buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
            buffer.setBarWidth(mChart.barData.barWidth)
            buffer.feed(dataSet)

            trans.pointValuesToPixel(buffer.buffer)

            val isSingleColor = dataSet.colors.size == 1

            if (isSingleColor) {
                mRenderPaint.color = dataSet.color
            }

            var j = 0
            while (j < buffer.size()) {
                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                    j += 4
                    continue
                }

                if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break

                if (!isSingleColor) {
                    mRenderPaint.color = dataSet.getColor(j / 4)
                }

                val path = Path()
                path.addRoundRect(RectF(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3]),
                    floatArrayOf(0f, 0f, 0f, 0f, mRadius, mRadius, mRadius, mRadius), Path.Direction.CW)
                c.drawPath(path, mRenderPaint)

                // Add value text inside the bar
                val entry = dataSet.getEntryForIndex(j / 4)
                val valueText = entry.y.toInt().toString() + "원"
                val textWidth = valuePaint.measureText(valueText)
                val textHeight = valuePaint.descent() - valuePaint.ascent()
                val yPos = buffer.buffer[j + 1] + (buffer.buffer[j + 3] - buffer.buffer[j + 1]) / 2 + textHeight / 2 - valuePaint.descent()

                valuePaint.color = Color.BLACK
                c.drawText(valueText, (buffer.buffer[j + 2] - textWidth - 5) / 1.8f, yPos, valuePaint)

                j += 4
            }
        }

        override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {
            val barData = mChart.barData

            highlightedEntries.clear()
            for (high in indices) {
                val set = barData.getDataSetByIndex(high.dataSetIndex)
                if (set == null || !set.isHighlightEnabled)
                    continue

                val e = set.getEntryForXValue(high.x, high.y) ?: continue
                highlightedEntries.add(e)

                val transformer = mChart.getTransformer(set.axisDependency)

                mHighlightPaint.color = Color.RED // highlight color
                mHighlightPaint.alpha = 255 // fully opaque

                val isStack = high.stackIndex >= 0 && e.isStacked

                val y1: Float
                val y2: Float

                if (isStack) {
                    if (mChart.isHighlightFullBarEnabled) {
                        y1 = e.positiveSum
                        y2 = -e.negativeSum
                    } else {
                        val range = e.ranges[high.stackIndex]
                        y1 = range.from
                        y2 = range.to
                    }
                } else {
                    y1 = e.y
                    y2 = 0f
                }

                prepareBarHighlight(e.x, y1, y2, barData.barWidth / 2f, transformer)

                setHighlightDrawPos(high, mBarRect)

                val path = Path()
                path.addRoundRect(mBarRect, floatArrayOf(0f, 0f, 0f, 0f, mRadius, mRadius, mRadius, mRadius), Path.Direction.CW)
                c.drawPath(path, mHighlightPaint)

                // Add highlighted value text inside the bar
                val valueText = e.y.toInt().toString() + "원"
                val textWidth = valuePaint.measureText(valueText)
                val textHeight = valuePaint.descent() - valuePaint.ascent()
                val yPos = mBarRect.top + (mBarRect.bottom - mBarRect.top) / 2 + textHeight / 2 - valuePaint.descent()

                valuePaint.color = Color.WHITE
                c.drawText(valueText, (mBarRect.right - textWidth - 5) / 1.8f, yPos, valuePaint)
            }
        }
    }

    class BarValueFormatter : ValueFormatter() {
        override fun getBarLabel(barEntry: BarEntry?): String {
//            return barEntry?.y?.toInt().toString() + " 원"
            return ""
        }
    }
}
