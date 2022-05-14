package com.example.vegeyuk.restopatner.activities.resto;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Satuan;
import com.example.vegeyuk.restopatner.responses.ResponseSatuan;
import com.example.vegeyuk.restopatner.responses.ResponseValue;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMenuActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;

    SessionManager sessionManager;
    HashMap<String,String> user;
    Context mContext;
    String id_kategoriStr,id_restoranStr;
    private Uri filePath ;
    ApiService mApiService ;
    String mediaPath = "";
    private ProgressDialog progressDialog;
    String ketersediaan = "0";
    String idSatuan ="";
    List<Satuan> semuaSatuanItems;


    @BindView(R.id.imageViewMenu)
    ImageView mPhoto;
    @BindView(R.id.etNamaMenu)
    EditText mNamamenu;
    @BindView(R.id.etHargaMenu)
    EditText mHargaMenu;
    @BindView(R.id.etMenuDiscount)
    EditText mDiscountMenu;
    @BindView(R.id.etDeskripsi)
    EditText mDeskripsi;
    @BindView(R.id.swKetersediaan)
    Switch swKetersediaan;
    @BindView(R.id.btnSubmitMenu)
    TextView mSubmitMenu;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.spinnerSatuan)
    Spinner spinnerSatuan;
    private String nama_kategoriStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        mContext = this;
        ButterKnife.bind(this);
        //mHargaMenu.addTextChangedListener(onTextChangedListener());
        setCurrency(mHargaMenu);
        mApiService = ServerConfig.getAPIService();
        getIntentComing();
        getSupportActionBar().setTitle("Tambah "+nama_kategoriStr);
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getRestoDetail();
        id_restoranStr = user.get(SessionManager.ID_RESTORAN);

        Toast.makeText(mContext,"id resto "+id_restoranStr+" id kat"+id_kategoriStr,Toast.LENGTH_SHORT).show();

        swKetersediaan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Toast.makeText(mContext, "Menu " + (isChecked ? "Tersedia" : "Tidak Tersedia"),
                        Toast.LENGTH_SHORT).show();
                if(isChecked) {
                    ketersediaan = "1";
                } else {
                    ketersediaan = "0";
                }
            }
        });


    }




    private void getIntentComing() {

        if(getIntent().hasExtra("id_kategori")){
            id_kategoriStr =getIntent().getStringExtra("id_kategori");
            nama_kategoriStr = getIntent().getStringExtra("nama_kategori");
        }

        final ProgressDialog loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);



        mApiService.satuan().enqueue(new Callback<ResponseSatuan>() {
            @Override
            public void onResponse(Call<ResponseSatuan> call, Response<ResponseSatuan> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    semuaSatuanItems = response.body().getSatuan();
                    List<String> id = new ArrayList<String>();
                    List<String> listSpinner = new ArrayList<String>();
                    for (int i = 0; i < semuaSatuanItems.size(); i++){
                        id.add(semuaSatuanItems.get(i).getId().toString());
                        listSpinner.add(semuaSatuanItems.get(i).getSatuanNama());
                    }
                    // Set hasil result json ke dalam adapter spinner
                    ArrayAdapter<Satuan> adapter = new ArrayAdapter<Satuan>(mContext, android.R.layout.simple_spinner_dropdown_item, semuaSatuanItems);
                    spinnerSatuan.setAdapter(adapter);
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Gagal mengambil data satuan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseSatuan> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }
        });

    }





    @OnClick(R.id.imageViewMenu) void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath =  pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);

        intent.setDataAndType(data,"image/*");


        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                mPhoto.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
                Cursor cursor = getContentResolver().query(filePath, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);



                Toast.makeText(AddMenuActivity.this,mediaPath,Toast.LENGTH_SHORT).show();
                // Set the Image in ImageView for Previewing the Media
                mPhoto.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();


//            android.net.Uri selectedImage = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//            if (cursor == null)
//                return;
//
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String filePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            File file = new File(filePath);
//
//            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
//            body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
//            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

            }else {
                Toast.makeText(mContext,"anda belum memilih foto",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(mContext,"ada yang error",Toast.LENGTH_SHORT).show();
        }

    }


    @OnClick(R.id.btnSubmitMenu) void SubmitMenu () {


        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);




        spinnerSatuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedName = parent.getItemAtPosition(position).toString();
                Satuan satuan = (Satuan) parent.getSelectedItem();
                idSatuan= satuan.getId().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Satuan satuan = (Satuan) parent.getSelectedItem();
                idSatuan= satuan.getId().toString();

            }
        });

        String id_satuanStr = semuaSatuanItems.get(spinnerSatuan.getSelectedItemPosition()).getId().toString();


        //create progres dialog
        progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);

        if(mNamamenu.getText().toString().isEmpty()||mNamamenu.getText().toString().equals(null)) {
            progressDialog.dismiss();
            mNamamenu.setError("Nama diperlukan");
            mNamamenu.requestFocus();
            return;
        }else if(mHargaMenu.getText().toString().toString().replaceAll("[.]","").isEmpty()||mHargaMenu.getText().toString().toString().replaceAll("[.]","").equals(null)){
            progressDialog.dismiss();
            mHargaMenu.setError("Harga diperlukan");
            mHargaMenu.requestFocus();
            return;
        }else if(mDeskripsi.getText().toString().isEmpty()||mDeskripsi.getText().toString().equals(null)){
            progressDialog.dismiss();
            mDeskripsi.setError("Deskripsi diperluka");
            mDeskripsi.requestFocus();
            return;
        }else if(mDiscountMenu.getText().toString().equals(null)||mDiscountMenu.getText().toString().isEmpty()){
            progressDialog.dismiss();
            mDiscountMenu.setError("Discount diperlukan");
            mDiscountMenu.requestFocus();
            return;
        }else if(mediaPath.equals(null)||mediaPath.isEmpty()){
            progressDialog.dismiss();
            Toast.makeText(mContext,"anda belum memilih foto",Toast.LENGTH_SHORT).show();
            return;
        }else {
            progressDialog.dismiss();


            File file = new File(mediaPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("menu_foto", file.getName(), requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            RequestBody menu_nama = RequestBody.create(MediaType.parse("text/plain"), mNamamenu.getText().toString());
            RequestBody menu_harga = RequestBody.create(MediaType.parse("text/plain"), mHargaMenu.getText().toString().replaceAll("[.]",""));
            RequestBody menu_deskripsi = RequestBody.create(MediaType.parse("text/plain"), mDeskripsi.getText().toString());
            RequestBody menu_discount = RequestBody.create(MediaType.parse("text/plain"), mDiscountMenu.getText().toString());
            RequestBody id_restoran = RequestBody.create(MediaType.parse("text/plain"), id_restoranStr);
            RequestBody id_kategori = RequestBody.create(MediaType.parse("text/plain"), id_kategoriStr);
            RequestBody id_satuan = RequestBody.create(MediaType.parse("text/plain"), id_satuanStr);
            RequestBody menu_ketersediaan = RequestBody.create(MediaType.parse("text/plain"),ketersediaan);

            //Toast.makeText(mContext,mHargaMenu.getText().toString().replaceAll("[.]",""),Toast.LENGTH_SHORT).show();


            mApiService.addMenu(fileToUpload, menu_nama, menu_deskripsi, menu_harga, id_restoran, id_kategori,id_satuan, menu_discount, menu_ketersediaan).enqueue(new Callback<ResponseValue>() {
                @Override
                public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        if (response.body().getValue().equals("1")) {
                            Toast.makeText(mContext,"Berhasil Menambah Menu",Toast.LENGTH_LONG).show();
                            onBackPressed();
                        } else {
                            Snackbar.make(coordinatorLayout,message,Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseValue> call, Throwable t) {
                    Snackbar.make(coordinatorLayout,R.string.loss_connect,Snackbar.LENGTH_SHORT).show();
                }
            });





        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext,MainActivity.class);
        intent.putExtra("menu","menu");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void clear(){
        mPhoto.setImageResource(R.drawable.ic_add_kurir);
        mNamamenu.setText(null);
        mHargaMenu.setText(null);
        mDeskripsi.setText(null);
        mDiscountMenu.setText(null);
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


