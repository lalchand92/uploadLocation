package com.quarantine.uploadlocation;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.quarantine.uploadlocation.utils.Constants;

public class ReBootBroadcastReceiver extends BroadcastReceiver {

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent != null && intent.getExtras() != null) {
            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    Constants.INSTANCE.startService(context);
                    return null;
                }
            }.execute();
        }
    }
}
