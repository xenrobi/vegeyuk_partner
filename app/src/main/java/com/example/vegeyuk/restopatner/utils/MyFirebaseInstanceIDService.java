package com.example.vegeyuk.restopatner.utils;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";

   /* @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh: "+refreshedToken);
        storeToken(refreshedToken);
        */
   @Override
   public void onNewToken(String s) {
       super.onNewToken(s);
       Log.d(TAG, s);
       storeToken(s);
   }


    private  void storeToken(String token){
            SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }
}
