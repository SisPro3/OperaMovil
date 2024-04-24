package com.abarrotescasavargas.corporativo.Transferencias;

import com.abarrotescasavargas.corporativo.Gastos.ResponseZonaGastos;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PeticionTransferencias {
    @GET("getSucursales.php")
    Call<ResponseZonaTranferencia> getZona();


    @GET("insert.php")
    Call<PostInsertTransferencia> insertTransferencia(
            @Query("user_id") String user_id,
            @Query("date_authorized") String date_authorized,
            @Query("sucursal_id") String sucursal_id,
            @Query("amount") String amount,
            @Query("saldo") String saldo,
            @Query("concept") String concept,
            @Query("exp_description") String exp_description,
            @Query("code") String code,
            @Query("aplicado") String aplicado
    );

    @GET("getTransferencias.php")
    Call<List<ResponseGetTransferencias>> getTransferencias(
            @Query("fecha_inicio") String fecha_inicio,
            @Query("fecha_fin") String fecha_fin,
            @Query("user") String user

    );


}
