package com.abarrotescasavargas.corporativo.Gastos;

import com.google.gson.annotations.SerializedName;

public class ObjSucursalByZona {
    @SerializedName("idRONDINES")
    private String idRONDINES;

    @SerializedName("RO_CVESUC")
    private String RO_CVESUC;

    @SerializedName("RO_MONTOS")
    private String RO_MONTOS;

    @SerializedName("Sucursal")
    private String Sucursal;

    @SerializedName("KS_ZONAOP")
    private String KS_ZONAOP;

    @SerializedName("KS_COLORS")
    private String KS_COLORS;

    public String getIdRONDINES() {
        return idRONDINES;
    }

    public void setIdRONDINES(String idRONDINES) {
        this.idRONDINES = idRONDINES;
    }

    public String getRO_CVESUC() {
        return RO_CVESUC;
    }

    public void setRO_CVESUC(String RO_CVESUC) {
        this.RO_CVESUC = RO_CVESUC;
    }

    public String getRO_MONTOS() {
        return RO_MONTOS;
    }

    public void setRO_MONTOS(String RO_MONTOS) {
        this.RO_MONTOS = RO_MONTOS;
    }

    public String getSucursal() {
        return Sucursal;
    }

    public void setSucursal(String sucursal) {
        Sucursal = sucursal;
    }

    public String getKS_ZONAOP() {
        return KS_ZONAOP;
    }

    public void setKS_ZONAOP(String KS_ZONAOP) {
        this.KS_ZONAOP = KS_ZONAOP;
    }

    public String getKS_COLORS() {
        return KS_COLORS;
    }

    public void setKS_COLORS(String KS_COLORS) {
        this.KS_COLORS = KS_COLORS;
    }
}
