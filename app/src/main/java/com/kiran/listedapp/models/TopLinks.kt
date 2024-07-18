package com.example.listedtask.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import java.io.Serializable

@Keep
data class TopLinks(
    @Json(name = "url_id") var urlId: Int? = null,
    @Json(name = "web_link") var webLink: String? = null,
    @Json(name = "smart_link") var smartLink: String? = null,
    @Json(name = "title") var title: String? = null,
    @Json(name = "total_clicks") var totalClicks: Int? = null,
    @Json(name = "original_image") var originalImage: String? = null,
    @Json(name = "thumbnail") var thumbnail: String? = null,
    @Json(name = "times_ago") var timesAgo: String? = null,
    @Json(name = "created_at") var createdAt: String? = null,
    @Json(name = "domain_id") var domainId: String? = null,
    @Json(name = "url_prefix") var urlPrefix: String? = null,
    @Json(name = "url_suffix") var urlSuffix: String? = null,
    @Json(name = "app") var app: String? = null
) : Serializable