package com.example.vegeyuk.restopatner.rest;



import com.example.vegeyuk.restopatner.responses.ResponseAuth;
import com.example.vegeyuk.restopatner.responses.ResponseLaporan;
import com.example.vegeyuk.restopatner.responses.ResponseLaporanDelivery;
import com.example.vegeyuk.restopatner.responses.ResponseOneOrder;
import com.example.vegeyuk.restopatner.responses.ResponseOrder;
import com.example.vegeyuk.restopatner.responses.ResponseKurir;
import com.example.vegeyuk.restopatner.responses.ResponseListKategori;
import com.example.vegeyuk.restopatner.responses.ResponseMenuKategori;
import com.example.vegeyuk.restopatner.responses.ResponseRestoran;
import com.example.vegeyuk.restopatner.responses.ResponseSatuan;
import com.example.vegeyuk.restopatner.responses.ResponseValue;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //fungsi ini untuk memanggil API localhost:8080/
    //login
    @GET("restopartner/login/{phone}")
    Call<ResponseAuth> signinRequest(@Path("phone") String phone);


    //Update Restoran Token if Logged
    @FormUrlEncoded
    @PATCH("restoran/{restoran}")
    Call<ResponseValue> updateTokenRestoran (@Path("restoran") String id,
                                            @Field("token") String token);

    //Update Kurir Token if Logged
    @FormUrlEncoded
    @PATCH("kurir/{kurir}")
    Call<ResponseValue> updateTokenKurir (@Path("kurir") String id,
                                             @Field("token") String token);

    //Get Menu Restoran
    @GET("menu")
    Call<ResponseMenuKategori> getMenu (@Query("id") String id);


    //add new menu
    @Multipart
    @POST("menu")
    Call<ResponseValue> addMenu (@Part  MultipartBody.Part menu_foto,
                                @Part ("menu_nama") RequestBody menu_nama,
                                @Part ("menu_deskripsi") RequestBody menu_deskripsi,
                                @Part("menu_harga") RequestBody menu_harga,
                                @Part("id_restoran") RequestBody id_restoran,
                                @Part("id_kategori") RequestBody id_kategori,
                                 @Part("id_satuan") RequestBody id_satuan,
                                 @Part("menu_discount") RequestBody menu_discount,
                                 @Part("menu_ketersediaan") RequestBody menu_ketersediaan);

    //Edit new menu
    @Multipart
    @POST("menu/{menu}/update")
    Call<ResponseValue> editMenuWithPoto (@Path("menu") String id_menu,
                                @Part  MultipartBody.Part menu_foto,
                                 @Part ("menu_nama") RequestBody menu_nama,
                                 @Part ("menu_deskripsi") RequestBody menu_deskripsi,
                                 @Part("menu_harga") RequestBody menu_harga,
                                 @Part("id_restoran") RequestBody id_restoran,
                                 @Part("id_kategori") RequestBody id_kategori,
                                          @Part("id_satuan") RequestBody id_satuan,
                                 @Part("menu_discount") RequestBody menu_discount,
                                 @Part("menu_ketersediaan") RequestBody menu_ketersediaan);
    @FormUrlEncoded
    @PATCH("menu/{menu}")
    Call<ResponseValue> editMenu (@Path("menu") String id_menu,
                                  @Field("menu_foto") String menu_foto,
                                  @Field ("menu_nama") String menu_nama,
                                  @Field("menu_deskripsi") String menu_deskripsi,
                                  @Field("menu_harga") String menu_harga,
                                  @Field("id_restoran") String id_restoran,
                                  @Field("id_kategori") String id_kategori,
                                  @Field("id_satuan") String id_satuan,
                                  @Field("menu_discount") String menu_discount,
                                  @Field("menu_ketersediaan") String menu_ketersediaan);


    //update ketersediaan
    @FormUrlEncoded
    @PATCH("menu/{menu}")
    Call<ResponseValue> updateKetersedianMenu (@Path("menu") String id,
                                            @Field("menu_ketersediaan") int menu_ketersediaan);

    //delete menu
    @DELETE("menu/{menu}")
    Call<ResponseValue> hapusMenu (@Path("menu") String id);





    //Update Restoran Token if Logged
    @FormUrlEncoded
    @PATCH("restoran/{restoran}")
    Call<ResponseValue> updateLokasiRestoran (@Path("restoran") String id,
                                             @Field("restoran_latitude") String restoran_latitude,
                                              @Field("restoran_longitude") String restoran_longitude);

    //get all order
    @GET("order")
    Call<ResponseOrder> getOrder (@Query ("id_restoran") String id_restoran,
                                  @Query("status[]") ArrayList<String> status,
                                  @Query ("start") String start,
                                  @Query ("end") String end);

    //get all order
    @GET("order/pengantaran")
    Call<ResponseOrder> getOrderOnDelivery (@Query ("order_delivery_id") String id,
                                  @Query("order_delivery_type") String type);


    //get all order
    @GET("order/historipengantaran")
    Call<ResponseOrder> getOrderHistoryDelivery (@Query ("order_delivery_id") String id,
                                            @Query("order_delivery_type") String type);

    //get all order
    @GET("order/laporanpengantaran")
    Call<ResponseLaporanDelivery> laporanDelivery (@Query ("order_delivery_id") String id,
                                                   @Query("order_delivery_type") String type);

    //get all order
    @GET("order/{order}")
    Call<ResponseOneOrder> getOneOrder (@Path ("order") String id_restoran);



    //update status pesan
    @FormUrlEncoded
    @PATCH("order/{order}")
    Call<ResponseValue> updateStatusPesan (@Path("order") String id_pesan,
                                           @Field("order_status") String status);

    //update status pesan
    @FormUrlEncoded
    @PATCH("order/{order}")
    Call<ResponseValue> updateStatusPengantaran (@Path("order") String id_pesan,
                                           @Field("order_status") String status,
                                            @Field("order_delivery_id") String id,
                                                 @Field("order_delivery_type") String type);

    //update metode pesan
    @FormUrlEncoded
    @PATCH("order/{order}")
    Call<ResponseValue> changeMetodeBayar (@Path("order") String id_pesan,
                                           @Field("order_metode_bayar") String metodeBayar);


    //    get one restoran
    @GET("restoran/{restoran}")
    Call<ResponseRestoran> getRestoranByID (@Path("restoran") String id_restoran);

    //change oprasional
    @FormUrlEncoded
    @PATCH("restoran/{restoran}")
    Call<ResponseValue> setOperasional (@Path("restoran") String id_restoran,
                                        @Field("restoran_oprasional") int operasional);

    //insert kurir
    @FormUrlEncoded
    @POST("kurir")
    Call<ResponseValue> addKurir (@Field("id_restoran") String kurir_restoran_id,
                                  @Field("kurir_phone") String kurir_phone,
                                  @Field("kurir_nama") String kurir_nama,
                                  @Field("kurir_email") String kurir_email);

    //get list kurir
    @GET("kurir")
    Call<ResponseKurir> getKurir (@Query("id_restoran") String id_restoran);


    //editKurir
    @FormUrlEncoded
    @PATCH("kurir/{kurir}")
    Call<ResponseValue> editKurir (@Path("kurir") String id_kurir,
                                   @Field("kurir_phone") String kurir_phone,
                                   @Field("kurir_nama") String kurir_nama,
                                   @Field("kurir_email") String kurir_email);

    //Delete Kurir
    @DELETE("kurir/{kurir}")
    Call<ResponseValue> deleteKurir (@Path("kurir") String id_kurir);

    //edit data pemilik restoran
    @FormUrlEncoded
    @PATCH("restoran/{restoran}")
    Call<ResponseValue> editPemilik (@Path("restoran") String id_restoran,
                                     @Field("restoran_pemilik_nama") String pemilik_nama,
                                     @Field("restoran_pemilik_phone") String restoran_pemilik_phone,
                                     @Field("restoran_pemilik_email") String restoran_pemilik_email);
    //edit data restoran
    @FormUrlEncoded
    @PATCH("restoran/{restoran}")
    Call<ResponseValue> editRestoran (@Path("restoran") String id_restoran,
                                     @Field("restoran_nama") String restoran_nama,
                                     @Field("restoran_phone") String restoran_phone,
                                     @Field("restoran_email") String restoran_email,
                                      @Field("restoran_alamat") String restoran_alamat,
                                      @Field("restoran_pajak_pb_satu") Integer restoran_pajak_pb_satu,
                                      @Field("restoran_deskripsi") String restoran_deskripsi);
    //edit data restoran
    @FormUrlEncoded
    @PATCH("restoran/{restoran}")
    Call<ResponseValue> editDelivery (@Path("restoran") String id_restoran,
                                      @Field("restoran_delivery") String restoran_delivery,
                                      @Field("restoran_delivery_tarif") String restoran_delivery_tarif,
                                      @Field("restoran_delivery_jarak") String restoran_delivery_jarak,
                                      @Field("restoran_delivery_minimum") String restoran_delivery_minimum
                                      );




    @POST("order/{order}/pembayaran")
    Call<ResponseValue> pembayaran(@Path("order") String id_order);

    @POST("order/{order}/changeMetodeBayar")
    Call<ResponseValue> changeMetodeBayar(@Path("order") String id_order);


    @FormUrlEncoded
    @POST("restoran/{restoran}/laporan")
    Call<ResponseLaporan> laporan(@Path("restoran") String id,
                                  @Field ("start") String start,
                                  @Field ("end") String end);


    //top up
    @FormUrlEncoded
    @POST("konsumen/topup")
    Call<ResponseValue> setTopUp (@Field("id_restoran") String id_restoran,
                                  @Field ("id_konsumen") String id_konsumen,
                                  @Field ("value") String value);

    @GET("satuan")
    Call<ResponseSatuan> satuan();





//    this old

    //get All Kategori
    @GET("kategori/")
    Call<ResponseListKategori> getAllKategori ();

//    @GET("auth/signin/resto/{phone}")
//    Call<ResponseSignIn> signinRequest(@Path("phone") String phone);

    //update token
    @FormUrlEncoded
    @PUT("auth/token/{id_pengguna}")
    Call<ResponseAuth> updateToken (@Path("id_pengguna") String id_pengguna,
                                    @Field("token") String token);

    //sign up resto
    @FormUrlEncoded
    @POST("auth/signup/resto")
    Call<ResponseAuth> signupRequest(@Field("restoran_nama") String restoran_nama,
                                     @Field("restoran_phone") String restoran_phone,
                                     @Field("restoran_email")String restoran_email,
                                     @Field("restoran_alamat")String restoran_alamat,
                                     @Field("restoran_lokasi")String restoran_lokasi,
                                     @Field("restoran_deskripsi") String restoran_deskripsi,
                                     @Field("restoran_gambar") String restoran_gambar,
                                     @Field("restoran_pemilik_nama") String restoran_pemilik_nama,
                                     @Field("restoran_pemilik_phone") String restoran_pemilik_phone,
                                     @Field("restoran_pemilik_email") String restoran_pemilik_email,
                                     @Field("restoran_delivery") String restoran_delivery,
                                     @Field("tarif_delivery") String tarif_delivery,
                                     @Field("restoran_delivery_jarak") String restoran_delivery_jarak,
                                     @Field("restoran_delivery_minimum") String restoran_delivery_minimum,
                                     @Field("token") String token,
                                     @Field("tipe" ) String tipe
                                     );

    //sign up resto Kategori
    @FormUrlEncoded
    @POST("auth/signup/resto/kategori")
    Call<ResponseAuth> signupKategoriRequest(@Field("restoran_id") String restoran_id,
                                     @Field("kategori_id") String kategori_id);



    //get menu & kategori
    @GET("menu/restoran/{id_restoran}")
    Call<ResponseMenuKategori> getMenuKategori(@Path("id_restoran") String id_restoran);

//    //add menu
//    @Multipart
//    @POST("menu/")
//    Call<ResponseBody> addMenu (@Part MultipartBody.Part photo,
//                                @Part ("menu_nama") String menu_nama,
//                                @Part ("menu_deskripsi") String menu_deskripsi,
//                                @Part("menu_harga") String menu_harga,
//                                @Part("menu_restoran_id") String menu_restoran_id,
//                                @Part("menu_kategori_id") String menu_kategori_id);


    @FormUrlEncoded
    @PUT("menu/{id_menu}")
    Call<ResponseValue> setKetersedianMenu (@Path("id_menu") String id_menu,
                                        @Field("ketersedian") int ketersedian);


}
