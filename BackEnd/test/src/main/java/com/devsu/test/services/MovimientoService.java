package com.devsu.test.services;

import com.devsu.test.domain.dto.request.MovimientoCreateRequest;
import com.devsu.test.domain.dto.request.MovimientoUpdateRequest;
import com.devsu.test.domain.dto.response.MovimientoDetailResponse;
import com.devsu.test.domain.dto.response.MovimientoListadoResponse;
import com.devsu.test.domain.entities.Cuenta;
import com.devsu.test.domain.entities.Movimiento;
import com.devsu.test.domain.enums.TipoMovimiento;
import com.devsu.test.exceptions.BusinessException;
import com.devsu.test.repositories.CuentaRepository;
import com.devsu.test.repositories.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MovimientoService {

    private static final BigDecimal LIMITE_DIARIO_RETIRO = new BigDecimal("1000.00");

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public MovimientoService(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    // ==========================
    // CREATE (POST /movimientos)
    // ==========================
    @Transactional
    public MovimientoDetailResponse create(MovimientoCreateRequest req) {

        Cuenta cuenta = cuentaRepository.findById(req.getNumeroCuenta())
                .orElseThrow(() -> new BusinessException("Cuenta no existe"));

        if (!Boolean.TRUE.equals(cuenta.getEstado())) {
            throw new BusinessException("Cuenta inactiva");
        }

        if (!Boolean.TRUE.equals(cuenta.getCliente().getEstado())) {
            throw new BusinessException("Cliente inactivo");
        }

        LocalDate fecha = (req.getFecha() == null || req.getFecha().isBlank())
                ? LocalDate.now()
                : LocalDate.parse(req.getFecha());

        TipoMovimiento tipo = parseTipoMovimiento(req.getTipoMovimiento());

        // saldo actual: último movimiento o saldo inicial
        BigDecimal saldoActual = movimientoRepository
                .findTopByCuenta_NumeroCuentaOrderByIdDesc(cuenta.getNumeroCuenta())
                .filter(Movimiento::getEstado) // solo si el último está activo
                .map(Movimiento::getSaldo)
                .orElse(cuenta.getSaldoInicial());

        BigDecimal valorAbs = req.getValor().abs();

        // regla: depósito positivo / retiro negativo
        BigDecimal valor;
        if (tipo == TipoMovimiento.DEPOSITO) {
            valor = valorAbs;
        } else {
            valor = valorAbs.negate();
        }

        // validación saldo
        if (tipo == TipoMovimiento.RETIRO) {
            if (saldoActual.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("Saldo no disponible");
            }

            BigDecimal saldoLuego = saldoActual.add(valor); // valor es negativo
            if (saldoLuego.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Saldo no disponible");
            }

            // validación cupo diario
            BigDecimal retirosHoy = movimientoRepository.sumRetirosDelDia(cuenta.getNumeroCuenta(), fecha);
            BigDecimal nuevoAcumulado = retirosHoy.add(valorAbs);
            if (nuevoAcumulado.compareTo(LIMITE_DIARIO_RETIRO) > 0) {
                throw new BusinessException("Cupo diario Excedido");
            }
        }

        BigDecimal saldoFinal = saldoActual.add(valor);

        Movimiento m = new Movimiento();
        m.setCuenta(cuenta);
        m.setFecha(fecha);
        m.setTipoMovimiento(tipo);
        m.setValor(valor);
        m.setSaldo(saldoFinal);
        m.setEstado(true);

        Movimiento saved = movimientoRepository.save(m);

        return new MovimientoDetailResponse(
                saved.getId(),
                saved.getFecha().toString(),
                saved.getTipoMovimiento().name(),
                saved.getValor(),
                saved.getSaldo(),
                saved.getEstado(),
                saved.getCuenta().getNumeroCuenta()
        );
    }

    // ==========================
    // READ ONE (GET /movimientos/{id})
    // ==========================
    @Transactional(readOnly = true)
    public MovimientoDetailResponse findById(Long id) {
        Movimiento m = movimientoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Movimiento no existe"));

        return new MovimientoDetailResponse(
                m.getId(),
                m.getFecha().toString(),
                m.getTipoMovimiento().name(),
                m.getValor(),
                m.getSaldo(),
                m.getEstado(),
                m.getCuenta().getNumeroCuenta()
        );
    }

    // ==========================
    // READ LIST (GET /movimientos?clienteId&desde&hasta&q)
    // ==========================
    @Transactional(readOnly = true)
    public List<MovimientoListadoResponse> listarPorClienteYRango(Long clienteId, LocalDate desde, LocalDate hasta, String q) {
        if (desde.isAfter(hasta)) {
            throw new BusinessException("Rango de fechas inválido: 'desde' no puede ser mayor que 'hasta'");
        }

        return movimientoRepository.findByClienteRangoYQuery(clienteId, desde, hasta, q)
                .stream()
                .map(m -> new MovimientoListadoResponse(
                        m.getFecha().toString(),
                        m.getCuenta().getCliente().getPersona().getNombre(),
                        m.getCuenta().getNumeroCuenta(),
                        m.getCuenta().getTipoCuenta().name(),
                        m.getCuenta().getSaldoInicial(),
                        m.getCuenta().getEstado(),
                        m.getValor(),
                        m.getSaldo()
                ))
                .toList();
    }

    // ==========================
    // PATCH (anular) /movimientos/{id}
    // ==========================
    @Transactional
    public MovimientoDetailResponse patchEstado(Long id, MovimientoUpdateRequest req) {
        Movimiento m = movimientoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Movimiento no existe"));

        // En banca: no se re-activa ni se edita, solo se anula
        if (Boolean.FALSE.equals(req.getEstado()) && Boolean.TRUE.equals(m.getEstado())) {
            m.setEstado(false);
            Movimiento saved = movimientoRepository.save(m);

            return new MovimientoDetailResponse(
                    saved.getId(),
                    saved.getFecha().toString(),
                    saved.getTipoMovimiento().name(),
                    saved.getValor(),
                    saved.getSaldo(),
                    saved.getEstado(),
                    saved.getCuenta().getNumeroCuenta()
            );
        }

        throw new BusinessException("Operación no permitida. Solo se permite anular (estado=false).");
    }

    // ==========================
    // DELETE lógico /movimientos/{id}
    // ==========================
    @Transactional
    public void delete(Long id) {
        Movimiento m = movimientoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Movimiento no existe"));
        m.setEstado(false);
        movimientoRepository.save(m);
    }

    private TipoMovimiento parseTipoMovimiento(String raw) {
        try {
            return TipoMovimiento.valueOf(raw.trim().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("tipoMovimiento inválido. Use: DEPOSITO o RETIRO");
        }
    }
}
