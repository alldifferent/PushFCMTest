package com.example.pushfcmtest.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyMessageServer extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("새 토큰 발급", s);
    }
}
