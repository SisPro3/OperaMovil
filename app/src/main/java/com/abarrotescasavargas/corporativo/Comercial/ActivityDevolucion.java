package com.abarrotescasavargas.corporativo.Comercial;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abarrotescasavargas.corporativo.FunGenerales.CustomAutoCompleteAdapter;
import com.abarrotescasavargas.corporativo.FunGenerales.CustomProgressBar;
import com.abarrotescasavargas.corporativo.FunGenerales.Dialogo;
import com.abarrotescasavargas.corporativo.FunGenerales.HistoricoGenerico;
import com.abarrotescasavargas.corporativo.Gastos.Inventarios.ObjProveedores;
import com.abarrotescasavargas.corporativo.Gastos.PeticionGastos;
import com.abarrotescasavargas.corporativo.Gastos.ResponseProveedor;
import com.abarrotescasavargas.corporativo.Login.LoginRepository;
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

public class ActivityDevolucion extends AppCompatActivity {
    LinearLayout buscadorSucLayout, proveedorLayout;
    Spinner spinnerBuscadorSuc, spinnerProveedor;
    AutoCompleteTextView autoCompleteTextViewBuscadorSuc, autoCompleteTextViewProveedor;
    EditText descripcionEditText,cantidadEditText;
    Button enviarButton;
    Context contexto;
    private ArrayAdapter<String> adapter;
    private List<String> dataValorProveedor;
    private CustomProgressBar customProgressBar;
    private ArrayList<String> sucursalesList = new ArrayList<>();
    private PeticionDevolucion apiService;
    LoginRepository loginRepository ;
    private static final String BASE_URL = "https://abarrotescasavargas.mx/api/mobile/comercial/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion);

        setup();
        events();
    }
    private void setup() {
        contexto= ActivityDevolucion.this;
        customProgressBar =  new CustomProgressBar();
        buscadorSucLayout = findViewById(R.id.buscadorSuc);
        spinnerBuscadorSuc = findViewById(R.id.spinnerBuscadorSuc);
        autoCompleteTextViewBuscadorSuc = findViewById(R.id.autoCompleteTextViewBuscadorSuc);

        proveedorLayout = findViewById(R.id.proveedor);
        spinnerProveedor = findViewById(R.id.spinnerProveedor);
        autoCompleteTextViewProveedor = findViewById(R.id.autoCompleteTextViewProveedor);

        descripcionEditText = findViewById(R.id.descripcion);
        cantidadEditText = findViewById(R.id.cantidad);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(PeticionDevolucion.class);
        loginRepository= new LoginRepository(contexto);

        obtenerProveedores();
        llenaSucursales();

    }

    private void events() {
        enviarButton = findViewById(R.id.button);

        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores seleccionados
                String sucursalSeleccionada = autoCompleteTextViewBuscadorSuc.getText().toString().trim();
                String proveedorSeleccionado = autoCompleteTextViewProveedor.getText().toString().trim();
                String descripcion = descripcionEditText.getText().toString().trim();
                String cantidad = cantidadEditText.getText().toString().trim();

                // Validar que todos los campos estén llenos
                if (sucursalSeleccionada.isEmpty() || proveedorSeleccionado.isEmpty() || descripcion.isEmpty() || cantidad.isEmpty()) {
                    // Establecer mensajes de error en los campos vacíos
                    if (sucursalSeleccionada.isEmpty()) {
                        autoCompleteTextViewBuscadorSuc.setError("Campo requerido");
                    }
                    if (proveedorSeleccionado.isEmpty()) {
                        autoCompleteTextViewProveedor.setError("Campo requerido");
                    }
                    if (descripcion.isEmpty()) {
                        descripcionEditText.setError("Campo requerido");
                    }
                    if (cantidad.isEmpty()) {
                        cantidadEditText.setError("Campo requerido");
                    }
                    return;
                }
                String[] partesSucursal = sucursalSeleccionada.split(" ");
                String cveSucursal = partesSucursal[0];
                String[] partesProveedor = proveedorSeleccionado.split(" ");
                String cveProv = partesProveedor[0];
                String cve_user = loginRepository.getPerfil("US_USUARI");

                submitRefund(cve_user, cveSucursal, cveProv, cantidad, descripcion);
            }
        });



    }
    private void submitRefund(String userId, String sucursalId, String providerId, String amount, String observations) {
        Call<ResponseDevolucion> call = apiService.submitRefund(userId, sucursalId, providerId, amount, observations);
        call.enqueue(new Callback<ResponseDevolucion>() {
            @Override
            public void onResponse(Call<ResponseDevolucion> call, Response<ResponseDevolucion> response) {
                if (response.isSuccessful()) {
                    ResponseDevolucion refundResponse = response.body();
                    if (refundResponse != null) {
                        if (refundResponse.getStatus().equals("success"))
                        {
                            Dialogo.creaPopUpConCopia(contexto,"Generación exitosa, el codigo es el siguiente.", refundResponse.getCode(), "OK");
                            autoCompleteTextViewBuscadorSuc.setText("");
                            autoCompleteTextViewProveedor.setText("");
                            descripcionEditText.setText("");
                            cantidadEditText.setText("");
                        }
                        else
                        {
                            Dialogo.creaPopUp(contexto, "Alerta", "Algo salio mal, intente de nuevo.", "OK");
                        }

                    }
                } else {
                    // Manejar otro tipo de respuesta no exitosa aquí
                }
            }

            @Override
            public void onFailure(Call<ResponseDevolucion> call, Throwable t) {
                // Manejar el fallo de la conexión aquí
            }

        });
    }
    private void limpiarCampos() {
        // Limpiar los campos de texto
        autoCompleteTextViewBuscadorSuc.setText("");
        autoCompleteTextViewProveedor.setText("");
        descripcionEditText.setText("");
        cantidadEditText.setText("");
        // Restablecer errores
        autoCompleteTextViewBuscadorSuc.setError(null);
        autoCompleteTextViewProveedor.setError(null);
        descripcionEditText.setError(null);
        cantidadEditText.setError(null);
        // Opcional: Restablecer también los spinners si es necesario
        spinnerBuscadorSuc.setSelection(0);
        spinnerProveedor.setSelection(0);
    }

    private void llenaSucursales ()
    {
        Retrofit retrofit = new RetrofitTransferencias().getRuta();
        PeticionTransferencias peticionTransferencias = retrofit.create(PeticionTransferencias.class);
        Call<ResponseZonaTranferencia> call = peticionTransferencias.getZona();

        call.enqueue(new Callback<ResponseZonaTranferencia>() {
            @Override
            public void onResponse(Call<ResponseZonaTranferencia> call, Response<ResponseZonaTranferencia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseZonaTranferencia responseSucursalByZona = response.body();
                    ArrayList<ObjSucusalTransferencias> sucursalTransferencias = responseSucursalByZona.getData();

                    for (ObjSucusalTransferencias sucursal : sucursalTransferencias) {
                        sucursalesList.add(sucursal.getSucursal());
                    }

                    ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(contexto,
                            android.R.layout.simple_dropdown_item_1line, sucursalesList);
                    autoCompleteTextViewBuscadorSuc.setAdapter(autoCompleteAdapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseZonaTranferencia> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void obtenerProveedores() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(contexto, MainActivity.class);
            startActivity(intent);
            return;
        }
        customProgressBar.showProgressBar(contexto, "Consultando..");
        Retrofit retrofit = new RetrofitTransferencias().getRuta();
        PeticionGastos peticionGastos = retrofit.create(PeticionGastos.class);
        Call<ResponseProveedor> call = peticionGastos.getProveedores();

        call.enqueue(new Callback<ResponseProveedor>() {
            @Override
            public void onResponse(Call<ResponseProveedor> call, @NonNull Response<ResponseProveedor> response) {
                if (response.isSuccessful()  && response.body() != null) {
                    ResponseProveedor conceptoGastos = response.body();
                    if (conceptoGastos != null) {
                        List<ObjProveedores> conceptos = conceptoGastos.getData();
                        dataValorProveedor = new ArrayList<>();
                        if (conceptos != null && !conceptos.isEmpty()) {
                            for (ObjProveedores concepto : conceptos) {
                                dataValorProveedor.add(concepto.getProveedor());
                            }
                        }
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new CustomAutoCompleteAdapter(contexto, android.R.layout.simple_dropdown_item_1line, dataValorProveedor);
                                    autoCompleteTextViewProveedor.setAdapter(adapter);

                                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(contexto, android.R.layout.simple_spinner_item, dataValorProveedor);
                                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerProveedor.setAdapter(spinnerAdapter);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    customProgressBar.dismissProgressBar();
                                }
                            });
                        }
                    }
                }
                else {
                    Toast.makeText(contexto, "Error 197, contacte a programacion", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(contexto, MainActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<ResponseProveedor> call, Throwable t) {
                t.printStackTrace();
                Log.v("Error",t.toString());
            }
        });
    }

}
