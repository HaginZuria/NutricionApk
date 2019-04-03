package com.example.hagin.nutricion.Services;

import com.example.hagin.nutricion.Models.Alimento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AlimentoService {

    @GET("api/Alimentos")
    Call<List<Alimento>> getAlimentos();
}
