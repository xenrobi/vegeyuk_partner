package com.example.vegeyuk.restopatner.responses;

import com.example.vegeyuk.restopatner.models.Kurir;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseKurir {
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("kurir")
    @Expose
    private List<Kurir> kurir = null;

    public String getValue() {
        return value;
    }

    public void setValue(String status) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Kurir> getKurir() {
        return kurir;
    }

    public void setKurir(List<Kurir> kurir) {
        this.kurir = kurir;
    }
}
