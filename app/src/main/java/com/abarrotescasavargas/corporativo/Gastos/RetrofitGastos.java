package com.abarrotescasavargas.corporativo.Gastos;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitGastos {
    static final String BASE_URL = "https://abarrotescasavargas.mx/api/opera/gastos/";
    Retrofit retrofit;
    public RetrofitGastos() { }
    public Retrofit getRuta() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        return retrofit;
    }
}
