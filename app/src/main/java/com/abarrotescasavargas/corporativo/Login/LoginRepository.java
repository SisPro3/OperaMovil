package com.abarrotescasavargas.corporativo.Login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abarrotescasavargas.corporativo.DataBases.DbHelper;
import com.abarrotescasavargas.corporativo.DataBases.OperaMovilContract;

public class LoginRepository {
    private static DbHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public LoginRepository(Context context) {
        dbHelper = new DbHelper(context);
    }
    public Cursor ejecutarConsulta(String query) {
        return database.rawQuery(query, null);
    }

    public String getPerfil(String campo) {
        String valorUS_USUARI = null;
        open();

        String consulta = "SELECT "+campo+" FROM USUARIO LIMIT 1;";
        Cursor cursor = ejecutarConsulta(consulta);

        if (cursor.moveToFirst()) {
            valorUS_USUARI = cursor.getString(0);
        }

        close();

        return valorUS_USUARI;
    }

    public static void insertarUsuario(String usuario, String recuerda, String cveUsuario, String perfil, Context context) {

        dbHelper = new DbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(OperaMovilContract.USUARIO.Table, null, null);

        ContentValues values = new ContentValues();
        values.put(OperaMovilContract.USUARIO.US_USUARI, usuario);
        values.put(OperaMovilContract.USUARIO.US_RECUER, recuerda);
        values.put(OperaMovilContract.USUARIO.US_CVEUSR, cveUsuario);
        values.put(OperaMovilContract.USUARIO.US_PERFIL, perfil);
        db.insert(OperaMovilContract.USUARIO.Table, null, values);

        db.close();
    }
    public boolean getRecuerdame() {
        boolean valorRecuerda = false;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columnas = {OperaMovilContract.USUARIO.US_RECUER};
        Cursor cursor = db.query(OperaMovilContract.USUARIO.Table, columnas, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(OperaMovilContract.USUARIO.US_RECUER);
            if (columnIndex != -1) {
                String recuerdaString = cursor.getString(columnIndex);
                valorRecuerda = recuerdaString.equals("true");
            }
            cursor.close();
        }

        return valorRecuerda;
    }




    public void cerrarConexion() {
        dbHelper.close();
    }
}
