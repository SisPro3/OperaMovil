package com.abarrotescasavargas.corporativo.FunGenerales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.abarrotescasavargas.corporativo.R;

public class MenuNuevaHistorico extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_nuava_historico);

        Context contexto =  MenuNuevaHistorico.this;
        int backgroundResourceId = getIntent().getIntExtra("backgroundResource", R.color.acvblack);
        String textoBotonNuevo = getIntent().getStringExtra("textoBotonNuevo");
        String textoBotonHistorico = getIntent().getStringExtra("textoBotonHistorico");
        String mantenimiento = getIntent().getStringExtra("mantenimiento");

        // Configurar el fondo del ConstraintLayout
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutNuevoHistorico);
        constraintLayout.setBackgroundResource(backgroundResourceId);

        // Configurar el texto de los botones
        Button nuevoButton = findViewById(R.id.nuevo);
        Button historicoButton = findViewById(R.id.historico);

        nuevoButton.setText(textoBotonNuevo);
        historicoButton.setText(textoBotonHistorico);

        // Obtener las acciones como parámetros
        final Intent nuevoButtonAction = getIntent().getParcelableExtra("nuevoButtonAction");
        final Intent historicoButtonAction = getIntent().getParcelableExtra("historicoButtonAction");

        // Configurar las acciones de los botones
        nuevoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nuevoButtonAction != null) {
                    startActivity(nuevoButtonAction); // Iniciar la actividad correspondiente
                }
            }
        });

        historicoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (historicoButtonAction != null && !mantenimiento.equals("mantenimiento")) {
                    startActivity(historicoButtonAction); // Iniciar la actividad correspondiente
                }
                else {
                    Dialogo.creaPopUp(contexto,"Alerta","Modulo en mantenimiento, espere actualizaciones.","OK");
                }
            }
        });
    }
}
