package com.example.listedtask.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import java.io.Serializable

@Keep
data class Data(
    @Json(name = "recent_links") var recentLinks: List<RecentLinks> = arrayListOf(),
    @Json(name = "top_links") var topLinks: List<TopLinks> = arrayListOf(),
    @Json(name = "overall_url_chart") var overallUrlChart: MutableMap<String, Int>? = mutableMapOf()
) : Serializable