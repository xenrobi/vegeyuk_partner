package com.example.vegeyuk.restopatner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.models.Kurir;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KurirAdapter extends RecyclerView.Adapter<KurirAdapter.MyViewHolder> {

   private List<Kurir> kurirList = new ArrayList<>();
   private Context mContext;
    private ClickListener clickListener;


   public KurirAdapter(Context context,List<Kurir> data,ClickListener clickListener){
       super();
       this.kurirList = data;
       this.mContext = context;
       this.clickListener =clickListener;
   }

    public void removeAt(int position) {
        kurirList.remove(position);
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public KurirAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_kurir_list, parent, false);
        KurirAdapter.MyViewHolder holder = new KurirAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull KurirAdapter.MyViewHolder holder, final int position) {
            final Kurir kurir = kurirList.get(position);
            holder.mNamaKurir.setText(kurir.getKurirNama());
            holder.mPhoneKurir.setText("+"+kurir.getKurirPhone());
            holder.mEmailKurir.setText(kurir.getKurirEmail());

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null){
                        clickListener.itemDeleted(view,position);
                    }
                }
            });

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null){
                        clickListener.itemEdit(view,position);
                    }
                }
            });


    }

    @Override
    public int getItemCount() {
        return kurirList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

       @BindView(R.id.tvNamaKurir)
        TextView mNamaKurir;
       @BindView(R.id.tvPhoneKurir)
       TextView mPhoneKurir;
       @BindView(R.id.tvEmailKurir)
       TextView mEmailKurir;
       @BindView(R.id.btnDelete)
        ImageView btnDelete;
        @BindView(R.id.btnEdit)
        ImageView btnEdit;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }

    public interface ClickListener {
        //public void itemClicked(View view, int position);

        public void itemDeleted(View view, int position);

        public void itemEdit(View view,int position);


    }
}
