package com.example.vegeyuk.restopatner.activities.kurir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.activities.ListDeliveryActivity;
import com.example.vegeyuk.restopatner.activities.SignInActivity;
import com.example.vegeyuk.restopatner.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class KurirMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;
    SessionManager sessionManager;
    private  View navHeader;
    HashMap<String,String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurir_main);
        mContext = this;
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getKurirDetail();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Kurir Resto");




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);

        //Select Home by default
        navigationView.setCheckedItem(R.id.nav_order);
        Fragment fragment = new OrderFragment();
        displaySelectedFragment(fragment);


        TextView navUsername = (TextView) navHeader.findViewById(R.id.title_nav);
        TextView subTitle = (TextView) navHeader.findViewById(R.id.sub_title_nav);
        navUsername.setText(user.get(SessionManager.KURIR_NAMA));
        subTitle.setText("+"+user.get(SessionManager.KURIR_PHONE));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kurir, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pengantaran:
//                if(sessionManager.isPengantaran()){
//                    HashMap<String,String> pengantaran = sessionManager.getPengantaran();
//                    Intent intent = new Intent(mContext, DeliveryActivity.class);
//                    intent.putExtra("pesan",pengantaran.get(SessionManager.ID_PENGANTARAN));
//                    intent.putExtra("lat",pengantaran.get(SessionManager.LAT));
//                    intent.putExtra("lang",pengantaran.get(SessionManager.LANG));
//                    intent.putExtra("alamat",pengantaran.get(SessionManager.ALAMAT));
////                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                }else {
//                    Toast.makeText(mContext,"Anda Tidak Dalam Pengantaran",Toast.LENGTH_SHORT).show();
//                }
                Intent intent1 = new Intent(mContext, ListDeliveryActivity.class);
                startActivity(intent1);
                break;
//            case R.id.account:
//                break;
//            case R.id.Logout:
//                FirebaseAuth.getInstance().signOut();
//                sessionManager.logoutUser();
//                Intent intent = new Intent(mContext, SignInActivity.class);
//                startActivity(intent);
//                finish();
//                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_order) {
            fragment = new OrderFragment();
            displaySelectedFragment(fragment);
        } else if (id == R.id.nav_riwayat) {
            fragment = new RiwayatDeliveryFragment();
            displaySelectedFragment(fragment);
        } else if (id == R.id.nav_laporan) {
            fragment = new LaporanDeliveryFragment();
            displaySelectedFragment(fragment);
        } else if (id == R.id.nav_keluar) {
            alertKonfirmasi();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    ProgressDialog progressDialogLogout;
    public void alertKonfirmasi(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Apakah Anda Yakin Akan Keluar");
        builder.setCancelable(false);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //send data to server

                progressDialogLogout = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);
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
        progressDialogLogout.dismiss();
        FirebaseAuth.getInstance().signOut();
        sessionManager.logoutUser();
        Intent intent = new Intent(mContext, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
