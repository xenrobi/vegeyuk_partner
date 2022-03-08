package com.example.vegeyuk.restopatner.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Satuan {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("satuan_nama")
    @Expose
    private String satuanNama;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSatuanNama() {
        return satuanNama;
    }

    public void setSatuanNama(String satuanNama) {
        this.satuanNama = satuanNama;
    }

    public String toString()
    {
        return( satuanNama);
    }

}