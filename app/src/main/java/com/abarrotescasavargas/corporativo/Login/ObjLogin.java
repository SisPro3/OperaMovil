package com.abarrotescasavargas.corporativo.Login;

import com.google.gson.annotations.SerializedName;

public class ObjLogin {
    @SerializedName("status")
    private boolean status;
    @SerializedName("user")
    private String user;
    @SerializedName("ku_usuari")
    private String ku_usuari;
    @SerializedName("role")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKu_usuari() {
        return ku_usuari;
    }

    public void setKu_usuari(String ku_usuari) {
        this.ku_usuari = ku_usuari;
    }
}
