package com.kiran.listedapp.data.repository

import com.kiran.listedapp.api.DashboardApi
import com.kiran.listedapp.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DashboardRepository @Inject constructor(private val dashboardApi: DashboardApi) {
    suspend fun getDashboardData() = flow {
        try {
            val response = dashboardApi.getDashboardData()
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}