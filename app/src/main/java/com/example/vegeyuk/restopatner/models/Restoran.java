package com.example.vegeyuk.restopatner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Restoran implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("restoran_nama")
    @Expose
    private String restoranNama;
    @SerializedName("restoran_phone")
    @Expose
    private String restoranPhone;
    @SerializedName("restoran_email")
    @Expose
    private String restoranEmail;
    @SerializedName("restoran_alamat")
    @Expose
    private String restoranAlamat;
    @SerializedName("restoran_latitude")
    @Expose
    private String restoranLatitude;
    @SerializedName("restoran_longitude")
    @Expose
    private String restoranLongitude;
    @SerializedName("restoran_deskripsi")
    @Expose
    private String restoranDeskripsi;
    @SerializedName("restoran_oprasional")
    @Expose
    private Integer restoranOprasional;
    @SerializedName("restoran_foto")
    @Expose
    private String restoranFoto;
    @SerializedName("restoran_pemilik_nama")
    @Expose
    private String restoranPemilikNama;
    @SerializedName("restoran_pemilik_email")
    @Expose
    private String restoranPemilikEmail;
    @SerializedName("restoran_pemilik_phone")
    @Expose
    private String restoranPemilikPhone;
    @SerializedName("restoran_balance")
    @Expose
    private String restoranBalance;
    @SerializedName("restoran_delivery")
    @Expose
    private String restoranDelivery;
    @SerializedName("restoran_delivery_tarif")
    @Expose
    private String restoranDeliveryTarif;
    @SerializedName("restoran_pajak_pb_satu")
    @Expose
    private Integer restoran_pajak_pb_satu;
    @SerializedName("restoran_delivery_jarak")
    @Expose
    private Integer restoranDeliveryJarak;
    @SerializedName("restoran_delivery_minimum")
    @Expose
    private String restoranDeliveryMinimum;
    @SerializedName("restoran_status")
    @Expose
    private String restoranStatus;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("jumlah_kurir")
    @Expose
    private Integer jumlahKurir;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRestoranNama() {
        return restoranNama;
    }

    public void setRestoranNama(String restoranNama) {
        this.restoranNama = restoranNama;
    }

    public String getRestoranPhone() {
        return restoranPhone;
    }

    public void setRestoranPhone(String restoranPhone) {
        this.restoranPhone = restoranPhone;
    }

    public String getRestoranEmail() {
        return restoranEmail;
    }

    public void setRestoranEmail(String restoranEmail) {
        this.restoranEmail = restoranEmail;
    }

    public String getRestoranAlamat() {
        return restoranAlamat;
    }

    public void setRestoranAlamat(String restoranAlamat) {
        this.restoranAlamat = restoranAlamat;
    }

    public String getRestoranLatitude() {
        return restoranLatitude;
    }

    public void setRestoranLatitude(String restoranLatitude) {
        this.restoranLatitude = restoranLatitude;
    }

    public String getRestoranLongitude() {
        return restoranLongitude;
    }

    public void setRestoranLongitude(String restoranLongitude) {
        this.restoranLongitude = restoranLongitude;
    }

    public String getRestoranDeskripsi() {
        return restoranDeskripsi;
    }

    public void setRestoranDeskripsi(String restoranDeskripsi) {
        this.restoranDeskripsi = restoranDeskripsi;
    }

    public Integer getRestoranOprasional() {
        return restoranOprasional;
    }

    public void setRestoranOprasional(Integer restoranOprasional) {
        this.restoranOprasional = restoranOprasional;
    }

    public String getRestoranFoto() {
        return restoranFoto;
    }

    public void setRestoranFoto(String restoranFoto) {
        this.restoranFoto = restoranFoto;
    }

    public String getRestoranPemilikNama() {
        return restoranPemilikNama;
    }

    public void setRestoranPemilikNama(String restoranPemilikNama) {
        this.restoranPemilikNama = restoranPemilikNama;
    }

    public String getRestoranPemilikEmail() {
        return restoranPemilikEmail;
    }

    public void setRestoranPemilikEmail(String restoranPemilikEmail) {
        this.restoranPemilikEmail = restoranPemilikEmail;
    }

    public String getRestoranPemilikPhone() {
        return restoranPemilikPhone;
    }

    public void setRestoranPemilikPhone(String restoranPemilikPhone) {
        this.restoranPemilikPhone = restoranPemilikPhone;
    }

    public String getRestoranBalance() {
        return restoranBalance;
    }

    public void setRestoranBalance(String restoranBalance) {
        this.restoranBalance = restoranBalance;
    }

    public String getRestoranDelivery() {
        return restoranDelivery;
    }

    public void setRestoranDelivery(String restoranDelivery) {
        this.restoranDelivery = restoranDelivery;
    }

    public String getRestoranDeliveryTarif() {
        return restoranDeliveryTarif;
    }

    public void setRestoranDeliveryTarif(String restoranDeliveryTarif) {
        this.restoranDeliveryTarif = restoranDeliveryTarif;
    }

    public Integer getRestoranDeliveryJarak() {
        return restoranDeliveryJarak;
    }

    public void setRestoranDeliveryJarak(Integer restoranDeliveryJarak) {
        this.restoranDeliveryJarak = restoranDeliveryJarak;
    }

    public String getRestoranDeliveryMinimum() {
        return restoranDeliveryMinimum;
    }

    public void setRestoranDeliveryMinimum(String restoranDeliveryMinimum) {
        this.restoranDeliveryMinimum = restoranDeliveryMinimum;
    }

    public String getRestoranStatus() {
        return restoranStatus;
    }

    public void setRestoranStatus(String restoranStatus) {
        this.restoranStatus = restoranStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getJumlahKurir() {
        return jumlahKurir;
    }

    public void setJumlahKurir(Integer jumlahKurir) {
        this.jumlahKurir = jumlahKurir;
    }

    public Integer getRestoran_pajak_pb_satu() {
        return restoran_pajak_pb_satu;
    }

    public void setRestoran_pajak_pb_satu(Integer restoran_pajak_pb_satu) {
        this.restoran_pajak_pb_satu = restoran_pajak_pb_satu;
    }
}

