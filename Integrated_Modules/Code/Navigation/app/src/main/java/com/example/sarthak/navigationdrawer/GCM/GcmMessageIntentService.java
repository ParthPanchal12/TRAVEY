package com.example.sarthak.navigationdrawer.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.sarthak.navigationdrawer.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by sarthak on 27/3/16.
 */
public class GcmMessageIntentService extends IntentService {
    private Handler handler;
    GoogleCloudMessaging gcm;
    String message;
    private int mNotificationId = 0;
    public GcmMessageIntentService() {
        super("GcmMessageHandler");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        message = extras.getString("title");
        showNotification();
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void showNotification() {
        handler.post(new Runnable() {
            @Override
            public void run() {

                NotificationCompat.Builder mBuilder =
                        (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(message)
                                .setContentText("Hello World!");

                mNotificationId++;
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }
        });
    }
}
