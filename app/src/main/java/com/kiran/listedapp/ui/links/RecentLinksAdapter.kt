package com.kiran.listedapp.ui.links

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listedtask.models.RecentLinks
import com.kiran.listedapp.databinding.ItemLinkListBinding
import java.time.format.DateTimeFormatter

const val DELIMITER = "T"
const val DATE_PATTERN = "yyyy-MM-dd"
const val DEFAULT_DATE = "2022-08-09"
const val DATE_OF_PATTERN = "dd MMM yyyy"

class RecentLinksAdapter(
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<RecentLinksAdapter.ViewHolder>() {
    private var recentLinkList: List<RecentLinks> = emptyList()

    interface ItemClickListener {
        fun onItemClick(smartLink: String?)
    }

    fun setChange(recentLinkList: List<RecentLinks>) {
        this.recentLinkList = recentLinkList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLinkListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return recentLinkList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recentLinkList[position])
    }

    inner class ViewHolder(private val binding: ItemLinkListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recentLinksList: RecentLinks) {
            Glide.with(binding.logo.context).load(recentLinksList.originalImage).into(binding.logo)
            binding.linkName.text = recentLinksList.title
            binding.linkDate.text = filterDate(recentLinksList.createdAt)
            binding.noClicks.text = recentLinksList.totalClicks.toString()
            binding.linkUrl.text = recentLinksList.smartLink
            binding.linkCopy.setOnClickListener {
                itemClickListener.onItemClick(recentLinksList.smartLink)
            }
        }

        private fun filterDate(timestamp: String?): String {
            val dateTime = timestamp?.split(DELIMITER)?.toTypedArray()
            val format = DateTimeFormatter.ofPattern(DATE_PATTERN)
            val date = format.parse(dateTime?.get(0) ?: DEFAULT_DATE)
            return DateTimeFormatter.ofPattern(DATE_OF_PATTERN).format(date)
        }
    }
}