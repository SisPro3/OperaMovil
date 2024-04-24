package com.abarrotescasavargas.corporativo.Login;

import com.abarrotescasavargas.corporativo.Gastos.ResponseSucursalByZona;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PeticionLogin {
    @GET("login.php")
    Call<ObjLogin> getAcceso(
            @Query("parametro1") String parametro1,
            @Query("parametro2") String parametro2
    );
}
