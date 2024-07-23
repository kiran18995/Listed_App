package com.kiran.listedapp.ui.links

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.kiran.listedapp.R
import com.kiran.listedapp.databinding.FragmentLinksBinding
import com.kiran.listedapp.data.models.Dashboard
import com.kiran.listedapp.utils.GradientFillDrawable
import com.kiran.listedapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val URI_STRING: String = "smsto:"
private const val PACKAGE_NAME: String = "com.whatsapp"

@AndroidEntryPoint
class LinksFragment : Fragment() {
    private var _binding: FragmentLinksBinding? = null
    private lateinit var topLinksAdapter: TopLinksAdapter
    private lateinit var recentLinksAdapter: RecentLinksAdapter
    private val viewModel by viewModels<LinksViewModel>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLinksBinding.inflate(inflater, container, false)
        setupAdapters()
        focusChangeListenersInit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.greetings.collect {
                binding.greetings.text = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dashboardData.collect {
                when (it) {
                    is Resource.Error -> {
                        binding.dashboardContainer.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Loading -> {
                        binding.dashboardContainer.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.dashboardContainer.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        updateDataToViews(it.dataFetched)
                    }
                }
            }
        }
    }

    private fun setupAdapters() {
        topLinksAdapter =
            TopLinksAdapter(object : TopLinksAdapter.ItemClickListener {
                override fun onItemClick(smartLink: String?) {
                    smartLinkCopied(smartLink)
                }
            })
        binding.topLinkList.layoutManager = LinearLayoutManager(requireContext())
        binding.topLinkList.adapter = topLinksAdapter

        recentLinksAdapter =
            RecentLinksAdapter(object : RecentLinksAdapter.ItemClickListener {
                override fun onItemClick(smartLink: String?) {
                    smartLinkCopied(smartLink)
                }
            })
        binding.recentLinkList.layoutManager = LinearLayoutManager(requireContext())
        binding.recentLinkList.adapter = recentLinksAdapter
    }

    private fun smartLinkCopied(smartLink: String?) {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(getString(R.string.label), smartLink)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(
            context, getString(R.string.clipboard_message), Toast.LENGTH_SHORT
        ).show()
    }

    private fun focusChangeListenersInit() {
        binding.apply {
            topLinks.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    topLinks.setTextColor(resources.getColor(R.color.white, null))
                    topLinkList.visibility = View.VISIBLE
                } else {
                    topLinks.setTextColor(resources.getColor(R.color.not_select_tab, null))
                    topLinkList.visibility = View.INVISIBLE
                }
            }

            recentLinks.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    recentLinks.setTextColor(resources.getColor(R.color.white, null))
                    recentLinkList.visibility = View.VISIBLE
                } else {
                    recentLinks.setTextColor(resources.getColor(R.color.not_select_tab, null))
                    recentLinkList.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun updateDataToViews(dashboard: Dashboard) {
        loadGraphData(dashboard.data?.overallUrlChart!!)
        binding.apply {
            clickCount.text = dashboard.todayClicks.toString()
            locationName.text = dashboard.topLocation
            socialName.text = dashboard.topSource
            bestTime.text = dashboard.startTime
            talkWithUs.setOnClickListener {
                val uri = Uri.parse(URI_STRING + dashboard.supportWhatsappNumber)
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.setPackage(PACKAGE_NAME)
                startActivity(Intent.createChooser(intent, ""))
            }
        }
        binding.topLinks.isSelected = true
        binding.topLinks.requestFocus()
        topLinksAdapter.setChange(dashboard.data?.topLinks ?: emptyList())
        recentLinksAdapter.setChange(dashboard.data?.recentLinks ?: emptyList())
    }

    private fun loadGraphData(overallUrlChart: MutableMap<String, Int>) {
        val months = overallUrlChart.keys.toMutableList()
        val values = overallUrlChart.values.toMutableList()

        val entries = mutableListOf<Entry>()
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

        binding.apply {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}