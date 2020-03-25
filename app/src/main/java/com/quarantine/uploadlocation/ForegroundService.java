package com.quarantine.uploadlocation;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.quarantine.uploadlocation.poll.PollHandler;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    private final int pollingInterval = 60000; // 60 sec

    private PollHandler pollHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        pollHandler = new PollHandler(pollingInterval, callback, Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.upload_location)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        startUploadingLocation();

        return START_NOT_STICKY;
    }

    @SuppressLint("StaticFieldLeak")
    private void startUploadingLocation() {
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                // make an api call upload location

                Log.d("Forground service : ", "service has been connected, we are start uploading location ");
                startPoller();
                return null;
            }

        }.execute();
    }

    PollHandler.PollCallback callback = new PollHandler.PollCallback(){

        @Override
        public void onPolled() {
            Log.d("Forground service : ", "let's upload location again ");
        }

        @Override
        public void onPolledCompleted() {

        }

        @Override
        public boolean shouldPollNext() {
            return true;
        }

        @Override
        public void stopPolling() {

        }
    };

    private void startPoller() {
        pollHandler.sendMessage(PollHandler.getMessageToStartPolling(true));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}