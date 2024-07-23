package com.example.listedtask.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Data(
    @SerializedName("recent_links") var recentLinks: List<RecentLinks> = arrayListOf(),
    @SerializedName("top_links") var topLinks: List<TopLinks> = arrayListOf(),
    @SerializedName("overall_url_chart") var overallUrlChart: MutableMap<String, Int>? = mutableMapOf()
) : Parcelable