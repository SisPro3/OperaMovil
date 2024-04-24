package com.abarrotescasavargas.corporativo.Gastos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjZona implements Parcelable {
    @SerializedName("KS_COLORS")
    @Expose
    private String ksColors;

    @SerializedName("KS_ZONAOP")
    @Expose
    private String ksZonaOp;

    public ObjZona() {
        // Constructor vacío
    }

    public String getKsColors() {
        return ksColors;
    }

    public void setKsColors(String ksColors) {
        this.ksColors = ksColors;
    }

    public String getKsZonaOp() {
        return ksZonaOp;
    }

    public void setKsZonaOp(String ksZonaOp) {
        this.ksZonaOp = ksZonaOp;
    }

    protected ObjZona(Parcel in) {
        ksColors = in.readString();
        ksZonaOp = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ksColors);
        dest.writeString(ksZonaOp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ObjZona> CREATOR = new Parcelable.Creator<ObjZona>() {
        @Override
        public ObjZona createFromParcel(Parcel in) {
            return new ObjZona(in);
        }

        @Override
        public ObjZona[] newArray(int size) {
            return new ObjZona[size];
        }
    };
}
