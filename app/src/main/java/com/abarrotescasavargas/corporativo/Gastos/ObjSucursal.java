package com.abarrotescasavargas.corporativo.Gastos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjSucursal implements Parcelable {
    @SerializedName("idRONDINES")
    @Expose
    private int idRONDINES;

    @SerializedName("RO_CVESUC")
    @Expose
    private String RO_CVESUC;

    @SerializedName("RO_MONTOS")
    @Expose
    private String RO_MONTOS;

    @SerializedName("Sucursal")
    @Expose
    private String Sucursal;

    @SerializedName("KS_ZONAOP")
    @Expose
    private String KS_ZONAOP;

    @SerializedName("KS_COLORS")
    @Expose
    private String KS_COLORS;

    public ObjSucursal() {
        // Default constructor
    }

    protected ObjSucursal(Parcel in) {
        idRONDINES = in.readInt();
        RO_CVESUC = in.readString();
        RO_MONTOS = in.readString();
        Sucursal = in.readString();
        KS_ZONAOP = in.readString();
        KS_COLORS = in.readString();
    }

    public int getIdRONDINES() {
        return idRONDINES;
    }

    public String getRO_CVESUC() {
        return RO_CVESUC;
    }

    public String getRO_MONTOS() {
        return RO_MONTOS;
    }

    public String getSucursal() {
        return Sucursal;
    }

    public String getKS_ZONAOP() {
        return KS_ZONAOP;
    }

    public String getKS_COLORS() {
        return KS_COLORS;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idRONDINES);
        dest.writeString(RO_CVESUC);
        dest.writeString(RO_MONTOS);
        dest.writeString(Sucursal);
        dest.writeString(KS_ZONAOP);
        dest.writeString(KS_COLORS);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ObjSucursal> CREATOR = new Parcelable.Creator<ObjSucursal>() {
        @Override
        public ObjSucursal createFromParcel(Parcel in) {
            return new ObjSucursal(in);
        }

        @Override
        public ObjSucursal[] newArray(int size) {
            return new ObjSucursal[size];
        }
    };
}
