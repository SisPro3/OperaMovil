package com.abarrotescasavargas.corporativo.DataBases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3   ;
    private static final String DATABASE_NOMBRE = "OperaMovil.db";
    Context context;
    static String USUARIO = "CREATE TABLE " + OperaMovilContract.USUARIO.Table + " (" +
            OperaMovilContract.USUARIO._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            OperaMovilContract.USUARIO.US_USUARI + " TEXT NOT NULL," +
            OperaMovilContract.USUARIO.US_RECUER + " TEXT NOT NULL," +
            OperaMovilContract.USUARIO.US_CVEUSR + " TEXT NOT NULL," +
            OperaMovilContract.USUARIO.US_PERFIL + " TEXT NOT NULL); ";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(USUARIO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + OperaMovilContract.USUARIO.Table);

        onCreate(db);
    }
}
