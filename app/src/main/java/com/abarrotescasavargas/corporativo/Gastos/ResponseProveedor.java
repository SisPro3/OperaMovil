package com.abarrotescasavargas.corporativo.Gastos;

import com.abarrotescasavargas.corporativo.Gastos.Inventarios.ObjProveedores;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseProveedor {
    @SerializedName("data")
    @Expose

    private ArrayList<ObjProveedores> data;

    public ArrayList<ObjProveedores> getData() {
        return data;
    }

    public void setData(ArrayList<ObjProveedores> conceptDescriptions) {
        this.data = conceptDescriptions;
    }
}
