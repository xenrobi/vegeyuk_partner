package com.example.vegeyuk.restopatner.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLaporanDelivery {
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("selesaiHariIni")
    @Expose
    private Integer selesaiHariIni;
    @SerializedName("batalHariIni")
    @Expose
    private Integer batalHariIni;
    @SerializedName("selesaiBulanIni")
    @Expose
    private Integer selesaiBulanIni;
    @SerializedName("batalBulanIni")
    @Expose
    private Integer batalBulanIni;
    @SerializedName("totalBerhasil")
    @Expose
    private Integer totalBerhasil;
    @SerializedName("totalBatal")
    @Expose
    private Integer totalBatal;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSelesaiHariIni() {
        return selesaiHariIni;
    }

    public void setSelesaiHariIni(Integer selesaiHariIni) {
        this.selesaiHariIni = selesaiHariIni;
    }

    public Integer getBatalHariIni() {
        return batalHariIni;
    }

    public void setBatalHariIni(Integer batalHariIni) {
        this.batalHariIni = batalHariIni;
    }

    public Integer getSelesaiBulanIni() {
        return selesaiBulanIni;
    }

    public void setSelesaiBulanIni(Integer selesaiBulanIni) {
        this.selesaiBulanIni = selesaiBulanIni;
    }

    public Integer getBatalBulanIni() {
        return batalBulanIni;
    }

    public void setBatalBulanIni(Integer batalBulanIni) {
        this.batalBulanIni = batalBulanIni;
    }

    public Integer getTotalBerhasil() {
        return totalBerhasil;
    }

    public void setTotalBerhasil(Integer totalBerhasil) {
        this.totalBerhasil = totalBerhasil;
    }

    public Integer getTotalBatal() {
        return totalBatal;
    }

    public void setTotalBatal(Integer totalBatal) {
        this.totalBatal = totalBatal;
    }
}
