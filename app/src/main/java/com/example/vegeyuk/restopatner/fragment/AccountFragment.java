package com.example.vegeyuk.restopatner.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.activities.SignInActivity;
import com.example.vegeyuk.restopatner.activities.resto.DataKurirActivity;
import com.example.vegeyuk.restopatner.activities.resto.EditDeliveryActivity;
import com.example.vegeyuk.restopatner.activities.resto.EditPemilikActivity;
import com.example.vegeyuk.restopatner.activities.resto.EditRestoranActivity;
import com.example.vegeyuk.restopatner.activities.resto.MapsActivity;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Kurir;
import com.example.vegeyuk.restopatner.models.Restoran;
import com.example.vegeyuk.restopatner.responses.ResponseRestoran;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SessionManager;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    Context mContext ;
    SessionManager sessionManager;
    TextView btnSignout,btnDataDelivery,btnDataPemilik,tvNamaResto,tvPhoneResto,tvEmailResto,tvAlamatResto,tvBalance,tvJumlahKurir,btnGantiLokasi,btnTesFirebase;
    LinearLayout btnDataKurir;
    ImageButton edit;
    HashMap<String,String> user ;
    ApiService mApiService;
    List<Kurir> kurirList= new ArrayList<>() ;
    Restoran restoran;
    int jumlahKurir;

    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,container,false);

        mApiService = ServerConfig.getAPIService();
        mContext =getActivity();
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getRestoDetail();
        init(view);


        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertKonfirmasi();
            }
        });


        btnDataKurir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DataKurirActivity.class);
                intent.putExtra("kurir", (Serializable) kurirList);
                startActivity(intent);
            }
        });

        btnDataDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"data Delivery",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, EditDeliveryActivity.class);
                intent.putExtra("restoran", (Serializable) restoran);
                startActivity(intent);
            }
        });

        btnDataPemilik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"data pemilik",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, EditPemilikActivity.class);
                intent.putExtra("restoran", (Serializable) restoran);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"data pemilik",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, EditRestoranActivity.class);
                intent.putExtra("restoran", (Serializable) restoran);
                startActivity(intent);
            }
        });

        btnGantiLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MapsActivity.class);
                String lat = restoran.getRestoranLatitude();
                String lang =  restoran.getRestoranLongitude();
                intent.putExtra("lat", lat);
                intent.putExtra("lang", lang);
                startActivity(intent);
            }
        });

//        btnTesFirebase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, TesFirebaseActivity.class);
//                startActivity(intent);
//            }
//        });

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);
        Toast.makeText(mContext,"onResume",Toast.LENGTH_SHORT).show();
        getAllKurir();
    }

    private void setValue() {
        tvNamaResto.setText(restoran.getRestoranNama());
        tvPhoneResto.setText("+"+restoran.getRestoranPhone());
        tvEmailResto.setText(restoran.getRestoranEmail());
        tvAlamatResto.setText(restoran.getRestoranAlamat());
        tvBalance.setText(kursIndonesia(Double.parseDouble(restoran.getRestoranBalance())));
        tvJumlahKurir.setText(restoran.getJumlahKurir()+" Kurir");
    }



    private void init(View view) {
        btnSignout = (TextView) view.findViewById(R.id.btn_sign_out);
        btnDataKurir =(LinearLayout) view.findViewById(R.id.btnDataKurir);
        btnDataDelivery =(TextView) view.findViewById(R.id.btnDataDelivery);
        btnDataPemilik = (TextView) view.findViewById(R.id.btnDataPemilik);
       // btnTesFirebase = (TextView) view.findViewById(R.id.btnTesFirebase);
        tvNamaResto = (TextView) view.findViewById(R.id.tvNamaResto);
        tvPhoneResto = (TextView) view.findViewById(R.id.tvPhoneResto);
        tvEmailResto = (TextView)view.findViewById(R.id.tvEmailResto);
        tvAlamatResto =(TextView)view.findViewById(R.id.tvAlamatResto);
        tvBalance = (TextView) view.findViewById(R.id.tvBalance);
        tvJumlahKurir =(TextView)view.findViewById(R.id.tvJumlahKurir);
        edit =(ImageButton) view.findViewById(R.id.edit);
        btnGantiLokasi = (TextView) view.findViewById(R.id.btnDataLokasi);

    }



    //get All kurir
    private void getAllKurir(){
        mApiService.getRestoranByID(user.get(SessionManager.ID_RESTORAN)).enqueue(new Callback<ResponseRestoran>() {
            @Override
            public void onResponse(Call<ResponseRestoran> call, Response<ResponseRestoran> response) {
                if(response.isSuccessful()){
                    if(response.body().getValue().equals("1")){
                        progressDialog.dismiss();
                        restoran = response.body().getData();
                        setValue();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRestoran> call, Throwable t) {
                    progressDialog.dismiss();
            }
        });
    }

    //konfersi ke mata uang rupiah
    public String kursIndonesia(double nominal){
        Locale localeID = new Locale("in","ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }


    public void alertKonfirmasi(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Apakah Anda Yakin Akan Keluar");
        builder.setCancelable(false);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //send data to server

                progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);
                logout();


            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });


        final AlertDialog alert =builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            }
        });
        alert.show();
    }

    void logout (){

        FirebaseAuth.getInstance().signOut();
        sessionManager.logoutUser();
        Intent intent = new Intent(mContext, SignInActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
