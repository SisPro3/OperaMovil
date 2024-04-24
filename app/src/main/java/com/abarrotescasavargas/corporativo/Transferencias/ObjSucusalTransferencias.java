package com.abarrotescasavargas.corporativo.Transferencias;

import com.google.gson.annotations.SerializedName;

public class ObjSucusalTransferencias {
    @SerializedName("Sucursal")
    private String Sucursal;
    @SerializedName("id_sucursal")
    private String id_sucursal;

    public String getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(String id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public String getSucursal() {
        return Sucursal;
    }

    public void setSucursal(String Sucursal) {
        this.Sucursal = Sucursal;
    }

}
