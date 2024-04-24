package com.abarrotescasavargas.corporativo.Gastos;

import com.abarrotescasavargas.corporativo.Gastos.Inventarios.ResponseInventaristas;
import com.abarrotescasavargas.corporativo.Transferencias.PostInsertTransferencia;
import com.abarrotescasavargas.corporativo.Transferencias.ResponseGetTransferencias;

import org.json.JSONArray;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PeticionGastos {
    @POST("insertTest.php")
    Call<ObjInsertExpense> insertExpense(@Body ObjInsertExpense objInsertExpense);
    @GET("getConceptosGastos.php")
    Call<ResponseConceptoGastos>
    obtieneListado();
    @POST("insertTest.php")
    Call<ResponseBody> insertarGastos(@Body RequestBody body);
    @GET("getSucursalByZona.php")
    Call<ResponseSucursalByZona> getSucursalByZona(
            @Query("zona") String zona
    );
    @GET("zonas.php")
    Call<ResponseZonaGastos> getZona();
    @GET("getGastos.php")
    Call<List<ResponseGetGastos>> getGastos(
            @Query("fecha_inicio") String fecha_inicio,
            @Query("fecha_fin") String fecha_fin,
            @Query("user") String user

    );

    //Inventarios
    @GET("Inventarios/getInventaristas.php")
    Call<List<ResponseInventaristas>> getInventaristas();


    @Multipart
    @POST("Inventarios/insertGastos.php")
    Call<PostInsertTransferencia> insertTransferencia(
            @Part("user_id") RequestBody user_id,
            @Part("date_authorized") RequestBody date_authorized,
            @Part("sucursal_id") RequestBody sucursal_id,
            @Part("amount") RequestBody amount,
            @Part("saldo") RequestBody saldo,
            @Part("concept") RequestBody concept,
            @Part("exp_description") RequestBody exp_description,
            @Part("code") RequestBody code,
            @Part("aplicado") RequestBody aplicado
    );
    @GET("Inventarios/getProveedores.php")
    Call<ResponseProveedor> getProveedores();
}
