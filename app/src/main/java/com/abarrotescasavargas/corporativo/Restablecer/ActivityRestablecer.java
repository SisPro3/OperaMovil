package com.abarrotescasavargas.corporativo.Restablecer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.abarrotescasavargas.corporativo.FunGenerales.Dialogo;
import com.abarrotescasavargas.corporativo.FunGenerales.ExpresionesRegulares;
import com.abarrotescasavargas.corporativo.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityRestablecer extends AppCompatActivity {

    private EditText codigo, usuario;
    private Button enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasenia);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setup();
        events();
    }

    private void setup() {
        enviar = findViewById(R.id.buttonEnviar);
        codigo = findViewById(R.id.codigo);
        usuario = findViewById(R.id.usuarioReset);

        codigo.setEnabled(false);

        usuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                codigo.setEnabled(!editable.toString().isEmpty());
            }
        });
    }

    private void events() {
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigoText = codigo.getText().toString();
                String usuarioText = usuario.getText().toString();

                if (!usuarioText.isEmpty() && ExpresionesRegulares.validaCorreo(codigoText)) {
                    // Realizar la acción de restablecimiento de contraseña

                } else {
                    // Mostrar un mensaje de error si el campo código no es un correo válido
                    if (!ExpresionesRegulares.validaCorreo(codigoText)) {
                        Dialogo.creaPopUp(ActivityRestablecer.this,"Alerta","Ingrese un correo electronico valido","OK");

                    } else {
                        // Mostrar otro mensaje de error o realizar alguna otra acción de manejo de errores
                    }
                }
            }
        });
    }
}
