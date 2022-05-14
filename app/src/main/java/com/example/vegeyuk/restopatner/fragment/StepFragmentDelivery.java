package com.example.vegeyuk.restopatner.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.models.Restoran;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class StepFragmentDelivery extends Fragment implements BlockingStep {


    private Spinner spinnerDelivery ;
    private String[] daftar = {
            "Gratis",
            "Flat/pesan",
    };
    String biayaantar = "0",delivery;
    EditText mBiayaAntar,mJarakMax,mMinPesanan;
    Restoran resto;
    int jarak = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_delivery, container,false);

        mBiayaAntar = (EditText) view.findViewById(R.id.biayaAntar);
        mJarakMax = (EditText) view.findViewById(R.id.jarakMax);
        mMinPesanan =(EditText) view.findViewById(R.id.minPesanan);
        resto = new Restoran();

        spinnerDelivery = (Spinner) view.findViewById(R.id.spinerDelivery);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,daftar);

        spinnerDelivery.setAdapter(adapter);
        spinnerDelivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),"select "+adapter.getItem(i),Toast.LENGTH_SHORT).show();
                selectSpiner(adapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void selectSpiner(String item) {
        if(item.equals("Gratis")){
            mBiayaAntar.setVisibility(View.GONE);
            mBiayaAntar.setText("0");
            delivery = "gratis";
        }else if(item.equals("Flat/pesan")){
            mBiayaAntar.setVisibility(View.VISIBLE);

            delivery = "flat";
        }


    }

    public Restoran resto (){

        Toast.makeText(getActivity(),biayaantar,Toast.LENGTH_SHORT).show();
        resto.setRestoranDeliveryJarak(Integer.valueOf(mJarakMax.getText().toString()));
        resto.setRestoranDeliveryMinimum(mMinPesanan.getText().toString());
        resto.setRestoranDelivery(delivery);
       // resto.setTarifDelivery(biayaantar);
        return resto;
    }



    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.goToNextStep();
            }
        },0L);
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(final StepperLayout.OnBackClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.goToPrevStep();
            }
        },0L);
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
}
