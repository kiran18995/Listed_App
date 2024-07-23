package com.kiran.listedapp.data.models

import android.os.Parcelable
import com.example.listedtask.models.RecentLinks
import com.example.listedtask.models.TopLinks
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    @SerializedName("recent_links") var recentLinks: List<RecentLinks> = arrayListOf(),
    @SerializedName("top_links") var topLinks: List<TopLinks> = arrayListOf(),
    @SerializedName("overall_url_chart") var overallUrlChart: MutableMap<String, Int>? = mutableMapOf()
) : Parcelable