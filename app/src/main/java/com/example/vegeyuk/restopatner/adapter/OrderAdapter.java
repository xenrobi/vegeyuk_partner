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
import com.example.vegeyuk.restopatner.activities.resto.DetailOrderActivity;
import com.example.vegeyuk.restopatner.models.Menu;
import com.example.vegeyuk.restopatner.models.Order;
import com.example.vegeyuk.restopatner.utils.DateHelper;
import com.example.vegeyuk.restopatner.utils.SessionManager;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context mContext;
   // private List<Detailorder> detailorderList;
    private List<Menu> menuOrderList;
    SessionManager sessionManager;


    public OrderAdapter(Context context,List<Order> orders){
        this.mContext = context;
        this.orderList = orders;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_order_list,parent,false);
        OrderViewHolder holder = new OrderViewHolder(view);
        sessionManager = new SessionManager(mContext);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, final int position) {
        final Order order = orderList.get(position);
        menuOrderList = order.getDetailOrder();
        //detailorderList = orderList.get(position).getDetailorder();
        String pesan ="";
        String tanda = ", ";
        for (int i = 0; i < menuOrderList.size() ; i++) {
            if(i == menuOrderList.size()-1){
                tanda =".";
            }
            pesan += "("+menuOrderList.get(i).getPivot().getQty()+") "+menuOrderList.get(i).getMenuNama()+""+tanda;
        }

        holder.mNamaPemesan.setText(order.getOrderKonsumen() +"\n"+"+"+order.getOrderKonsumenPhone());
        holder.mAlamat.setText(satuan_jarak(order.getOrderJarakAntar().toString())+"\n"+order.getOrderAlamat());
        holder.mDaftarPesan.setText(pesan);
        String strTimeOrder = order.getCreatedAt();
        long longDate = timeStringToMilis(strTimeOrder);
        holder.mTime.setText(DateHelper.getGridDate(mContext,longDate));
        holder.mIdPesan.setText("#"+order.getId().toString());

        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Anda memilih "+order.getOrderKonsumen(),Toast.LENGTH_SHORT).show();
                Intent intent;
                intent= new Intent(mContext, DetailOrderActivity.class);
                intent.putExtra("pesan", (Serializable) order);
                mContext.startActivity(intent);
            }
        });

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
        @BindView(R.id.tvAlamat)
        TextView mAlamat;
        @BindView(R.id.tvTime)
        TextView mTime;
        @BindView(R.id.tvIDpesan)
        TextView mIdPesan;
        @BindView(R.id.parentLayout)RelativeLayout mParentLayout;


        public OrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

//convert time string to milis date
    public long timeStringToMilis (String strDate ){

        SimpleDateFormat tgl = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        long milliseconds = 0;
        Date date = null;
        try {
            date = tgl.parse(strDate);
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;

    }

    private String satuan_jarak(String jarak){
        String jarakStr = null;
        if(Double.parseDouble(jarak) < 1) {
            jarakStr = jarak + "m";
        }else{
            jarakStr = jarak + "Km";
        }
        return jarakStr;
    }

    // Clean all elements of the recycler

    public void clear() {

        orderList.clear();

        notifyDataSetChanged();

    }



// Add a list of items -- change to type used

    public void addAll(List<Order> list) {

        orderList.addAll(list);

        notifyDataSetChanged();

    }
}
