package com.quarantine.data.base

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceHelper {

    companion object {

        private const val BASE_API_HOST = "http://35.200.182.243/DkTrackApi/api/"
        private val mClient = OkHttpClient()



        private val okHttpClient: OkHttpClient = mClient.newBuilder()
            .addInterceptor(getLoggingInterceptor())
            .readTimeout(45, TimeUnit.SECONDS)
            .connectTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(45, TimeUnit.SECONDS)
            .build()

        private fun getLoggingInterceptor(): HttpLoggingInterceptor {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_API_HOST)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun getNetworkClient(): Retrofit {
            return retrofit
        }
    }

}