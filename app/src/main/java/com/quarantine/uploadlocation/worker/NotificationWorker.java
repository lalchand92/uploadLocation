package com.quarantine.uploadlocation.worker;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.quarantine.uploadlocation.utils.Constants;

import static android.content.Context.ACTIVITY_SERVICE;

public class NotificationWorker extends Worker {
    public static final String WORK= "NotificationWorker";
    private static final String WORK_RESULT = "work_result";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // start foreground service
        boolean serviceRunning = isServiceRunning();
        Log.d("Forground service : ", "from: do work got the callback, serviceRunning " + serviceRunning);
        if(!serviceRunning) {
            Constants.INSTANCE.startService(getApplicationContext());
            Log.d("Forground service : ", "from do work started service ");
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Do nothing
        }

        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();
        return Result.success(outputData);
    }

    private boolean isServiceRunning() {
        try {
            ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                if("com.quarantine.uploadlocation.ForegroundService".equals(service.service.getClassName())) {
                    return true;
                }
            }
        } catch (SecurityException e) {
            // Do nothing
        }
        return false;
    }
}