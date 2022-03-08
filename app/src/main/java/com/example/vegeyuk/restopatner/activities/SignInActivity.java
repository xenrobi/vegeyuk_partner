package com.example.vegeyuk.restopatner.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Kurir;
import com.example.vegeyuk.restopatner.models.Restoran;
import com.example.vegeyuk.restopatner.responses.ResponseAuth;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    Context mContext;
    ApiService mApiService ;
    @BindView(R.id.etPhone)
    EditText mPhone ;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    String strPhone,value,message,tipe;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mApiService = ServerConfig.getAPIService();
        mContext = this;
        ButterKnife.bind(this);


    }

    @OnClick (R.id.btnSendCode) void getCode (){
        //hidden keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);

        //progress dialog
        progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);

        strPhone = clearPhone(mPhone.getText().toString());

        if(strPhone.equals("62")){
            progressDialog.dismiss();
            mPhone.setError("Nomor telepon diperlukan");
            mPhone.requestFocus();
            return;
        }

        if(strPhone.length() < 12){
            progressDialog.dismiss();
            mPhone.setError("Nomor telepon tidak valid");
            mPhone.requestFocus();
            return;
        }


        if(cekToken()) {
            mApiService.signinRequest(strPhone).enqueue(new Callback<ResponseAuth>() {

                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {

                        value = response.body().getValue();
                        message = response.body().getMessage();
                        tipe = response.body().getTipe();
                        if (value.equals("1")) {
                            Intent intent = new Intent(mContext, VerifyActifity.class);
                            if (tipe.equals("restoran")) {
                                Toast.makeText(mContext, tipe, Toast.LENGTH_SHORT).show();
                                Restoran resto = response.body().getRestoran();
                                intent.putExtra("resto", resto);
                            } else if (tipe.equals("kurir")) {
                                Kurir kurir = response.body().getKurir();
                                intent.putExtra("kurir", kurir);
                                Toast.makeText(mContext, tipe, Toast.LENGTH_SHORT).show();
                            }
                            startActivity(intent);
                        } else {
                            Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Snackbar.make(coordinatorLayout, R.string.loss_connect, Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout, R.string.loss_connect, Snackbar.LENGTH_LONG).show();
                }
            });
        }else {
            progressDialog.dismiss();
            Snackbar.make(coordinatorLayout, "Terjadi Kesalahan, Instal Ulang App!", Snackbar.LENGTH_LONG).show();
        }
    }


//    @OnClick(R.id.btnDisplayToken) void viewToken () {
//        String Token = SharedPrefManager.getInstance(this).getDeviceToken();
//        mToken.setText(Token);
//    }

    public String clearPhone (String phoneNumber){
        String hp = phoneNumber.replaceAll("-","");
        String clearPhone = hp.substring(1,hp.length());
        return clearPhone;
    }

    public boolean cekToken (){
        Boolean value = true;
        String Token = SharedPrefManager.getInstance(this).getDeviceToken();
        if (Token == null){
            value = false;
        }
        return value;
    }
}
