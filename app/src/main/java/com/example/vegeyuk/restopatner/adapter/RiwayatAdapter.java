package com.example.vegeyuk.restopatner.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.activities.resto.DetailRiwayatActivity;
import com.example.vegeyuk.restopatner.models.Menu;
import com.example.vegeyuk.restopatner.models.Order;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context mContext;
   // private List<Detailorder> detailorderList;
    private List<Menu> menuOrderList;


    public RiwayatAdapter(Context context, List<Order> orders){
        this.mContext = context;
        this.orderList = orders;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_riwayat_list,parent,false);
        OrderViewHolder holder = new OrderViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, final int position) {
        final Order order = orderList.get(position);
        menuOrderList = order.getDetailOrder();
        //detailorderList = orderList.get(position).getDetailorder();

        double total = 0;
        //hitung harga
        for (int i = 0; i < menuOrderList.size(); i++) {
            if(menuOrderList.get(i).getPivot().getDiscount().toString().isEmpty() || menuOrderList.get(i).getPivot().getDiscount() ==0) {
                total += Double.parseDouble(menuOrderList.get(i).getPivot().getHarga()) * menuOrderList.get(i).getPivot().getQty();
            }else{
                Double harga_discount = HitungDiscount(Double.parseDouble(menuOrderList.get(i).getPivot().getHarga()),menuOrderList.get(i).getPivot().getDiscount());
                total +=harga_discount * menuOrderList.get(i).getPivot().getQty();
            }
        }
        total = total+ Double.parseDouble(order.getOrderBiayaAnatar());


        String pesan ="";
        String tanda = ", ";
        for (int i = 0; i < menuOrderList.size() ; i++) {
            if(i == menuOrderList.size()-1){
                tanda =".";
            }
            pesan += menuOrderList.get(i).getMenuNama()+" "+menuOrderList.get(i).getPivot().getQty()+tanda;
        }

        holder.mNamaPemesan.setText(order.getOrderKonsumen());
//        holder.mAlamat.setText(order.getOrderAlamat());
        holder.mDaftarPesan.setText(kursIndonesia(total)+" - "+order.getOrderMetodeBayar().toString()   +"\n"+pesan);
        String strTimeOrder = order.getCreatedAt();


        holder.mIdPesan.setText("#"+order.getId().toString());

        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Anda memilih "+order.getOrderKonsumen(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DetailRiwayatActivity.class);
                intent.putExtra("pesan", (Serializable) order);

                mContext.startActivity(intent);
            }
        });

        if (order.getOrderStatus().equalsIgnoreCase("batal")){
            holder.mTime.setVisibility(View.GONE);
            holder.mTimeBatal.setVisibility(View.VISIBLE);
            holder.mTimeBatal.setText(strTimeOrder.substring(0,16));
        }else {
            holder.mTimeBatal.setVisibility(View.GONE);
            holder.mTime.setVisibility(View.VISIBLE);
            holder.mTime.setText(strTimeOrder.substring(0,16));
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvNamaPemesan)
        TextView mNamaPemesan;
        @BindView(R.id.tvDaftarPesan)
        TextView mDaftarPesan;
        @BindView(R.id.tvTime)
        TextView mTime;
        @BindView(R.id.tvIDpesan)
        TextView mIdPesan;
        @BindView(R.id.parentLayout)
        RelativeLayout mParentLayout;
        @BindView(R.id.tvTimeBatal)
        TextView mTimeBatal;


        public OrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public Double HitungDiscount (Double Harga,Integer Discount){
        double harga_potongan = ((Discount/100.00)*Harga);
        return Harga-harga_potongan;
    }


    //konfersi ke mata uang rupiah
    public String kursIndonesia(double nominal){
        Locale localeID = new Locale("in","ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }
}
