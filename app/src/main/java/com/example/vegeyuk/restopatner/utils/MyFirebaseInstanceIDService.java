package com.example.vegeyuk.restopatner.utils;

import android.support.design.widget.TabLayout;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh: "+refreshedToken);
        storeToken(refreshedToken);
    }

    private  void storeToken(String token){
            SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }
}
