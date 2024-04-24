package com.abarrotescasavargas.corporativo.Gastos.Inventarios;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abarrotescasavargas.corporativo.FunGenerales.CustomProgressBar;
import com.abarrotescasavargas.corporativo.FunGenerales.Dialogo;
import com.abarrotescasavargas.corporativo.FunGenerales.Generales;
import com.abarrotescasavargas.corporativo.FunGenerales.MultiSpinner;
import com.abarrotescasavargas.corporativo.FunGenerales.PreLoader;
import com.abarrotescasavargas.corporativo.Gastos.ActivityGastos;
import com.abarrotescasavargas.corporativo.Gastos.AdapterGastos;
import com.abarrotescasavargas.corporativo.Gastos.Gastos;
import com.abarrotescasavargas.corporativo.Gastos.ObjInsertExpense;
import com.abarrotescasavargas.corporativo.Gastos.ObjZona;
import com.abarrotescasavargas.corporativo.Gastos.PeticionGastos;
import com.abarrotescasavargas.corporativo.Gastos.ResponseZonaGastos;
import com.abarrotescasavargas.corporativo.Gastos.RetrofitGastos;
import com.abarrotescasavargas.corporativo.Login.LoginRepository;
import com.abarrotescasavargas.corporativo.R;
import com.abarrotescasavargas.corporativo.Transferencias.ObjSucusalTransferencias;
import com.abarrotescasavargas.corporativo.Transferencias.PeticionTransferencias;
import com.abarrotescasavargas.corporativo.Transferencias.PostInsertTransferencia;
import com.abarrotescasavargas.corporativo.Transferencias.ResponseZonaTranferencia;
import com.abarrotescasavargas.corporativo.Transferencias.RetrofitTransferencias;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.ViewHolder> {
    private List<ObjInventarista> listaDeDatos;
    private ArrayList<String> data,idSucursales;
    private Context context;
    private List<String> nombresParaSpinner;
    private PeticionGastos apiService;
    LoginRepository loginRepository ;

    private Calendar calendar;
    Date date = new Date();
    private int currentProgress = 0;
    public InventarioAdapter(Context context, List<ObjInventarista> listaDeDatos, ArrayList<String>data,ArrayList<String> idSucursales) {
        this.context = context;
        this.data= data;
        this.idSucursales=idSucursales;
        this.listaDeDatos = listaDeDatos;
        calendar = Calendar.getInstance();



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://abarrotescasavargas.mx/api/mobile/finanzas/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(PeticionGastos.class);
        loginRepository= new LoginRepository(context);

        nombresParaSpinner = new ArrayList<>();
        for (ObjInventarista inventarista : listaDeDatos) {
            nombresParaSpinner.add(inventarista.getNombre());
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreTextView,idSucursal;
        public EditText inputComida,inputPasaje,inputHospedaje;
        public ImageButton enviarImageView;
        public Button fechaGenerar;
        public CheckBox corporativo,deducible;
        public Spinner spinner;
        public boolean isFirstSelection = true;
        public ViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.inputNombre);
            inputComida = itemView.findViewById(R.id.inputComida);
            inputPasaje = itemView.findViewById(R.id.inputPasaje);
            inputHospedaje = itemView.findViewById(R.id.inputHospedaje);
            enviarImageView = itemView.findViewById(R.id.EnviarGastoInventario);
            fechaGenerar=itemView.findViewById(R.id.fechaGenerar);
            spinner = itemView.findViewById(R.id.multi_spinnerInventarios);
            idSucursal = itemView.findViewById(R.id.id_sucursal);
            deducible = itemView.findViewById(R.id.deducibleCheckBox);
            corporativo = itemView.findViewById(R.id.corporativoCheckBox);

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, data);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(spinnerAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        idSucursal.setText(idSucursales.get(position));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }
            });



            fechaGenerar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fechaDialog(getAdapterPosition(), fechaGenerar);
                }
            });
            inputComida.setText("40");
            enviarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        String fechaValoida = fechaGenerar.getText().toString();
                        String cve_user = loginRepository.getPerfil("US_USUARI");
                       if (!fechaValoida.equals("yyyy/mm/dd")) {
                            Dialogo.popUpValidar(
                                    context,
                                    "Alto",
                                    "¿Seguro que deseas generar este gasto?",
                                    "No",
                                    "Continuar",
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            String comida = inputComida.getText().toString();
                                            String sucursal = spinner.getSelectedItem().toString();
                                            String pasaje = inputPasaje.getText().toString();
                                            String hospedaje = inputHospedaje.getText().toString();
                                            String codigo="";
                                            int corporativoInt = corporativo.isChecked() ? 1 : 0;
                                            int deducibleInt = deducible.isChecked() ? 1 : 0;
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                                            String suc= idSucursal.getText().toString();
                                            String nombre = nombreTextView.getText().toString();
                                            String mensaje= "Buen dia, te adjunto clave(s) de retiro para la sucursal *"+sucursal+"*\n"+ "Colaborador: "+nombre+"\n";

                                            String descripcion = "";
                                            String fechaYHoraActual = dateFormat.format(date);

                                            if (!comida.isEmpty() && Integer.parseInt(comida) > 0)
                                            {
                                                descripcion = "$"+comida+" Comida de "+nombre+" "+fechaYHoraActual;
                                                codigo=Generales.generarStringAleatorio(6);

                                                insertExpense(cve_user, suc, Integer.parseInt(comida), "Alimentacion", descripcion, codigo, 0, Integer.parseInt(comida), null, 0,fechaValoida,fechaYHoraActual, corporativoInt,deducibleInt,115,null);
                                                mensaje =mensaje + "Clave para gasto de comida:\n\n"
                                                        + "Monto: $" + comida + "\n"
                                                        + "Fecha Generación: " + fechaYHoraActual + "\n"
                                                        + "Clave Retiro: *" + codigo + "*\n\n";
                                            }
                                            if (!pasaje.isEmpty() && Integer.parseInt(pasaje) > 0)
                                            {
                                                descripcion = "$"+pasaje+" Pasaje de "+nombre+" "+fechaYHoraActual;
                                                codigo=Generales.generarStringAleatorio(6);

                                                insertExpense(cve_user, suc, Integer.parseInt(pasaje), "Pasajes", descripcion, codigo, 0, Integer.parseInt(pasaje), null, 0,fechaValoida,fechaYHoraActual, corporativoInt,deducibleInt,116,null);

                                                mensaje =mensaje + "Clave para gasto de pasaje:\n\n"
                                                        + "Monto: $" + pasaje + "\n"
                                                        + "Fecha Generación: " + fechaYHoraActual + "\n"
                                                        + "Clave Retiro: *" + Generales.generarStringAleatorio(6) + "*\n\n";
                                            }
                                            if (!hospedaje.isEmpty() && Integer.parseInt(hospedaje) > 0)
                                            {
                                                descripcion = "$"+hospedaje+" Hospedaje de "+nombre+" "+fechaYHoraActual;
                                                codigo=Generales.generarStringAleatorio(6);

                                                insertExpense(cve_user, suc, Integer.parseInt(hospedaje), "Hospedajes", descripcion, codigo, 0, Integer.parseInt(hospedaje), null, 0,fechaValoida,fechaYHoraActual, corporativoInt,deducibleInt,113,null);

                                                mensaje =mensaje + "Clave para gasto de hospedaje:\n\n"
                                                        + "Monto: $" + hospedaje + "\n"
                                                        + "Fecha Generación: " + fechaYHoraActual + "\n"
                                                        + "Clave Retiro: *" + Generales.generarStringAleatorio(6) + "*\n\n";
                                            }
                                            mensaje=mensaje+"\nRecuerda que su duración es de 24 horas \n  ¡Gracias!";

                                            Dialogo.creaPopUpConCopia(context,"Generación exitosa",mensaje,"OK");

                                            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText("Mensaje", mensaje);
                                            clipboard.setPrimaryClip(clip);

                                            Intent intent = new Intent(Intent.ACTION_SEND);
                                            intent.setType("text/plain");
                                            intent.setPackage("com.whatsapp.w4b");

                                            intent.putExtra(Intent.EXTRA_TEXT, mensaje);

                                            try {
                                                context.startActivity(intent);
                                            } catch (android.content.ActivityNotFoundException ex) {
                                                Toast.makeText(context, "WhatsApp no está instalado.", Toast.LENGTH_SHORT).show();
                                            }
                                            Toast.makeText(context, "Meter la accion", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            ,false);
                        }
                       else {
                           Toast.makeText(context, "Seleccione una fecha primero", Toast.LENGTH_SHORT).show();
                       }
                    }
                }
            });;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ficha_gastos_inventario, parent, false);
        return new ViewHolder(view);
    }
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ObjInventarista datos = listaDeDatos.get(position);
        String nombre = datos.getNombre();
        setTextWithMarquee(holder.nombreTextView, nombre);
    }


    @Override
    public int getItemCount() {
        return listaDeDatos.size();
    }
    private void setTextWithMarquee(TextView textView, String text) {
        if (textView != null && !TextUtils.isEmpty(text)) {
            textView.setText(text);
            textView.setSelected(true);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSingleLine(true);
        }
    }
    private void insertExpense( String user_id, String sucursal_id,
                                double amount, String concept, String exp_description, String code, int aplicado,
                                double saldo, Date fech_uso, int evidencia,String formattedDateAuthorized,String formattedDateRegisteredServer,int corporativo, int deducible, int concept_id,String cvepro) {

        ObjInsertExpense expenseData = new ObjInsertExpense(
                user_id,
                formattedDateAuthorized,
                formattedDateRegisteredServer,
                sucursal_id,
                amount,
                concept,
                exp_description,
                code,
                aplicado,
                saldo,
                fech_uso,
                evidencia,
                cvepro,
                corporativo,
                deducible,
                concept_id
        );

        Call<ObjInsertExpense> call = apiService.insertExpense(expenseData);
        call.enqueue(new Callback<ObjInsertExpense>() {
            @Override
            public void onResponse(Call<ObjInsertExpense> call, Response<ObjInsertExpense> response) {
                if (response.isSuccessful()) {
                    ObjInsertExpense insertedExpense = response.body();

                    if (insertedExpense != null) {
                        String userId = insertedExpense.getUser_id();
                    }
                } else {
                }

            }

            @Override
            public void onFailure(Call<ObjInsertExpense> call, Throwable t) {


                Log.e("Error", t.toString());
            }
        });
    }
    private void fechaDialog(int position, Button fechaGenerarButton) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String selectedDate = dateFormat.format(calendar.getTime());
                fechaGenerarButton.setText(selectedDate);
                fechaGenerarButton.setBackgroundColor(Color.parseColor("#FF8300"));
            }
        };

        new DatePickerDialog(
                context,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

}
