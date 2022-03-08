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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailOrderAdapter extends BaseAdapter{

    Context mContext;
    List<Menu> detailOrders ;
    ViewHolder viewHolder;



    public DetailOrderAdapter (Context context, List<Menu> data){
        super();
        this.mContext = context;
        this.detailOrders = data;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.single_row_detail_order_list,null);

            viewHolder = new ViewHolder();

            viewHolder.mNamaMenu = (TextView)view.findViewById(R.id.tvNamaMenu);
            viewHolder.mQty = (TextView)view.findViewById(R.id.tvQty);
            viewHolder.catatan =(TextView) view.findViewById(R.id.tvCatatan);
            viewHolder.mHarga = (TextView)view.findViewById(R.id.tvHarga);
            viewHolder.mJml = (TextView) view.findViewById(R.id.tvjumlah);

            view.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) view.getTag();
        }


        final Menu order = (Menu) getItem(position);
        viewHolder.mNamaMenu.setText(order.getMenuNama());
        viewHolder.mQty.setText(String.valueOf(order.getPivot().getQty()));
//        viewHolder.qty.setText(String .valueOf(cart.getQty()));
        Double jml = null;
        if(order.getPivot().getDiscount() == 0|| order.getPivot().getDiscount().toString().isEmpty()){
            viewHolder.mHarga.setText("@"+kursIndonesia(Double.parseDouble(order.getPivot().getHarga())));
            jml = order.getPivot().getQty() * Double.parseDouble(order.getPivot().getHarga());
        }else {
            Double harga_discount = HitungDiscount(Double.parseDouble(order.getPivot().getHarga()),order.getPivot().getDiscount());
            viewHolder.mHarga.setText("@"+kursIndonesia(harga_discount));
            jml = order.getPivot().getQty() * harga_discount;
        }

        viewHolder.mJml.setText(kursIndonesia(jml));



        if(order.getPivot().getCatatan() == null){
            viewHolder.catatan.setVisibility(View.GONE);
        }else {
            viewHolder.catatan.setVisibility(View.VISIBLE);
            viewHolder.catatan.setText(order.getPivot().getCatatan());
        }

        return view;
    }

    @Override
    public int getCount() {
        return detailOrders.size();
    }

    @Override
    public Object getItem(int i) {
        return detailOrders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    private class ViewHolder{
        TextView mNamaMenu;
        TextView mQty;
        TextView mHarga;
        TextView mJml;
        TextView catatan;

    }

    public Double HitungDiscount (Double Harga,Integer Discount){
        double harga_potongan = ((Discount/100.00)*Harga);
        return Harga-harga_potongan;
    }

    public String kursIndonesia(double nominal){
        Locale localeID = new Locale("in","ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }

}
