package com.abarrotescasavargas.corporativo.Comercial;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PeticionDevolucion {
    @FormUrlEncoded
    @POST("insertDevolucion.php")
    Call<ResponseDevolucion> submitRefund(
            @Field("user_id") String userId,
            @Field("sucursal_id") String sucursalId,
            @Field("provider_id") String providerId,
            @Field("amount") String amount,
            @Field("observations") String observations
    );
}
