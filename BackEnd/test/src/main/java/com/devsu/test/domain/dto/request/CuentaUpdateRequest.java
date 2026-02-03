package com.devsu.test.domain.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CuentaUpdateRequest {

    @NotBlank(message = "tipoCuenta es requerido (AHORROS o CORRIENTE)")
    private String tipoCuenta;

    @NotNull(message = "saldoInicial es requerido")
    @DecimalMin(value = "0.00", inclusive = true, message = "saldoInicial no puede ser negativo")
    private BigDecimal saldoInicial;

    @NotNull(message = "estado es requerido")
    private Boolean estado;

    private Long clienteId;

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}
