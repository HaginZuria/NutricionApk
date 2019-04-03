package com.example.hagin.nutricion.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Caloria {

    @SerializedName("Alimentos")
    @Expose
    private Alimento alimento;
    @SerializedName("Usuarios")
    @Expose
    private Usuario usuario;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("fecha")
    @Expose
    private String fecha;
    @SerializedName("tipoComida")
    @Expose
    private String tipoComida;
    @SerializedName("codigoAlimento")
    @Expose
    private Integer codigoAlimento;
    @SerializedName("cantidad")
    @Expose
    private Integer cantidad;

    public Alimento getAlimento() {
        return alimento;
    }

    public void setAlimento(Alimento alimento) {
        this.alimento = alimento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    public Integer getCodigoAlimento() {
        return codigoAlimento;
    }

    public void setCodigoAlimento(Integer codigoAlimento) {
        this.codigoAlimento = codigoAlimento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

}