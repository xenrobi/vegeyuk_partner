package com.example.vegeyuk.restopatner.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.activities.kurir.KurirMainActivity;
import com.example.vegeyuk.restopatner.activities.resto.MainActivity;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Kurir;
import com.example.vegeyuk.restopatner.models.Restoran;
import com.example.vegeyuk.restopatner.responses.ResponseValue;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SessionManager;
import com.example.vegeyuk.restopatner.utils.SharedPrefManager;
import com.github.irvingryan.VerifyCodeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyActifity extends AppCompatActivity {

    @BindView(R.id.editTextCode)
    VerifyCodeView editTextCode;
    @BindView(R.id.buttonSignIn)
    Button btnSigin ;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.textSendCode)
    TextView txtResend;


    PhoneAuthProvider.ForceResendingToken mResendToken;


    Context mContext;
    FirebaseAuth mAuth;
    String codeSent,phone,index;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    HashMap<String,String> sessionRestoran;
    Restoran resto;
    Kurir kurir;
    ApiService mApiservice;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mContext = this;
        sessionManager = new SessionManager(mContext);
        sessionRestoran = sessionManager.getRestoDetail();



        ButterKnife.bind(this);
        mApiservice = ServerConfig.getAPIService();
        kurir = new Kurir();
        resto = new Restoran();


        mAuth = FirebaseAuth.getInstance();

        getIncomingIntent();
        txtResend.setText("Masukan kode verifikasi yang dikirim melalui \n SMS pada nomor ponsel +"+phone);


//      Memanggil method untuk mengirim code
        //sendVerificationCode(phone);
    }



    @OnClick (R.id.buttonSignIn) void signin (){
        progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);
//        untuk melakukan verifikasi dari code OTP yang di inputkan
        //verifySignInCode();
//        jika menguji login tanpa menggunakan code OTP
         SessionUser();


    }


    @OnClick (R.id.resendCode) void onResendCode(){
        ResendCode(phone);
    }

    private void verifySignInCode(){
        String code =  editTextCode.getText();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //to sucses login
                            Toast.makeText(getApplicationContext(),"login successfuli", Toast.LENGTH_LONG).show();
                            SessionUser();

                            // ...
                        } else {

                            // The verification code entered was invalid
                            progressDialog.dismiss();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                               // Toast.makeText(getApplicationContext(),"Incorrect Verificarion Code",Toast.LENGTH_LONG).show();
                                Snackbar.make(coordinatorLayout,"Code Verifikasi Tidak Valid", Snackbar.LENGTH_INDEFINITE).show();
                            }
                        }
                    }
                });
    }


    private void sendVerificationCode(String phone){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            Toast.makeText(mContext,"verification completed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(mContext,"verification fialed", Toast.LENGTH_SHORT).show();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // [START_EXCLUDE]
                Toast.makeText(mContext, "invalid mob no", Toast.LENGTH_LONG).show();
                // [END_EXCLUDE]
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // [START_EXCLUDE]
                Toast.makeText(mContext, "quota over", Toast.LENGTH_LONG).show();
                // [END_EXCLUDE]
            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(mContext,"Code sent", Toast.LENGTH_SHORT).show();
            codeSent = s;
            mResendToken = forceResendingToken;
        }
    };

    public void ResendCode(String phone){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                1  ,               // Timeout duration
                TimeUnit.MINUTES,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                mResendToken);             // Force Resending Token from callbacks
    }


    private void SessionUser() {

        String Token = SharedPrefManager.getInstance(this).getDeviceToken();

        if(index.equals("1")) {
            sessionManager.createLoginSessionResto(resto);
            updateTokenResto(resto.getId().toString(),Token);

        }else {
            sessionManager.createLoginSessionKurir(kurir);
            updateTokenKurir(kurir.getId().toString(),Token);
        }


    }


    //update token restoran
    private void updateTokenResto(String id_resto,String token) {
        mApiservice.updateTokenRestoran(id_resto,token).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    String value = response.body().getValue();
                    if(value.equals("1")){
                        progressDialog.dismiss();
                        Toast.makeText(mContext,"Token Resto Update",Toast.LENGTH_SHORT).show();
                        Intent intent;
                        intent = new Intent(mContext, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(mContext,"Token  Resto Update Filure",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(coordinatorLayout,R.string.loss_connect, Snackbar.LENGTH_LONG).show();

            }
        });
    }


    //update token kurir
    private void updateTokenKurir(String id_kurir, String token) {

        mApiservice.updateTokenKurir(id_kurir,token).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    String value = response.body().getValue();
                    if(value.equals("1")){
                        Toast.makeText(mContext,"Token Kurir Update",Toast.LENGTH_SHORT).show();
                        Intent intent;
                        intent = new Intent(mContext, KurirMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(mContext,"Token  Kurir Update Filure",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(mContext,"Terjadi Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(coordinatorLayout,R.string.loss_connect, Snackbar.LENGTH_LONG).show();
            }
        });

    }



    private void getIncomingIntent (){

        if(getIntent().hasExtra("resto")){
            resto = (Restoran)getIntent().getSerializableExtra("resto");
            phone = resto.getRestoranPhone();
            index = "1";
        }else if(getIntent().hasExtra("kurir")){
            kurir = (Kurir) getIntent().getSerializableExtra("kurir");
            phone = kurir.getKurirPhone();
            index = "0";
        }
    }




}
