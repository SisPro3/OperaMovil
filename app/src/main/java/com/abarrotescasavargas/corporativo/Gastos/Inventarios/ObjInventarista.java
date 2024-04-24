package com.abarrotescasavargas.corporativo.Gastos.Inventarios;

public class ObjInventarista {
    private String nombre;
    private String cveEmpleado;

    public ObjInventarista(String nombre, String cveEmpleado) {
        this.nombre = nombre;
        this.cveEmpleado = cveEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCveEmpleado() {
        return cveEmpleado;
    }

    public void setCveEmpleado(String cveEmpleado) {
        this.cveEmpleado = cveEmpleado;
    }
}
