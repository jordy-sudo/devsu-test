package com.devsu.test.services;

import com.devsu.test.domain.dto.request.ClienteCreateRequest;
import com.devsu.test.domain.dto.response.ClienteResponse;
import com.devsu.test.domain.entities.Cliente;
import com.devsu.test.domain.entities.Persona;
import com.devsu.test.exceptions.BusinessException;
import com.devsu.test.repositories.ClienteRepository;
import com.devsu.test.repositories.PersonaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final PersonaRepository personaRepository;
    private final ClienteRepository clienteRepository;

    public ClienteService(PersonaRepository personaRepository, ClienteRepository clienteRepository) {
        this.personaRepository = personaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteResponse createClienteFlujo1(ClienteCreateRequest req) {

        // 1) Validación de identificacion única (a nivel persona)
        if (personaRepository.existsByIdentificacion(req.getIdentificacion())) {
            throw new BusinessException("Ya existe una persona con esa identificación");
        }

        // 2) Crear Persona
        Persona persona = new Persona();
        persona.setNombre(req.getNombre());
        persona.setGenero(req.getGenero());
        persona.setEdad(req.getEdad());
        persona.setIdentificacion(req.getIdentificacion());
        persona.setDireccion(req.getDireccion());
        persona.setTelefono(req.getTelefono());

        Persona savedPersona = personaRepository.save(persona);

        // 3) Crear Cliente con el MISMO ID (PK compartida)
        Cliente cliente = new Cliente();
        cliente.setPersona(savedPersona); // MapsId toma el id automáticamente
        cliente.setContrasena(req.getContrasena());
        cliente.setEstado(req.getEstado() != null ? req.getEstado() : true);

        Cliente savedCliente = clienteRepository.save(cliente);

        // 4) Respuesta (no devuelvas contraseña)
        return new ClienteResponse(
                savedCliente.getId(),
                savedPersona.getNombre(),
                savedPersona.getIdentificacion(),
                savedCliente.getEstado()
        );
    }
}
