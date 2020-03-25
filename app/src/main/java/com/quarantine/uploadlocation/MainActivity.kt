package com.quarantine.uploadlocation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        start_upload_location.setOnClickListener { view ->
            startService()
        }
    }


    // for starting the service
    fun startService() {
        val serviceIntent = Intent(this, ForegroundService::class.java)
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    // for stopping the service
    fun stopService() {
        val serviceIntent = Intent(this, ForegroundService::class.java)
        stopService(serviceIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
