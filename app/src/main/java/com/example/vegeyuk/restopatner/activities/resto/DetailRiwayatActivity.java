package com.example.vegeyuk.restopatner.activities.resto;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ebanx.swipebtn.SwipeButton;
import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.adapter.DetailOrderAdapter;
import com.example.vegeyuk.restopatner.config.ServerConfig;
import com.example.vegeyuk.restopatner.models.Menu;
import com.example.vegeyuk.restopatner.models.Order;
import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.utils.NonScrollListView;
import com.example.vegeyuk.restopatner.utils.SessionManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRiwayatActivity extends AppCompatActivity {

    private List<Menu> detailOrders = new ArrayList<>();
    private DetailOrderAdapter orderAdapter;
    private Order pesan;
    ApiService mApiService;
    Context mContext ;
    ProgressDialog progressOrder;
    String id_order;

    @BindView(R.id.listview)
    NonScrollListView list;
    @BindView(R.id.sbDelivery)
    SwipeButton mSwipeDelivery;
    @BindView(R.id.tvNamaKonsumen)
    TextView mNamaKonsumen;
    @BindView(R.id.tvPhoneKonsumen)
    TextView mPhoneKonsumen;
    @BindView(R.id.tvAlamatAntar)
    TextView mAlamat;
    @BindView(R.id.tvSubTotal)
    TextView mSubTotal;
    @BindView(R.id.tvBiayaAntar)
    TextView mBiayaAntar;
    @BindView(R.id.tvTotal)
    TextView mTotal;

    @BindView(R.id.btnCancel)
    TextView mCancel;
    @BindView(R.id.layoutCatatan)
    LinearLayout layoutCatatn;
    @BindView(R.id.catatan)
    TextView mCatatan;
    @BindView(R.id.layoutTelepon)
    LinearLayout layoutTelepon;
    @BindView(R.id.layoutStatus)
    LinearLayout layoutStatus;
    @BindView(R.id.tvStatusSelesai)
    TextView mStatusSelesai;
    @BindView(R.id.tvStatusBatal)
    TextView mStatusBatal;
    @BindView(R.id.tvMetodeBayar)
     TextView mMetodeBayar;

    @BindView(R.id.viewpb1) View viewpb1;
    @BindView(R.id.lyt_pb1)
    LinearLayout lyt_pb1;
    @BindView(R.id.pajak_pb1) TextView pajak_pb1;
    @BindView(R.id.tvPajakPB1) TextView tvPajakPB1;

    SessionManager sessionManager;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        ButterKnife.bind(this);
        mContext = this;
        sessionManager = new SessionManager(mContext);
        mApiService = ServerConfig.getAPIService();
        setListViewHeightBasedOnChildren(list);
        getIncomingIntent();
        orderAdapter = new DetailOrderAdapter(DetailRiwayatActivity.this,detailOrders);
        list.setAdapter(orderAdapter);

        setData();


    }



    private void setData() {
        id_order = pesan.getId().toString();
        mNamaKonsumen.setText(pesan.getOrderKonsumen());
        mPhoneKonsumen.setText("+"+pesan.getOrderKonsumenPhone());
        mAlamat.setText(pesan.getOrderAlamat());
         Double subtotal = 0.0;
        for (int i = 0; i <  detailOrders.size(); i++) {
            if(detailOrders.get(i).getMenuDiscount() == 0 ||detailOrders.get(i).getMenuDiscount().toString().isEmpty()) {
                subtotal += Double.parseDouble(detailOrders.get(i).getPivot().getHarga()) * detailOrders.get(i).getPivot().getQty();
            }else {
                Double harga_discount = HitungDiscount(Double.parseDouble(detailOrders.get(i).getPivot().getHarga()),detailOrders.get(i).getPivot().getDiscount());
                subtotal += harga_discount * detailOrders.get(i).getPivot().getQty();
            }
        }
        mSubTotal.setText(kursIndonesia(subtotal));
        mBiayaAntar.setText(kursIndonesia(Double.parseDouble(pesan.getOrderBiayaAnatar())));
        double total = subtotal+ Double.parseDouble(pesan.getOrderBiayaAnatar());

        //cek pajak
        if (pesan.getOrder_pajak_pb_satu() == 0){
            viewpb1.setVisibility(View.GONE);
            lyt_pb1.setVisibility(View.GONE);
        }else{
            viewpb1.setVisibility(View.VISIBLE);
            lyt_pb1.setVisibility(View.VISIBLE);
            pajak_pb1.setText("PB1 ("+pesan.getOrder_pajak_pb_satu()+"%)");
            double pb1 = (pesan.getOrder_pajak_pb_satu()/100.0)*total;
            tvPajakPB1.setText(kursIndonesia(pb1));
            total = total + pb1;
        }

        mTotal.setText(kursIndonesia(total));
        //catatan
        if(pesan.getOrderCatatan() != null){
            layoutCatatn.setVisibility(View.VISIBLE);
            mCatatan.setText(pesan.getOrderCatatan());

        }


            mSwipeDelivery.setVisibility(View.GONE);
            mCancel.setVisibility(View.GONE);
            layoutTelepon.setVisibility(View.GONE);
            layoutStatus.setVisibility(View.VISIBLE);


        if (pesan.getOrderStatus().equals("batal")){
            mStatusBatal.setVisibility(View.VISIBLE);
        }else {
            mStatusSelesai.setVisibility(View.VISIBLE);
        }

        mMetodeBayar.setText(pesan.getOrderMetodeBayar().toString());



    }

//get inten comming
    private void getIncomingIntent (){

        if(getIntent().hasExtra("pesan")){
            pesan = (Order) getIntent().getSerializableExtra("pesan");
            detailOrders = pesan.getDetailOrder();

        }
    }





//listview not scrolll
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }



//konfersi ke mata uang rupiah
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
