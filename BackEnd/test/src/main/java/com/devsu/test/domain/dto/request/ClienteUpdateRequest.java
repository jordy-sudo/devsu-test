package com.devsu.test.domain.dto.request;

import jakarta.validation.constraints.NotNull;

public class ClienteUpdateRequest {

    @NotNull(message = "estado es requerido")
    private Boolean estado;

    private String contrasena;

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
