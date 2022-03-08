package com.example.vegeyuk.restopatner.responses;

import com.example.vegeyuk.restopatner.models.Menu;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseLaporan {
    @SerializedName("jumlah_order")
    @Expose
    private Integer jumlahOrder;
    @SerializedName("jumlah_pengantaran")
    @Expose
    private Integer jumlahPengantaran;
    @SerializedName("jumlah_selesai")
    @Expose
    private Integer jumlahSelesai;
    @SerializedName("jumlah_batal")
    @Expose
    private Integer jumlahBatal;
    @SerializedName("order_month_selesai")
    @Expose
    private Integer orderMonthSelesai;
    @SerializedName("order_month_batal")
    @Expose
    private Integer orderMonthBatal;
    @SerializedName("menu")
    @Expose
    private List<Menu> menu = null;

    public Integer getJumlahOrder() {
        return jumlahOrder;
    }

    public void setJumlahOrder(Integer jumlahOrder) {
        this.jumlahOrder = jumlahOrder;
    }

    public Integer getJumlahPengantaran() {
        return jumlahPengantaran;
    }

    public void setJumlahPengantaran(Integer jumlahPengantaran) {
        this.jumlahPengantaran = jumlahPengantaran;
    }

    public Integer getJumlahSelesai() {
        return jumlahSelesai;
    }

    public void setJumlahSelesai(Integer jumlahSelesai) {
        this.jumlahSelesai = jumlahSelesai;
    }

    public Integer getJumlahBatal() {
        return jumlahBatal;
    }

    public void setJumlahBatal(Integer jumlahBatal) {
        this.jumlahBatal = jumlahBatal;
    }

    public Integer getOrderMonthSelesai() {
        return orderMonthSelesai;
    }

    public void setOrderMonthSelesai(Integer orderMonthSelesai) {
        this.orderMonthSelesai = orderMonthSelesai;
    }

    public Integer getOrderMonthBatal() {
        return orderMonthBatal;
    }

    public void setOrderMonthBatal(Integer orderMonthBatal) {
        this.orderMonthBatal = orderMonthBatal;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

}
