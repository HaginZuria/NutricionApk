package com.example.hagin.nutricion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public class CaloriaService {

    @GET("api/Calorias")
    Call<List<Caloria>> getCalorias() {
        return null;
    }
}
