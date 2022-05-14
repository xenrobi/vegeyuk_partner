package com.example.vegeyuk.restopatner.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.models.Restoran;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class StepFragmentResto extends Fragment implements BlockingStep {

    private EditText namaResto,emailResto,alamatResto,phoneResto,deskripsiResto;
    Restoran resto ;
    String strNama,strEmail,strAlamat,strPhone,strDeskripsi;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_resto, container,false);

        namaResto = (EditText)view.findViewById(R.id.namaResto);
        emailResto = (EditText)view.findViewById(R.id.emailResto);
        phoneResto = (EditText) view.findViewById(R.id.phoneResto);
        alamatResto = (EditText) view.findViewById(R.id.alamatResto);
        deskripsiResto = (EditText) view.findViewById(R.id.deskripsiResto);

        resto = new Restoran();

        return view;
    }

    public Restoran resto (){

        strNama = namaResto.getText().toString();
        strPhone =clearPhone( phoneResto.getText().toString());
        strEmail = emailResto.getText().toString() ;
        strAlamat = alamatResto.getText().toString() ;
        strDeskripsi = deskripsiResto.getText().toString();

        resto.setRestoranNama(strNama);
        resto.setRestoranEmail(strEmail);
        resto.setRestoranPhone(strPhone);
        resto.setRestoranAlamat(strAlamat);
        resto.setRestoranDeskripsi(strDeskripsi);
        return resto;
    }



    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                strNama = namaResto.getText().toString();
                strPhone = clearPhone(phoneResto.getText().toString());
                strEmail = emailResto.getText().toString() ;
                strAlamat = alamatResto.getText().toString() ;
                strDeskripsi = deskripsiResto.getText().toString();

//                if(strNama.isEmpty()){
//                    namaResto.setError("Nama Diperlukan !");
//                    namaResto.requestFocus();
//                }else if (strPhone.equals("62")) {
//                    phoneResto.setError("Nomor Ponsel Diperlukan !");
//                    phoneResto.requestFocus();
//                }else if (strPhone.length()<12) {
//                    phoneResto.setError("Nomor Ponsel Tidak Valid !");
//                    phoneResto.requestFocus();
//                }else if (strEmail.isEmpty()) {
//                    emailResto.setError("Email Diperlukan !");
//                    emailResto.requestFocus();
//                }else if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()){
//                    emailResto.setError("Email Tidak Valid !");
//                    emailResto.requestFocus();
//
//                }else if(strAlamat.isEmpty()){
//                    alamatResto.setError("Alamat Diperlukan !");
//                    alamatResto.requestFocus();
//                }else if(strDeskripsi.isEmpty()){
//                    deskripsiResto.setError("Deskripsi Diperlukan !");
//                    deskripsiResto.requestFocus();
//                }else {
                    //you can do anythings you want
                    callback.goToNextStep();
//                }
            }
        },0L);
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

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

    //untuk menggambil no hp
    public String clearPhone (String phoneNumber){
        String hp = phoneNumber.replaceAll("-","");
        String clearPhone = hp.substring(1,hp.length());
        return clearPhone;
    }
}