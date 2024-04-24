package com.abarrotescasavargas.corporativo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;


import com.abarrotescasavargas.corporativo.DataBases.DbHelper;
import com.abarrotescasavargas.corporativo.FunGenerales.Dialogo;
import com.abarrotescasavargas.corporativo.FunGenerales.PreLoader;
import com.abarrotescasavargas.corporativo.Gastos.ActivityGastos;
import com.abarrotescasavargas.corporativo.Gastos.Inventarios.ActivityGastosInventarios;
import com.abarrotescasavargas.corporativo.Login.LoginRepository;
import com.abarrotescasavargas.corporativo.Login.ObjLogin;
import com.abarrotescasavargas.corporativo.Login.PeticionLogin;
import com.abarrotescasavargas.corporativo.Login.RetrofitLogin;
import com.abarrotescasavargas.corporativo.Restablecer.ActivityRestablecer;
import com.abarrotescasavargas.corporativo.Transferencias.ActivityTransferenciasPrincipal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Principal extends AppCompatActivity {
    Button buttonLogin;
    TextView usuario, restablece;
    EditText password;
    PreLoader.ProgressDialogFragment progressDialogFragment;
    private boolean isPasswordVisible = false;
    private ImageButton showHidePasswordButton;
    CheckBox recuerdae;
    private static DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setup();
        events();
    }

    private void setup() {
        LoginRepository consultasDB = new LoginRepository(this);
        boolean valorRecuerda = consultasDB.getRecuerdame();
        consultasDB.cerrarConexion();
        if (valorRecuerda==true){
            finish();
            Intent intent = new Intent(Principal.this, MainActivity.class);
            startActivity(intent);
        }

        dbHelper = new DbHelper(this);
        dbHelper.getWritableDatabase();

        buttonLogin = findViewById(R.id.buttonLogin);
        usuario = findViewById(R.id.usuario);
        password = findViewById(R.id.contrasenia);
        showHidePasswordButton = findViewById(R.id.toggleButtonShowPassword);
        restablece= findViewById(R.id.textViewResetPassword);
        recuerdae= findViewById(R.id.checkBox);

    }
    private void events() {

        restablece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogo.creaPopUp(Principal.this,"Alerta","Modulo en mantenimiento, espere actualizaciones","Ok");
//                Intent intent = new Intent(Principal.this, ActivityRestablecer.class);
//                startActivity(intent);
//                finish();
            }
        });
        showHidePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameText = usuario.getText().toString();
                final String passwordText = password.getText().toString();
                final String recuerdaUsuario = recuerdae.isChecked() ? "true" : "false";
                progressDialogFragment = new PreLoader.ProgressDialogFragment();
                progressDialogFragment.show(getSupportFragmentManager(), "ProgressDialog");

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Retrofit retrofit = new RetrofitLogin().getRuta();
                        PeticionLogin peticionTransferencias = retrofit.create(PeticionLogin.class);
                        Call<ObjLogin> call = peticionTransferencias.getAcceso(usernameText, passwordText);

                        call.enqueue(new Callback<ObjLogin>() {
                            @Override
                            public void onResponse(Call<ObjLogin> call, Response<ObjLogin> response) {
                                if (response.isSuccessful()) {
                                    ObjLogin objLogin = response.body();
                                    if (objLogin != null && objLogin.isStatus()) {
                                        LoginRepository.insertarUsuario(objLogin.getUser(), recuerdaUsuario, objLogin.getKu_usuari(), objLogin.getRole(), Principal.this);
                                        finish();
                                        Intent intent = new Intent(Principal.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        progressDialogFragment.dismiss();
                                        Toast.makeText(Principal.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(Principal.this, ActivityGastosInventarios.class);
//                                        startActivity(intent);
                                    }
                                } else {
                                    progressDialogFragment.dismiss();
                                    Toast.makeText(Principal.this, "Ocurrio un error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ObjLogin> call, Throwable t) {
                                progressDialogFragment.dismiss();

                                Toast.makeText(Principal.this, "Ocurrio un error", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }, 2000);
            }
        });


    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Si la contraseña está visible, ocultarla
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showHidePasswordButton.setImageResource(R.drawable.eye_password);
            showHidePasswordButton.setContentDescription("Mostrar contraseña");
        } else {
            // Si la contraseña está oculta, mostrarla
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showHidePasswordButton.setImageResource(R.drawable.eye_password_closed);
            showHidePasswordButton.setContentDescription("Ocultar contraseña");
        }
        password.setSelection(password.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

}
