package com.devsu.test.repositories;

import com.devsu.test.domain.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, String> {
    List<Cuenta> findByCliente_Id(Long clienteId);
}
