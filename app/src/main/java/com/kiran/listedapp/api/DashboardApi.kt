package com.example.listedtask.api

import com.example.listedtask.models.Dashboard
import retrofit2.http.GET

interface DashboardApi {
    @GET("api/v1/dashboardNew")
    suspend fun getData(): Dashboard

}