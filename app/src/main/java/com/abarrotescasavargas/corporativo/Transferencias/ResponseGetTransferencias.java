package com.abarrotescasavargas.corporativo.Transferencias;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ResponseGetTransferencias {
        private String amount;
        private String saldo;
        private String aplicado;
        private String date_authorized;
        private String code;
        private String KS_NOMSUC;
        private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getSaldo() {
            return saldo;
        }

        public void setSaldo(String saldo) {
            this.saldo = saldo;
        }

        public String getAplicado() {
            return aplicado;
        }

        public void setAplicado(String aplicado) {
            this.aplicado = aplicado;
        }

        public String getDate_authorized() {
            return date_authorized;
        }

        public void setDate_authorized(String date_authorized) {
            this.date_authorized = date_authorized;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getKS_NOMSUC() {
            return KS_NOMSUC;
        }

        public void setKS_NOMSUC(String KS_NOMSUC) {
            this.KS_NOMSUC = KS_NOMSUC;
        }
    }

