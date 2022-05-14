package com.example.vegeyuk.restopatner.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.vegeyuk.restopatner.fragment.StepFragmentDelivery;
import com.example.vegeyuk.restopatner.fragment.StepFragmentKategori;
import com.example.vegeyuk.restopatner.fragment.StepFragmentMaps;
import com.example.vegeyuk.restopatner.fragment.StepFragmentPemilik;
import com.example.vegeyuk.restopatner.fragment.StepFragmentResto;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

public class StepperAdapter extends AbstractFragmentStepAdapter{

    private static final String CURRENT_STEP_POSITION_KEY = "messageResauceID";

    public StepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position){
            case 0:
                final StepFragmentResto step1 = new StepFragmentResto();
                Bundle b1 = new Bundle();
                b1.putInt(CURRENT_STEP_POSITION_KEY,position);
                step1.setArguments(b1);
                return step1;
            case 1:
                final StepFragmentPemilik step2 = new StepFragmentPemilik();
                Bundle b2 = new Bundle();
                b2.putInt(CURRENT_STEP_POSITION_KEY, position);
                step2.setArguments(b2);
                return step2;
            case 2:
                final StepFragmentDelivery step3 = new StepFragmentDelivery();
                Bundle b3 = new Bundle();
                b3.putInt(CURRENT_STEP_POSITION_KEY, position);
                step3.setArguments(b3);
                return step3;
            case 3:
                final StepFragmentMaps step4 = new StepFragmentMaps();
                Bundle b4 = new Bundle();
                b4.putInt(CURRENT_STEP_POSITION_KEY, position);
                step4.setArguments(b4);
                return step4;

            case 4:
                final StepFragmentKategori step5 = new StepFragmentKategori();
                Bundle b5 = new Bundle();
                b5.putInt(CURRENT_STEP_POSITION_KEY, position);
                step5.setArguments(b5);
                return step5;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
