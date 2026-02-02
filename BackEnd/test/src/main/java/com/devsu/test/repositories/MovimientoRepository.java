package com.devsu.test.repositories;

import com.devsu.test.domain.entities.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    // Último movimiento de una cuenta (para saldo actual)
    Optional<Movimiento> findTopByCuenta_NumeroCuentaOrderByIdDesc(String numeroCuenta);

    // Suma de retiros del día (límite diario)
    @Query("""
        SELECT COALESCE(SUM(ABS(m.valor)), 0)
        FROM Movimiento m
        WHERE m.cuenta.numeroCuenta = :numeroCuenta
          AND m.fecha = :fecha
          AND m.valor < 0
    """)
    BigDecimal sumRetirosDelDia(String numeroCuenta, LocalDate fecha);

    // Listado de moviemientos por Clientid
    @Query("""
        SELECT m
        FROM Movimiento m
        WHERE m.cuenta.cliente.id = :clienteId
          AND m.fecha BETWEEN :desde AND :hasta
        ORDER BY m.fecha ASC, m.id ASC
    """)
    List<Movimiento> findByClienteAndRangoFechas(Long clienteId, LocalDate desde, LocalDate hasta);
}
