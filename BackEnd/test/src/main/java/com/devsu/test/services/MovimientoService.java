package com.devsu.test.services;

import com.devsu.test.domain.dto.request.MovimientoCreateRequest;
import com.devsu.test.domain.dto.response.MovimientoListadoResponse;
import com.devsu.test.domain.dto.response.MovimientoResponse;
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

    @Transactional
    public MovimientoResponse crear(MovimientoCreateRequest req) {

        Cuenta cuenta = cuentaRepository.findById(req.getNumeroCuenta())
                .orElseThrow(() -> new BusinessException("Cuenta no existe"));

        if (!Boolean.TRUE.equals(cuenta.getEstado())) {
            throw new BusinessException("Cuenta inactiva");
        }

        // Fecha
        LocalDate fecha = (req.getFecha() == null || req.getFecha().isBlank())
                ? LocalDate.now()
                : LocalDate.parse(req.getFecha());

        // Tipo
        TipoMovimiento tipo;
        try {
            tipo = TipoMovimiento.valueOf(req.getTipoMovimiento().trim().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("tipoMovimiento inválido. Use: DEPOSITO o RETIRO");
        }

        // Saldo actual = último saldo si hay movimientos, sino saldo_inicial
        BigDecimal saldoActual = movimientoRepository
                .findTopByCuenta_NumeroCuentaOrderByIdDesc(cuenta.getNumeroCuenta())
                .map(Movimiento::getSaldo)
                .orElse(cuenta.getSaldoInicial());

        BigDecimal valor = req.getValor();

        // Regla: crédito positivo / débito negativo
        BigDecimal movimientoValor;
        if (tipo == TipoMovimiento.DEPOSITO) {
            movimientoValor = valor.abs(); // positivo
        } else {
            movimientoValor = valor.abs().negate(); // negativo
        }

        // Validación: saldo no disponible si retiro y saldoActual <= 0
        if (tipo == TipoMovimiento.RETIRO && saldoActual.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Saldo no disponible");
        }

        // Validación: saldo suficiente (si retiro)
        if (tipo == TipoMovimiento.RETIRO) {
            BigDecimal saldoLuego = saldoActual.add(movimientoValor); // movimientoValor es negativo
            if (saldoLuego.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Saldo no disponible");
            }

            // Validación límite diario: suma de retiros del día + este retiro <= 1000
            BigDecimal retirosHoy = movimientoRepository.sumRetirosDelDia(cuenta.getNumeroCuenta(), fecha);
            BigDecimal nuevoAcumulado = retirosHoy.add(valor.abs());

            if (nuevoAcumulado.compareTo(LIMITE_DIARIO_RETIRO) > 0) {
                throw new BusinessException("Cupo diario Excedido");
            }
        }

        // Calcular saldo final
        BigDecimal saldoFinal = saldoActual.add(movimientoValor);

        // Guardar movimiento
        Movimiento mov = new Movimiento();
        mov.setCuenta(cuenta);
        mov.setFecha(fecha);
        mov.setTipoMovimiento(tipo);
        mov.setValor(movimientoValor);
        mov.setSaldo(saldoFinal);

        Movimiento saved = movimientoRepository.save(mov);

        String clienteNombre = cuenta.getCliente().getPersona().getNombre();

        return new MovimientoResponse(
                saved.getId(),
                saved.getFecha().toString(),
                clienteNombre,
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta().name(),
                cuenta.getSaldoInicial(),
                cuenta.getEstado(),
                saved.getValor(),
                saved.getSaldo()
        );
    }

    public List<MovimientoListadoResponse> listarPorClienteYRango(Long clienteId, LocalDate desde, LocalDate hasta) {

        if (desde.isAfter(hasta)) {
            throw new BusinessException("Rango de fechas inválido: 'desde' no puede ser mayor que 'hasta'");
        }

        List<Movimiento> movimientos = movimientoRepository.findByClienteAndRangoFechas(clienteId, desde, hasta);

        return movimientos.stream().map(m -> new MovimientoListadoResponse(
                m.getFecha().toString(),
                m.getCuenta().getCliente().getPersona().getNombre(),
                m.getCuenta().getNumeroCuenta(),
                m.getCuenta().getTipoCuenta().name(),
                m.getCuenta().getSaldoInicial(),
                m.getCuenta().getEstado(),
                m.getValor(),
                m.getSaldo()
        )).toList();
    }
}
