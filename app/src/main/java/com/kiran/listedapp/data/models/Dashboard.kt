package com.kiran.listedapp.data.models

import android.os.Parcelable
import com.example.listedtask.models.Data
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dashboard(
    @SerializedName("status") var status: Boolean,
    @SerializedName("statusCode") var statusCode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("support_whatsapp_number") var supportWhatsappNumber: String? = null,
    @SerializedName("extra_income") var extraIncome: Double? = null,
    @SerializedName("total_links") var totalLinks: Int? = null,
    @SerializedName("total_clicks") var totalClicks: Int? = null,
    @SerializedName("today_clicks") var todayClicks: Int? = null,
    @SerializedName("top_source") var topSource: String? = null,
    @SerializedName("top_location") var topLocation: String? = null,
    @SerializedName("startTime") var startTime: String? = null,
    @SerializedName("links_created_today") var linksCreatedToday: Int? = null,
    @SerializedName("applied_campaign") var appliedCampaign: Int? = null,
    @SerializedName("data") var data: Data? = null
) : Parcelable