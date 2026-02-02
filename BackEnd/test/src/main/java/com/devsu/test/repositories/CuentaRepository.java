package com.devsu.test.repositories;

import com.devsu.test.domain.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta, String> {
    boolean existsByNumeroCuenta(String numeroCuenta);
}
