package com.example.vegeyuk.restopatner.adapter;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.models.Menu;

import java.util.List;

import butterknife.ButterKnife;

public class LaporanAdapter extends BaseAdapter {
    private List<Menu> menuList ;
    private Context mContext;


    ViewHolder viewHolder;



    public LaporanAdapter (Context context, List<Menu> data){
        super();
        this.mContext = context;
        this.menuList = data;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.single_row_laporan,null);

            viewHolder = new ViewHolder();

            viewHolder.mNamaMenu = (TextView)view.findViewById(R.id.tvNamaMenu);
            viewHolder.mJumlahFavorit = (TextView)view.findViewById(R.id.tvJumlahFavorit);
            viewHolder.mJumalahDibeli =(TextView) view.findViewById(R.id.tvJumlahDibeli);


            view.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) view.getTag();
        }


        final Menu menu = (Menu) getItem(position);
        viewHolder.mNamaMenu.setText(menu.getMenuNama().toString());
        viewHolder.mJumlahFavorit.setText(menu.getMenuJumlahFavorit().toString());
        viewHolder.mJumalahDibeli.setText(menu.getMenuJumlahDipesan().toString());







        return view;
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int i) {
        return menuList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    private class ViewHolder{
        TextView mNamaMenu;
        TextView mJumlahFavorit;
        TextView mJumalahDibeli;

    }
}
