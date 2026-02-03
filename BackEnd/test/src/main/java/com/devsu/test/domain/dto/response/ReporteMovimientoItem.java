package com.devsu.test.domain.dto.response;

import java.math.BigDecimal;

public record ReporteMovimientoItem(
        Long id,
        String fecha,
        String numeroCuenta,
        String tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo
) { }
