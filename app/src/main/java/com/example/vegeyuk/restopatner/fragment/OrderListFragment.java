package com.example.vegeyuk.restopatner.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.adapter.OrderAdapter;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Order;
import com.example.vegeyuk.restopatner.responses.ResponseOrder;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.MyFirebaseMessagingService;
import com.example.vegeyuk.restopatner.utils.SessionManager;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListFragment extends Fragment {

   
    private List<Order> orderList = new ArrayList<>();
    ApiService mApiService;
    Context mContext;
    String id_restoran;
    HashMap<String, String> user;
    SessionManager sessionManager;
    RecyclerView recyclerView;
    View message,a;
    ImageView icon_message;
    TextView title_message, sub_title_message;

    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_list_view, container, false);
        a =view;
        mApiService = ServerConfig.getAPIService();
        mContext = getActivity();
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getRestoDetail();
        id_restoran = user.get(SessionManager.ID_RESTORAN.toString());

        //error
        message = view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message = (TextView) view.findViewById(R.id.title_msg);
        sub_title_message = (TextView) view.findViewById(R.id.sub_title_msg);



        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        return view;
    }


    public void setDAta() {

        ArrayList<String> status = new ArrayList<String>();
        status.add("proses");

        mApiService.getOrder(id_restoran, status).enqueue(new Callback<ResponseOrder>() {
            @Override
            public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {
                orderList = response.body().getData();
                String value = response.body().getValue();

                if (value.equals("1")) {
                    OrderAdapter orderAdapter = new OrderAdapter(mContext, orderList);
                    recyclerView.setAdapter(orderAdapter);
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    message.setVisibility(View.VISIBLE);
                    icon_message.setImageResource(R.drawable.msg_not_found);
                    title_message.setText("Anda Tidak Memiliki Pesanan ");
                    sub_title_message.setText("Tawarkan produk anda kepada pelanggan ");
                    recyclerView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ResponseOrder> call, Throwable t) {
                progressDialog.dismiss();
                message.setVisibility(View.VISIBLE);
                icon_message.setImageResource(R.drawable.msg_no_connection);
                title_message.setText("Opps..Gagal Terhubung Keserver");
                sub_title_message.setText("Priksa kembali koneksi internet  \n perangkat anda \n");
                recyclerView.setVisibility(View.GONE);
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);
        Toast.makeText(mContext, "on Resume Order", Toast.LENGTH_SHORT).show();
        setDAta();
    }


    MyFirebaseMessagingService b = new MyFirebaseMessagingService();

    public void update (){
        final RecyclerView recyclerView = (RecyclerView) a.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ArrayList<String> status = new ArrayList<String>();
        status.add("proses");

        mApiService.getOrder("1", status).enqueue(new Callback<ResponseOrder>() {
            @Override
            public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {
                orderList = response.body().getData();
                String value = response.body().getValue();

                if (value.equals("1")) {
                    OrderAdapter orderAdapter = new OrderAdapter(mContext, orderList);
                    recyclerView.setAdapter(orderAdapter);
                } else {
                    message.setVisibility(View.VISIBLE);
                    icon_message.setImageResource(R.drawable.msg_not_found);
                    title_message.setText("Anda Tidak Memiliki Pesanan ");
                    // sub_title_message.setText("Restoran di Sekitar Anda Siap \n Mengantarkan Pesaan Anda \n");
                }
            }

            @Override
            public void onFailure(Call<ResponseOrder> call, Throwable t) {

            }
        });
    }



}
