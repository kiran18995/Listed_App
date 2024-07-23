package com.kiran.listedapp.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.kiran.listedapp.R

private const val GRADIENT_COLOR = "#84B6FF"
class GradientFillDrawable : Drawable() {
    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val paint = Paint().apply {
            shader = LinearGradient(
                bounds.left.toFloat(),
                bounds.top.toFloat(),
                bounds.left.toFloat(),
                bounds.bottom.toFloat(),
                Color.parseColor(GRADIENT_COLOR),
                Color.TRANSPARENT,
                Shader.TileMode.MIRROR
            )
            style = Paint.Style.FILL
        }
        canvas.drawRect(bounds, paint)
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(colorFilter: ColorFilter?) {}

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}

fun LineChart.setStyle(context : Context, overallUrlChart: MutableMap<String, Int>) {
    val months = overallUrlChart.keys.toMutableList()
    val values = overallUrlChart.values.toMutableList()

    val entries = mutableListOf<Entry>()
    for (i in months.indices) {
        entries.add(Entry(i.toFloat(), values[i].toFloat()))
    }
    val dataSet = LineDataSet(entries, "")
    dataSet.color = ContextCompat.getColor(context, R.color.primary_color)
    dataSet.lineWidth = 2f
    dataSet.circleRadius = 4f
    dataSet.setDrawValues(false)
    dataSet.valueTextSize = 12f
    dataSet.setDrawFilled(true)
    val fillDrawable = GradientFillDrawable()
    dataSet.fillDrawable = fillDrawable
    dataSet.setDrawCircles(false)
    val rightAxis = this.axisRight
    rightAxis.isEnabled = false
    // Create a LineData object and set the data set
    val lineData = LineData(dataSet)
    // Set the data to the chart
    this.data = lineData
    // Customize the x-axis labels
    val xAxis: XAxis = this.xAxis
    xAxis.valueFormatter = IndexAxisValueFormatter(months)
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.setDrawAxisLine(false)
    val legend: Legend = this.legend
    legend.isEnabled = true // Enable the legend
    legend.setDrawInside(false) // Place the legend outside the chart
    legend.form = Legend.LegendForm.NONE
    this.description.isEnabled = false
    // Refresh the chart
    this.invalidate()
}