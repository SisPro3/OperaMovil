package com.abarrotescasavargas.corporativo.DataBases;

import android.provider.BaseColumns;

public class OperaMovilContract {
    private OperaMovilContract() {
    }
    public static class USUARIO implements BaseColumns {
        public static final String Table = "USUARIO";
        public static final String US_USUARI = "US_USUARI";
        public static final String US_PERFIL = "US_PERFIL";
        public static final  String US_RECUER ="US_RECUER";
        public static final String US_CVEUSR = "US_CVEUSR";
    }

}
