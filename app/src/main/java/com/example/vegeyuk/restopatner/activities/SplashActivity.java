package com.example.vegeyuk.restopatner.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.activities.kurir.KurirMainActivity;
import com.example.vegeyuk.restopatner.activities.resto.MainActivity;
import com.example.vegeyuk.restopatner.activities.resto.MapsActivity;
import com.example.vegeyuk.restopatner.utils.SessionManager;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    SessionManager sessionManager;
    HashMap<String,String> sessionRestoran;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        sessionManager = new SessionManager(mContext);
        sessionRestoran = sessionManager.getRestoDetail();

        //menghilangkan ActionBar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        //deklarasi objek handler
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
//                finish();
                checkSessionLogin();
            }
        },3000L) ;//3000L = 3 detik
    }


    private void checkSessionLogin (){

        if(!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(mContext,SignInActivity.class);
        //    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        }else {
            if(!sessionManager.isRestoran()){
                //kurir

                Intent intent = new Intent(mContext , KurirMainActivity.class);
                //    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();


            }else {
                //resto
                if(sessionRestoran.get(SessionManager.RESTORAN_LAT)== null||sessionRestoran.get(SessionManager.RESTORAN_LAT)== null){
                    Intent intent = new Intent(mContext, MapsActivity.class);
                    //    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    //    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}
