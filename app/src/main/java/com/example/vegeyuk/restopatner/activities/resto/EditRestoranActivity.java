package com.example.vegeyuk.restopatner.activities.resto;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Restoran;
import com.example.vegeyuk.restopatner.responses.ResponseValue;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRestoranActivity extends AppCompatActivity {

    Context mContext;
    ApiService mApiService;
    Restoran restoran;
    int pb1;

    private String[] persentase = {
            "Tidak dipungut pajak",
            "1 %","2 %","3 %","4 %","5 %","6 %","7 %","8 %","9 %","10 %"
    };

    @BindView(R.id.etNama)
    EditText etNama;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etAlamat)
    EditText etAlamat;
    @BindView(R.id.btn_edit)
    TextView btnEdit;
    @BindView(R.id.etDeskripsi)
    EditText etDiskripsi;
    @BindView(R.id.rootLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.spinerPb1)
    Spinner mSpinerPb1;

    SessionManager sessionManager;

    ProgressDialog progressDialog;

    ArrayAdapter<String> adapter;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restoran);
        getSupportActionBar().setTitle("Data Usaha");
        restoran = (Restoran) getIntent().getSerializableExtra("restoran");
        mContext = this;
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        mApiService = ServerConfig.getAPIService();

        mSpinerPb1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectSpiner(adapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        init();
    }


    public void init(){
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, persentase);
        pb1 = restoran.getRestoran_pajak_pb_satu();
        mSpinerPb1.setAdapter(adapter);
        if (pb1==0) {
            //int spinnerPosition = adapter.getPosition(metodeBayar);
            mSpinerPb1.setSelection(0);
        }else {
            int spinnerPosition = adapter.getPosition(pb1+" %");
            mSpinerPb1.setSelection(spinnerPosition);
        }
        etNama.setText(restoran.getRestoranNama());
        etPhone.setText(restoran.getRestoranPhone());
        etEmail.setText(restoran.getRestoranEmail());
        etAlamat.setText(restoran.getRestoranAlamat());
        etDiskripsi.setText(restoran.getRestoranDeskripsi());

    }


    @OnClick(R.id.btn_edit) void ubah(){
        progressDialog = ProgressDialog.show(this,null,getString(R.string.memuat),true,false);

        final String nama = etNama.getText().toString();
        final String email = etEmail.getText().toString();
        final String phone = etPhone.getText().toString();
        final String alamat =etAlamat.getText().toString();
        final String diskripsi =etDiskripsi.getText().toString();

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
        }
        else if (phone.isEmpty()||phone.equals(null)) {
            progressDialog.dismiss();
            etPhone.setError("Field Tidak Boleh Kosong");
            etPhone.requestFocus();
            return;
        }else if (phone.length()<12) {
            progressDialog.dismiss();
            etPhone.setError("Nomor Phone Tidak valid");
            etPhone.requestFocus();
            return;
        }else if (alamat.isEmpty()||alamat.equals(null)) {
            progressDialog.dismiss();
            etAlamat.setError("Field Tidak Boleh Kosong");
            etAlamat.requestFocus();
            return;
        }else if (diskripsi.isEmpty()||diskripsi.equals(null)) {
            progressDialog.dismiss();
            etDiskripsi.setError("Field Tidak Boleh Kosong");
            etDiskripsi.requestFocus();
            return;
        }else {

            mApiService.editRestoran(restoran.getId().toString(), nama, phone,email , alamat,pb1, diskripsi).enqueue(new Callback<ResponseValue>() {
                @Override
                public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        if (response.body().getValue().equals("1")) {
                            Snackbar.make(coordinatorLayout, "Berhasil Edit", Snackbar.LENGTH_SHORT).show();
                            sessionManager.updateRestoran(nama, email, phone, alamat, diskripsi);

                        } else {
                            progressDialog.dismiss();
                            Snackbar.make(coordinatorLayout, "Gagal Edit", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseValue> call, Throwable t) {
                    progressDialog.dismiss();

                }
            });
        }
    }

    private void selectSpiner(String item) {
        if(item.equals("Tidak dipungut pajak")){
             pb1 = 0;
            Toast.makeText(mContext,pb1+"",Toast.LENGTH_SHORT).show();
        }else{
           // String tmp = item.replaceAll("%","");
            pb1 =Integer.parseInt(item.replaceAll("[%\\s+]",""));
            Toast.makeText(mContext,pb1+"",Toast.LENGTH_SHORT).show();



        }


    }
}
