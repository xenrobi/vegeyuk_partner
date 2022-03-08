package com.example.vegeyuk.restopatner.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.adapter.DetailPrinterAdapter;
import com.example.vegeyuk.restopatner.models.Menu;
import com.example.vegeyuk.restopatner.models.Order;
import com.example.vegeyuk.restopatner.utils.NonScrollListView;
import com.example.vegeyuk.restopatner.utils.SessionManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrintActivity extends AppCompatActivity {

    private Order pesan;
    private List<Menu> detailOrders = new ArrayList<>();
    private DetailPrinterAdapter orderAdapter;
    SessionManager sessionManager;
    HashMap<String,String> restoran;


    @BindView(R.id.tvNamaPemesan)
    TextView mNama;
    @BindView(R.id.tvNamaResto)
    TextView mNamaResto;
    @BindView(R.id.tvTime)
    TextView mTime;
    @BindView(R.id.tvAlamatResto)
    TextView mAlamatResto;
    @BindView(R.id.noHp)
    TextView mNoHp;
    @BindView(R.id.tvNoOrder)
    TextView mNoOrder;
    @BindView(R.id.tvMetodeBayar)
    TextView mMetodeBayar;
    @BindView(R.id.tvSubTotal)
    TextView mSubTotal;
    @BindView(R.id.tvTotalDiskon)
    TextView mDiscount;
    @BindView(R.id.tvBiayaAntar)
    TextView mBiayaAntar;
    @BindView(R.id.tvPajakPB1)
    TextView mPajakPb1;
    @BindView(R.id.tvTotal)
    TextView mTotal;
    @BindView(R.id.listview)
    NonScrollListView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        restoran = sessionManager.getRestoDetail();
        getIncomingIntent();
        setData();

    }

    private void setData() {
        mNama.setText("Cost. Name : "+pesan.getOrderKonsumen().toString());
        mNamaResto.setText(pesan.getOrderRestoran());
        mTime.setText(pesan.getCreatedAt());
        mAlamatResto.setText( restoran.get(SessionManager.ALAMAT_RESTORAN));
        mNoHp.setText("+"+restoran.get(SessionManager.NO_HP_RESTORAN));
        mNoOrder.setText("No Order : "+pesan.getId().toString());
        mMetodeBayar.setText("Metode Bayar : "+pesan.getOrderMetodeBayar());

        orderAdapter = new DetailPrinterAdapter(PrintActivity.this,detailOrders);
        list.setAdapter(orderAdapter);

        Double subtotal = 0.0;
        Double discount = 0.0;
        for (int i = 0; i <  detailOrders.size(); i++) {
            if(detailOrders.get(i).getMenuDiscount() == 0 ||detailOrders.get(i).getMenuDiscount().toString().isEmpty()) {
                subtotal += Double.parseDouble(detailOrders.get(i).getPivot().getHarga()) * detailOrders.get(i).getPivot().getQty();

            }else {
                Double harga_discount = HitungDiscount(Double.parseDouble(detailOrders.get(i).getPivot().getHarga()),detailOrders.get(i).getPivot().getDiscount());
                //subtotal += harga_discount * detailOrders.get(i).getPivot().getQty();
                subtotal += Double.parseDouble(detailOrders.get(i).getPivot().getHarga()) * detailOrders.get(i).getPivot().getQty();
                discount += harga_discount * detailOrders.get(i).getPivot().getQty();
            }
        }
        mSubTotal.setText(kursIndonesia(subtotal));
        mBiayaAntar.setText(kursIndonesia(Double.parseDouble(pesan.getOrderBiayaAnatar())));
        mDiscount.setText(kursIndonesia(discount));
        double total =  (subtotal+Double.parseDouble(pesan.getOrderBiayaAnatar()))-discount;

        //cek pajak
        if (pesan.getOrder_pajak_pb_satu() == 0){
            mPajakPb1.setText(kursIndonesia(0.0));
        }else{

            double pb1 = (pesan.getOrder_pajak_pb_satu()/100.0)*total;
            mPajakPb1.setText(kursIndonesia(pb1));
            total = total + pb1;
        }

        mTotal.setText(kursIndonesia(total));



    }


    //get inten comming
    private void getIncomingIntent (){

        if(getIntent().hasExtra("pesan")){


            pesan = (Order) getIntent().getSerializableExtra("pesan");
            detailOrders = pesan.getDetailOrder();

        }
    }

    public Double HitungDiscount (Double Harga,Integer Discount){
        double harga_potongan = ((Discount/100.00)*Harga);
        return harga_potongan;
    }

    //konfersi ke mata uang rupiah
    public String kursIndonesia(double nominal){
        Locale localeID = new Locale("in","ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String idnNominal = formatRupiah.format(nominal);
        return idnNominal;


    }
}
