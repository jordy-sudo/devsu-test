package com.devsu.test.domain.dto.response;

import java.math.BigDecimal;

public record ReporteCuentaItem(
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        Boolean estado
) { }
