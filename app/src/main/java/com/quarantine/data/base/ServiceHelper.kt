package com.quarantine.data.base

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceHelper {

    companion object {

        private const val BASE_API_HOST = "http://35.200.182.243/DkTrackApi/api"
        private val mClient = OkHttpClient()

        private val okHttpClient: OkHttpClient = mClient.newBuilder()
                .addInterceptor(ServiceInterceptor())
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .build()

        private val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_API_HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        fun getNetworkClient() : Retrofit {
            return retrofit
        }
    }

    class ServiceInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
                request = request.newBuilder()
                        .addHeader("Authorization", "bearer 9AdLfiAWmOf5vBj2j2pjmWAHa6jPxldUPRllwnYrdNTbR_EpfcAjVSrgseGrictMwyTysPyS9WrxVSZsTZq0f-ApR6g3lzb5au2FbQVsXlUnQ1s8yDC9hqw42a19tG9sWzp9j_707_F2UblJX_VtbBuljykddQLjE-jzNEgV56HNByt08WhdaWcK28LMAn-ZNrHrz5Y4jfptBRkiD_Yw81_6HBXAmx_VWtZKrVrkLjq5TtXkz1KSGzV1madu4yCZ")
                        .build()
            return chain.proceed(request)
        }
    }
}