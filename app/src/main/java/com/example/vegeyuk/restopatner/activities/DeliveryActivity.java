package com.example.vegeyuk.restopatner.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.activities.kurir.KurirMainActivity;
import com.example.vegeyuk.restopatner.activities.resto.MainActivity;
import com.example.vegeyuk.restopatner.adapter.DetailOrderAdapter;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Delivery;
import com.example.vegeyuk.restopatner.models.Menu;
import com.example.vegeyuk.restopatner.models.Order;
import com.example.vegeyuk.restopatner.responses.ResponseOneOrder;
import com.example.vegeyuk.restopatner.responses.ResponseValue;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.AbsRunTimePermission;
import com.example.vegeyuk.restopatner.utils.NonScrollListView;
import com.example.vegeyuk.restopatner.utils.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.shadowfax.proswipebutton.ProSwipeButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryActivity extends AppCompatActivity implements OnMapReadyCallback {
    private AbsRunTimePermission absRuntimePermission;
    private DatabaseReference database;
    private static final int REQUEST_PERMISSION = 10;

    LocationManager lm;
    LocationListener locationListener;
    String latt, langg,type_delivery,id_delivery,order_type_delivery,order_id_delivery;

    private GoogleMap mMap;
    Marker marker;


    @BindView(R.id.tvNamaPemesan)
    TextView mNamaPemesan;
    @BindView(R.id.tvPhonePemesan)
    TextView mPhonePemesan;
    @BindView(R.id.tvAlamatAntar)
    TextView mAlamatAntar;
    @BindView(R.id.tvTotal)
    TextView mTotal;
    @BindView(R.id.bottom_sheet)
    LinearLayout llBottomSheet;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.listview)
    NonScrollListView list;
    @BindView(R.id.btnBayar)
    ProSwipeButton btnBayar;
    @BindView(R.id.biayaAntar)
    TextView mBiayaantar;
    @BindView(R.id.btnBatal)
    TextView btnBatal;
    @BindView(R.id.pb1)
    TextView mPb1;

    String id_order, lat, lang, alamat;
    Order pesan;
    List<Menu> menuList;
    //    private List<Menu> detailOrders = new ArrayList<>();
    private DetailOrderAdapter orderAdapter;
    ApiService mApiService;
    SessionManager sessionManager;
    HashMap<String,String> user;

    ProgressDialog progressOn, progressOrder, progressChnageMetode;

    Context mContext;

    private LatLng pickUpLatLng; // Jakarta
    private LatLng locationLatLng = null; // Cirebon

    AlertDialog alert;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        ButterKnife.bind(this);
        //mengambil referensi ke firebase database
        database = FirebaseDatabase.getInstance().getReference();
        getIncomingIntent();
        pickUpLatLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));
        mContext = this;
        sessionManager = new SessionManager(mContext);
        mApiService = ServerConfig.getAPIService();

        if (sessionManager.isRestoran()){
            type_delivery = "restoran";
            user = sessionManager.getRestoDetail();
            id_delivery = user.get(SessionManager.ID_RESTORAN);

        }else {
            type_delivery = "kurir";
            user = sessionManager.getKurirDetail();
            id_delivery = user.get(SessionManager.ID_KURIR);
        }

        if (order_id_delivery.equalsIgnoreCase(id_delivery)&&order_type_delivery.equalsIgnoreCase(type_delivery)) {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new MyLocationListener();
            //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10 * 1000L, 0, locationListener);
        }





        //request permission here
        absRuntimePermission = new AbsRunTimePermission() {
            @Override
            public void onPermissionGranted(int requestcode) {
                Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
            }
        };

//        absRuntimePermission.requestAppPermissions(new String[]{
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION},
//                R.string.msg, REQUEST_PERMISSION);






        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
       // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // set the peek height
        bottomSheetBehavior.setPeekHeight(300);

        // set hideable or not
        bottomSheetBehavior.setHideable(false);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });

        setListViewHeightBasedOnChildren(list);

        //maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        //set Title bar
        getSupportActionBar().setTitle("Delivery");


        btnBayar.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // task success! show TICK icon in ProSwipeButton
//                        btnBayar.showResultIcon(true); // false if task failed
//                    }
//                }, 2000);
                mApiService.pembayaran(id_order).enqueue(new Callback<ResponseValue>() {
                    @Override
                    public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                        if(response.isSuccessful()){
                            if(response.body().getValue().equals("1")){
                                btnBayar.showResultIcon(true);
                                Toast.makeText(mContext,"Selesai Pengantaran",Toast.LENGTH_SHORT).show();
                                if (order_id_delivery.equalsIgnoreCase(id_delivery)&&order_type_delivery.equalsIgnoreCase(type_delivery)) {
                                    //sessionManager.selesaiPenganataran();
                                    lm.removeUpdates(locationListener);
                                    lm = null;
                                }
                            }else if(response.body().getValue().equals("2")){

                                btnBayar.showResultIcon(false);
                                alertChangeMetode();
                                Toast.makeText(mContext,"saldo tidak mencukupi",Toast.LENGTH_SHORT).show();
                            }else {
                                btnBayar.showResultIcon(false);
                                Toast.makeText(mContext,"Terjadi Kesalahan, Coba Lagi",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseValue> call, Throwable t) {
                        btnBayar.showResultIcon(false);
                        Toast.makeText(mContext,R.string.loss_connect,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressOn = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,true);
        getData();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            absRuntimePermission.requestAppPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    R.string.msg, REQUEST_PERMISSION);
        }
        if (order_id_delivery.equalsIgnoreCase(id_delivery)&&order_type_delivery.equalsIgnoreCase(type_delivery)) {
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    locationListener
            );
        }

    }

    private void getIncomingIntent (){

        if(getIntent().hasExtra("pesan")){
            id_order = getIntent().getStringExtra("pesan");
            lat = getIntent().getStringExtra("lat");
            lang = getIntent().getStringExtra("lang");
            alamat = getIntent().getStringExtra("alamat");
            order_id_delivery = getIntent().getStringExtra("id_delivery");
            order_type_delivery = getIntent().getStringExtra("type_delivery");
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        marker = mMap.addMarker(new MarkerOptions().position(pickUpLatLng).title("Lokasi Antar: ").snippet(alamat).draggable(false));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickUpLatLng,15),5000,null);
        marker.showInfoWindow();

//        String lokasiAwal = pickUpLatLng.latitude + "," + pickUpLatLng.longitude;
//        String lokasiAkhir = locationLatLng.latitude + "," + locationLatLng.longitude;
//
//
//
//
//
//        // Tambah Marker
//        Marker lokasiAntar = mMap.addMarker(new MarkerOptions().position(pickUpLatLng).title("Lokasi Awal"));
//        mMap.addMarker(new MarkerOptions().position(locationLatLng).title("Lokasi Akhir"));
//        mMap.addPolyline(new PolylineOptions()
//                .add(pickUpLatLng, locationLatLng)
//                .width(5)
//                .color(Color.RED));
//        /** START
//         * Logic untuk membuat layar berada ditengah2 dua koordinat
//         */
//
//        LatLngBounds.Builder latLongBuilder = new LatLngBounds.Builder();
//        latLongBuilder.include(pickUpLatLng);
//        latLongBuilder.include(locationLatLng);
//
//        // Bounds Coordinata
//        LatLngBounds bounds = latLongBuilder.build();
//
//        int width = getResources().getDisplayMetrics().widthPixels;
//        int height = getResources().getDisplayMetrics().heightPixels;
//        int paddingMap = (int) (width * 0.2); //jarak dari
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, paddingMap);
//        mMap.animateCamera(cu);
//
//        /** END
//         * Logic untuk membuat layar berada ditengah2 dua koordinat
//         */
//


    }


    public void setValue (){
        mNamaPemesan.setText(pesan.getOrderKonsumen());
        mAlamatAntar.setText(pesan.getOrderAlamat());
        mPhonePemesan.setText("+"+pesan.getOrderKonsumenPhone());
        mBiayaantar.setText("Biaya Antar: "+kursIndonesia(Double.parseDouble(pesan.getOrderBiayaAnatar())));
        double total = 0;
        //hitung harga
        for (int i = 0; i < menuList.size(); i++) {
            if(menuList.get(i).getPivot().getDiscount().toString().isEmpty() || menuList.get(i).getPivot().getDiscount() ==0) {
                total += Double.parseDouble(menuList.get(i).getPivot().getHarga()) * menuList.get(i).getPivot().getQty();
            }else{
                Double harga_discount = HitungDiscount(Double.parseDouble(menuList.get(i).getPivot().getHarga()),menuList.get(i).getPivot().getDiscount());
                total +=harga_discount * menuList.get(i).getPivot().getQty();
            }
        }
        total = total+ Double.parseDouble(pesan.getOrderBiayaAnatar());

        //cek pajak
        if (pesan.getOrder_pajak_pb_satu() == 0){
            mPb1.setVisibility(View.GONE);

        }else{
            mPb1.setVisibility(View.VISIBLE);
            double pb1 = (pesan.getOrder_pajak_pb_satu()/100.0)*total;
            total = total + pb1;
            mPb1.setText("PB1 ("+pesan.getOrder_pajak_pb_satu()+"%) "+kursIndonesia(pb1));

        }

        mTotal.setText(kursIndonesia(total)+"-"+pesan.getOrderMetodeBayar());

        if (pesan.getOrderStatus().equalsIgnoreCase("seleai")){
            btnBayar.setActivated(true);
        }
    }


    @OnClick(R.id.fab) void navigation  (){
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+pesan.getOrderLat()+","+pesan.getOrderLong()+"&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }


    void getData(){
        mApiService.getOneOrder(id_order).enqueue(new Callback<ResponseOneOrder>() {
            @Override
            public void onResponse(Call<ResponseOneOrder> call, Response<ResponseOneOrder> response) {
                if(response.isSuccessful()){
                    if (response.body().getValue().equals("1")){
                        progressOn.dismiss();
                         pesan = response.body().getOrder();
                         menuList = pesan.getDetailOrder();
                        //set value
                        orderAdapter = new DetailOrderAdapter(DeliveryActivity.this,menuList);
                        list.setAdapter(orderAdapter);



                        setValue();
                    }else {
                        progressOn.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseOneOrder> call, Throwable t) {
                progressOn.dismiss();
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





    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
       // return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_top_up, (android.view.Menu) menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.topup){
            Toast.makeText(mContext,"top Up",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext,TopUpActivity.class);
            intent.putExtra("id_konsumen",pesan.getIdKonsumen().toString());
            intent.putExtra("nama_konsumen",pesan.getOrderKonsumen().toString());
            startActivity(intent);
        }else if(item.getItemId() == R.id.telpon){
            String numberPhone = "tel:"+"+"+pesan.getOrderKonsumenPhone().toString();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(numberPhone));
            startActivity(intent);
        }
        return true;
    }


    //listview not scrolll
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public Double HitungDiscount (Double Harga,Integer Discount){
        double harga_potongan = ((Discount/100.00)*Harga);
        return Harga-harga_potongan;
    }


    @Override
    public void onBackPressed() {
        Intent intent;
        if (!sessionManager.isRestoran()){
            intent = new Intent(mContext, KurirMainActivity.class);
        }else {
            intent = new Intent(mContext, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void alertChangeMetode(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Opps..Saldo Konsumen Tidak Cukup");
        builder.setMessage("Lakukan TOP UP Saldo atau Ubah Pembayaran Secara Tunai");
        builder.setCancelable(true);
        builder.setPositiveButton("TOP UP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //send data to top up
                Toast.makeText(mContext,"top Up",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,TopUpActivity.class);
                intent.putExtra("id_konsumen",pesan.getIdKonsumen().toString());
                intent.putExtra("nama_konsumen",pesan.getOrderKonsumen().toString());
                startActivity(intent);

            }
        });
        builder.setNegativeButton("BAYAR TUNAI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressChnageMetode = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,true);
                    changeMetodeBayar();

            }
        });


       alert =builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            }
        });
        alert.show();
    }

    private void changeMetodeBayar() {
//        mApiService.changeMetodeBayar(id_order).enqueue(new Callback<ResponseValue>() {
//            @Override
//            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
//                btnBayar.showResultIcon(true);
//                Toast.makeText(mContext,"Selesai Pengantaran",Toast.LENGTH_SHORT).show();
//                sessionManager.selesaiPenganataran();
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseValue> call, Throwable t) {
//
//            }
//        });

        mApiService.changeMetodeBayar(id_order,"ambil").enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if (response.isSuccessful()){
                    if(response.body().getValue().equalsIgnoreCase("1")){
                     alert.dismiss();
                     progressChnageMetode.dismiss();
                     Toast.makeText(mContext,"Berhasil Mengganti Metode Bayar",Toast.LENGTH_SHORT).show();
                     onResume();
                    }else {
                        alert.dismiss();
                        progressChnageMetode.dismiss();
                        Toast.makeText(mContext,"Gagal Mengganti Metode Bayar",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                alert.dismiss();
                progressChnageMetode.dismiss();
                Toast.makeText(mContext,R.string.loss_connect,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick({R.id.btnBatal})void  batal (){
        alertBatalPesanan();
    }

    private void alertBatalPesanan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Batalkan Peasanan");
        builder.setMessage("Apakah Anda yakin akan membatalkan pesanan ini ? ");
        builder.setCancelable(false);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //send data to server
                progressOrder = new ProgressDialog(mContext);
                progressOrder = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,true);
                cancelOrder();
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

    private void cancelOrder() {
        mApiService.updateStatusPesan(id_order,"batal").enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                progressOrder.dismiss();
                if(response.isSuccessful()){
                    if(response.body().getValue().equals("1")){
                        Intent intent = new Intent(mContext,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        if (order_id_delivery.equalsIgnoreCase(id_delivery)&&order_type_delivery.equalsIgnoreCase(type_delivery)) {
                            sessionManager.selesaiPenganataran();
                            lm.removeUpdates(locationListener);
                            lm = null;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                progressOrder.dismiss();
                Toast.makeText(mContext,R.string.loss_connect,Toast.LENGTH_SHORT).show();
            }
        });
    }


    //method MyloacationListener
    int a= 0;
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                latt = String.valueOf(location.getLatitude());


                langg = String.valueOf(location.getLongitude());
                HashMap<String,String> pengantaran = sessionManager.getPengantaran();
                //mAlamatAntar.setText(id_order+","+ (a++) +","+latt+" , "+langg);

                //Toast.makeText(DeliveryActivity.this, latt + ","+ id_order+"," + langg, Toast.LENGTH_SHORT).show();
                Delivery delivery = new Delivery(id_order,latt,langg);
                updateDelivery(delivery);

            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String statusString = "";
            switch (status) {
                case LocationProvider.AVAILABLE:
                    statusString = "available";
                case LocationProvider.OUT_OF_SERVICE:
                    statusString = "out of service";
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    statusString = "temporarily unavailable";
            }

            Toast.makeText(getBaseContext(),
                    provider + " " + statusString,
                    Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getBaseContext(),
                    "Provider: " + provider + " enabled",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getBaseContext(),
                    "Provider: " + provider + " disabled",
                    Toast.LENGTH_SHORT).show();
            showSettingsAlert();
        }
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
                       // Toast.makeText(mContext, "Data berhasil ditubah", Toast.LENGTH_LONG).show();

                    }
                });
    }



    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliveryActivity.this);

        //Setting Dialog Title
        alertDialog.setTitle("Permision");

        //Setting Dialog Message
        alertDialog.setMessage("izinkan semua permision");

        //On Pressing Setting button
        alertDialog.setPositiveButton("setting", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                DeliveryActivity.this.startActivity(intent);
            }
        });

        //On pressing cancel button
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


}
