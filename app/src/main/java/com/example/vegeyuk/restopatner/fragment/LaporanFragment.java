package com.example.vegeyuk.restopatner.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.adapter.LaporanAdapter;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Menu;
import com.example.vegeyuk.restopatner.responses.ResponseLaporan;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.NonScrollListView;
import com.example.vegeyuk.restopatner.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanFragment extends Fragment {

    ApiService mApiService;
    NonScrollListView list;
    SessionManager sessionManager;
    HashMap<String,String> user;
    Context mContext;
    LaporanAdapter laporanAdapter;
    NestedScrollView nestedScrollView;
    private List<Menu> menuList = new ArrayList<>();

    TextView mPesanHarian,mPengantaranHarian,mSelesaiHarian,mBatalHarian,mSelesaiBulanan,mBatalBulanan;

    View message;
    ImageView icon_message;
    TextView title_message,sub_title_message;

    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_laporan, container, false);
        mContext = getContext();
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getRestoDetail();
        mApiService = ServerConfig.getAPIService();






        init(view);





        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);
        getData();
    }

    private void init(View view) {
        list = (NonScrollListView) view.findViewById(R.id.listview);
        mPesanHarian = (TextView) view.findViewById(R.id.tvPesanHarian);
        mPengantaranHarian = (TextView) view.findViewById(R.id.tvPengantaranHarian);
        mSelesaiHarian = (TextView) view.findViewById(R.id.tvSelesaiHarian);
        mBatalHarian = (TextView) view.findViewById(R.id.tvBatlHarian);
        mSelesaiBulanan = (TextView) view.findViewById(R.id.tvSelesaiBulanan);
        mBatalBulanan= (TextView) view.findViewById(R.id.tvBatalBulanan);

        nestedScrollView = view.findViewById(R.id.nestesScrollView);
        message = view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message =  (TextView) view.findViewById(R.id.title_msg);
        sub_title_message =  (TextView) view.findViewById(R.id.sub_title_msg);

        setListViewHeightBasedOnChildren(list);

    }

    void getData(){
        mApiService.laporan(user.get(SessionManager.ID_RESTORAN)).enqueue(new Callback<ResponseLaporan>() {
            @Override
            public void onResponse(Call<ResponseLaporan> call, Response<ResponseLaporan> response) {
                if(response.isSuccessful()){
                    menuList = response.body().getMenu();
                    laporanAdapter = new LaporanAdapter(mContext,menuList);
                    list.setAdapter(laporanAdapter);
                    mPesanHarian.setText(response.body().getJumlahOrder().toString());
                    mPengantaranHarian.setText(response.body().getJumlahPengantaran().toString());
                    mBatalHarian.setText(response.body().getJumlahBatal().toString());
                    mSelesaiHarian.setText(response.body().getJumlahSelesai().toString());
                    mBatalBulanan.setText(response.body().getOrderMonthBatal().toString());
                    mSelesaiBulanan.setText(response.body().getOrderMonthSelesai().toString());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseLaporan> call, Throwable t) {
                progressDialog.dismiss();
                nestedScrollView.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                icon_message.setImageResource(R.drawable.msg_no_connection);
                title_message.setText("Opps..Gagal Terhubung Keserver");
                sub_title_message.setText("Priksa kembali koneksi internet  \n perangkat anda \n");
            }
        });
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

}
