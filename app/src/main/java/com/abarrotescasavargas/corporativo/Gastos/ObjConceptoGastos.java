package com.abarrotescasavargas.corporativo.Gastos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjConceptoGastos implements Parcelable {
    @SerializedName("id_concept")
    @Expose
    private String id_concept;

    @SerializedName("description")
    @Expose
    private String description;

    public ObjConceptoGastos() {
        // Constructor vacío
    }

    public String getIdConcept() {
        return id_concept;
    }

    public void setIdConcept(String idConcept) {
        this.id_concept = idConcept;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected ObjConceptoGastos(Parcel in) {
        id_concept = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_concept);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ObjConceptoGastos> CREATOR = new Creator<ObjConceptoGastos>() {
        @Override
        public ObjConceptoGastos createFromParcel(Parcel in) {
            return new ObjConceptoGastos(in);
        }

        @Override
        public ObjConceptoGastos[] newArray(int size) {
            return new ObjConceptoGastos[size];
        }
    };
}
