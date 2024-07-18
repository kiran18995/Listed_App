package com.kiran.listedapp.ui.links

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listedtask.models.RecentLinks
import com.example.listedtask.models.TopLinks
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.kiran.listedapp.R
import com.kiran.listedapp.databinding.FragmentLinksBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private const val URI_STRING: String = "smsto:"
private const val PACKAGE_NAME: String = "com.whatsapp"
private const val GRADIENT_COLOR = "#84B6FF"
private const val GOOD_MORNING = "Good morning"
private const val GOOD_EVENING = "Good evening"
private const val GOOD_AFTERNOON = "Good afternoon"
private const val GOOD_NIGHT = "Good night"
private const val TAG = "LinksFragment"

class LinksFragment : Fragment() {
    private var _binding: FragmentLinksBinding? = null
    private lateinit var topLinksAdapter: TopLinksAdapter
    private var topLinks = mutableListOf<TopLinks>()
    private var recentLinks = mutableListOf<RecentLinks>()
    private lateinit var recentLinksAdapter: RecentLinksAdapter
    private lateinit var lineChart: LineChart

    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val linksViewModel = ViewModelProvider(this)[LinksViewModel::class.java]
        _binding = FragmentLinksBinding.inflate(inflater, container, false)
        linksViewModel.getData()
        if (!linksViewModel.isLoaded) {
            val anim = AnimationUtils.loadAnimation(context, R.anim.slide_up)
            binding.dashboardData.startAnimation(anim)
        }

        val currentTime = LocalTime.now()

        val greeting = when (currentTime.hour) {
            in 6..11 -> GOOD_MORNING
            in 12..16 -> GOOD_AFTERNOON
            in 17..20 -> GOOD_EVENING
            else -> GOOD_NIGHT
        }

        binding.greetings.text = greeting

        linksViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        linksViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Network error message: $errorMessage")
            }
        }
        lineChart = binding.lineChart
        linksViewModel.dashboard.observe(viewLifecycleOwner) { dashboard ->
            loadGraphData(dashboard.data?.overallUrlChart!!)
            topLinks.clear()
            topLinks.addAll(dashboard.data?.topLinks ?: emptyList())
            topLinksAdapter.notifyDataSetChanged()
            recentLinks.clear()
            recentLinks.addAll(dashboard.data?.recentLinks ?: emptyList())
            recentLinksAdapter.notifyDataSetChanged()
            binding.clickCount.text = dashboard.todayClicks.toString()
            binding.locationName.text = dashboard.topLocation
            binding.socialName.text = dashboard.topSource
            binding.bestTime.text = dashboard.startTime
            binding.talkWithUs.setOnClickListener {
                val uri = Uri.parse(URI_STRING + dashboard.supportWhatsappNumber)
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.setPackage(PACKAGE_NAME)
                startActivity(Intent.createChooser(intent, ""))
            }
        }

        adaptersInit()

        focusChangeListenersInit()

        binding.topLinkList.adapter = topLinksAdapter
        binding.topLinkList.layoutManager = LinearLayoutManager(context)
        binding.recentLinkList.adapter = recentLinksAdapter
        binding.recentLinkList.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    private fun loadGraphData(overallUrlChart: MutableMap<String, Int>) {
        var months = overallUrlChart.keys.toMutableList()
        val values = overallUrlChart.values.toMutableList()

        val entries = mutableListOf<Entry>()
        months = convertToMonthFormat(months)
        for (i in months.indices) {
            entries.add(Entry(i.toFloat(), values[i].toFloat()))
        }

        // Create a data set and customize it
        val dataSet = LineDataSet(entries, "")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.primary_color)
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawValues(false)
        dataSet.valueTextSize = 12f
        dataSet.setDrawFilled(true)

        val fillDrawable = GradientFillDrawable()
        dataSet.fillDrawable = fillDrawable

        dataSet.setDrawCircles(false)
        val rightAxis = lineChart.axisRight
        rightAxis.isEnabled = false
        // Create a LineData object and set the data set
        val lineData = LineData(dataSet)
        // Set the data to the chart
        lineChart.data = lineData
        // Customize the x-axis labels
        val xAxis: XAxis = lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false)
        val legend: Legend = lineChart.legend
        legend.isEnabled = true // Enable the legend
        legend.setDrawInside(false) // Place the legend outside the chart
        legend.form = Legend.LegendForm.NONE
        lineChart.description.isEnabled = false
        // Refresh the chart
        lineChart.invalidate()
    }

    private fun convertToMonthFormat(dates: MutableList<String>): MutableList<String> {
        val formatter = DateTimeFormatter.ofPattern(getString(R.string.yyyy_mm_dd))
        val outputFormatter = DateTimeFormatter.ofPattern(getString(R.string.mmm))

        return dates.map { date ->
            val localDate = LocalDate.parse(date, formatter)
            localDate.format(outputFormatter)
        }.toMutableList()
    }

    private class GradientFillDrawable : Drawable() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun adaptersInit() {
        topLinksAdapter =
            TopLinksAdapter(requireContext(), topLinks, object : TopLinksAdapter.ItemClickListener {
                override fun onItemClick(smartLink: String?) {
                    val clipboardManager =
                        requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText(getString(R.string.label), smartLink)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(
                        context, getString(R.string.clipboard_message), Toast.LENGTH_SHORT
                    ).show()
                }
            })

        recentLinksAdapter = RecentLinksAdapter(
            requireContext(),
            recentLinks,
            object : RecentLinksAdapter.ItemClickListener {
                override fun onItemClick(smartLink: String?) {
                    val clipboardManager =
                        requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText(getString(R.string.label), smartLink)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(
                        context, getString(R.string.clipboard_message), Toast.LENGTH_SHORT
                    ).show()
                }
            })

        binding.topLinks.isSelected = true
        binding.topLinks.requestFocus()
    }

    private fun focusChangeListenersInit() {
        binding.topLinks.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.topLinks.setTextColor(resources.getColor(R.color.white, null))
                binding.topLinkList.visibility = View.VISIBLE
                binding.topLinkList.animate().alpha(1f).translationY(1f).setDuration(100)
                    .setInterpolator(AccelerateDecelerateInterpolator()).start()
            } else {
                binding.topLinks.setTextColor(resources.getColor(R.color.not_select_tab, null))
                binding.topLinkList.visibility = View.INVISIBLE
                binding.topLinkList.alpha = 0f
                binding.topLinkList.translationY = 100f
            }
        }

        binding.recentLinks.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.recentLinks.setTextColor(resources.getColor(R.color.white, null))
                binding.recentLinkList.visibility = View.VISIBLE
                binding.recentLinkList.animate().alpha(1f).translationY(1f).setDuration(100)
                    .setInterpolator(AccelerateDecelerateInterpolator()).start()
            } else {
                binding.recentLinks.setTextColor(resources.getColor(R.color.not_select_tab, null))
                binding.recentLinkList.visibility = View.INVISIBLE
                binding.recentLinkList.alpha = 0f
                binding.recentLinkList.translationY = 100f
            }
        }
    }
}