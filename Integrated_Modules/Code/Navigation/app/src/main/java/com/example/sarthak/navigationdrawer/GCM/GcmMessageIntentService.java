package com.example.sarthak.navigationdrawer.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.sarthak.navigationdrawer.ContactDisplay.MainActivity_Contacts;
import com.example.sarthak.navigationdrawer.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by sarthak on 27/3/16.
 */
public class GcmMessageIntentService extends IntentService {
    private Handler handler;
    GoogleCloudMessaging gcm;
    String message;
    String message_message;
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
        message_message = extras.getString("message");
        showNotification();
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void showNotification() {
        handler.post(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), MainActivity_Contacts.class);
                PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder =
                        (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(message)
                                .setSound(sound)
                                .setContentIntent(pendingIntent)
                                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                                .setContentText(message_message);

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
