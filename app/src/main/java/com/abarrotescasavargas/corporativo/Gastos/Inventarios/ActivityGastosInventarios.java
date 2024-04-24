package com.abarrotescasavargas.corporativo.Gastos.Inventarios;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abarrotescasavargas.corporativo.FunGenerales.PreLoader;
import com.abarrotescasavargas.corporativo.Gastos.ActivityGastos;
import com.abarrotescasavargas.corporativo.Gastos.PeticionGastos;
import com.abarrotescasavargas.corporativo.MainActivity;
import com.abarrotescasavargas.corporativo.R;
import com.abarrotescasavargas.corporativo.Transferencias.ObjSucusalTransferencias;
import com.abarrotescasavargas.corporativo.Transferencias.PeticionTransferencias;
import com.abarrotescasavargas.corporativo.Transferencias.ResponseZonaTranferencia;
import com.abarrotescasavargas.corporativo.Transferencias.RetrofitTransferencias;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityGastosInventarios extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InventarioAdapter adapter;
    private ArrayList<String> sucursalesList = new ArrayList<>();
    private ArrayList<String> idSucursalesList = new ArrayList<>();
    List<ObjInventarista> listaDeInventaristas = new ArrayList<>();
    PreLoader preLoader ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos_inventario);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        recyclerView = findViewById(R.id.reciclerInventarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        preLoader = new PreLoader();
        preLoader.showProgressDialog(ActivityGastosInventarios.this, "Prueba");

        if (isNetworkAvailable()) {
            Retrofit retrofit = new RetrofitTransferencias().getRuta();
            PeticionTransferencias peticionTransferencias = retrofit.create(PeticionTransferencias.class);
            Call<ResponseZonaTranferencia> call = peticionTransferencias.getZona();
            final Context context = this;

            call.enqueue(new Callback<ResponseZonaTranferencia>() {
                @Override
                public void onResponse(Call<ResponseZonaTranferencia> call, Response<ResponseZonaTranferencia> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseZonaTranferencia responseSucursalByZona = response.body();
                        ArrayList<ObjSucusalTransferencias> sucursalTransferencias = responseSucursalByZona.getData();
                        for (ObjSucusalTransferencias sucursal : sucursalTransferencias) {
                            sucursalesList.add(sucursal.getSucursal());
                            idSucursalesList.add(sucursal.getId_sucursal());
                        }
                        adapter = new InventarioAdapter(context, listaDeInventaristas, sucursalesList, idSucursalesList);
                        recyclerView.setAdapter(adapter);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                preLoader.dismissProgressDialog();
                            }
                        }, 2000);
                    }
                }

                @Override
                public void onFailure(Call<ResponseZonaTranferencia> call, Throwable t) {
                    t.printStackTrace();
                }
            });
            getInventaristas();
        } else {
            Toast.makeText(this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ActivityGastosInventarios.this, MainActivity.class);
            startActivity(intent);
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public void getInventaristas() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://abarrotescasavargas.mx/api/opera/transferencia/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PeticionGastos peticionGastos = retrofit.create(PeticionGastos.class);
        Call<List<ResponseInventaristas>> call = peticionGastos.getInventaristas();

        call.enqueue(new Callback<List<ResponseInventaristas>>() {
            @Override
            public void onResponse(Call<List<ResponseInventaristas>> call, Response<List<ResponseInventaristas>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ResponseInventaristas> inventaristasList = response.body();
                    for (ResponseInventaristas inventarista : inventaristasList) {
                        listaDeInventaristas.add(new ObjInventarista(inventarista.getKE_NOMBRE(), inventarista.getKE_CVETRA()));
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ResponseInventaristas>> call, Throwable t) {
                Log.v("Error",t.toString());
            }
        });
    }

}

