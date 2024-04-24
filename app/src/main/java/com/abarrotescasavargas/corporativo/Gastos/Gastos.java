package com.abarrotescasavargas.corporativo.Gastos;

public class Gastos {
    private String sucursal,color,idRondines;
    private Double monto;
    private boolean checkBoxChecked;
    private boolean selected;
    private boolean corporativo, deducible;

    public Gastos(String sucursal, Double monto, boolean checkBoxChecked,boolean selected, String color,String idRondines) {
        this.sucursal = sucursal;
        this.monto = monto;
        this.checkBoxChecked = checkBoxChecked;
        this.selected = selected;
        this.color = color;
        this.idRondines =idRondines;
        this.deducible=false;
        this.corporativo=false;
    }
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public String getColor() {
        return color;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setCheckBoxChecked(boolean checkBoxChecked) {
        this.checkBoxChecked = checkBoxChecked;
    }

    public Double getMonto() {
        return monto;
    }

    public boolean isCheckBoxChecked() {
        return checkBoxChecked;
    }

    public String getIdRondines() {
        return idRondines;
    }

    public void setIdRondines(String idRondines) {
        this.idRondines = idRondines;
    }

    public void setCorporativo(boolean corporativo) {
        this.corporativo = corporativo;
    }

    public void setDeducible(boolean deducible) {
        this.deducible = deducible;
    }

    public boolean isCorporativo() {
        return corporativo;
    }

    public boolean isDeducible() {
        return deducible;
    }
}
