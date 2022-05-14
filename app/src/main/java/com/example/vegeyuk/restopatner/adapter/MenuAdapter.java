package com.example.vegeyuk.restopatner.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.activities.resto.EditMenuActivity;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Menu;
import com.example.vegeyuk.restopatner.responses.ResponseValue;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private List<Menu> menuList = new ArrayList<>();
    private Context mContext;
    ApiService mApiService;
    String path;



    public MenuAdapter(Context context, List<Menu> data) {
        super();
        this.menuList = data;
        this.mContext = context;

    }





    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_list_menu, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        path = view.getResources().getString(R.string.urlmenu);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Menu menu = menuList.get(position);

        holder.bind(position);

        holder.mNamaMenu.setText(menu.getMenuNama());

        if(menu.getMenuDiscount().toString().isEmpty()||menu.getMenuDiscount() == 0){
            holder.mDiskon.setVisibility(View.GONE);
            holder.mHargaMenu.setVisibility(View.GONE);
            holder.mHargaJual.setText("Harga Jual "+kursIndonesia(Double.parseDouble(menu.getMenuHarga())));
        }else {
            Double harga_discount = HitungDiscount(Double.parseDouble(menu.getMenuHarga()),menu.getMenuDiscount());
            holder.mDiskon.setVisibility(View.VISIBLE);
            holder.mDiskon.setText("Diskon "+menu.getMenuDiscount().toString()+"%");
            holder.mHargaMenu.setText(kursIndonesia(Double.parseDouble(menu.getMenuHarga())));
            holder.mHargaMenu.setPaintFlags(holder.mHargaMenu.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mHargaJual.setText("Harga Jual "+kursIndonesia(harga_discount));

        }

        Picasso.get()
                .load( path+ menu.getMenuFoto())
                .resize(500, 500)
                .centerCrop()
                .into(holder.mImageMenu);


            if(menu.getMenuJumlahFavorit() > 0) {
                holder.mFavorit.setText(menu.getMenuJumlahFavorit().toString());
                holder.imgFavorit.setVisibility(View.VISIBLE);
            }else {
                holder.mFavorit.setVisibility(View.GONE);
                holder.imgFavorit.setVisibility(View.GONE);
            }





        holder.mParentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Menu " + menu.getMenuNama(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, EditMenuActivity.class);
                intent.putExtra("menu", menu);
                mContext.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        if (menuList == null) {
            return 0;
        }
        return menuList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        @BindView(R.id.tvNamaMenu)
        TextView mNamaMenu;
        @BindView(R.id.tvHargaMenu)
        TextView mHargaMenu;
        @BindView(R.id.tvFavorit)
        TextView mFavorit;
        @BindView(R.id.imgFavorit)
        ImageView imgFavorit;
        @BindView(R.id.swKetersediaan)
        CheckBox mSwitchKetersedian;
        @BindView(R.id.imageMenu)
        ImageView mImageMenu;
        @BindView(R.id.parentLayout)
        LinearLayout mParentlayout;
        @BindView(R.id.tvDiskon)
        TextView mDiskon;
        @BindView(R.id.tvHargaJual)
        TextView mHargaJual;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mApiService = ServerConfig.getAPIService();
            // mSwitchKetersedian.setOnCheckedChangeListener(this);
            //  itemView.setOnClickListener(this);
            mSwitchKetersedian.setOnClickListener(this);
        }

        void bind(int position) {

            if (menuList.get(position).getMenuKetersediaan() == 1) {
                mSwitchKetersedian.setChecked(true);
            } else {
                mSwitchKetersedian.setChecked(false);
            }
        }

        void fav (int position){
            if (menuList.get(position).getMenuJumlahFavorit() > 0) {
                mSwitchKetersedian.setChecked(true);
            } else {
                mSwitchKetersedian.setChecked(false);
            }
        }


        public void setKetersedian(String id_menu, int intKetersedian) {


            mApiService.updateKetersedianMenu(id_menu, intKetersedian).enqueue(new Callback<ResponseValue>() {
                @Override
                public void onResponse(Call<ResponseValue> call, Response<ResponseValue> response) {
                    if (response.isSuccessful()) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();
                        if (value.equals("1")) {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseValue> call, Throwable t) {
                    Toast.makeText(mContext, "ini dia errornya", Toast.LENGTH_SHORT).show();
                }
            });
        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (menuList.get(adapterPosition).getMenuKetersediaan() == 1) {
                mSwitchKetersedian.setChecked(false);
                Toast.makeText(mContext, menuList.get(adapterPosition).getMenuNama() + "false", Toast.LENGTH_SHORT).show();
                menuList.get(adapterPosition).setMenuKetersediaan(0);
                setKetersedian(menuList.get(adapterPosition).getId().toString(), 0);
            } else {
                mSwitchKetersedian.setChecked(true);
                Toast.makeText(mContext, menuList.get(adapterPosition).getMenuNama() + " true", Toast.LENGTH_SHORT).show();
                menuList.get(adapterPosition).setMenuKetersediaan(1);
                setKetersedian(menuList.get(adapterPosition).getId().toString(), 1);
            }
        }
    }


    public String kursIndonesia(double nominal){
        Locale localeID = new Locale("in","ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;
    }

    public Double HitungDiscount (Double Harga,Integer Discount){
        double harga_potongan = ((Discount/100.00)*Harga);
        return Harga-harga_potongan;
    }


}
