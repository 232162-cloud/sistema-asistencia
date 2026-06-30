package com.unamba.asistencia.dto;

public class DocenteRequest {

    private String username;
    private String password;
    private String nombreCompleto;

    public DocenteRequest() {
    }

    public DocenteRequest(String username, String password, String nombreCompleto) {
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
}
