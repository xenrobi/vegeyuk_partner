package com.example.vegeyuk.restopatner.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.models.Delivery;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TesFirebaseActivity extends AppCompatActivity {

    //variable yang mereference ke firebase realtime database
    private DatabaseReference database;

    EditText id,lat, lng;
    Button submit, edit;
    TextView tvid,tvlat, tvlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tes_firebase);
        id = (EditText) findViewById(R.id.id);
        lat = (EditText) findViewById(R.id.lat);
        lng = (EditText) findViewById(R.id.lng);
        submit = (Button) findViewById(R.id.submit);
        edit = (Button) findViewById(R.id.edit);
        tvid = (TextView) findViewById(R.id.tvId);
        tvlat = (TextView) findViewById(R.id.tvLat);
        tvlng = (TextView) findViewById(R.id.tvLng);

        //mengambil referensi ke firebase database
        database = FirebaseDatabase.getInstance().getReference();

        database.child("delivery").child("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Delivery delivery = dataSnapshot.getValue(Delivery.class);
                tvid.setText(delivery.getId().toString());
                tvlat.setText(delivery.getLat().toString());
                tvlng.setText(delivery.getLng().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty(id.getText().toString()) && !isEmpty(lat.getText().toString()) && !isEmpty(lng.getText().toString())){
                    submitDelivery(new Delivery(id.getText().toString(),lat.getText().toString(),lng.getText().toString()));
                }else {
                    Toast.makeText(TesFirebaseActivity.this, "Data barang tidak boleh kosong", Toast.LENGTH_LONG).show();

                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            id.getWindowToken(), 0);
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delivery delivery = new Delivery(id.getText().toString(),lat.getText().toString(),lng.getText().toString());
                updateDelivery(delivery);
            }
        });
    }

    private boolean isEmpty(String s) {
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s);
    }

    private void updateDelivery(Delivery pesan) {
        /**
         * Baris kode yang digunakan untuk mengupdate data barang
         * yang sudah dimasukkan di Firebase Realtime Database
         */
        database.child("delivery") //akses parent index, ibaratnya seperti nama tabel
                .child(pesan.getId()) //select barang berdasarkan key
                .setValue(pesan) //set value barang yang baru
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        /**
                         * Baris kode yang akan dipanggil apabila proses update barang sukses
                         */
                        Toast.makeText(TesFirebaseActivity.this, "Data berhasil ditubah", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void submitDelivery(Delivery pesan) {
        /**
         * Ini adalah kode yang digunakan untuk mengirimkan data ke Firebase Realtime Database
         * dan juga kita set onSuccessListener yang berisi kode yang akan dijalankan
         * ketika data berhasil ditambahkan
         */
        database.child("delivery").child(pesan.getId()).setValue(pesan).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                id.setText("");
                lat.setText("");
                lng.setText("");
                Toast.makeText(TesFirebaseActivity.this, "Data berhasil ditambahkan", Toast.LENGTH_LONG).show();
            }
        });
    }



}
