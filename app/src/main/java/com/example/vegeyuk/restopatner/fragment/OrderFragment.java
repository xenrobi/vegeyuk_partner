package com.example.vegeyuk.restopatner.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.activities.ListDeliveryActivity;
import com.example.vegeyuk.restopatner.adapter.PagerOrderTabAdapter;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.responses.ResponseValue;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SessionManager;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {

    SessionManager sessionManager;
    Context mContext;
    Switch aSwitchOprasional, mOprasional;
    ApiService mApiService;
    HashMap<String,String> user;
    String id_restoran ;
    TextView tvOperasional;
    ImageButton btnPengantaran;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        final TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        final ViewPager viewPager = view.findViewById(R.id.pager);
        mOprasional = (Switch) view.findViewById(R.id.swOprasional);
        tvOperasional = (TextView) view.findViewById(R.id.tvOprasional);
        btnPengantaran =(ImageButton) view.findViewById(R.id.action_pengantaran);
        mContext = getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        sessionManager = new SessionManager(getActivity());
        user =sessionManager.getRestoDetail();
        id_restoran = user.get(SessionManager.ID_RESTORAN);

        mApiService = ServerConfig.getAPIService();

        PagerOrderTabAdapter pagerAdapterTab = new PagerOrderTabAdapter(getFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapterTab);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mOprasional.setChecked(getOperasional());

        if(getOperasional()){
            tvOperasional.setText("Buka");
            tvOperasional.setTextColor(ContextCompat.getColor(mContext,R.color.green));
        }else {
            tvOperasional.setText("Tutup");
            tvOperasional.setTextColor(ContextCompat.getColor(mContext,R.color.red));
        }

        mOprasional.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    tvOperasional.setText("Buka");
                    tvOperasional.setTextColor(ContextCompat.getColor(mContext,R.color.green));
                    setOprasional(b,id_restoran);
                }else {
                    tvOperasional.setText("Tutup");
                    tvOperasional.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                    alert( b);
                }
            }
        });

        btnPengantaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Intent intent = new Intent(mContext, ListDeliveryActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }




    public Boolean getOperasional(){
        String operasional = (user.get(SessionManager.OPERASIONAL));
        if(operasional.equals("1")){
            return true;
        }else {
            return false;
        }

    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_actionbar, menu);
        MenuItem itemSwitch = menu.findItem(R.id.switchId);
        itemSwitch.setActionView(R.layout.switch_layout);
        final Switch sw = (Switch) menu.findItem(R.id.switchId).getActionView().findViewById(R.id.switchAB);
        Toast.makeText(mContext,"Oprasional "+getOperasional(),Toast.LENGTH_SHORT).show();
        sw.setChecked(getOperasional());
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                if(b){
                    Toast.makeText(mContext,"ON",Toast.LENGTH_SHORT).show();
                    setOprasional(b,id_restoran);
                }else {

                }
            }
        });

    }


    public void setOprasional(boolean oprasional,String id_restoran){
        int value =0;
        if (oprasional){
            value = 1;
        }

        final int finalValue = value;
        mApiService.setOperasional(id_restoran,value).enqueue(new Callback<ResponseValue>() {
            @Override
            public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                if(response.isSuccessful()){
                    String status = response.body().getValue();
                    String message = response.body().getMessage();
                    if(status.equals("1")){
                        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();

                        sessionManager.updateOprasinal(finalValue);
                    }
                    Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseValue> call, Throwable t) {
                Toast.makeText(mContext,R.string.loss_connect,Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void alert(final boolean b){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Operasional Restoran");
        builder.setMessage("Anda yakin akan menutup Restoran ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //send data to server
                setOprasional(b,id_restoran);

            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                mOprasional.setChecked(true);
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



}
