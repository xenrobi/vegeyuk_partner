package com.example.vegeyuk.restopatner.activities.kurir;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.adapter.OrderAdapter;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Order;
import com.example.vegeyuk.restopatner.responses.ResponseOrder;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment{

    SessionManager sessionManager;
    HashMap<String,String> user ;
    Context mContext;
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    List<Order> orderList;
    ApiService mApiService;
    CoordinatorLayout coordinatorLayout;

    View message;
    ImageView icon_message;
    TextView title_message,sub_title_message;

    SwipeRefreshLayout swipeRefreshLayout;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout._activity_main_kurir, container, false);

        mContext = getContext();

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        mApiService = ServerConfig.getAPIService();
        sessionManager = new SessionManager(mContext);
        user = sessionManager.getKurirDetail();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        swipeRefreshLayout =(SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        message = view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message =  (TextView) view.findViewById(R.id.title_msg);
        sub_title_message =  (TextView) view.findViewById(R.id.sub_title_msg);

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


        return view;
    }


    private void newAsycn() {
        String id_restoran = user.get(SessionManager.ID_RESTORAN).toString();
        ArrayList<String> status = new ArrayList<String>();
        status.add("proses");
        mApiService.getOrder(id_restoran,status).enqueue(new Callback<ResponseOrder>() {
            @Override
            public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {
                if (response.isSuccessful()){
                    if (response.body().getValue().equals("1")){
                        if(orderAdapter!=null) {
                            orderAdapter.clear();
                            orderAdapter.addAll(response.body().getData());
                        }else {
                            orderAdapter = new OrderAdapter(mContext,response.body().getData());
                            recyclerView.setAdapter(orderAdapter);
                            message.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
//                            orderAdapter.clear();
//                            orderAdapter.addAll(response.body().getData());
                        }

                        swipeRefreshLayout.setRefreshing(false);
                    }else {
                        swipeRefreshLayout.setRefreshing(false);
                        message.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        icon_message.setImageResource(R.drawable.msg_checklist);
                        title_message.setText("Anda Tidak Memiliki Pesanan ");
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
    public void onResume() {
        super.onResume();
        progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);
        setData();
    }

    private void setData() {
        String id_restoran = user.get(SessionManager.ID_RESTORAN).toString();
        ArrayList<String> status = new ArrayList<String>();
        status.add("proses");
        mApiService.getOrder(id_restoran,status).enqueue(new Callback<ResponseOrder>() {
            @Override
            public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {
                orderList = response.body().getData();
                String value = response.body().getValue();
                String messageStr = response.body().getMessage();
                if(value.equals("1"))
                {
                    progressDialog.dismiss();
                    orderAdapter = new OrderAdapter(mContext,orderList);
                    recyclerView.setAdapter(orderAdapter);
                }else {
                    progressDialog.dismiss();
                    progressDialog.dismiss();
                    message.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    icon_message.setImageResource(R.drawable.msg_checklist);
                    title_message.setText("Anda Tidak Memiliki Pesanan ");
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
