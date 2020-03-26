package com.quarantine.data

import android.util.Log
import com.quarantine.data.remote.LocationService
import com.quarantine.data.remote.model.response.Location
import com.quarantine.data.remote.model.response.Raw
import com.quarantine.uploadlocation.utils.Constants
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class LocationRepository(private val locationService: LocationService) {

    fun getLocation() {
        val call = locationService.getLocation(Constants.DEVICE_ID)
        call.enqueue(object : Callback, retrofit2.Callback<Location> {
            override fun onResponse(call: Call<Location>, response: Response<Location>) {
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
            }
        })
    }

    fun postLocation(lat: Double, lng: Double) {
        if(Constants.DEVICE_ID != "hardCoded") {
            val locationRequest = Location()
            locationRequest.deviceId = Constants.DEVICE_ID
            locationRequest.lat = lat
            locationRequest.lng = lng

            val call = locationService.postLocation(locationRequest)
            call.enqueue(object : Callback, retrofit2.Callback<Location> {
                override fun onResponse(call: Call<Location>, response: Response<Location>) {
                    Log.d("Forground service : ", "success response $response")

                }

                override fun onFailure(call: Call<Location>, t: Throwable) {
                    Log.d(
                        "Forground service : ",
                        "fail response  ${t.message},  ${t.printStackTrace()}"
                    )
                }
            })
        }
    }
}