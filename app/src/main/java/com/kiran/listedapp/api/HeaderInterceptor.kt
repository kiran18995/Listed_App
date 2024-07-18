package com.example.listedtask.api

import com.kiran.listedapp.ListedApplication
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = ListedApplication.sharedPreferences.getString("token", "")
        val request = chain.request().newBuilder().addHeader(
                "Authorization", "Bearer $token"
            ).build()
        return chain.proceed(request)
    }
}