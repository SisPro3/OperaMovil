package com.abarrotescasavargas.corporativo.Transferencias;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.abarrotescasavargas.corporativo.FunGenerales.Dialogo;
import com.abarrotescasavargas.corporativo.FunGenerales.PreLoader;
import com.abarrotescasavargas.corporativo.R;

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

public class ActivityTransferenciasHistorico extends AppCompatActivity {
    private Button fechaInicio, fechaFin, buscar;
    private Calendar calendar;
    private TextView fechaInicioText, fechaFinText;
    Spinner spinner;
    AutoCompleteTextView autoCompleteTextView;
    private ArrayList<String> sucursalesList = new ArrayList<>();
    private RecyclerView recyclerViewHistorico;
    private HistoricoAdapter historicoAdapter;
    PreLoader.ProgressDialogFragment progressDialogFragment;
    private TextView totalRegistrosTextView;
    private Handler handler = new Handler();
    private Runnable filterRunnable;
    LinearLayout buscador ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencias_historico);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setup();
        events();

    }
    public  void events()
    {


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
        fechaFin = findViewById(R.id.fechaFin);
        buscar= findViewById(R.id.buscar);
        fechaInicio = findViewById(R.id.fechaInicio);
        fechaInicioText = findViewById(R.id.fechaInicioText);
        fechaFinText = findViewById(R.id.fechaFinText);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextViewHistorico);
        spinner = findViewById(R.id.spinnerHistorico);
        calendar = Calendar.getInstance();
        recyclerViewHistorico = findViewById(R.id.contenedorHistorico);
        recyclerViewHistorico.setLayoutManager(new LinearLayoutManager(this));
        buscador= findViewById(R.id.buscadorHistorico);
        buscador.setVisibility(View.GONE);
        totalRegistrosTextView = findViewById(R.id.contadorHistorico);
    }
    private void getDataHistorico() {
        String fechaInicio = fechaInicioText.getText().toString();
        String fechaFin = fechaFinText.getText().toString();

        if (TextUtils.isEmpty(fechaInicio) || TextUtils.isEmpty(fechaFin)) {
            Dialogo.creaPopUp(ActivityTransferenciasHistorico.this,"Alerta","Debes seleccionar las dos fechas para continuar","OK");
            progressDialogFragment.dismiss();
            return; // Salir del método si las fechas están en blanco
        }

        Retrofit retrofit = new RetrofitTransferencias().getRuta();
        PeticionTransferencias peticionTransferencias = retrofit.create(PeticionTransferencias.class);
        TransferenciasRepository transferenciasRepository = new TransferenciasRepository(ActivityTransferenciasHistorico.this);
        String valorUS_USUARI = transferenciasRepository.getKU_Usuario();

        Call<List<ResponseGetTransferencias>> call = peticionTransferencias.getTransferencias(fechaInicio, fechaFin,valorUS_USUARI);
        String url = call.request().url().toString();
        call.enqueue(new Callback<List<ResponseGetTransferencias>>() {
            @Override
            public void onResponse(Call<List<ResponseGetTransferencias>> call, Response<List<ResponseGetTransferencias>> response) {
                if (response.isSuccessful()) {
                    List<ResponseGetTransferencias> responseData = response.body();
                    historicoAdapter = new HistoricoAdapter(ActivityTransferenciasHistorico.this,responseData);
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
            public void onFailure(Call<List<ResponseGetTransferencias>> call, Throwable t) {
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
                    Dialogo.creaPopUp(ActivityTransferenciasHistorico.this,"Alerta","Fecha de inicio no puede ser mayor a un año en el pasado ni mayor que la fecha actual.","OK");
                    fechaInicioText.setText(null);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = sdf.format(calendar.getTime());
                    fechaInicioText.setText(formattedDate);
                }
            }
        };

        new DatePickerDialog(
                ActivityTransferenciasHistorico.this,
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
                    Dialogo.creaPopUp(ActivityTransferenciasHistorico.this, "Alerta", "Fecha de fin no puede ser mayor a la fecha actual ni menor que la fecha de inicio.", "OK");
                    fechaFinText.setText(null);
                } else {
                    String formattedDate = sdf.format(calendar.getTime());
                    fechaFinText.setText(formattedDate);
                }
            }
        };

        new DatePickerDialog(
                ActivityTransferenciasHistorico.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

}
