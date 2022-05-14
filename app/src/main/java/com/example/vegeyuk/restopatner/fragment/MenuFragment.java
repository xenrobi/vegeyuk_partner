package com.example.vegeyuk.restopatner.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.adapter.KategoriTabAdapter;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Kategori;
import com.example.vegeyuk.restopatner.models.Menu;
import com.example.vegeyuk.restopatner.responses.ResponseMenuKategori;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SessionManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ApiService mApiService;
    private List<Kategori> kategoriList = new ArrayList<>();
    private List<Menu> menuList = new ArrayList<>();
    Context mContext;
    HashMap<String,String> user;
    SessionManager sessionManager;

    View message;
    ImageView icon_message;
    TextView title_message,sub_title_message;

    ProgressDialog progressDialog;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu,container,false);
        mContext = getActivity();
        mApiService = ServerConfig.getAPIService();
        sessionManager = new SessionManager(mContext);
        user =sessionManager.getRestoDetail();
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager.setOffscreenPageLimit(5);

        message = view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message =  (TextView) view.findViewById(R.id.title_msg);
        sub_title_message =  (TextView) view.findViewById(R.id.sub_title_msg);



        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        setValue(user.get(SessionManager.ID_RESTORAN.toString()));




        return view;
    }


    private void initViews() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        setDynamicFragmentToTabLayout();

    }

    private void setDynamicFragmentToTabLayout() {
        for (int i = 0; i < kategoriList.size() ; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(kategoriList.get(i).getKategoriNama().toString()));
        }

        KategoriTabAdapter kategoriTabAdapter = new KategoriTabAdapter(getFragmentManager(),tabLayout.getTabCount(),menuList,kategoriList);



        viewPager.setAdapter(kategoriTabAdapter);
        //viewPager.setCurrentItem(0);

    }

    private void setValue(String id_restoran){
        progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);
        mApiService.getMenu(id_restoran).enqueue(new Callback<ResponseMenuKategori>() {
            @Override
            public void onResponse(Call<ResponseMenuKategori> call, Response<ResponseMenuKategori> response) {
                if(response.isSuccessful()){
                    if(response.body().getValue().equals("1")){
                        menuList = response.body().getRestoranMenu();
                        kategoriList = response.body().getRestoranKategori();
                        progressDialog.dismiss();
                        initViews();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMenuKategori> call, Throwable t) {
                progressDialog.dismiss();
                message.setVisibility(View.VISIBLE);
                icon_message.setImageResource(R.drawable.msg_no_connection);
                title_message.setText("Opps..Gagal Terhubung Keserver");
                sub_title_message.setText("Priksa kembali koneksi internet  \n perangkat anda \n");
                tabLayout.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
            }
        });

    }


}
