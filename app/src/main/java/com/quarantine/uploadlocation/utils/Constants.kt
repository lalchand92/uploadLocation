package com.quarantine.uploadlocation.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.quarantine.uploadlocation.ForegroundService

object Constants {
    const val MESSAGE_STATUS = "message_status"


    // for starting the service
    fun startService(context : Context) {
        val serviceIntent = Intent(context, ForegroundService::class.java)
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
        ContextCompat.startForegroundService(context, serviceIntent)
    }
}