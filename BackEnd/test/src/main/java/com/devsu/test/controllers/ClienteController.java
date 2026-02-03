package com.devsu.test.controllers;

import com.devsu.test.domain.dto.request.ClienteCreateRequest;
import com.devsu.test.domain.dto.request.ClienteUpdateRequest;
import com.devsu.test.domain.dto.response.ClienteResponse;
import com.devsu.test.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // ===============================
    // CREATE
    // ===============================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse create(@Valid @RequestBody ClienteCreateRequest req) {
        return clienteService.createClienteFlujo1(req);
    }

    // ===============================
    // READ - ALL
    // ===============================
    @GetMapping
    public List<ClienteResponse> findAll() {
        return clienteService.findAll();
    }

    // ===============================
    // READ - BY ID
    // ===============================
    @GetMapping("/{id}")
    public ClienteResponse findById(@PathVariable Long id) {
        return clienteService.findById(id);
    }

    // ===============================
    // UPDATE (PUT)
    // ===============================
    @PutMapping("/{id}")
    public ClienteResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ClienteUpdateRequest req
    ) {
        return clienteService.update(id, req);
    }

    // ===============================
    // DELETE (LÓGICO)
    // ===============================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clienteService.delete(id);
    }
}
