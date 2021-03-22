package com.example.numad21s_firebase_team_puzzlers.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.numad21s_firebase_team_puzzlers.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.logging.Handler;

public class DEBUGMessagingService extends FirebaseMessagingService {
    private static final String TAG = DEBUGMessagingService.class.getSimpleName();
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_NAME = "CHANNEL_NAME";
    private static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage);
        }
        extractPayloadDataForegroundCase(remoteMessage);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage FCM message  received.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showNotification(RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, DEBUGMessagingService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification;
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        } else {
            builder = new NotificationCompat.Builder(this);
        }
        notification = builder.setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(0, notification);

    }

    public void postToastMessage(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG);
    }

    private void extractPayloadDataForegroundCase(RemoteMessage remoteMessage) {
        if (remoteMessage.getData() != null) {
            postToastMessage(remoteMessage.getData().get("title"));
        }

    }
}
