package com.quarantine.uploadlocation.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.quarantine.uploadlocation.ForegroundService
import java.util.*

object Constants {
    const val MESSAGE_STATUS = "message_status"
    var DEVICE_ID = "hardCoded"

    // for starting the service
    fun startService(context : Context) {
        val serviceIntent = Intent(context, ForegroundService::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)
    }
}