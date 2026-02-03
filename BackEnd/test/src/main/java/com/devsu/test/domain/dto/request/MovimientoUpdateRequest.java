package com.devsu.test.domain.dto.request;

import jakarta.validation.constraints.NotNull;

public class MovimientoUpdateRequest {

    @NotNull(message = "estado es requerido")
    private Boolean estado;

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
