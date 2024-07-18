package com.example.listedtask.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import java.io.Serializable

@Keep
data class Dashboard(
    @Json(name = "status") var status: Boolean,
    @Json(name = "statusCode") var statusCode: Int? = null,
    @Json(name = "message") var message: String? = null,
    @Json(name = "support_whatsapp_number") var supportWhatsappNumber: String? = null,
    @Json(name = "extra_income") var extraIncome: Double? = null,
    @Json(name = "total_links") var totalLinks: Int? = null,
    @Json(name = "total_clicks") var totalClicks: Int? = null,
    @Json(name = "today_clicks") var todayClicks: Int? = null,
    @Json(name = "top_source") var topSource: String? = null,
    @Json(name = "top_location") var topLocation: String? = null,
    @Json(name = "startTime") var startTime: String? = null,
    @Json(name = "links_created_today") var linksCreatedToday: Int? = null,
    @Json(name = "applied_campaign") var appliedCampaign: Int? = null,
    @Json(name = "data") var data: Data? = Data()
) : Serializable