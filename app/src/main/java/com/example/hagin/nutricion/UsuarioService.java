package com.example.hagin.nutricion;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuarioService {

    @GET("api/Usuarios")
    Call<List<Usuario>> getUsuarios();

    @GET("api/usuarios/obtenerUsuario/{email}/{password}")
    Call<Usuario> GetobtenerUsuario(@Path("email") String email, @Path("password") String password);
}
