package com.abarrotescasavargas.corporativo.FunGenerales;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abarrotescasavargas.corporativo.DataBases.DbHelper;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Generales {
    private static final String CARACTERES_PERMITIDOS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private Context context;
    private DbHelper dbHelper;
    private SQLiteDatabase database;
    private static final Map<Character, String> pronunciationMap = new HashMap<>();

    static {
        pronunciationMap.put('A', "A");
        pronunciationMap.put('B', "BE");
        pronunciationMap.put('C', "CE");
        pronunciationMap.put('D', "DE");
        pronunciationMap.put('E', "E");
        pronunciationMap.put('F', "EFE");
        pronunciationMap.put('G', "GE");
        pronunciationMap.put('H', "HACHE");
        pronunciationMap.put('I', "I");
        pronunciationMap.put('J', "JOTA");
        pronunciationMap.put('K', "KA");
        pronunciationMap.put('L', "ELE");
        pronunciationMap.put('M', "EME");
        pronunciationMap.put('N', "ENE");
        pronunciationMap.put('O', "O");
        pronunciationMap.put('P', "PE");
        pronunciationMap.put('Q', "CU");
        pronunciationMap.put('R', "ERRE");
        pronunciationMap.put('S', "ESE");
        pronunciationMap.put('T', "TE");
        pronunciationMap.put('U', "U");
        pronunciationMap.put('V', "UVE");
        pronunciationMap.put('W', "DOBLEU");
        pronunciationMap.put('X', "EQUIS");
        pronunciationMap.put('Y', "YE");
        pronunciationMap.put('Z', "ZETA");
        pronunciationMap.put('0', "CERO");
        pronunciationMap.put('1', "UNO");
        pronunciationMap.put('2', "DOS");
        pronunciationMap.put('3', "TRES");
        pronunciationMap.put('4', "CUATRO");
        pronunciationMap.put('5', "CINCO");
        pronunciationMap.put('6', "SEIS");
        pronunciationMap.put('7', "SIETE");
        pronunciationMap.put('8', "OCHO");
        pronunciationMap.put('9', "NUEVE");
    }

    public Generales(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public static String generarStringAleatorio(int longitud) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            int index = random.nextInt(CARACTERES_PERMITIDOS.length());
            char caracter = CARACTERES_PERMITIDOS.charAt(index);
            sb.append(caracter);
        }

        return sb.toString();
    }

    public static String convertToPronunciation(String input) {
        StringBuilder pronunciation = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (pronunciationMap.containsKey(c)) {
                if (pronunciation.length() > 0) {
                    pronunciation.append(" - ");
                }
                pronunciation.append(pronunciationMap.get(c));
            }
        }
        return pronunciation.toString();
    }


    public Cursor ejecutarConsulta(String query) {
        return database.rawQuery(query, null);
    }

    public String getUsuario() {
        String valorUS_USUARI = null;
        open();

        String consulta = "SELECT US_USUARI FROM USUARIO LIMIT 1;";
        Cursor cursor = ejecutarConsulta(consulta);

        if (cursor.moveToFirst()) {
            valorUS_USUARI = cursor.getString(0);
        }

        close();

        return valorUS_USUARI;
    }
}
