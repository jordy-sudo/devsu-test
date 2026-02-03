package com.devsu.test.domain.dto.response;

import java.math.BigDecimal;

public record MovimientoDetailResponse(
        Long id,
        String fecha,
        String tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldoDisponible,
        Boolean estado,
        String numeroCuenta
) { }
