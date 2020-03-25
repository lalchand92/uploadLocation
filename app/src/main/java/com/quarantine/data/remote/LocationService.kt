package com.quarantine.data.remote

import com.quarantine.data.remote.model.response.Location
import retrofit2.Call
import retrofit2.http.*

interface LocationService {

    @GET("LatLng")
    fun getLocation(@Query("DeviceId") deviceId: String) : Call<Location>

    @Headers("Content-Type: application/json")
    @POST("LatLng")
    fun postLocation(@Header("Authorization") authorization: String, @Body body: Location): Call<Location>
}