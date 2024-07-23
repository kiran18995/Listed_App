package com.kiran.listedapp.ui.links

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listedtask.models.TopLinks
import com.kiran.listedapp.databinding.ItemLinkListBinding
import java.time.format.DateTimeFormatter

class TopLinksAdapter(
    private val context: Context,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<TopLinksAdapter.ViewHolder>() {

    private var topLinksList: List<TopLinks> = emptyList()

    interface ItemClickListener {
        fun onItemClick(smartLink: String?)
    }

    fun setChange(topLinksList: List<TopLinks>) {
        this.topLinksList = topLinksList
        notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLinkListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return topLinksList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(topLinksList[position])
    }

    inner class ViewHolder(private val binding: ItemLinkListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(topLinksList: TopLinks) {
            Glide.with(context).load(topLinksList.originalImage).into(binding.logo)
            binding.linkName.text = topLinksList.title
            binding.linkDate.text = filterDate(topLinksList.createdAt)
            binding.noClicks.text = topLinksList.totalClicks.toString()
            binding.linkUrl.text = topLinksList.smartLink
            binding.linkCopy.setOnClickListener {
                itemClickListener.onItemClick(topLinksList.smartLink)
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