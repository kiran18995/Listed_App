package com.kiran.listedapp.ui.links

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listedtask.models.TopLinks
import com.kiran.listedapp.R
import java.time.format.DateTimeFormatter

class TopLinksAdapter(
    private val context: Context,
    private val topLinksList: List<TopLinks>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<TopLinksAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(smartLink: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_link_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return topLinksList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(topLinksList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val logo = itemView.findViewById<ImageView>(R.id.logo)
        private val linkName = itemView.findViewById<TextView>(R.id.link_name)
        private val linkDate = itemView.findViewById<TextView>(R.id.link_date)
        private val noClicks = itemView.findViewById<TextView>(R.id.no_clicks)
        private val linkUrl = itemView.findViewById<TextView>(R.id.link_url)
        private val copyLink = itemView.findViewById<ImageView>(R.id.copy_to_clipboard)

        fun bind(topLinksList: TopLinks) {
            Glide.with(context).load(topLinksList.originalImage).into(logo)
            linkName.text = topLinksList.title
            linkDate.text = filterDate(topLinksList.createdAt)
            noClicks.text = topLinksList.totalClicks.toString()
            linkUrl.text = topLinksList.smartLink
            copyLink.setOnClickListener {
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