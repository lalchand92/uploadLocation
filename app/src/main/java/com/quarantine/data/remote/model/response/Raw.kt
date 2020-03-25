package com.quarantine.data.remote.model.response

import com.google.gson.annotations.SerializedName

class Raw {
    @SerializedName("DeviceId")
    var deviceId: String? = null
    @SerializedName("Lng")
    var lng = 0.0
    @SerializedName("Lat")
    var lat = 0.0
}