package com.example.vegeyuk.restopatner.responses;
import com.example.vegeyuk.restopatner.models.Satuan;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseSatuan {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("satuan")
    @Expose
    private List<Satuan> satuan = null;

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

    public List<Satuan> getSatuan() {
        return satuan;
    }

    public void setSatuan(List<Satuan> satuan) {
        this.satuan = satuan;
    }

}
