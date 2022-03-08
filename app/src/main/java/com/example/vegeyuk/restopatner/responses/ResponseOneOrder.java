package com.example.vegeyuk.restopatner.responses;

import com.example.vegeyuk.restopatner.models.Order;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseOneOrder {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order")
    @Expose
    private Order order;

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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
