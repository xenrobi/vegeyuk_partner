package com.example.vegeyuk.restopatner.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Restoran;
import com.example.vegeyuk.restopatner.responses.ResponseRestoran;
import com.example.vegeyuk.restopatner.responses.ResponseValue;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SessionManager;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopUpActivity extends AppCompatActivity{

    @BindView(R.id.tvMaxTransfer)
    TextView mMaxTransfer;
    @BindView(R.id.etNominal1)
    EditText mNominal1;
    @BindView(R.id.etNominal2)
    EditText mNominal2;
    @BindView(R.id.btnTransfer)
    Button btnTransfer;

    ApiService mApiService ;
    SessionManager sessionManager;
    HashMap<String,String> user;
    Restoran restoran;
    Context mContext;
    String id_restoran,id_konsumen,nama_konsumen;



    ProgressDialog progressDialog, progressSend;

    @OnClick(R.id.btnTransfer) void Transfer(){
        progressDialog = ProgressDialog.show(this,null,getString(R.string.memuat),true,false);

        String nominal1 = mNominal1.getText().toString().replaceAll("[.]","");
        String nominal2 = mNominal2.getText().toString().replaceAll("[.]","");
        Double balance = Double.parseDouble(restoran.getRestoranBalance());

        if(nominal1.isEmpty()||nominal1.equals(null)) {
            progressDialog.dismiss();
            mNominal1.setError("Field Tidak Boleh Kosong");
            mNominal1.requestFocus();
            return;
        }else if (nominal2.isEmpty()||nominal2.equals(null)){
            progressDialog.dismiss();
            mNominal2.setError("Field Tidak Boleh Kosong");
            mNominal2.requestFocus();
            return;
        }else if(!nominal1.equals(nominal2)){
            progressDialog.dismiss();
            mNominal2.setError("Nominal Tidak Sama");
            mNominal2.requestFocus();
            return;
        } else if(Double.parseDouble(nominal1) > balance|| Double.parseDouble(nominal2) > balance) {
            progressDialog.dismiss();
            mNominal1.setError("Jumlah Transfer Berlebih");
            mNominal1.requestFocus();
            return;
        }else if (id_konsumen.isEmpty() || id_konsumen.equals(null)) {
            Toast.makeText(mContext,"Opps.. Terjadi Kesalahan",Toast.LENGTH_SHORT).show();
        }else if (id_restoran.isEmpty() || id_restoran.equals(null)) {
            Toast.makeText(mContext,"Opps.. Terjadi Kesalahan",Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.dismiss();
            alertKonfirmasi();
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);
        mContext = this;
        getSupportActionBar().setTitle("Top Up");
        ButterKnife.bind(this);
        setCurrency(mNominal1);
        setCurrency(mNominal2);

        getIncomingIntent();
        sessionManager = new SessionManager(this);

        if(!sessionManager.isRestoran()){
            user = sessionManager.getKurirDetail();
        }else {
            user = sessionManager.getRestoDetail();
        }

        id_restoran = user.get(SessionManager.ID_RESTORAN);

        Toast.makeText(this,id_restoran+","+id_konsumen,Toast.LENGTH_SHORT).show();

        mApiService = ServerConfig.getAPIService();



    }

    @Override
    protected void onResume() {
        super.onResume();
        getRestoran();
    }

    void init(){

        if(Double.parseDouble(restoran.getRestoranBalance()) >0) {
            mMaxTransfer.setText(kursIndonesia(Double.parseDouble(restoran.getRestoranBalance())));
            btnTransfer.setClickable(true);
            mNominal1.setText(null);
            mNominal2.setText(null);
        }else {
            mMaxTransfer.setText("Tidak Ada Saldo");
            btnTransfer.setClickable(false);
            btnTransfer.setText("Saldo Tidak Ada");
        }

    }


    void onTransfer (String value){
        mApiService.setTopUp(id_restoran,id_konsumen,value).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if (response.isSuccessful()){

                    String message = response.body().getMessage();
                    if(response.body().getValue().equals("1")){
                        progressSend.dismiss();
                        Toast.makeText(TopUpActivity.this,message,Toast.LENGTH_SHORT).show();
                        getRestoran();
                    }else {
                        progressSend.dismiss();
                        Toast.makeText(TopUpActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                progressSend.dismiss();
                Toast.makeText(TopUpActivity.this,"Gagal",Toast.LENGTH_SHORT).show();
            }
        });
    }




    void  getRestoran(){
        mApiService.getRestoranByID(id_restoran).enqueue(new Callback<ResponseRestoran>() {
            @Override
            public void onResponse(Call<ResponseRestoran> call, Response<ResponseRestoran> response) {
                if(response.isSuccessful()){
                    if(response.body().getValue().equals("1")){
                        restoran = response.body().getData();
                        init();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRestoran> call, Throwable t) {

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


    void getIncomingIntent() {

        if (getIntent().hasExtra("id_konsumen")) {
           id_konsumen = getIntent().getStringExtra("id_konsumen");
           nama_konsumen = getIntent().getStringExtra("nama_konsumen");
        }
    }


    private void alertKonfirmasi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Konfirmasi Transfer");
        builder.setMessage("Anda akan transfer "+kursIndonesia(Double.parseDouble(mNominal2.getText().toString().replaceAll("[.]","")))+"\ndengan penerima "+nama_konsumen.toUpperCase());
        builder.setCancelable(false);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //send data to server
                progressSend = ProgressDialog.show(mContext,null,"Transfer...",true,false);
                onTransfer(mNominal2.getText().toString().replaceAll("[.]",""));
                // intentSucess();

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



    private void setCurrency(final EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    edt.removeTextChangedListener(this);

                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String cleanString = s.toString().replaceAll(replaceable,
                            "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    NumberFormat formatter = NumberFormat
                            .getCurrencyInstance(local);
                    formatter.setMaximumFractionDigits(0);
                    formatter.setParseIntegerOnly(true);
                    String formatted = formatter.format((parsed));

                    String replace = String.format("[Rp\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String clean = formatted.replaceAll(replace, "");

                    current = formatted;
                    edt.setText(clean);
                    edt.setSelection(clean.length());
                    edt.addTextChangedListener(this);
                }
            }
        });
    }

}
