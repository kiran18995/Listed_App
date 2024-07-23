package com.kiran.listedapp.di

import android.content.Context
import android.content.SharedPreferences
import com.kiran.listedapp.api.DashboardApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val AUTHORIZATION = "Authorization"
    private const val BEARER = "Bearer"
    private const val TIMEOUT = 120L
    private const val PREFERENCE_NAME = "com.kiran.listedApp.PREFERENCE_FILE_KEY"
    private const val API_READ_ACCESS_TOKEN_KEY = "API_READ_ACCESS_TOKEN"

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideRetrofit(sharedPreferences: SharedPreferences): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val authToken = sharedPreferences.getString(API_READ_ACCESS_TOKEN_KEY, "")

        val authInterceptor = Interceptor { chain ->
            val request =
                chain.request().newBuilder().addHeader(AUTHORIZATION, "$BEARER $authToken").build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder().readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS).addInterceptor(logging)
            .addInterceptor(authInterceptor).build()

        return Retrofit.Builder().baseUrl(DashboardApi.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideMoviesAPi(retrofit: Retrofit): DashboardApi =
        retrofit.create(DashboardApi::class.java)
}