package com.devsu.test.services;

import com.devsu.test.domain.dto.response.ReporteCuentaItem;
import com.devsu.test.domain.dto.response.ReporteEstadoCuentaResponse;
import com.devsu.test.domain.dto.response.ReporteMovimientoItem;
import com.devsu.test.domain.entities.Cuenta;
import com.devsu.test.domain.entities.Movimiento;
import com.devsu.test.exceptions.BusinessException;
import com.devsu.test.repositories.ClienteRepository;
import com.devsu.test.repositories.CuentaRepository;
import com.devsu.test.repositories.MovimientoRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.Base64;
import java.util.List;

@Service
public class ReporteService {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public ReporteService(ClienteRepository clienteRepository,
                          CuentaRepository cuentaRepository,
                          MovimientoRepository movimientoRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional(readOnly = true)
    public ReporteEstadoCuentaResponse generarEstadoCuenta(Long clienteId, LocalDate desde, LocalDate hasta) {

        if (clienteId == null) {
            throw new BusinessException("clienteId es obligatorio");
        }
        if (desde == null || hasta == null) {
            throw new BusinessException("desde y hasta son obligatorios");
        }
        if (desde.isAfter(hasta)) {
            throw new BusinessException("Rango de fechas inválido: 'desde' no puede ser mayor que 'hasta'");
        }

        var cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new BusinessException("Cliente no existe"));

        var persona = cliente.getPersona();

        // 1) Cuentas del cliente
        List<Cuenta> cuentas = cuentaRepository.findByCliente_Id(clienteId);

        // 2) Movimientos del cliente en el rango (q = null)
        List<Movimiento> movimientos = movimientoRepository.findByClienteRangoYQuery(clienteId, desde, hasta, null);

        // 3) Totales (en rango)
        BigDecimal totalCreditos = BigDecimal.ZERO;
        BigDecimal totalDebitos = BigDecimal.ZERO;

        for (Movimiento m : movimientos) {
            if (m.getValor() == null) continue;

            if (m.getValor().compareTo(BigDecimal.ZERO) > 0) {
                totalCreditos = totalCreditos.add(m.getValor());
            } else {
                totalDebitos = totalDebitos.add(m.getValor().abs());
            }
        }

        // 4) Saldo final por cuenta (según último movimiento del rango; si no hay, saldoInicial)
        Map<String, BigDecimal> saldoFinalPorCuenta = new HashMap<>();
        Map<String, Movimiento> ultimoMovPorCuenta = new HashMap<>();

        for (Movimiento m : movimientos) {
            if (m.getCuenta() == null) continue;
            String num = m.getCuenta().getNumeroCuenta();
            if (num == null) continue;
            ultimoMovPorCuenta.put(num, m);
        }

        for (Cuenta c : cuentas) {
            if (c.getNumeroCuenta() == null) continue;
            Movimiento last = ultimoMovPorCuenta.get(c.getNumeroCuenta());
            saldoFinalPorCuenta.put(
                    c.getNumeroCuenta(),
                    (last != null && last.getSaldo() != null) ? last.getSaldo() : c.getSaldoInicial()
            );
        }

        // 5) JSON: cuentas
        List<ReporteCuentaItem> cuentasDto = cuentas.stream()
                .map(c -> new ReporteCuentaItem(
                        c.getNumeroCuenta(),
                        c.getTipoCuenta() != null ? c.getTipoCuenta().name() : "",
                        c.getSaldoInicial(),
                        saldoFinalPorCuenta.getOrDefault(c.getNumeroCuenta(), c.getSaldoInicial()),
                        c.getEstado()
                ))
                .toList();

        // 6) JSON: movimientos
        List<ReporteMovimientoItem> movsDto = movimientos.stream()
                .map(m -> new ReporteMovimientoItem(
                        m.getId(),
                        m.getFecha() != null ? m.getFecha().toString() : "",
                        (m.getCuenta() != null ? m.getCuenta().getNumeroCuenta() : ""),
                        m.getTipoMovimiento() != null ? m.getTipoMovimiento().name() : "",
                        m.getValor() != null ? m.getValor() : BigDecimal.ZERO,
                        m.getSaldo() != null ? m.getSaldo() : BigDecimal.ZERO
                ))
                .toList();

        // 7) PDF -> Base64
        String pdfBase64 = buildPdfBase64(
                persona.getNombre(),
                persona.getIdentificacion(),
                desde,
                hasta,
                cuentasDto,
                totalCreditos,
                totalDebitos,
                movsDto
        );

        // 8) Respuesta
        return new ReporteEstadoCuentaResponse(
                persona.getNombre(),
                persona.getIdentificacion(),
                desde.toString(),
                hasta.toString(),
                cuentasDto,
                totalCreditos,
                totalDebitos,
                movsDto,
                pdfBase64
        );
    }

    private String buildPdfBase64(
            String clienteNombre,
            String identificacion,
            LocalDate desde,
            LocalDate hasta,
            List<ReporteCuentaItem> cuentas,
            BigDecimal totalCreditos,
            BigDecimal totalDebitos,
            List<ReporteMovimientoItem> movimientos
    ) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, baos);

            document.open();

            Font title = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 10, Font.NORMAL);

            document.add(new Paragraph("Estado de Cuenta", title));
            document.add(new Paragraph("Cliente: " + clienteNombre, normal));
            document.add(new Paragraph("Identificación: " + identificacion, normal));
            document.add(new Paragraph("Rango: " + desde + " a " + hasta, normal));
            document.add(Chunk.NEWLINE);

            // Tabla Cuentas
            document.add(new Paragraph("Cuentas", new Font(Font.HELVETICA, 12, Font.BOLD)));
            PdfPTable tableC = new PdfPTable(5);
            tableC.setWidthPercentage(100);
            tableC.setSpacingBefore(6);

            addHeader(tableC, "Número", "Tipo", "Saldo Inicial", "Saldo Disponible", "Estado");
            for (ReporteCuentaItem c : cuentas) {
                tableC.addCell(s(c.numeroCuenta()));
                tableC.addCell(s(c.tipoCuenta()));
                tableC.addCell(n(c.saldoInicial()));
                tableC.addCell(n(c.saldoDisponible()));
                tableC.addCell(Boolean.TRUE.equals(c.estado()) ? "True" : "False");
            }
            document.add(tableC);

            document.add(Chunk.NEWLINE);

            // Totales
            document.add(new Paragraph("Totales del rango", new Font(Font.HELVETICA, 12, Font.BOLD)));
            document.add(new Paragraph("Total Créditos: " + n(totalCreditos), normal));
            document.add(new Paragraph("Total Débitos: " + n(totalDebitos), normal));
            document.add(Chunk.NEWLINE);

            // Tabla Movimientos
            document.add(new Paragraph("Movimientos", new Font(Font.HELVETICA, 12, Font.BOLD)));
            PdfPTable tableM = new PdfPTable(5);
            tableM.setWidthPercentage(100);
            tableM.setSpacingBefore(6);

            addHeader(tableM, "Fecha", "Cuenta", "Tipo Movimiento", "Valor", "Saldo");

            for (ReporteMovimientoItem m : movimientos) {
                tableM.addCell(s(m.fecha()));
                tableM.addCell(s(m.numeroCuenta()));
                tableM.addCell(s(m.tipoMovimiento()));
                tableM.addCell(n(m.valor()));
                tableM.addCell(n(m.saldo()));
            }

            document.add(tableM);
            document.close();

            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new BusinessException("No se pudo generar el PDF del reporte");
        }
    }

    private void addHeader(PdfPTable table, String... headers) {
        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setPadding(6);
            table.addCell(cell);
        }
    }

    // Helpers: evita nulls y formatea números
    private String s(String v) {
        return (v == null) ? "" : v;
    }

    private String n(BigDecimal v) {
        return (v == null) ? "0.00" : v.toPlainString();
    }
}
