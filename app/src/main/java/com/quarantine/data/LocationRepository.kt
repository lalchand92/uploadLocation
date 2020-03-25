package com.quarantine.data

import android.util.Log
import com.quarantine.data.remote.LocationService
import com.quarantine.data.remote.model.response.Location
import com.quarantine.data.remote.model.response.Raw
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class LocationRepository(private val locationService: LocationService) {

    fun getLocation() {
        val call = locationService.getLocation(getDeviceId())
        call.enqueue(object : Callback, retrofit2.Callback<Location> {
            override fun onResponse(call: Call<Location>, response: Response<Location>) {
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
            }
        })
    }

    fun postLocation(lat: Double, lng: Double) {
        val locationRequest = Location()
        locationRequest.mode = "raw"
        val raw = Raw()
        raw.deviceId = getDeviceId()
        raw.lat = lat
        raw.lng = lng
        locationRequest.raw = raw

        val call = locationService.postLocation(getToken(),locationRequest)
        call.enqueue(object : Callback, retrofit2.Callback<Location> {
            override fun onResponse(call: Call<Location>, response: Response<Location>) {
                Log.d("Forground service : ", "success response $response")
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
                Log.d("Forground service : ", "fail response  ${t.message},  ${t.printStackTrace()}")
            }
        })
    }

    fun getDeviceId(): String {
        return "Lalchand"
    }

    fun getToken(): String {
        return "bearer 9AdLfiAWmOf5vBj2j2pjmWAHa6jPxldUPRllwnYrdNTbR_EpfcAjVSrgseGrictMwyTysPyS9WrxVSZsTZq0f-ApR6g3lzb5au2FbQVsXlUnQ1s8yDC9hqw42a19tG9sWzp9j_707_F2UblJX_VtbBuljykddQLjE-jzNEgV56HNByt08WhdaWcK28LMAn-ZNrHrz5Y4jfptBRkiD_Yw81_6HBXAmx_VWtZKrVrkLjq5TtXkz1KSGzV1madu4yCZ"
    }
}