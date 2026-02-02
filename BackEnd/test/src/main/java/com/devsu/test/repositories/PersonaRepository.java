package com.devsu.test.repositories;

import com.devsu.test.domain.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    boolean existsByIdentificacion(String identificacion);
}
