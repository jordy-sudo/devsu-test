package com.devsu.test.domain.dto.response;

import java.math.BigDecimal;

public class MovimientoResponse {

    private Long id;
    private String fecha;
    private String cliente;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private Boolean estadoCuenta;
    private BigDecimal movimiento; // valor (deposito + / retiro -)
    private BigDecimal saldoDisponible;

    public MovimientoResponse(Long id, String fecha, String cliente, String numeroCuenta,
                              String tipoCuenta, BigDecimal saldoInicial, Boolean estadoCuenta,
                              BigDecimal movimiento, BigDecimal saldoDisponible) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.estadoCuenta = estadoCuenta;
        this.movimiento = movimiento;
        this.saldoDisponible = saldoDisponible;
    }

    public Long getId() { return id; }
    public String getFecha() { return fecha; }
    public String getCliente() { return cliente; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTipoCuenta() { return tipoCuenta; }
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public Boolean getEstadoCuenta() { return estadoCuenta; }
    public BigDecimal getMovimiento() { return movimiento; }
    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
}
