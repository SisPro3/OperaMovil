package com.abarrotescasavargas.corporativo.Transferencias;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitTransferencias {
    static final String BASE_URL = "https://abarrotescasavargas.mx/api/opera/transferencia/";
    Retrofit retrofit;
    public RetrofitTransferencias() { }
    public Retrofit getRuta() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        return retrofit;
    }
}
