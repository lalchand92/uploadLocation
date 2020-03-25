package com.quarantine.data.remote

import com.quarantine.data.remote.model.response.Location
import retrofit2.Call
import retrofit2.http.*

interface LocationService {

    @GET("/LatLng")
    fun getLocation(@Query("DeviceId") deviceId: String) : Call<Location>

    @POST("/LatLng")
    @Headers("Content-Type: application/json")
    fun postLocation(@Body body: Location): Call<Location>
}