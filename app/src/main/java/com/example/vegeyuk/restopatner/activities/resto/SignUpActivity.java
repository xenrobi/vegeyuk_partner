package com.example.vegeyuk.restopatner.activities.resto;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.activities.WelcomeActivity;
import com.example.vegeyuk.restopatner.adapter.StepperAdapter;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.fragment.StepFragmentDelivery;
import com.example.vegeyuk.restopatner.fragment.StepFragmentKategori;
import com.example.vegeyuk.restopatner.fragment.StepFragmentMaps;
import com.example.vegeyuk.restopatner.fragment.StepFragmentPemilik;
import com.example.vegeyuk.restopatner.fragment.StepFragmentResto;
import com.example.vegeyuk.restopatner.models.Restoran;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SharedPrefManager;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements StepperLayout.StepperListener {

    private StepperLayout mStepperLayout;
    private StepperAdapter mStepperAdapter ;
    Context mContext;
    Restoran resto;
    List<Integer> id_kategori = new ArrayList<>();
    String Token,value,message;
    ApiService mApiService;
    boolean sucses;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mContext = this;
        mApiService = ServerConfig.getAPIService();
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperAdapter = new StepperAdapter(getSupportFragmentManager(),this);
        mStepperLayout.setAdapter(mStepperAdapter);
        mStepperLayout.setListener(this);
        Token = SharedPrefManager.getInstance(this).getDeviceToken();


    }

// on complite stepper
    @Override
    public void onCompleted(View completeButton) {
        progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);

        StepFragmentResto a = (StepFragmentResto) mStepperAdapter.findStep(0);
         resto = a.resto();

        StepFragmentPemilik b = (StepFragmentPemilik) mStepperAdapter.findStep(1);
        resto.setRestoranPemilikNama(b.resto().getRestoranPemilikNama());
        resto.setRestoranPemilikPhone(b.resto().getRestoranPemilikPhone());
        resto.setRestoranPemilikEmail(b.resto().getRestoranPemilikEmail());

        StepFragmentDelivery z = (StepFragmentDelivery) mStepperAdapter.findStep(2);
        resto.setRestoranDeliveryJarak(z.resto().getRestoranDeliveryJarak());
        resto.setRestoranDeliveryMinimum(z.resto().getRestoranDeliveryMinimum());
        resto.setRestoranDelivery(z.resto().getRestoranDelivery());
      //  resto.setTarifDelivery(z.resto().getTarifDelivery());

        StepFragmentMaps c = (StepFragmentMaps) mStepperAdapter.findStep(3);
        //resto.setRestoranLokasi(c.resto().getRestoranLokasi());

        StepFragmentKategori d = (StepFragmentKategori) mStepperAdapter.findStep(4);
        id_kategori = d.id_kategori();

        String restoran_nama = resto.getRestoranNama();
        String restoran_phone = resto.getRestoranPhone();
        String restoran_email = resto.getRestoranEmail();
        String restoran_alamat = resto.getRestoranAlamat();
    //    String restoran_lokasi = resto.getRestoranLokasi();
        String restoran_deskripsi = resto.getRestoranDeskripsi();
    //    String restoran_gambar = resto.getRestoranGambar();
        String restoan_pemilik_nama = resto.getRestoranPemilikNama();
        String restoan_pemilik_phone = resto.getRestoranPemilikPhone();
        final String restoan_pemilik_email =  resto.getRestoranPemilikEmail();
        String restoran_delivery = resto.getRestoranDelivery();
  //      String tarif_delivery = resto.getTarifDelivery();
        String restoran_delivery_jarak = String.valueOf(resto.getRestoranDeliveryJarak());
        String restoran_delivery_minimun = resto.getRestoranDeliveryMinimum();
        String token =Token ;
        String tipe = "restoran";


//        Toast.makeText(this,resto.getRestoranLokasi(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,resto.getRestoranPemilikPhone(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,resto.getTarifDelivery(), Toast.LENGTH_SHORT).show();

//sign up restoran baru
//        mApiService.signupRequest(restoran_nama,restoran_phone,restoran_email,
//                restoran_alamat,restoran_lokasi,restoran_deskripsi,
//                restoran_gambar,restoan_pemilik_nama,restoan_pemilik_phone,
//                restoan_pemilik_email,restoran_delivery,tarif_delivery,
//                restoran_delivery_jarak,restoran_delivery_minimun,token,tipe).enqueue(new Callback<ResponseAuth>() {
//            @Override
//            public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
//                if(response.isSuccessful()){
//                    value = response.body().getValue();
//                     message = response.body().getMessage();
//                    if(value.equals("1")){
//                        String ID = response.body().getID();
//                        Toast.makeText(mContext,message+" ID : "+ID,Toast.LENGTH_SHORT).show();
//                        inputKategori(ID);
//                    }else {
//                        progressDialog.dismiss();
//                        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    progressDialog.dismiss();
//                    Toast.makeText(mContext,"gagal",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseAuth> call, Throwable t) {
//                Toast.makeText(mContext,R.string.loss_connect,Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
    }

//input kategori restoran
//    public void inputKategori (String ID){
//
//        for (int i = 0; i < id_kategori.size() ; i++) {
//            mApiService.signupKategoriRequest(ID,id_kategori.get(i).toString()).enqueue(new Callback<ResponseAuth>() {
//                @Override
//                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
//                    sucses = response.isSuccessful();
//
//                }
//
//                @Override
//                public void onFailure(Call<ResponseAuth> call, Throwable t) {
//
//                }
//            });
//        }
//            progressDialog.dismiss();
//            Toast.makeText(mContext, " Kategori Masuk", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(mContext, SignInActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY |Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//
//    }
//
    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(this, "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext,WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
