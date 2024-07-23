package com.kiran.listedapp.api

import com.kiran.listedapp.models.Dashboard
import com.kiran.listedapp.BuildConfig
import retrofit2.http.GET

interface DashboardApi {
    companion object {
        const val BASE_URL = BuildConfig.BASE_URL
    }

    @GET("api/v1/dashboardNew")
    suspend fun getDashboardData(): Dashboard
}