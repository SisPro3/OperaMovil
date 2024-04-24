package com.abarrotescasavargas.corporativo.Restablecer;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRestablecer {
    static final String BASE_URL = "https://abarrotescasavargas.mx/api/opera/restablecer/";
    Retrofit retrofit;
    public RetrofitRestablecer() { }
    public Retrofit getRuta() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        return retrofit;
    }
}
