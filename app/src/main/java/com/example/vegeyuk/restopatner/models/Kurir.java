package com.example.vegeyuk.restopatner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Kurir implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_restoran")
    @Expose
    private Integer idRestoran;
    @SerializedName("kurir_nama")
    @Expose
    private String kurirNama;
    @SerializedName("kurir_phone")
    @Expose
    private String kurirPhone;
    @SerializedName("kurir_email")
    @Expose
    private String kurirEmail;
    @SerializedName("kurir_delete")
    @Expose
    private Integer kurirDelete;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdRestoran() {
        return idRestoran;
    }

    public void setIdRestoran(Integer idRestoran) {
        this.idRestoran = idRestoran;
    }

    public String getKurirNama() {
        return kurirNama;
    }

    public void setKurirNama(String kurirNama) {
        this.kurirNama = kurirNama;
    }

    public String getKurirPhone() {
        return kurirPhone;
    }

    public void setKurirPhone(String kurirPhone) {
        this.kurirPhone = kurirPhone;
    }

    public String getKurirEmail() {
        return kurirEmail;
    }

    public void setKurirEmail(String kurirEmail) {
        this.kurirEmail = kurirEmail;
    }

    public Integer getKurirDelete() {
        return kurirDelete;
    }

    public void setKurirDelete(Integer kurirDelete) {
        this.kurirDelete = kurirDelete;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

}
