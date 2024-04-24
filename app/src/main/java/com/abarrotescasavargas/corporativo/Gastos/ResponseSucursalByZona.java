package com.abarrotescasavargas.corporativo.Gastos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseSucursalByZona {
    @SerializedName("data")
    @Expose

    private ArrayList<ObjSucursalByZona> data;

    public ArrayList<ObjSucursalByZona> getData() {
        return data;
    }

    public void setData(ArrayList<ObjSucursalByZona> conceptDescriptions) {
        this.data = conceptDescriptions;
    }

}
