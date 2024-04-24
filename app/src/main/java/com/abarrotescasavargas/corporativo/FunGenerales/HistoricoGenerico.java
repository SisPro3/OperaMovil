package com.abarrotescasavargas.corporativo.FunGenerales;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abarrotescasavargas.corporativo.Gastos.PeticionGastos;
import com.abarrotescasavargas.corporativo.Gastos.ResponseGetGastos;
import com.abarrotescasavargas.corporativo.Gastos.RetrofitGastos;
import com.abarrotescasavargas.corporativo.R;
import com.abarrotescasavargas.corporativo.FunGenerales.Globales;
import com.abarrotescasavargas.corporativo.Transferencias.ActivityTransferenciasHistorico;
import com.abarrotescasavargas.corporativo.Transferencias.HistoricoAdapter;
import com.abarrotescasavargas.corporativo.Transferencias.ObjSucusalTransferencias;
import com.abarrotescasavargas.corporativo.Transferencias.PeticionTransferencias;
import com.abarrotescasavargas.corporativo.Transferencias.ResponseGetTransferencias;
import com.abarrotescasavargas.corporativo.Transferencias.ResponseZonaTranferencia;
import com.abarrotescasavargas.corporativo.Transferencias.RetrofitTransferencias;
import com.abarrotescasavargas.corporativo.Transferencias.TransferenciasRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoricoGenerico extends AppCompatActivity {
    private Button fechaInicio, fechaFin, buscar;
    private Calendar calendar;
    private ConstraintLayout base;
    private TextView fechaInicioText, fechaFinText;
    Spinner spinner;
    AutoCompleteTextView autoCompleteTextView;
    private ArrayList<String> sucursalesList = new ArrayList<>();
    private RecyclerView recyclerViewHistorico;
    private HistoricoGenericoAdapter historicoAdapter;
    PreLoader.ProgressDialogFragment progressDialogFragment;
    private TextView totalRegistrosTextView;
    private Handler handler = new Handler();
    private Runnable filterRunnable;
    LinearLayout buscador ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_generico);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setup();
        events();

    }
    public  void events()
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

                    ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(HistoricoGenerico.this,
                            android.R.layout.simple_dropdown_item_1line, sucursalesList);
                    //autoCompleteTextView.setAdapter(autoCompleteAdapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseZonaTranferencia> call, Throwable t) {
                t.printStackTrace();
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialogFragment = new PreLoader.ProgressDialogFragment();
                progressDialogFragment.show(getSupportFragmentManager(), "ProgressDialog");
                getDataHistorico();
            }
        });
        fechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaFinDialog();
            }
        });

        fechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaInicioDialog();
            }
        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (filterRunnable != null) {
                    handler.removeCallbacks(filterRunnable);
                }

                filterRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String searchText = s.toString();
                        historicoAdapter.filterBySucursal(searchText);
                        totalRegistrosTextView.setText("Total de registros: " + historicoAdapter.getItemCount());
                    }
                };

                handler.postDelayed(filterRunnable, 100);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No es necesario implementar este método aquí
            }
        });


    }
    public void setup ()
    {
        String origen = getIntent().getStringExtra("origen");
        base = findViewById(R.id.baseGenerico);

        if (Globales.contabilidad.equals(origen)) {
            base.setBackgroundResource(R.drawable.fondo_contabilidad);
        } else if (Globales.tesoreria.equals(origen)) {
            // Hacer algo cuando origen es igual a "Tesoreria"
        } else if (Globales.inventarios.equals(origen)) {
            base.setBackgroundResource(R.drawable.fondo_inventario);
        } else if (Globales.recursosHumanos.equals(origen)) {
            // Hacer algo cuando origen es igual a "Recursos Humanos"
        } else {
            // Hacer algo en caso de que origen no coincida con ninguno de los valores anteriores
        }


        fechaFin = findViewById(R.id.fechaFinGenerico);
        buscar= findViewById(R.id.buscarGenerico);
        fechaInicio = findViewById(R.id.fechaInicioGenerico);
        fechaInicioText = findViewById(R.id.fechaInicioTextGenerico);
        fechaFinText = findViewById(R.id.fechaFinTextGenerico);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextViewHistoricoGenerico);
        spinner = findViewById(R.id.spinnerHistoricoGenerico);
        calendar = Calendar.getInstance();
        recyclerViewHistorico = findViewById(R.id.contenedorHistoricoGenerico);
        recyclerViewHistorico.setLayoutManager(new LinearLayoutManager(this));
        buscador= findViewById(R.id.buscadorHistoricoGenerico);
        buscador.setVisibility(View.GONE);
        totalRegistrosTextView = findViewById(R.id.contadorHistoricoGenerico);
    }
    private void getDataHistorico() {
        String fechaInicio = fechaInicioText.getText().toString();
        String fechaFin = fechaFinText.getText().toString();

        if (TextUtils.isEmpty(fechaInicio) || TextUtils.isEmpty(fechaFin)) {
            Dialogo.creaPopUp(HistoricoGenerico.this,"Alerta","Debes seleccionar las dos fechas para continuar","OK");
            progressDialogFragment.dismiss();
            return;
        }

        Retrofit retrofit = new RetrofitGastos().getRuta();
        PeticionGastos peticionTransferencias = retrofit.create(PeticionGastos.class);

        TransferenciasRepository transferenciasRepository = new TransferenciasRepository(HistoricoGenerico.this);
        String valorUS_USUARI = transferenciasRepository.getKU_Usuario();

        Call<List<ResponseGetGastos>> call = peticionTransferencias.getGastos(fechaInicio, fechaFin,valorUS_USUARI);
        String url = call.request().url().toString();
        call.enqueue(new Callback<List<ResponseGetGastos>>() {
            @Override
            public void onResponse(Call<List<ResponseGetGastos>> call, Response<List<ResponseGetGastos>> response) {
                if (response.isSuccessful()) {
                    List<ResponseGetGastos> responseData = response.body();
                    historicoAdapter = new HistoricoGenericoAdapter(HistoricoGenerico.this,responseData);
                    recyclerViewHistorico.setAdapter(historicoAdapter);
                    int totalRegistros = responseData.size();
                    totalRegistrosTextView.setText("Total de registros: " + totalRegistros);
                    buscador.setVisibility(View.VISIBLE);
                } else {
                    Log.v("Error Retrofit", "Código de respuesta: " + response.code());
                }
                progressDialogFragment.dismiss();
            }

            @Override
            public void onFailure(Call<List<ResponseGetGastos>> call, Throwable t) {
                Log.e("Error Retrofit", "Error en la llamada Retrofit: " + t.getMessage());
                progressDialogFragment.dismiss();
            }

        });

    }


    private void fechaInicioDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date currentDate = new Date();
                Calendar oneYearAgo = Calendar.getInstance();
                oneYearAgo.setTime(currentDate);
                oneYearAgo.add(Calendar.YEAR, -1);

                if (calendar.getTime().after(currentDate) || calendar.getTime().before(oneYearAgo.getTime())) {
                    Dialogo.creaPopUp(HistoricoGenerico.this,"Alerta","Fecha de inicio no puede ser mayor a un año en el pasado ni mayor que la fecha actual.","OK");
                    fechaInicioText.setText(null);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = sdf.format(calendar.getTime());
                    fechaInicioText.setText(formattedDate);
                }
            }
        };

        new DatePickerDialog(
                HistoricoGenerico.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void fechaFinDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date currentDate = new Date();

                String fechaInicioStr = fechaInicioText.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date fechaInicio = null;

                try {
                    fechaInicio = sdf.parse(fechaInicioStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (fechaInicio == null || calendar.getTime().after(currentDate) || calendar.getTime().before(fechaInicio)) {
                    Dialogo.creaPopUp(HistoricoGenerico.this, "Alerta", "Fecha de fin no puede ser mayor a la fecha actual ni menor que la fecha de inicio.", "OK");
                    fechaFinText.setText(null);
                } else {
                    String formattedDate = sdf.format(calendar.getTime());
                    fechaFinText.setText(formattedDate);
                }
            }
        };

        new DatePickerDialog(
                HistoricoGenerico.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
