package com.example.vegeyuk.restopatner.activities.resto;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.EditText;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Restoran;
import com.example.vegeyuk.restopatner.responses.ResponseRestoran;
import com.example.vegeyuk.restopatner.responses.ResponseValue;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SessionManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPemilikActivity extends AppCompatActivity {

    Context mContext;
    ApiService mApiService;
    Restoran restoran;

    @BindView(R.id.etNama)
    EditText etNama;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.rootLayout)
    CoordinatorLayout coordinatorLayout;

    SessionManager sessionManager;
    HashMap<String,String> user;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pemilik);
        getSupportActionBar().setTitle("Data Pemilik");
        mContext = this;
        ButterKnife.bind(this);
        mApiService = ServerConfig.getAPIService();
        sessionManager = new SessionManager(this);
        user = sessionManager.getRestoDetail();

    }


    @Override
    protected void onResume() {
        super.onResume();
        getRestoran();
    }

    public void init(){
        etNama.setText(restoran.getRestoranPemilikNama());
        etPhone.setText(restoran.getRestoranPemilikPhone());
        etEmail.setText(restoran.getRestoranPemilikEmail());
    }

    @OnClick(R.id.btnSubmit) void ubah (){
        progressDialog = ProgressDialog.show(this,null,getString(R.string.memuat),true,false);

        final String nama = etNama.getText().toString();
        final String email = etEmail.getText().toString();
        final String phone = etPhone.getText().toString();
        final String id = user.get(SessionManager.ID_RESTORAN);

        if(nama.isEmpty()||nama.equals(null)) {
            progressDialog.dismiss();
            etNama.setError("Field Tidak Boleh Kosong");
            etNama.requestFocus();
            return;
        }else if (email.isEmpty()||email.equals(null)) {
            progressDialog.dismiss();
            etEmail.setError("Field Tidak Boleh Kosong");
            etEmail.requestFocus();
            return;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            progressDialog.dismiss();
            etEmail.setError("Email Tidak Valid");
            etEmail.requestFocus();
            return;
        }else if (phone.isEmpty()||phone.equals(null)) {
            progressDialog.dismiss();
            etPhone.setError("Field Tidak Boleh Kosong");
            etPhone.requestFocus();
            return;
        }else if (phone.length()<12) {
            progressDialog.dismiss();
            etPhone.setError("Nomor Phone Tidak valid");
            etPhone.requestFocus();
            return;
        }else {
            mApiService.editPemilik(id,nama,phone,email).enqueue(new Callback<ResponseValue>() {
                @Override
                public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){

                        if(response.body().getValue().equals("1")){
                            Snackbar.make(coordinatorLayout,"Berhasil Edit", Snackbar.LENGTH_SHORT).show();
                            sessionManager.updatePemilik(nama,phone,email);
                        }else {
                            Snackbar.make(coordinatorLayout,"Gagal Edit", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseValue> call, Throwable t) {
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout,"Periksa Konseksi Anda", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void getRestoran() {
        String id = user.get(SessionManager.ID_RESTORAN);
       mApiService.getRestoranByID(id).enqueue(new Callback<ResponseRestoran>() {
           @Override
           public void onResponse(Call<ResponseRestoran> call, Response<ResponseRestoran> response) {
               if(response.isSuccessful()){
                   if(response.body().getValue().equals("1")){
                    restoran= response.body().getData();
                       init();
                   }else {
                       Snackbar.make(coordinatorLayout,"Gagal", Snackbar.LENGTH_SHORT).show();
                   }
               }
           }

           @Override
           public void onFailure(Call<ResponseRestoran> call, Throwable t) {
               Snackbar.make(coordinatorLayout,"Periksa Konseksi Anda", Snackbar.LENGTH_SHORT).show();
           }
       });
    }
}
