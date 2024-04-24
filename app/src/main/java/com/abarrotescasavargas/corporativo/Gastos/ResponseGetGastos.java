package com.abarrotescasavargas.corporativo.Gastos;

public class ResponseGetGastos {
    private String Usuario;
    private String UsuarioInsert;
    private String Sucursal;
    private String Autorizado;
    private String Monto;
    private String Saldo;
    private String Codigo;

    public String getUsuarioInsert() {
        return UsuarioInsert;
    }

    public void setUsuarioInsert(String usuarioInsert) {
        UsuarioInsert = usuarioInsert;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getSucursal() {
        return Sucursal;
    }

    public void setSucursal(String sucursal) {
        Sucursal = sucursal;
    }

    public String getAutorizado() {
        return Autorizado;
    }

    public void setAutorizado(String autorizado) {
        Autorizado = autorizado;
    }

    public String getMonto() {
        return Monto;
    }

    public void setMonto(String monto) {
        Monto = monto;
    }

    public String getSaldo() {
        return Saldo;
    }

    public void setSaldo(String saldo) {
        Saldo = saldo;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }
}
