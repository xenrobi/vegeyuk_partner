package com.example.vegeyuk.restopatner.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.models.Kategori;

import java.util.List;

public class kategoriAdapter extends BaseAdapter {

    private final List<Kategori> kategoriList ;
    Context context;


    public kategoriAdapter(Context context, List<Kategori> kategoriList) {
        this.kategoriList = kategoriList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return kategoriList.size();
    }

    @Override
    public Object getItem(int i) {
        return kategoriList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return kategoriList.indexOf(getItem(i));
    }

    public class ViewHolder{
        protected TextView kategoriNama;
        protected CheckBox kategoriCheckBox;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View rowView = view;

        ViewHolder viewHolder = new ViewHolder();
        if(rowView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rowView =inflater.inflate(R.layout.single_row_kategori_list,null);

            viewHolder.kategoriCheckBox = (CheckBox) rowView.findViewById(R.id.row_kategori_checkbox);
            viewHolder.kategoriNama = (TextView) rowView.findViewById(R.id.tvNamaKategori);
            rowView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.kategoriNama.setText(kategoriList.get(position).getKategoriNama().toString());
        //viewHolder.kategoriCheckBox.setChecked(kategoriList.get(position).isSelected());

        viewHolder.kategoriCheckBox.setTag(position);

//        viewHolder.kategoriCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                kategoriList.get(position).setSelected(b);
//                Toast.makeText(context, ""+b,Toast.LENGTH_SHORT).show();
//            }
//        });

       // viewHolder.kategoriCheckBox.setChecked(isC);
       return view;
    }


}
