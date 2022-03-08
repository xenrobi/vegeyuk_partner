package com.example.vegeyuk.restopatner.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.adapter.DeliveryAdapter;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Order;
import com.example.vegeyuk.restopatner.responses.ResponseOrder;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDeliveryActivity extends AppCompatActivity{

    SessionManager sessionManager;
    HashMap<String,String> user ;
    Context mContext;
    RecyclerView recyclerView;
    DeliveryAdapter orderAdapter;
    List<Order> orderList;
    ApiService mApiService;
    CoordinatorLayout coordinatorLayout;
    String id_delivery,type_delivery;

    View message;
    ImageView icon_message;
    TextView title_message,sub_title_message;

    SwipeRefreshLayout swipeRefreshLayout;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._activity_main_kurir);
        mContext = this;
        getSupportActionBar().setTitle("Daftar Pengantaran");
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mApiService = ServerConfig.getAPIService();
        sessionManager = new SessionManager(mContext);

        if (sessionManager.isRestoran()){
            type_delivery = "restoran";
            user = sessionManager.getRestoDetail();
            id_delivery = user.get(SessionManager.ID_RESTORAN);

        }else {
            type_delivery = "kurir";
            user = sessionManager.getKurirDetail();
            id_delivery = user.get(SessionManager.ID_KURIR);
        }

        user = sessionManager.getKurirDetail();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        swipeRefreshLayout =(SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        message = findViewById(R.id.error);
        icon_message = (ImageView) findViewById(R.id.img_msg);
        title_message =  (TextView) findViewById(R.id.title_msg);
        sub_title_message =  (TextView) findViewById(R.id.sub_title_msg);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newAsycn();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private void newAsycn() {

        ArrayList<String> status = new ArrayList<String>();
            status.add("proses");
            mApiService.getOrderOnDelivery(id_delivery,type_delivery).enqueue(new Callback<ResponseOrder>() {
            @Override
            public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValue().equals("1")) {
                        if (orderAdapter != null) {
                            orderAdapter.clear();
                            orderAdapter.addAll(response.body().getData());
                        } else {
                            orderAdapter = new DeliveryAdapter(mContext, response.body().getData());
                            recyclerView.setAdapter(orderAdapter);
                            message.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
//                            orderAdapter.clear();
//                            orderAdapter.addAll(response.body().getData());
                        }

                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                        message.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        icon_message.setImageResource(R.drawable.msg_courier);
                        title_message.setText("Anda Tidak Memiliki Pengantaran ");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseOrder> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(coordinatorLayout,R.string.loss_connect, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);
        setData();
    }

    private void setData() {
        String id_restoran = user.get(SessionManager.ID_RESTORAN).toString();
        ArrayList<String> status = new ArrayList<String>();
        status.add("proses");
        mApiService.getOrderOnDelivery(id_delivery,type_delivery).enqueue(new Callback<ResponseOrder>() {
            @Override
            public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {
                orderList = response.body().getData();
                String value = response.body().getValue();
                String messageStr = response.body().getMessage();
                if(value.equals("1"))
                {
                    progressDialog.dismiss();
                    orderAdapter = new DeliveryAdapter(mContext,orderList);
                    recyclerView.setAdapter(orderAdapter);
                }else {
                    progressDialog.dismiss();
                    progressDialog.dismiss();
                    message.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    icon_message.setImageResource(R.drawable.msg_courier);
                    title_message.setText("Anda Tidak Memiliki Pengantaran ");
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

}
