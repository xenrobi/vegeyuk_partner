package com.example.vegeyuk.restopatner.responses;

import com.example.vegeyuk.restopatner.models.Restoran;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseRestoran {

    @SerializedName("data")
    @Expose
    private Restoran data;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;

    public Restoran getData() {
        return data;
    }

    public void setData(Restoran data) {
        this.data = data;
    }

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
}
