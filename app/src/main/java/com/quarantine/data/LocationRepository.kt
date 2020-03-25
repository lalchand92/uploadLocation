package com.quarantine.data

import com.quarantine.data.base.ServiceHelper
import com.quarantine.data.remote.LocationService
import com.quarantine.data.remote.model.response.Location
import com.quarantine.data.remote.model.response.Raw
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

object LocationRepository {

    var locationService: LocationService? = null

    fun getLocation() {
        if ( locationService == null ) { locationService = ServiceHelper.getNetworkClient().create(LocationService::class.java) }
        val call = locationService?.getLocation(getDeviceId())
        call?.enqueue(object : Callback, retrofit2.Callback<Location> {
            override fun onResponse(call: Call<Location>, response: Response<Location>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun postLocation(lat: Double, lng: Double) {
        if ( locationService == null ) { locationService = ServiceHelper.getNetworkClient().create(LocationService::class.java) }

        val locationRequest = Location()
        locationRequest.mode = "raw"
        val raw = Raw()
        raw.deviceId = getDeviceId()
        raw.lat = lat
        raw.lng = lng
        locationRequest.raw = raw

        val call = locationService?.postLocation(locationRequest)
        call?.enqueue(object : Callback, retrofit2.Callback<Location> {
            override fun onResponse(call: Call<Location>, response: Response<Location>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun getDeviceId(): String {
        return "Naveen"
    }
}