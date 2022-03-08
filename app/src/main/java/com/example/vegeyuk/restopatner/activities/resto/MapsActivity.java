package com.example.vegeyuk.restopatner.activities.resto;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.responses.ResponseValue;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int geocoderMaxResult = 100;
    LatLng location,midLatLng;
    String alamat;
    Button mBtnSetLocation ;
    TextView mLocation;
    Context mContext;
    ApiService mApiService;
    SessionManager sessionManager;
    HashMap<String,String> sessionRstoran;
    ProgressDialog progressDialog;
    private static final String TAG = "MapsActivity";

    Boolean changeLoaction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mContext = this;
        mApiService = ServerConfig.getAPIService();
        sessionManager = new SessionManager(mContext);
        sessionRstoran = sessionManager.getRestoDetail();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mBtnSetLocation = (Button) findViewById(R.id.btnSetLocation);
        mLocation =(TextView) findViewById(R.id.tvLocation);



        location = new LatLng(1.1296365806908568, 104.03463099542692);

        getIntenComing();

        mBtnSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,alamat+" "+location.latitude+","+location.longitude,Toast.LENGTH_SHORT).show();
                progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);
                updateLokasi();

            }
        });

    }

    private void getIntenComing() {

        if(getIntent().hasExtra("lat") && getIntent().hasExtra("lang")){
            changeLoaction = true;
            String lat  = getIntent().getStringExtra("lat");
            String lang = getIntent().getStringExtra("lang");
            location = new LatLng(Double.parseDouble(lat),Double.parseDouble(lang));
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,17),5000,null);

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                midLatLng = mMap.getCameraPosition().target;
                location = midLatLng;
                alamat =getAddressLine(mContext).toString();
                mLocation.setText(alamat);
            }
        });
    }

    public List<Address> getGeocoderAddress(Context context){
        if(location != null){
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

            try {
                List<Address> addresses = geocoder.getFromLocation(location.latitude,location.longitude,geocoderMaxResult);
                return addresses;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getAddressLine (Context context){
        List<Address> addresses = getGeocoderAddress(context);

        if(addresses != null && addresses.size() > 0){
            Address address = addresses.get(0);
            String addresLine = address.getAddressLine(0);
            return addresLine;
        }else {
            return "Alamat Tidak Ditemukan" ;
        }
    }

    public void updateLokasi(){
        final String lat = String.valueOf(location.latitude);
        final String lang = String.valueOf(location.longitude);
        mApiService.updateLokasiRestoran(sessionRstoran.get(SessionManager.ID_RESTORAN),lat,lang).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    String value = response.body().getValue();

                    if(value.equals("1")){
                        Toast.makeText(mContext,"Berhasil Mengambil Lokasi",Toast.LENGTH_SHORT).show();
                        sessionManager.updateLocation(lat,lang);
                        if (changeLoaction) {
                           onBackPressed();

                        }else {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            MapsActivity.this.finish();
                        }
                    }else {
                        Toast.makeText(mContext,"Gagal Mengambil Lokasi",Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage() );
                Toast.makeText(mContext,"lost",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
