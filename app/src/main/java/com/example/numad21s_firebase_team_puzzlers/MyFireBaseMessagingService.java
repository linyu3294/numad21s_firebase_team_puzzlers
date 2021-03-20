package com.example.numad21s_firebase_team_puzzlers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

class MyFireBaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    public String getToken() {
        SharedPreferences prefs = this.getSharedPreferences("numad21s_firebase_team_puzzlers", Context.MODE_PRIVATE);
        String value = "numad21s_firebase_team_puzzlers.value";
        int i = prefs.getInt(value, 0);
        return i + "";
    }
}