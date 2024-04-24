package com.abarrotescasavargas.corporativo.Gastos;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.abarrotescasavargas.corporativo.FunGenerales.CustomAutoCompleteAdapter;
import com.abarrotescasavargas.corporativo.FunGenerales.CustomProgressBar;
import com.abarrotescasavargas.corporativo.FunGenerales.Dialogo;
import com.abarrotescasavargas.corporativo.FunGenerales.EspacioEntreFichasItemDecoration;
import com.abarrotescasavargas.corporativo.FunGenerales.Generales;
import com.abarrotescasavargas.corporativo.FunGenerales.PreLoader;
import com.abarrotescasavargas.corporativo.Gastos.Inventarios.ObjProveedores;
import com.abarrotescasavargas.corporativo.Login.LoginRepository;
import com.abarrotescasavargas.corporativo.MainActivity;
import com.abarrotescasavargas.corporativo.R;
import com.abarrotescasavargas.corporativo.Transferencias.RetrofitTransferencias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityGastos extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextView,autoCompleteTextViewProveedor,autoCompleteTextViewBuscadorSuc;
    private ArrayAdapter<String> adapter;
    private Handler handler = new Handler();
    private Spinner spinner,spinnerProveedor;
    private Runnable filterRunnable;

    private List<String> dataValor,dataValorProveedor;
    private LinearLayout proveedor;
    RecyclerView recyclerView ;
    private AdapterGastos adaptador;
    PreLoader preLoader ;
    private CheckBox seleccion,corporativo,deducible;
    private TextView texto;
    private Button button;
    private boolean isCheckedAll = true;
    private EditText descripcion;

    public String concepto;
    private PeticionGastos apiService;
    LoginRepository loginRepository ;
    private CustomProgressBar customProgressBar;
    private int currentProgress = 0;
    private Context context;
    private Handler mainHandler;
    private boolean hasErrorOccurred = false;
    JSONArray jsonArray;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_gastos);

        setup();
        obtenerConceptos();
        events();
    }
    private void setup() {
        mainHandler = new Handler(Looper.getMainLooper());
        context = this;
        customProgressBar = new CustomProgressBar();

        proveedor=findViewById(R.id.proveedor);
        loginRepository= new LoginRepository(this);
        preLoader = new PreLoader();
        preLoader.showProgressDialog(ActivityGastos.this,"Prueba");
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextViewProveedor = findViewById(R.id.autoCompleteTextViewProveedor);
        spinnerProveedor = findViewById(R.id.spinnerProveedor);
        corporativo = findViewById(R.id.corporativoCheckBox);
        deducible = findViewById(R.id.deducibleCheckBox);
        spinner = findViewById(R.id.spinnerHistoricoGenerico);

        List<Gastos> elementos = new ArrayList<>();
        recyclerView= findViewById(R.id.recicler);
        descripcion=findViewById(R.id.descripcion);
        autoCompleteTextViewBuscadorSuc = findViewById(R.id.autoCompleteTextViewBuscadorSuc);
        adaptador = new AdapterGastos(ActivityGastos.this, elementos,"",false,false);
        recyclerView.setAdapter(adaptador);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        int espacioEntreFichas = getResources().getDimensionPixelSize(R.dimen.espacio_entre_fichas);
        recyclerView.addItemDecoration(new EspacioEntreFichasItemDecoration(this, espacioEntreFichas));
        seleccion= findViewById(R.id.seleccion);
        texto=findViewById(R.id.texto);
        seleccion.setVisibility(View.INVISIBLE);
        texto.setVisibility(View.INVISIBLE);
        button=findViewById(R.id.button);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(3000 , TimeUnit.SECONDS)
                .connectTimeout(3000, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://abarrotescasavargas.mx/api/mobile/finanzas/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        apiService = retrofit.create(PeticionGastos.class);
    }
    private void events() {
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                concepto = autoCompleteTextView.getText().toString();
                if (concepto.toLowerCase().contains("proveedor"))
                {
                    proveedor.setVisibility(View.VISIBLE);
                    obtenerProveedores();
                }
                else {
                    proveedor.setVisibility(View.GONE);
                }

                preLoader.showProgressDialog(ActivityGastos.this,"Prueba");
                adaptador.clear();
                fetchSucursalesByZona("");
            }
        });
        seleccion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedAll = isChecked;
                adaptador.selectAll(isChecked);
                updateTextViewText();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getDescripcion = descripcion.getText().toString();
                if (getDescripcion.trim().isEmpty()) {
                    descripcion.setError("La descripción no puede estar vacía");
                    return;
                }
                String stringConcepto = concepto;
                if (stringConcepto == null || stringConcepto.equals("")) {
                    Toast.makeText(ActivityGastos.this, "El concepto no puede estar vacío", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (dataValor.contains(autoCompleteTextView.getText().toString())) {
                        //if()
                        String acumulable = "";
                        int i = 0;
                        List<Gastos> elementos = adaptador.getElementos();
                        for (Gastos gastos : elementos) {
                            if (gastos.isSelected() == true && gastos.getMonto() != null) {
                                acumulable = acumulable + gastos.getSucursal() + " - $" + gastos.getMonto() + "\n";
                                i = i + 1;
                            }
                        }
                        if (i == 0) {
                            Dialogo.creaPopUp(ActivityGastos.this, "Alerta", "Llena mínimo una sucursal para continuar", "Ok");
                        } else {
                            Dialogo.popUpValidar(
                                    ActivityGastos.this,
                                    "Alto",
                                    "Asegúrate de que los gastos que deseas enviar son los correctos. \n" + acumulable,
                                    "No",
                                    "Continuar",
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Thread insertarGastosThread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String msg="";
                                                    ExecutorService executor = Executors.newSingleThreadExecutor();
                                                    Future<String> resultado = executor.submit(insertarGastosCallable());

                                                    try {
                                                        msg = resultado.get();

                                                        executor.shutdown();
                                                    } catch (InterruptedException |
                                                             ExecutionException e) {
                                                        e.printStackTrace();
                                                    }

                                                    String mensaje= "Buen dia, adjunto clave(s) de retiro para el concepto: *"+stringConcepto+"*\nDescripcion:*"+getDescripcion+"*\n"+msg;
                                                    if (isNetworkAvailable()) {
                                                        Intent intentWABusiness = new Intent(Intent.ACTION_SEND);
                                                        intentWABusiness.setType("text/plain");
                                                        intentWABusiness.setPackage("com.whatsapp.w4b"); // Package de WhatsApp Business
                                                        intentWABusiness.putExtra(Intent.EXTRA_TEXT, mensaje);

                                                        Intent intentWA = new Intent(Intent.ACTION_SEND);
                                                        intentWA.setType("text/plain");
                                                        intentWA.setPackage("com.whatsapp"); // Package de WhatsApp normal
                                                        intentWA.putExtra(Intent.EXTRA_TEXT, mensaje);
                                                        try {
                                                            startActivity(intentWABusiness);
                                                            finish();
                                                        } catch (android.content.ActivityNotFoundException ex) {
                                                            try {
                                                                startActivity(intentWA);
                                                                finish();
                                                            } catch (android.content.ActivityNotFoundException ex2) {
                                                                Toast.makeText(ActivityGastos.this, "WhatsApp no está instalado.", Toast.LENGTH_SHORT).show();

                                                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                                                ClipData clip = ClipData.newPlainText("Mensaje de Gastos", mensaje);
                                                                clipboard.setPrimaryClip(clip);

                                                                Toast.makeText(ActivityGastos.this, "Mensaje copiado al portapapeles.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                    else {
                                                        return;
                                                    }

                                                }
                                            });
                                            insertarGastosThread.start();
                                        }
                                    }
                                    ,true);
                        }
                    } else {
                        Dialogo.creaPopUp(ActivityGastos.this, "Alerta", "El concepto seleccionado no es válido", "Ok");
                    } 
                }
            }
        });
        autoCompleteTextViewBuscadorSuc.addTextChangedListener(new TextWatcher() {
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
                        adaptador.filterBySucursal(searchText);
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
    private RequestBody createRequestBodyFromJSONArray(JSONArray jsonArray) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        return RequestBody.create(mediaType, jsonArray.toString());
    }
    private Callable<String> insertarGastosCallable() {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                List<Gastos> elementos = adaptador.getElementos();
                int totalInsertions = 0;
                String mensaje = "";
                String cve_user = loginRepository.getPerfil("US_USUARI");
                jsonArray = new JSONArray();
                for (Gastos gastos : elementos) {
                    if (gastos.isSelected() && gastos.getMonto() != null) {
                        totalInsertions++;
                    }
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        customProgressBar.showProgressBar(ActivityGastos.this, "Enviando gastos...");
                    }
                });

                for (Gastos gastos : elementos) {
                    if (gastos.isSelected() && gastos.getMonto() != null) {
                        String suc = gastos.getSucursal();
                        String sucID = gastos.getIdRondines();
                        Double monto = gastos.getMonto();
                        int sendCorp = gastos.isCorporativo() ? 1 : 0;
                        int sendDedu = gastos.isDeducible() ? 1 : 0;
                        String getDescripcion = descripcion.getText().toString();
                        String cve_retiro = Generales.generarStringAleatorio(6);
                        String[] partes = concepto.split(" - ");
                        String concept_id = partes[0];
                        String conceptoFinal = partes[1];
                        String pronunciacion = Generales.convertToPronunciation(cve_retiro);
                        if (concepto.toLowerCase().contains("proveedor")) {
                            String proveedor = autoCompleteTextViewProveedor.getText().toString();
                            String[] partesProveedor = proveedor.split(" ");
                            String cveProv = partesProveedor[0];
                            //insertExpense(cve_user, sucID, monto, conceptoFinal, getDescripcion, cve_retiro, 0, monto, null, 0, totalInsertions,cveProv,sendCorp,sendDedu, Integer.parseInt(concept_id));
                            creaJson(cve_user, sucID, monto, conceptoFinal, getDescripcion, cve_retiro, 0, monto, null, 0, totalInsertions, cveProv, sendCorp, sendDedu, Integer.parseInt(concept_id));

                        } else {
                            creaJson(cve_user, sucID, monto, conceptoFinal, getDescripcion, cve_retiro, 0, monto, null, 0, totalInsertions, null, sendCorp, sendDedu, Integer.parseInt(concept_id));
                            //insertExpense(cve_user, sucID, monto, conceptoFinal, getDescripcion, cve_retiro, 0, monto, null, 0, totalInsertions,null,sendCorp,sendDedu, Integer.parseInt(concept_id));
                        }

                        mensaje = mensaje + "\nSucursal: *" + suc + "*\nClave retiro:*" + cve_retiro + "*\nPronunciación:*" + pronunciacion + "*\nMonto: *$" + monto + "*\n";
                    }
                }
                RequestBody requestBody = createRequestBodyFromJSONArray(jsonArray);
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(logging);  // agregar el logging interceptor

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://abarrotescasavargas.mx/api/mobile/finanzas/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build()) // establecer el cliente HTTP personalizado
                        .build();


                PeticionGastos apiService = retrofit.create(PeticionGastos.class);
                if (isNetworkAvailable()) {
                    Call<ResponseBody> call = apiService.insertarGastos(requestBody);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ActivityGastos.this, "Gasto enviado correctamente.", Toast.LENGTH_LONG).show();

                            } else {

                                Toast.makeText(ActivityGastos.this, "La petición falló.", Toast.LENGTH_LONG).show();
                                mostrarMensajeError();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("Failure", t.toString());
                            Toast.makeText(ActivityGastos.this, "Failure", Toast.LENGTH_LONG).show();
                            customProgressBar.dismissProgressBar();
                            mostrarMensajeError();
                        }
                    });
                }
                else {
                    // Si no hay conexión a Internet, muestra un mensaje y no procedas a abrir WhatsApp.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "No hay conexion a internet", Toast.LENGTH_SHORT).show();
                            customProgressBar.dismissProgressBar();
                        }
                    });
                }
                return mensaje; // Asegúrate de retornar un mensaje o estado adecuado
            }
        };
    }


    private void mostrarMensajeError()
    {
        hasErrorOccurred = true;
        runOnUiThread(() -> Toast.makeText(ActivityGastos.this, "Algo salió mal, por favor intenta nuevamente.", Toast.LENGTH_LONG).show());
    }

    private void creaJson(String user_id, String sucursal_id, double amount, String concept, String exp_description,
                          String code, int aplicado, double saldo, Date fech_uso, int evidencia, int totalEntriesToSend,
                          String cvepro, int corporativo, int deducible, int concept_id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateAuthorized = new Date();

        String formattedDateAuthorized = dateFormat.format(dateAuthorized);


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("date_authorized", formattedDateAuthorized);
            jsonObject.put("date_registered_server", formattedDateAuthorized);
            jsonObject.put("sucursal_id", sucursal_id);
            jsonObject.put("amount", amount);
            jsonObject.put("concept", concept);
            jsonObject.put("exp_description", exp_description);
            jsonObject.put("code", code);
            jsonObject.put("aplicado", aplicado);
            jsonObject.put("saldo", saldo);
            jsonObject.put("fech_uso", fech_uso);
            jsonObject.put("evidencia", evidencia);
            jsonObject.put("cvepro", cvepro);
            jsonObject.put("corporativo", corporativo);
            jsonObject.put("deducible", deducible);
            jsonObject.put("concept_id", concept_id);

            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    private void updateTextViewText() {
        if (isCheckedAll) {
            texto.setText("Deseleccionar");
        } else {
            texto.setText("Seleccionar");
        }
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
            Intent intent = new Intent(ActivityGastos.this, MainActivity.class);
            startActivity(intent);
            return;
        }
        customProgressBar.showProgressBar(ActivityGastos.this, "Consultando..");
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
                                    adapter = new CustomAutoCompleteAdapter(ActivityGastos.this, android.R.layout.simple_dropdown_item_1line, dataValorProveedor);
                                    autoCompleteTextViewProveedor.setAdapter(adapter);

                                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(ActivityGastos.this, android.R.layout.simple_spinner_item, dataValorProveedor);
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
                    Toast.makeText(ActivityGastos.this, "Error 197, contacte a programacion", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityGastos.this, MainActivity.class);
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
    private void obtenerConceptos() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ActivityGastos.this, MainActivity.class);
            startActivity(intent);
            return;
        }
        Retrofit retrofit = new RetrofitGastos().getRuta();
        PeticionGastos peticionGastos = retrofit.create(PeticionGastos.class);
        Call<ResponseConceptoGastos> call = peticionGastos.obtieneListado();

        call.enqueue(new Callback<ResponseConceptoGastos>() {
            @Override
            public void onResponse(Call<ResponseConceptoGastos> call, @NonNull Response<ResponseConceptoGastos> response) {
                if (response.isSuccessful()  && response.body() != null) {
                    ResponseConceptoGastos conceptoGastos = response.body();
                    if (conceptoGastos != null) {
                        List<ObjConceptoGastos> conceptos = conceptoGastos.getData();
                        dataValor = new ArrayList<>();
                        if (conceptos != null && !conceptos.isEmpty()) {
                            for (ObjConceptoGastos concepto : conceptos) {
                                dataValor.add(concepto.getIdConcept() +" - "+concepto.getDescription());
                            }
                        }
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new CustomAutoCompleteAdapter(ActivityGastos.this, android.R.layout.simple_dropdown_item_1line, dataValor);
                                    autoCompleteTextView.setAdapter(adapter);

                                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(ActivityGastos.this, android.R.layout.simple_spinner_item, dataValor);
                                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner.setAdapter(spinnerAdapter);
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
                    Toast.makeText(ActivityGastos.this, "Error 197, contacte a programacion", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityGastos.this, MainActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<ResponseConceptoGastos> call, Throwable t) {
                t.printStackTrace();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                preLoader.dismissProgressDialog();
            }
        }, 1500);
    }
    private void fetchSucursalesByZona(String zona) {

        Retrofit retrofit = new RetrofitGastos().getRuta();
        PeticionGastos peticionGastos = retrofit.create(PeticionGastos.class);
        Call<ResponseSucursalByZona> call = peticionGastos.getSucursalByZona(zona);
        call.enqueue(new Callback<ResponseSucursalByZona>() {
            @Override
            public void onResponse(Call<ResponseSucursalByZona> call, Response<ResponseSucursalByZona> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseSucursalByZona responseSucursalByZona = response.body();
                    List<ObjSucursalByZona> sucursales = responseSucursalByZona.getData();

                    if (sucursales != null && !sucursales.isEmpty()) {
                        List<Gastos> elementos = new ArrayList<>();

                        for (ObjSucursalByZona sucursal : sucursales) {
                            String idRONDINES = sucursal.getIdRONDINES();
                            String RO_CVESUC = sucursal.getRO_CVESUC();
                            String RO_MONTOS = sucursal.getRO_MONTOS();
                            String Sucursal = sucursal.getSucursal();
                            String KS_ZONAOP = sucursal.getKS_ZONAOP();
                            String KS_COLORS = sucursal.getKS_COLORS();

                            if (autoCompleteTextView.getText().toString().toLowerCase().contains("108"))
                            {
                                Double mto = Double.parseDouble(RO_MONTOS);

                                elementos.add(new Gastos(Sucursal, mto > 0 ? mto : null, true, true, KS_COLORS, idRONDINES));

                            }

                            else
                            {
                                elementos.add(new Gastos(Sucursal, null, true,true, KS_COLORS,idRONDINES));
                            }
                        }
                        adaptador.agregarElementos(elementos);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityGastos.this);
                        recyclerView.setLayoutManager(layoutManager);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseSucursalByZona> call, Throwable t) {
                t.printStackTrace();
            }
        });
        seleccion.setVisibility(View.VISIBLE);
        seleccion.setChecked(true);
        texto.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                preLoader.dismissProgressDialog();
            }
        }, 1500);
    }

}