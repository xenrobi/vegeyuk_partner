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

public class DeliveryListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Order> orderList = new ArrayList<>();
    ApiService mApiService;
    Context mContext;
    String id_restoran;
    HashMap<String,String> user;
    SessionManager sessionManager;

    View message;
    ImageView icon_message;
    TextView title_message,sub_title_message;
    View a;

    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_list_view,container,false);
        a=view;
        mApiService = ServerConfig.getAPIService();
        mContext = getActivity();
        sessionManager = new SessionManager(mContext);
        user =sessionManager.getRestoDetail();

        //error
        message = view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message =  (TextView) view.findViewById(R.id.title_msg);
        sub_title_message =  (TextView) view.findViewById(R.id.sub_title_msg);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        id_restoran = user.get(SessionManager.ID_RESTORAN.toString());



        return view;
    }


//   view @Override
//    public void onResume() {
//        super.onResume();
//        Toast.makeText(mContext,"onResume",Toast.LENGTH_SHORT).show();
//        getData();
//    }


    @Override
    public void onResume() {
        super.onResume();
        progressDialog = ProgressDialog.show(mContext,null,getString(R.string.memuat),true,false);
        getData();
    }




    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            getData();
        }
    }


    void getData(){

        ArrayList<String> status = new ArrayList<String>();
        status.add("pengantaran");

        mApiService.getOrder(id_restoran,status).enqueue(new Callback<ResponseOrder>() {
            @Override
            public void onResponse(Call<ResponseOrder> call, Response<ResponseOrder> response) {

                orderList = response.body().getData();
                String value = response.body().getValue();
                // String message = response.body().getMessage();
                if(value.equals("1")) {

                    DeliveryAdapter orderAdapter = new DeliveryAdapter(mContext, orderList);
                    recyclerView.setAdapter(orderAdapter);
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    message.setVisibility(View.VISIBLE);
                    icon_message.setImageResource(R.drawable.msg_order);
                    title_message.setText("Anda Tidak Memiliki Pengantaran ");
                    recyclerView.setVisibility(View.GONE);
                    sub_title_message.setText("Saat ini anda tidak memiliki pengantaran");
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
