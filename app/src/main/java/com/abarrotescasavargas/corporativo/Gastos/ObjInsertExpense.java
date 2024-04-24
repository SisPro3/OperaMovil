package com.abarrotescasavargas.corporativo.Gastos;

import org.json.JSONArray;

import java.util.Date;

public class ObjInsertExpense {

    private String user_id;
    private String date_authorized;
    private String date_registered_server;
    private String sucursal_id;
    private double amount;
    private String concept;
    private String exp_description;
    private String code;
    private int aplicado;
    private double saldo;
    private Date fech_uso;
    private String cvepro;
    private int evidencia;
    private int corporativo;
    private int deducible;
    private int concept_id;
    public ObjInsertExpense() {

    }
    public ObjInsertExpense(String user_id, String date_authorized, String date_registered_server, String sucursal_id,
                            double amount, String concept, String exp_description, String code, int aplicado,
                            double saldo, Date fech_uso, int evidencia, String cvepro,int corporativo, int deducible, int concept_id ) {
        this.user_id = user_id;
        this.date_authorized = date_authorized;
        this.date_registered_server = date_registered_server;
        this.sucursal_id = sucursal_id;
        this.amount = amount;
        this.concept = concept;
        this.exp_description = exp_description;
        this.code = code;
        this.aplicado = aplicado;
        this.saldo = saldo;
        this.fech_uso = fech_uso;
        this.cvepro = cvepro;
        this.evidencia = evidencia;
        this.corporativo = corporativo;
        this.deducible = deducible;
        this.concept_id = concept_id;
    }

    public ObjInsertExpense(JSONArray jsonArray) {

    }

    public String getCvepro() {
        return cvepro;
    }

    public void setCvepro(String cvepro) {
        this.cvepro = cvepro;
    }

    public int getCorporativo() {
        return corporativo;
    }

    public void setCorporativo(int corporativo) {
        this.corporativo = corporativo;
    }

    public int getDeducible() {
        return deducible;
    }

    public void setDeducible(int deducible) {
        this.deducible = deducible;
    }

    public int getConcept_id() {
        return concept_id;
    }

    public void setConcept_id(int concept_id) {
        this.concept_id = concept_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate_authorized() {
        return date_authorized;
    }

    public void setDate_authorized(String date_authorized) {
        this.date_authorized = date_authorized;
    }

    public String getDate_registered_server() {
        return date_registered_server;
    }

    public void setDate_registered_server(String date_registered_server) {
        this.date_registered_server = date_registered_server;
    }

    public String getSucursal_id() {
        return sucursal_id;
    }

    public void setSucursal_id(String sucursal_id) {
        this.sucursal_id = sucursal_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getExp_description() {
        return exp_description;
    }

    public void setExp_description(String exp_description) {
        this.exp_description = exp_description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getAplicado() {
        return aplicado;
    }

    public void setAplicado(int aplicado) {
        this.aplicado = aplicado;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Date getFech_uso() {
        return fech_uso;
    }

    public void setFech_uso(Date fech_uso) {
        this.fech_uso = fech_uso;
    }

    public int getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(int evidencia) {
        this.evidencia = evidencia;
    }
}
