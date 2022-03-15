package com.example.vegeyuk.restopatner.config;

import com.example.vegeyuk.restopatner.rest.ApiService;
import com.example.vegeyuk.restopatner.rest.ClientService;

public class ServerConfig {
    // .0.2.2 ini adalah localhost.
    //192.168.100.5
    //192.168.43.210
    //192.168.1.193
    //https://topapp.id/marketresto/api/v1/
    //private static final String BASE_URL_API = "http://192.168.100.5/marketresto/api/v1/";
    private static final String BASE_URL_API = "http://192.168.43.54/marketresto/api/v1/";
    //192.168.0.102
    //192.168.0.104
    //192.168.43.54
    // Mendeklarasikan Interface BaseApiService
    public static ApiService getAPIService(){
        return ClientService.getClient(BASE_URL_API).create(ApiService.class);
    }
}
