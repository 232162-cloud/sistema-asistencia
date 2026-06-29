package com.unamba.asistencia.dto;

import java.util.Date;

public class AuthResponse {

    private String token;
    private String username;
    private String rol;
    private Date emision;
    private Date expiracion;

    public AuthResponse() {
    }

    public AuthResponse(String token, String username, String rol, Date emision, Date expiracion) {
        this.token = token;
        this.username = username;
        this.rol = rol;
        this.emision = emision;
        this.expiracion = expiracion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Date getEmision() {
        return emision;
    }

    public void setEmision(Date emision) {
        this.emision = emision;
    }

    public Date getExpiracion() {
        return expiracion;
    }

    public void setExpiracion(Date expiracion) {
        this.expiracion = expiracion;
    }
}
