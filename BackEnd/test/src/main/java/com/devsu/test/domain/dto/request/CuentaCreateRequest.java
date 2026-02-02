package com.devsu.test.domain.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CuentaCreateRequest {

    @NotBlank(message = "numeroCuenta es requerido")
    @Size(max = 30, message = "numeroCuenta máximo 30 caracteres")
    private String numeroCuenta;

    /**
     * Valores esperados: "AHORROS" o "CORRIENTE"
     */
    @NotBlank(message = "tipoCuenta es requerido")
    private String tipoCuenta;

    @NotNull(message = "saldoInicial es requerido")
    @DecimalMin(value = "0.00", inclusive = true, message = "saldoInicial no puede ser negativo")
    private BigDecimal saldoInicial;

    @NotNull(message = "clienteId es requerido")
    private Long clienteId;

    // opcional: si no viene, se asume true
    private Boolean estado;

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
