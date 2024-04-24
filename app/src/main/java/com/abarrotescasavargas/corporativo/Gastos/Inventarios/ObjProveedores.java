package com.abarrotescasavargas.corporativo.Gastos.Inventarios;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjProveedores implements Parcelable {
    @SerializedName("Proveedor")
    @Expose
    private String Proveedor;

    public ObjProveedores() {
        // Constructor vacío
    }

    public String getProveedor() {
        return Proveedor;
    }

    public void setProveedor(String proveedor) {
        this.Proveedor = proveedor;
    }

    protected ObjProveedores(Parcel in) {
        Proveedor = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Proveedor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ObjProveedores> CREATOR = new Creator<ObjProveedores>() {
        @Override
        public ObjProveedores createFromParcel(Parcel in) {
            return new ObjProveedores(in);
        }

        @Override
        public ObjProveedores[] newArray(int size) {
            return new ObjProveedores[size];
        }
    };
}
