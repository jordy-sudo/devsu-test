package com.devsu.test.services;

import com.devsu.test.domain.dto.request.ClienteCreateRequest;
import com.devsu.test.domain.dto.request.ClienteUpdateRequest;
import com.devsu.test.domain.dto.response.ClienteResponse;
import com.devsu.test.domain.entities.Cliente;
import com.devsu.test.domain.entities.Persona;
import com.devsu.test.exceptions.BusinessException;
import com.devsu.test.repositories.ClienteRepository;
import com.devsu.test.repositories.PersonaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final PersonaRepository personaRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(PersonaRepository personaRepository,
                          ClienteRepository clienteRepository,
                          PasswordEncoder passwordEncoder) {
        this.personaRepository = personaRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * - Si NO existe Persona por identificacion => crear Persona
     * - Si ya existe => usarla
     * - Luego crear Cliente (si no existe para esa persona)
     */
    @Transactional
    public ClienteResponse createClienteFlujo1(ClienteCreateRequest req) {

        Persona persona = personaRepository.findByIdentificacion(req.getIdentificacion())
                .orElseGet(() -> {
                    Persona p = new Persona();
                    p.setNombre(req.getNombre());
                    p.setGenero(req.getGenero());
                    p.setEdad(req.getEdad());
                    p.setIdentificacion(req.getIdentificacion());
                    p.setDireccion(req.getDireccion());
                    p.setTelefono(req.getTelefono());
                    return personaRepository.save(p);
                });

        if (clienteRepository.existsById(persona.getId())) {
            throw new BusinessException("Ya existe un cliente para esta persona");
        }

        Cliente cliente = new Cliente();
        cliente.setPersona(persona);
        cliente.setContrasena(passwordEncoder.encode(req.getContrasena()));
        cliente.setEstado(req.getEstado() != null ? req.getEstado() : true);

        Cliente saved = clienteRepository.save(cliente);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> findAll() {
        return clienteRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no existe"));
        return mapToResponse(cliente);
    }

    @Transactional
    public ClienteResponse update(Long id, ClienteUpdateRequest req) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no existe"));

        cliente.setEstado(req.getEstado());

        if (req.getContrasena() != null && !req.getContrasena().isBlank()) {
            cliente.setContrasena(passwordEncoder.encode(req.getContrasena()));
        }

        return mapToResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public void delete(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente no existe"));

        cliente.setEstado(false);
        clienteRepository.save(cliente);
    }

    private ClienteResponse mapToResponse(Cliente cliente) {
        Persona p = cliente.getPersona();
        return new ClienteResponse(
                cliente.getId(),
                p.getNombre(),
                p.getGenero(),
                p.getEdad(),
                p.getIdentificacion(),
                p.getDireccion(),
                p.getTelefono(),
                cliente.getEstado()
        );
    }
}
