package com.example.vegeyuk.restopatner.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.vegeyuk.restopatner.models.Kurir;
import com.example.vegeyuk.restopatner.models.Restoran;

import java.util.HashMap;

public class SessionManager {

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private Context _context;

    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String IS_RESTORANT = "isrestorant";
    public static final String IS_PENGANTARAN = "fs";

    public static final String ID_RESTORAN = "idUser";


    public static final String ID_KURIR = "id_kurir";
    public static final String KURIR_NAMA ="kurir_nama";
    public static final String KURIR_PHONE ="kurir_phone";
    public static final String KURIR_EMAIL = "kurir_email";

    public static final String EMAIL_RESTORAN = "email";
    public static final String NAMA_RESTORAN = "namaLengkap";
    public static final String NO_HP_RESTORAN = "noHP";
    public static final String ALAMAT_RESTORAN ="alamat";
    public static final String RESTORAN_LAT = "LAT";
    public static final String RESTORAN_LANG ="LANG";
    public static final String DESKRIPSI_RESTORAN ="deskripsi";

    public static final String EMAIL_PEMILIK = "email";
    public static final String NAMA_PEMILIK = "namaPemilik";
    public static final String NO_HP_PEMILIK = "noPhonePemilik";
    public static final String OPERASIONAL = "opersional";

    public static final String RESTORAN_DELIVERY = "Delivery";
    public static final String TARIF_DELIVERY = "tarif Delivery";
    public static final String RESTORAN_DELIVERY_JARAK = "Delivery";
    public static final String RESTORAN_DELIVERY_MINIMUM = "Delivery";

    public static final String ID_PENGANTARAN = "asd";
    public static final String LAT = "ashbd";
    public static final String LANG = "asdnj";
    public static final String ALAMAT = "asdjnj";


    public Context get_context() {
        return _context;
    }

    //constuctor
    public SessionManager(Context context){
        this._context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void createLoginSessionResto(Restoran user){
        editor.putBoolean(IS_LOGGED_IN,true);
        editor.putBoolean(IS_RESTORANT,true);
        editor.putString(ID_RESTORAN, String.valueOf(user.getId()));

        editor.putString(NAMA_RESTORAN,user.getRestoranNama());
        editor.putString(EMAIL_RESTORAN,user.getRestoranEmail());
        editor.putString(NO_HP_RESTORAN,user.getRestoranPhone());
        editor.putString(ALAMAT_RESTORAN,user.getRestoranAlamat());
        editor.putString(RESTORAN_LAT,user.getRestoranLatitude());
        editor.putString(RESTORAN_LANG,user.getRestoranLongitude());
        editor.putString(DESKRIPSI_RESTORAN, user.getRestoranDeskripsi());

        editor.putString(NAMA_PEMILIK,user.getRestoranPemilikNama());
        editor.putString(EMAIL_PEMILIK,user.getRestoranPemilikEmail());
        editor.putString(NO_HP_PEMILIK,user.getRestoranPemilikPhone());
        editor.putInt(OPERASIONAL,user.getRestoranOprasional());

        editor.putString(RESTORAN_DELIVERY,user.getRestoranDelivery());
        editor.putString(TARIF_DELIVERY ,user.getRestoranDeliveryTarif());
        editor.putString(RESTORAN_DELIVERY_JARAK,String.valueOf(user.getRestoranDeliveryJarak()));
        editor.putString(RESTORAN_DELIVERY_MINIMUM,user.getRestoranDeliveryMinimum());


        editor.commit();
    }

    public void createLoginSessionKurir (Kurir user){
        editor.putBoolean(IS_LOGGED_IN,true);
        editor.putString(ID_KURIR,user.getId().toString());
        editor.putString(ID_RESTORAN, String.valueOf(user.getIdRestoran()));
        editor.putString(KURIR_NAMA,user.getKurirNama());
        editor.putString(KURIR_PHONE,user.getKurirPhone());
        editor.putString(KURIR_EMAIL,user.getKurirEmail());
        editor.commit();
    }



    public void updateOprasinal(int oprasional) {
        editor.putInt(OPERASIONAL, oprasional);
        editor.commit();
    }

    public void updateLocation(String lat,String lang) {
        editor.putString(RESTORAN_LAT, lat);
        editor.putString(RESTORAN_LAT, lang);
        editor.commit();
    }

    public void updateDelivery(String metodeBayar,String tarifDelivery,String jarakMax,String minimumOreder){
        editor.putString(RESTORAN_DELIVERY, metodeBayar);
        editor.putString(TARIF_DELIVERY, tarifDelivery);
        editor.putString(RESTORAN_DELIVERY_JARAK, jarakMax);
        editor.putString(RESTORAN_DELIVERY_MINIMUM, minimumOreder);
        editor.commit();
    }

    public void updatePemilik(String nama,String phone,String email){
        editor.putString(NAMA_PEMILIK, nama);
        editor.putString(NO_HP_PEMILIK, phone);
        editor.putString(EMAIL_PEMILIK, email);
    }

    public void updateRestoran(String Nama,String Phone,String Email,String Alamat,String Diskripsi)
    {
        editor.putString(NAMA_RESTORAN, Nama);
        editor.putString(NO_HP_RESTORAN, Phone);
        editor.putString(EMAIL_RESTORAN, Email);
        editor.putString(ALAMAT_RESTORAN, Alamat);
        editor.putString(DESKRIPSI_RESTORAN, Diskripsi);
        editor.commit();
    }

    public void pengantaran (String id,String lat,String lang,String alamat) {
        editor.putBoolean(IS_PENGANTARAN,true);
        editor.putString(ID_PENGANTARAN, id);
        editor.putString(LAT, lat);
        editor.putString(LANG, lang);
        editor.putString(ALAMAT, alamat);
        editor.commit();
    }

    public HashMap<String,String> getRestoDetail(){
        HashMap<String,String> user = new HashMap<>();
        user.put(ID_RESTORAN,sharedPreferences.getString(ID_RESTORAN,null));

        user.put(NAMA_RESTORAN, sharedPreferences.getString(NAMA_RESTORAN,null));
        user.put(EMAIL_RESTORAN, sharedPreferences.getString(EMAIL_RESTORAN,null));
        user.put(NO_HP_RESTORAN, sharedPreferences.getString(NO_HP_RESTORAN,null));

        user.put(RESTORAN_LAT, sharedPreferences.getString(RESTORAN_LAT,null));
        user.put(RESTORAN_LANG, sharedPreferences.getString(RESTORAN_LANG,null));
        user.put(ALAMAT_RESTORAN, sharedPreferences.getString(ALAMAT_RESTORAN,null));


        user.put(NAMA_PEMILIK, sharedPreferences.getString(NAMA_PEMILIK,null));
        user.put(EMAIL_PEMILIK, sharedPreferences.getString(EMAIL_PEMILIK,null));
        user.put(NO_HP_PEMILIK, sharedPreferences.getString(NO_HP_PEMILIK,null));
        user.put(OPERASIONAL, String.valueOf(sharedPreferences.getInt(OPERASIONAL,0)));
        return user;
    }

    public HashMap<String,String> getKurirDetail(){
        HashMap<String,String> user = new HashMap<>();
        user.put(ID_RESTORAN,sharedPreferences.getString(ID_RESTORAN,null));

        user.put(ID_KURIR, sharedPreferences.getString(ID_KURIR,null));
        user.put(KURIR_NAMA, sharedPreferences.getString(KURIR_NAMA,null));
        user.put(KURIR_PHONE, sharedPreferences.getString(KURIR_PHONE,null));



        user.put(KURIR_EMAIL, sharedPreferences.getString(KURIR_EMAIL,null));

        return user;
    }

    public HashMap<String,String> getPengantaran(){
        HashMap<String,String> pengantaran = new HashMap<>();
        pengantaran.put(ID_PENGANTARAN,sharedPreferences.getString(ID_PENGANTARAN,null));
        pengantaran.put(LAT,sharedPreferences.getString(LAT,null));
        pengantaran.put(LANG,sharedPreferences.getString(LANG,null));
        pengantaran.put(ALAMAT,sharedPreferences.getString(ALAMAT,null));

        return pengantaran;
    }

    public void selesaiPenganataran(){
        editor.putBoolean(IS_PENGANTARAN,false);
        editor.putString(ID_PENGANTARAN, null);
        editor.putString(LAT, null);
        editor.putString(LANG, null);
        editor.putString(ALAMAT, null);
        editor.commit();
    }



    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);
    }

    public boolean isRestoran(){ return sharedPreferences.getBoolean(IS_RESTORANT, false);}

    public boolean isPengantaran(){ return sharedPreferences.getBoolean(IS_PENGANTARAN, false);}
}
