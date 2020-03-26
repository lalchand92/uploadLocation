package com.quarantine.uploadlocation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import kotlinx.android.synthetic.main.activity_main.*
import androidx.work.WorkManager
import com.quarantine.uploadlocation.worker.NotificationWorker
import androidx.work.PeriodicWorkRequest
import com.quarantine.uploadlocation.utils.Constants
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // scheduling worker for start the work, job for this worker to recover the foreground service
        createWorkerAndEnqueueRequest()

        start_upload_location.setOnClickListener { view ->
            val etDeviceId = findViewById<EditText>(R.id.et_device_id)
            val deviceId = etDeviceId.text.toString()
            if(Constants.DEVICE_ID == "hardCoded"){
                if(!deviceId.isNullOrEmpty()) {
                    Constants.DEVICE_ID = deviceId
                    etDeviceId.isEnabled = false
                    Constants.startService(applicationContext)
                }else{
                    Toast.makeText(this, "Please enter your id ", Toast.LENGTH_LONG).show()
                }
            }else{
                Constants.startService(applicationContext)
            }
        }
    }


    private fun createWorkerAndEnqueueRequest() {
        val mWorkManager = WorkManager.getInstance()
        // We want to keep the work if its already there. If we keep on changing the request, we might never execute the work.
        val existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE

        val mRequest =
            PeriodicWorkRequest.Builder(NotificationWorker::class.java, 5, TimeUnit.MINUTES).build()

        mWorkManager.enqueueUniquePeriodicWork(
            NotificationWorker.WORK,
            existingPeriodicWorkPolicy,
            mRequest
        )
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
