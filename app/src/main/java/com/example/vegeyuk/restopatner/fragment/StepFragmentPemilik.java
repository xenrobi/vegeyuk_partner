package com.example.vegeyuk.restopatner.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.models.Restoran;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class StepFragmentPemilik extends Fragment implements BlockingStep {

    EditText namaPemilik , phonePemilik , emailPemilik, konfirmasiEmail ;
    Restoran resto ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_pemilik, container,false);


        namaPemilik = (EditText) view.findViewById(R.id.namaPemilik);
        phonePemilik = (EditText) view.findViewById(R.id.phonePemilik);
        emailPemilik = (EditText) view.findViewById(R.id.emailPemilik);
        konfirmasiEmail = (EditText)view.findViewById(R.id.konfirmasiEmailPemilik);

        resto = new Restoran();

        return view;
    }

    public Restoran resto (){

        resto.setRestoranPemilikNama(namaPemilik.getText().toString());
        resto.setRestoranPemilikPhone(clearPhone(phonePemilik.getText().toString()));
        resto.setRestoranPemilikEmail(emailPemilik.getText().toString());
        return resto;
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
    //untuk menggambil no hp
    public String clearPhone (String phoneNumber){
        String hp = phoneNumber.replaceAll("-","");
        String clearPhone = hp.substring(1,hp.length());
        return clearPhone;
    }
}
