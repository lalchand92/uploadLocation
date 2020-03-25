package com.quarantine.data.remote.model.response

import com.google.gson.annotations.SerializedName

class Location {
    @SerializedName("mode")
    var mode: String? = null
    @SerializedName("raw")
    var raw: Raw? = null
}