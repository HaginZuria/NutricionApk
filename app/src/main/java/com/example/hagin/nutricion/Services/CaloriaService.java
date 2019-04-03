package com.example.hagin.nutricion.Services;

import com.example.hagin.nutricion.Models.Caloria;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CaloriaService {

    @GET("api/Calorias")
    Call<List<Caloria>> getCalorias();

    //@FormUrlEncoded
    @POST("api/calorias")
    Call<Caloria> setCalorias(@Body Caloria caloria);
    //Call<Caloria> setCalorias(@Field("email") String email, @Field("fecha") String fecha, @Field("tipoComida") String tipoComida, @Field("codigoAlimento") int codigoAlimento, @Field("cantidad") int cantidad);
}
