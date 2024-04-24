package com.abarrotescasavargas.corporativo.Transferencias;

import com.abarrotescasavargas.corporativo.Gastos.ObjConceptoGastos;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseZonaTranferencia {
    @SerializedName("data")
    @Expose

    private ArrayList<ObjSucusalTransferencias> data;

    public ArrayList<ObjSucusalTransferencias> getData() {
        return data;
    }

    public void setData(ArrayList<ObjSucusalTransferencias> conceptDescriptions) {
        this.data = conceptDescriptions;
    }
}
