package com.devsu.test.domain.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class MovimientoCreateRequest {

    @NotBlank(message = "numeroCuenta es requerido")
    private String numeroCuenta;

    /**
     * "DEPOSITO" o "RETIRO"
     */
    @NotBlank(message = "tipoMovimiento es requerido")
    private String tipoMovimiento;

    @NotNull(message = "valor es requerido")
    @DecimalMin(value = "0.01", inclusive = true, message = "valor debe ser mayor a 0")
    private BigDecimal valor;

    // opcional, si no se envía se usa fecha actual
    private String fecha; // "YYYY-MM-DD"

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
