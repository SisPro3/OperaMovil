package com.abarrotescasavargas.corporativo.Transferencias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.abarrotescasavargas.corporativo.R;

public class ActivityTransferenciasPrincipal extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencias_principal);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Button historicoButton = findViewById(R.id.button4);
        Button nuevaButton = findViewById(R.id.button5);

        historicoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityTransferenciasPrincipal.this, ActivityTransferenciasHistorico.class);
                startActivity(intent);
            }
        });

        nuevaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la pantalla de nueva al hacer clic en el botón "Nueva"

                Intent intent = new Intent(ActivityTransferenciasPrincipal.this, ActivityTransferenciasNuevo.class);
                startActivity(intent);
            }
        });
    }
}
