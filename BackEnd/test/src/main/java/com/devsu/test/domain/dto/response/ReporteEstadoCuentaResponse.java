package com.devsu.test.domain.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ReporteEstadoCuentaResponse(
        String cliente,
        String identificacion,
        String desde,
        String hasta,

        List<ReporteCuentaItem> cuentas,
        BigDecimal totalCreditos,
        BigDecimal totalDebitos,

        List<ReporteMovimientoItem> movimientos,

        String pdfBase64
) { }
