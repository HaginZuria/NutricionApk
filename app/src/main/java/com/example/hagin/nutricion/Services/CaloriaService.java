package com.example.hagin.nutricion.Services;

import com.example.hagin.nutricion.Models.Caloria;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CaloriaService {

    @GET("api/Calorias")
    Call<List<Caloria>> getCalorias();
}
