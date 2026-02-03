package com.devsu.test.controllers;

import com.devsu.test.domain.dto.request.MovimientoCreateRequest;
import com.devsu.test.domain.dto.request.MovimientoUpdateRequest;
import com.devsu.test.domain.dto.response.MovimientoDetailResponse;
import com.devsu.test.domain.dto.response.MovimientoListadoResponse;
import com.devsu.test.services.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoDetailResponse create(@Valid @RequestBody MovimientoCreateRequest req) {
        return movimientoService.create(req);
    }

    // READ ONE
    @GetMapping("/{id}")
    public MovimientoDetailResponse findById(@PathVariable Long id) {
        return movimientoService.findById(id);
    }

    // READ LIST
    @GetMapping
    public List<MovimientoListadoResponse> listar(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) String q
    ) {
        return movimientoService.listarPorClienteYRango(clienteId, desde, hasta, q);
    }

    // PATCH (anular)
    @PatchMapping("/{id}")
    public MovimientoDetailResponse patchEstado(
            @PathVariable Long id,
            @Valid @RequestBody MovimientoUpdateRequest req
    ) {
        return movimientoService.patchEstado(id, req);
    }

    // DELETE lógico (anular)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        movimientoService.delete(id);
    }
}
