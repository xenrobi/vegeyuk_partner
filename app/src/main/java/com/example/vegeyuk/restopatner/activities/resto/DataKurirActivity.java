package com.example.vegeyuk.restopatner.activities.resto;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.adapter.KurirAdapter;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Kurir;
import com.example.vegeyuk.restopatner.responses.ResponseKurir;
import com.example.vegeyuk.restopatner.responses.ResponseValue;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataKurirActivity extends AppCompatActivity implements KurirAdapter.ClickListener{

    AlertDialog.Builder dialog;
    AlertDialog myAlert;
    ApiService mApiService ;
    Context mContext;
    EditText mNamaKurir,mPhoneKurir,mEmailKurir;
    SessionManager sessionManager;
    List<Kurir> kurirList = new ArrayList<>();
    HashMap<String,String> user;

    RecyclerView recyclerView;
    KurirAdapter kurirAdapter;
    String id_restoran,id_Kurir ;
    KurirAdapter.ClickListener listener;
    View message;
    ImageView icon_message;
    TextView title_message,sub_title_message;






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_view);
        getSupportActionBar().setTitle("Kurir");
        mApiService = ServerConfig.getAPIService();
        mContext = this;
        ButterKnife.bind(this);
        listener =this;
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getRestoDetail();
        id_restoran = user.get(SessionManager.ID_RESTORAN);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        message = findViewById(R.id.error);
        icon_message = (ImageView) findViewById(R.id.img_msg);
        title_message = (TextView) findViewById(R.id.title_msg);
        sub_title_message = (TextView) findViewById(R.id.sub_title_msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getKurir();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addkurir,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.addkurir){
                DialogAddKurir("Tambah Kurir");
        }
        return true;
    }


 //form input kurir baru
    private void DialogAddKurir(final String Index) {
        dialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_kurir,null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle(Index);

        mNamaKurir = (EditText) dialogView.findViewById(R.id.etNamaKurir);
        mPhoneKurir = (EditText) dialogView.findViewById(R.id.etPhoneKurir);
        mEmailKurir= (EditText) dialogView.findViewById(R.id.etEmailKurir);


        dialog.setPositiveButton(Index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            //insert new kurir

                String nama = mNamaKurir.getText().toString();
                String phone = mPhoneKurir.getText().toString();
                String email = mEmailKurir.getText().toString();
                if(phone.equals("62")){
                  //  progressDialog.dismiss();
                    mPhoneKurir.setError("Nomor telepon diperlukan");
                    mPhoneKurir.requestFocus();
                    return;
                }

                if(phone.length() < 12){
                    //progressDialog.dismiss();
                    mPhoneKurir.setError("Nomor telepon tidak valid");
                    mPhoneKurir.requestFocus();
                    return;
                }

                if(nama.isEmpty() ){
                    //progressDialog.dismiss();
                    mNamaKurir.setError("Nama diperlukan");
                    mNamaKurir.requestFocus();
                    return;
                }

                if(email.isEmpty() ){
                    //progressDialog.dismiss();
                    mEmailKurir.setError("Email diperlukan");
                    mEmailKurir.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() ){
                    //progressDialog.dismiss();
                    mEmailKurir.setError("Email tidak valid");
                    mEmailKurir.requestFocus();
                    return;
                }


                if(Index.equals("Tambah Kurir")) {
                    addNewKurir(id_restoran, phone, nama, email);
                }else if(Index.equals("Edit Kurir")){
                    editKurir(id_Kurir, phone, nama, email);
                }

            }
        });

        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myAlert.dismiss();

            }
        });

        myAlert = dialog.create();
        myAlert.show();
    }




    //get restoran
    private void getKurir(){
        mApiService.getKurir(id_restoran).enqueue(new Callback<ResponseKurir>() {
            @Override
            public void onResponse(Call<ResponseKurir> call, Response<ResponseKurir> response) {
                if(response.isSuccessful()){
                    if(response.body().getValue().equals("1")){
                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        kurirList = response.body().getKurir();
                        kurirAdapter = new KurirAdapter(mContext,kurirList,listener);
                        recyclerView.setAdapter(kurirAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        message.setVisibility(View.GONE);
                    }else {
                       noKurir();

                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseKurir> call, Throwable t) {

            }
        });
    }

    private void noKurir() {
        message.setVisibility(View.VISIBLE);
        icon_message.setImageResource(R.drawable.msg_courier);
        title_message.setText("Anda Tidak Memiliki Kurir ");
        sub_title_message.setText("Tambahkan kurir untuk membantu usaha anda");
        recyclerView.setVisibility(View.GONE);
    }


    //menambah kurir baru
    private void addNewKurir(String id_restoran,String phone,String nama, String email) {
        mApiService.addKurir(id_restoran,phone,nama,email).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {

                if(response.isSuccessful()){
                    String value = response.body().getValue();
                    String message = response.body().getMessage();

                    if(value.equals("1")){
                        myAlert.dismiss();
                        onResume();
                        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                    }else {
                        myAlert.dismiss();
                        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(mContext,R.string.loss_connect,Toast.LENGTH_SHORT).show();
                myAlert.dismiss();
            }
        });
    }

    private void editKurir(String id_kurir, String phone, String nama, String email) {
        mApiService.editKurir(id_kurir,phone,nama,email).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful()){
                    String value = response.body().getValue();
                    String message = response.body().getMessage();

                    if(value.equals("1")){
                        myAlert.dismiss();
                        getKurir();
                        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                    }else {
                        myAlert.dismiss();
                        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {

            }
        });

    }

    @Override
    public void itemDeleted(View view, int position) {
        Kurir kurir = kurirList.get(position);
        id_Kurir = kurir.getId().toString();
        alertHapus(position);
    }

    private void alertHapus(final int position) {
        final Kurir kurir = kurirList.get(position);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Konfirmasi Hapus Kurir");
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage("Apakah Anda Yakin Akan Menghapus Kurir "+kurir.getKurirNama().toString()+" ?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                mApiService.deleteKurir(id_Kurir).enqueue(new Callback<ResponseValue>() {
                    @Override
                    public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                        if(response.isSuccessful()){
                            String value = response.body().getValue();
                            String message = response.body().getMessage();

                            if(value.equals("1")){
                                kurirAdapter.removeAt(position);
                                if (kurirAdapter.getItemCount() == 0){
                                    noKurir();
                                }
                                Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseValue> call, Throwable t) {

                    }
                });
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        final AlertDialog alert = alertDialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            }
        });
        alert.show();
    }

    @Override
    public void itemEdit(View view, int position) {
        Kurir kurir = kurirList.get(position);
        DialogAddKurir("Edit Kurir");
        id_Kurir = kurir.getId().toString();
        mNamaKurir.setText(kurir.getKurirNama().toString());
        mEmailKurir.setText(kurir.getKurirEmail().toString());
        mPhoneKurir.setText(kurir.getKurirPhone().toString());

    }
}
