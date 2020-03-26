package com.quarantine.uploadlocation;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.quarantine.data.LocationRepository;
import com.quarantine.data.base.ServiceHelper;
import com.quarantine.data.remote.LocationService;
import com.quarantine.uploadlocation.poll.PollHandler;
import com.quarantine.uploadlocation.utils.Constants;

import java.util.UUID;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    private final int pollingInterval = 60000; // 60 sec

    private boolean isForgroundServiceRunning = false;

    private PollHandler pollHandler;
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate() {
        super.onCreate();
        pollHandler = new PollHandler(pollingInterval, callback, Looper.getMainLooper());

        try {
            String wakeLockId = UUID.randomUUID().toString();
            wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService " + wakeLockId);
            wakeLock.acquire();
        }catch (Exception e){
            // Unable to acquire wake lock
            Log.d("Forground service : ", "unable to get the log : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Forground service : ", "connected with service isForgroundServiceRunning : " + isForgroundServiceRunning);
        if (!isForgroundServiceRunning) {
            String input = intent.getStringExtra("inputExtra");

            createNotificationChannel();

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Location updates")
                    .setContentText(input)
                    .setSmallIcon(R.drawable.upload_location)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            startForeground(1, notification);

            startUploadingLocation();

            isForgroundServiceRunning = true;

        }

        return START_STICKY;
    }

    @SuppressLint("StaticFieldLeak")
    private void startUploadingLocation() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                // make an api call upload location

                Log.d("Forground service : ", "from back ground thread ");
                startPoller();
                return null;
            }

        }.execute();
    }

    LocationRepository locationRepository;

    PollHandler.PollCallback callback = new PollHandler.PollCallback() {


        @Override
        public void onPolled() {
            if (locationRepository == null) {
                locationRepository = new LocationRepository(ServiceHelper.Companion.getNetworkClient().create(LocationService.class));
            }
            Log.d("Forground service : ", "let's upload location again ");
            locationUsingFuesLocation();
        }

        private void locationUsingFuesLocation() {
            LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(buildLR(),  new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Log.d("Forground service : ", "locationUsingFuesLocation#onLocationResult " + locationResult.getLastLocation());
                    if (locationResult.getLastLocation() != null) {
                        locationRepository.postLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    }
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability) {
                    Log.d("Forground service : ", "locationUsingFuesLocation#onLocationAvailability " + locationAvailability.isLocationAvailable());
                }
            }, Looper.getMainLooper());
        }

        private LocationRequest buildLR() {
            LocationRequest request = new LocationRequest();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setNumUpdates(1);
            request.setInterval(0);
            return request;
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
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show();

        // service is getting destroyed let's start it again.
        Constants.INSTANCE.startService(getApplicationContext());

        super.onDestroy();
        isForgroundServiceRunning = false;
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

    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            Log.d("Forground service : ", "onLocationChanged");
//            locationRepository.postLocation(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Forground service : ", "onStatusChanged");

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("Forground service : ", "onProviderEnabled");

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Forground service : ", "onProviderDisabled");

        }
    };
}