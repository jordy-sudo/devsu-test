package com.devsu.test.domain.dto.response;

import java.math.BigDecimal;

public record CuentaResponse(
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial,
        Boolean estado,
        Long clienteId
) { }
