package com.example.vegeyuk.restopatner.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.activities.resto.SignUpActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

    Context mContext ;
    Button mOke ,mCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen1();

        mContext = this;

    }

    private void screen1() {
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }

    @OnClick (R.id.btnSignIn) void signin (){
        Intent intent = new Intent(mContext,SignInActivity.class);
        startActivity(intent);
    }

    @OnClick (R.id.btnSignup) void signup (){
        screen();
    }

    private void screen() {
        setContentView(R.layout.layout_syarat);
        mOke = (Button) findViewById(R.id.btnOke);
        mCancel = (Button) findViewById(R.id.btnCancel);

        mOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,SignUpActivity.class);
                startActivity(intent);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen1();
            }
        });
    }
}
