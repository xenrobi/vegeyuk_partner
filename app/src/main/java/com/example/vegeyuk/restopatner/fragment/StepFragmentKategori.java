package com.example.vegeyuk.restopatner.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Kategori;
import com.example.vegeyuk.restopatner.responses.ResponseListKategori;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StepFragmentKategori extends Fragment implements Step {

    LinearLayout linearMain;
    ApiService mApiService ;
    CheckBox checkBox;
    List<Kategori> kategoriList =new ArrayList<>();
    int count =0;
    int maxLimit=10;
    List<Integer> id_kategori = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_kategori, container,false);
        mApiService = ServerConfig.getAPIService();
        addData();
        linearMain = (LinearLayout) view.findViewById(R.id.linearmain);





        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }


    private void addData(){

        mApiService.getAllKategori().enqueue(new Callback<ResponseListKategori>() {

            @Override
            public void onResponse(Call<ResponseListKategori> call, Response<ResponseListKategori> response) {
                kategoriList = response.body().getData();
                Toast.makeText(getActivity(),String.valueOf(kategoriList.size()),Toast.LENGTH_SHORT).show();
                cb();
            }

            @Override
            public void onFailure(Call<ResponseListKategori> call, Throwable t) {
                Toast.makeText(getActivity(),"failure",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void cb (){
        for (int i = 0; i <kategoriList.size(); i++) {

//            checkBox = new CheckBox(getActivity());
//            checkBox.setId(kategoriList.get(i).getIdKategori());
//            checkBox.setText(kategoriList.get(i).getKategoriNama().toString());
//            for (int j = 0; j < id_kategori().size() ; j++) {
//                if(kategoriList.get(i).getIdKategori() == id_kategori().get(j))
//                checkBox.setChecked(true);
//            }
//            checkBox.setTextSize(15);
//            checkBox.setOnCheckedChangeListener(getOnClickDoSomting(checkBox));
//            linearMain.addView(checkBox);
        }
    }

    private CompoundButton.OnCheckedChangeListener getOnClickDoSomting(final CheckBox checkBox) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(count == maxLimit && b){
                    compoundButton.setChecked(false);
                    Toast.makeText(getActivity(),
                            "Hanya Bisa Memilih "+maxLimit + " Kategori", Toast.LENGTH_SHORT).show();

                }else if(b){

                    count++;
                    id_kategori.add(compoundButton.getId());
                    Toast.makeText(getActivity(),
                            compoundButton.getText().toString() + " dipilih!",
                            Toast.LENGTH_SHORT)
                            .show();
                }else if(!b){
                    for (int i = 0; i < id_kategori.size() ; i++) {
                        if(String.valueOf(compoundButton.getId()).equals(id_kategori.get(i).toString())){
                            id_kategori.remove(i);
                            count--;
                        }
                    }

                }
            }
        };
    }

    public List<Integer> id_kategori(){
        return id_kategori;
    }


}
