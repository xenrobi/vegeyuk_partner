package com.example.vegeyuk.restopatner.activities.kurir;

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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.adapter.LaporanAdapter;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Menu;
import com.example.vegeyuk.restopatner.responses.ResponseLaporanDelivery;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.NonScrollListView;
import com.example.vegeyuk.restopatner.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanDeliveryFragment extends Fragment {


    ApiService mApiService;
    NonScrollListView list;
    SessionManager sessionManager;
    HashMap<String,String> user;
    Context mContext;
    LaporanAdapter laporanAdapter;
    NestedScrollView nestedScrollView;
    private List<Menu> menuList = new ArrayList<>();

    TextView tvSelesaiHarian,tvBatalHarian,tvTotalHarian,tvSelesaiBulanan,tvBatalBulanan,tvTotalBulan,tvJumlahSelesai,tvJumlahBatal,tvJumlah;

    View message;
    ImageView icon_message;
    TextView title_message,sub_title_message;

    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_laporan_kurir, container, false);
        mContext = getContext();
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getKurirDetail();
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
        tvSelesaiHarian = (TextView) view.findViewById(R.id.tvSelesaiHarian);
        tvBatalHarian = (TextView) view.findViewById(R.id.tvBatalHarian);
        tvTotalHarian = (TextView) view.findViewById(R.id.tvTotalHarian);
        tvSelesaiBulanan = (TextView) view.findViewById(R.id.tvSelesaiBulanan);
        tvBatalBulanan = (TextView) view.findViewById(R.id.tvBatalBulanan);
        tvTotalBulan = (TextView) view.findViewById(R.id.tvTotalBulan);
        tvJumlahSelesai = (TextView) view.findViewById(R.id.tvJumlahSelesai);
        tvJumlahBatal= (TextView) view.findViewById(R.id.tvJumlahBatal);
        tvJumlah = (TextView) view.findViewById(R.id.tvJumlah);

        nestedScrollView = view.findViewById(R.id.nestesScrollView);
        message = view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message =  (TextView) view.findViewById(R.id.title_msg);
        sub_title_message =  (TextView) view.findViewById(R.id.sub_title_msg);


    }

    void getData(){
        mApiService.laporanDelivery(user.get(SessionManager.ID_KURIR),"kurir").enqueue(new Callback<ResponseLaporanDelivery>() {
            @Override
            public void onResponse(Call<ResponseLaporanDelivery> call, Response<ResponseLaporanDelivery> response) {
                if(response.isSuccessful()){

                    tvSelesaiHarian.setText(response.body().getSelesaiHariIni().toString());
                    tvBatalHarian.setText(response.body().getBatalHariIni().toString());
                    tvTotalHarian.setText(String.valueOf(response.body().getSelesaiHariIni()+response.body().getBatalHariIni()));
                    tvSelesaiBulanan.setText(response.body().getSelesaiBulanIni().toString());
                    tvBatalBulanan.setText(response.body().getBatalBulanIni().toString());
                    tvTotalBulan.setText(String.valueOf(response.body().getSelesaiBulanIni()+response.body().getBatalBulanIni()));
                    tvJumlahSelesai.setText(response.body().getTotalBerhasil().toString());
                    tvJumlahBatal.setText(response.body().getTotalBatal().toString());
                    tvJumlah.setText(String.valueOf(response.body().getTotalBerhasil()+response.body().getTotalBatal()));
                    progressDialog.dismiss();


                }
            }

            @Override
            public void onFailure(Call<ResponseLaporanDelivery> call, Throwable t) {
                progressDialog.dismiss();
                nestedScrollView.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                icon_message.setImageResource(R.drawable.msg_no_connection);
                title_message.setText("Opps..Gagal Terhubung Keserver");
                sub_title_message.setText("Priksa kembali koneksi internet  \n perangkat anda \n");
            }
        });
    }
}
