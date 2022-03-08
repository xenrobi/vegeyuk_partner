package com.example.vegeyuk.restopatner.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vegeyuk.restopatner.fragment.MenuListFragment;
import com.example.vegeyuk.restopatner.models.Kategori;
import com.example.vegeyuk.restopatner.models.Menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KategoriTabAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private List<Menu> menuList = new ArrayList<>();
    private List<Kategori> kategoriList = new ArrayList<>();

    public KategoriTabAdapter(FragmentManager fm,int NumOfTabs, List<Menu> menuList,List <Kategori> kategoriList) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.menuList = menuList;
        this.kategoriList = kategoriList;


    }

    public void update(List<Menu> menuList,List <Kategori> kategoriList) {
        this.menuList = menuList;
        this.kategoriList = kategoriList;
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        b.putInt("position", position);
        b.putSerializable("menu", (Serializable) menuList);
        b.putSerializable("kategori",(Serializable) kategoriList);
        Fragment frag = MenuListFragment.newInstance();
        frag.setArguments(b);
        return frag;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof UpdateableFragment) {
            ((UpdateableFragment) object).update(menuList,kategoriList);
        }
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(object);
    }

    public interface UpdateableFragment {
        public void update(List<Menu> menuList,List <Kategori> kategoriList);
    }
}
