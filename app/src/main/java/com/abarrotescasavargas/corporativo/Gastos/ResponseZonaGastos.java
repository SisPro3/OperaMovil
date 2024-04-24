package com.abarrotescasavargas.corporativo.Gastos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseZonaGastos {
    @SerializedName("data")
    @Expose

    private ArrayList<ObjZona> data;

    public ArrayList<ObjZona> getData() {
        return data;
    }

    public void setData(ArrayList<ObjZona> conceptDescriptions) {
        this.data = conceptDescriptions;
    }
}
