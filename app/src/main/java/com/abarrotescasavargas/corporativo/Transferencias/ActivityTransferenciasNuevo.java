package com.abarrotescasavargas.corporativo.Transferencias;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abarrotescasavargas.corporativo.FunGenerales.Dialogo;
import com.abarrotescasavargas.corporativo.FunGenerales.Generales;
import com.abarrotescasavargas.corporativo.Gastos.ObjConceptoGastos;
import com.abarrotescasavargas.corporativo.Gastos.PeticionGastos;
import com.abarrotescasavargas.corporativo.Gastos.ResponseSucursalByZona;
import com.abarrotescasavargas.corporativo.Gastos.RetrofitGastos;
import com.abarrotescasavargas.corporativo.Principal;
import com.abarrotescasavargas.corporativo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivityTransferenciasNuevo extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextView;
    private Spinner spinner;
    private EditText montoEditText;
    private Button enviarButton;
    private ArrayList<String> sucursalesList = new ArrayList<>();
    String codigo = "";
    String fechaHoraFormateada = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencias_nueva);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setup();
        events();
    }
    private void setup() {
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        spinner = findViewById(R.id.spinnerTransferencia);
        montoEditText = findViewById(R.id.montoEditText);
        enviarButton = findViewById(R.id.enviarButton);

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

                    ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(ActivityTransferenciasNuevo.this,
                            android.R.layout.simple_dropdown_item_1line, sucursalesList);
                    autoCompleteTextView.setAdapter(autoCompleteAdapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseZonaTranferencia> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    private void events() {
        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sucursal = autoCompleteTextView.getText().toString();
                String monto = montoEditText.getText().toString();

                String[] partes = sucursal.split(" - ");
                String valorAntesDelGuion = partes[0];

                if (sucursal.isEmpty() || monto.isEmpty()) {
                    Dialogo.creaPopUp(ActivityTransferenciasNuevo.this, "Alerta", "Por favor, seleccione tanto la sucursal como el monto antes de continuar.", "OK");
                } else {
                    double montoDouble = Double.parseDouble(monto);
                    if (montoDouble >= 1 )
                    {

                        Retrofit retrofit = new RetrofitTransferencias().getRuta();
                        PeticionTransferencias peticionTransferencias = retrofit.create(PeticionTransferencias.class);

                        TransferenciasRepository transferenciasRepository = new TransferenciasRepository(ActivityTransferenciasNuevo.this);
                        String valorUS_USUARI = transferenciasRepository.getUsuario();

                        Date fechaHoraActual = new Date();

                        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        fechaHoraFormateada = formato.format(fechaHoraActual);
                        codigo = Generales.generarStringAleatorio(6);

                        Call<PostInsertTransferencia> call = peticionTransferencias.insertTransferencia(
                                valorUS_USUARI,
                                fechaHoraFormateada,
                                valorAntesDelGuion,
                                monto,
                                monto,
                                "TRANSFERENCIA ELECTRONICA DE FONDOS",
                                "TRANSFERENCIA ELECTRONICA DE FONDOS",
                                codigo,
                                "0"
                        );

                        call.enqueue(new Callback<PostInsertTransferencia>() {
                            @Override
                            public void onResponse(Call<PostInsertTransferencia> call, Response<PostInsertTransferencia> response) {
                                if (response.isSuccessful()) {
                                    PostInsertTransferencia result = response.body();
                                    String mensaje = "Buen día, te mando la clave para validar la transferencia electronica de fondos:\n\n"
                                            + "Monto: $" + monto + "\n"
                                            + "Fecha Generación: " + fechaHoraFormateada + "\n"
                                            + "Código Retiro: *" + codigo + "*\n\n"
                                            + "Gracias";

                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("Mensaje", mensaje);
                                    clipboard.setPrimaryClip(clip);

                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.setPackage("com.whatsapp.w4b");

                                    intent.putExtra(Intent.EXTRA_TEXT, mensaje);

                                    try {
                                        startActivity(intent);

                                        finish();
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(ActivityTransferenciasNuevo.this, "WhatsApp no está instalado.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                }
                            }

                            @Override
                            public void onFailure(Call<PostInsertTransferencia> call, Throwable t) {
                                Log.v("Trone",t.toString());
                            }
                        });
                    }
                    else {
                        Dialogo.creaPopUp(ActivityTransferenciasNuevo.this, "Alerta", "El monto debe ser mayor a 1", "OK");
                    }
                }
            }
        });




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        montoEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });
    }
}
