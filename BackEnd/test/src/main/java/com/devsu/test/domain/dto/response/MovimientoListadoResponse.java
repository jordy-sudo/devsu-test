package com.devsu.test.domain.dto.response;

import java.math.BigDecimal;

public class MovimientoListadoResponse {

    private String fecha;
    private String cliente;
    private String numeroCuenta;
    private String tipo;
    private BigDecimal saldoInicial;
    private Boolean estado;
    private BigDecimal movimiento;
    private BigDecimal saldoDisponible;

    public MovimientoListadoResponse(String fecha, String cliente, String numeroCuenta, String tipo,
                                     BigDecimal saldoInicial, Boolean estado, BigDecimal movimiento, BigDecimal saldoDisponible) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.numeroCuenta = numeroCuenta;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.movimiento = movimiento;
        this.saldoDisponible = saldoDisponible;
    }

    public String getFecha() { return fecha; }
    public String getCliente() { return cliente; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTipo() { return tipo; }
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public Boolean getEstado() { return estado; }
    public BigDecimal getMovimiento() { return movimiento; }
    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
}
