package com.quarantine.uploadlocation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import kotlinx.android.synthetic.main.activity_main.*
import androidx.work.WorkManager
import com.quarantine.uploadlocation.worker.NotificationWorker
import androidx.work.PeriodicWorkRequest
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // scheduling worker for start the work, job for this worker to recover the foreground service
        createWorkerAndEnqueueRequest()

        start_upload_location.setOnClickListener { view ->
            startService()
        }
    }


    private fun createWorkerAndEnqueueRequest() {
        val mWorkManager = WorkManager.getInstance()
        // We want to keep the work if its already there. If we keep on changing the request, we might never execute the work.
        val existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE

        val mRequest =
            PeriodicWorkRequest.Builder(NotificationWorker::class.java, 3, TimeUnit.SECONDS).build()

        mWorkManager.enqueueUniquePeriodicWork(
            NotificationWorker.WORK,
            existingPeriodicWorkPolicy,
            mRequest
        )
    }

    // for starting the service
    private fun startService() {
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
