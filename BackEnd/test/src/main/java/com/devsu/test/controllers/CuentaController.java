package com.devsu.test.controllers;

import com.devsu.test.domain.dto.request.CuentaCreateRequest;
import com.devsu.test.domain.dto.request.CuentaUpdateRequest;
import com.devsu.test.domain.dto.response.CuentaResponse;
import com.devsu.test.services.CuentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaResponse create(@Valid @RequestBody CuentaCreateRequest req) {
        return cuentaService.create(req);
    }

    // READ ALL
    @GetMapping
    public List<CuentaResponse> findAll(@RequestParam(required = false) Long clienteId) {
        if (clienteId != null) {
            return cuentaService.findByCliente(clienteId);
        }
        return cuentaService.findAll();
    }

    // READ ONE
    @GetMapping("/{numeroCuenta}")
    public CuentaResponse findByNumeroCuenta(@PathVariable String numeroCuenta) {
        return cuentaService.findByNumeroCuenta(numeroCuenta);
    }

    // UPDATE
    @PutMapping("/{numeroCuenta}")
    public CuentaResponse update(
            @PathVariable String numeroCuenta,
            @Valid @RequestBody CuentaUpdateRequest req
    ) {
        return cuentaService.update(numeroCuenta, req);
    }

    // DELETE (lógico)
    @DeleteMapping("/{numeroCuenta}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String numeroCuenta) {
        cuentaService.delete(numeroCuenta);
    }
}
