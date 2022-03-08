package com.example.vegeyuk.restopatner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vegeyuk.restopatner.fragment.DeliveryListFragment;
import com.example.vegeyuk.restopatner.fragment.OrderListFragment;

public class PagerOrderTabAdapter extends FragmentStatePagerAdapter {

    private int number_tbas;

    public PagerOrderTabAdapter(FragmentManager fm, int number_tbas) {
        super(fm);
        this.number_tbas = number_tbas;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OrderListFragment();
            case 1:
                return new DeliveryListFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return number_tbas;
    }
}
