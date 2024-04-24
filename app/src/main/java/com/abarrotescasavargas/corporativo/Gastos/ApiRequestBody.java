package com.abarrotescasavargas.corporativo.Gastos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiRequestBody {
    @SerializedName("sucursal")
    private List<ObjInsertExpense> sucursal1;

    public List<ObjInsertExpense> getSucursal1() {
        return sucursal1;
    }

    public void setSucursal1(List<ObjInsertExpense> sucursal1) {
        this.sucursal1 = sucursal1;
    }
}