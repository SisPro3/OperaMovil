package com.abarrotescasavargas.corporativo.Gastos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseSucursalGastos {
    @SerializedName("data")
    @Expose

    private ArrayList<ObjConceptoGastos> data;

    public ArrayList<ObjConceptoGastos> getData() {
        return data;
    }

    public void setData(ArrayList<ObjConceptoGastos> conceptDescriptions) {
        this.data = conceptDescriptions;
    }
}
