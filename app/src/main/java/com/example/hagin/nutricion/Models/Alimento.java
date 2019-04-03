package com.example.hagin.nutricion.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Alimento {

    @SerializedName("codigo")
    @Expose
    private Integer codigo;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("calorias")
    @Expose
    private Integer calorias;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCalorias() {
        return calorias;
    }

    public void setCalorias(Integer calorias) {
        this.calorias = calorias;
    }

}