package com.abarrotescasavargas.corporativo.Transferencias;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.abarrotescasavargas.corporativo.DataBases.DbHelper;

public class TransferenciasRepository {
    private Context context;
    private DbHelper dbHelper;
    private SQLiteDatabase database;

    public TransferenciasRepository(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Cursor ejecutarConsulta(String query) {
        return database.rawQuery(query, null);
    }

    public String getUsuario() {
        String valorUS_USUARI = null;  // Variable para almacenar el valor de US_USUARI
        open();

        String consulta = "SELECT US_USUARI FROM USUARIO LIMIT 1;";
        Cursor cursor = ejecutarConsulta(consulta);

        if (cursor.moveToFirst()) {
            valorUS_USUARI = cursor.getString(0);
        }

        close();

        return valorUS_USUARI;
    }
    public String getKU_Usuario() {
        String valorUS_USUARI = null;  // Variable para almacenar el valor de US_USUARI
        open();

        String consulta = "SELECT US_CVEUSR FROM USUARIO LIMIT 1;";
        Cursor cursor = ejecutarConsulta(consulta);

        if (cursor.moveToFirst()) {
            valorUS_USUARI = cursor.getString(0);
        }

        close();

        return valorUS_USUARI;
    }
}
