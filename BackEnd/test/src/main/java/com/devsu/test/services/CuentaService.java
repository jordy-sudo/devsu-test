package com.devsu.test.services;

import com.devsu.test.domain.dto.request.CuentaCreateRequest;
import com.devsu.test.domain.dto.request.CuentaUpdateRequest;
import com.devsu.test.domain.dto.response.CuentaResponse;
import com.devsu.test.domain.entities.Cliente;
import com.devsu.test.domain.entities.Cuenta;
import com.devsu.test.domain.enums.TipoCuenta;
import com.devsu.test.exceptions.BusinessException;
import com.devsu.test.repositories.ClienteRepository;
import com.devsu.test.repositories.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public CuentaService(CuentaRepository cuentaRepository, ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public CuentaResponse create(CuentaCreateRequest req) {

        if (cuentaRepository.existsById(req.getNumeroCuenta())) {
            throw new BusinessException("Ya existe una cuenta con ese número");
        }

        Cliente cliente = clienteRepository.findById(req.getClienteId())
                .orElseThrow(() -> new BusinessException("Cliente no existe"));

        if (!Boolean.TRUE.equals(cliente.getEstado())) {
            throw new BusinessException("Cliente inactivo, no puede crear cuentas");
        }

        TipoCuenta tipo = parseTipoCuenta(req.getTipoCuenta());

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(req.getNumeroCuenta());
        cuenta.setTipoCuenta(tipo);
        cuenta.setSaldoInicial(req.getSaldoInicial());
        cuenta.setEstado(req.getEstado() != null ? req.getEstado() : true);
        cuenta.setCliente(cliente);

        Cuenta saved = cuentaRepository.save(cuenta);
        return map(saved);
    }

    @Transactional(readOnly = true)
    public List<CuentaResponse> findAll() {
        return cuentaRepository.findAll().stream().map(this::map).toList();
    }

    @Transactional(readOnly = true)
    public CuentaResponse findByNumeroCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new BusinessException("Cuenta no existe"));
        return map(cuenta);
    }

    @Transactional(readOnly = true)
    public List<CuentaResponse> findByCliente(Long clienteId) {
        return cuentaRepository.findByCliente_Id(clienteId).stream().map(this::map).toList();
    }

    @Transactional
    public CuentaResponse update(String numeroCuenta, CuentaUpdateRequest req) {

        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new BusinessException("Cuenta no existe"));

        cuenta.setTipoCuenta(parseTipoCuenta(req.getTipoCuenta()));
        cuenta.setSaldoInicial(req.getSaldoInicial());
        cuenta.setEstado(req.getEstado());

        // opcional: reasignar cuenta a otro cliente (si lo permites)
        if (req.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(req.getClienteId())
                    .orElseThrow(() -> new BusinessException("Cliente no existe"));

            if (!Boolean.TRUE.equals(cliente.getEstado())) {
                throw new BusinessException("Cliente inactivo, no puede reasignar cuenta");
            }

            cuenta.setCliente(cliente);
        }

        return map(cuentaRepository.save(cuenta));
    }

    /**
     * Delete lógico: estado = false
     */
    @Transactional
    public void delete(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new BusinessException("Cuenta no existe"));
        cuenta.setEstado(false);
        cuentaRepository.save(cuenta);
    }

    private TipoCuenta parseTipoCuenta(String raw) {
        try {
            return TipoCuenta.valueOf(raw.trim().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("tipoCuenta inválido. Use: AHORROS o CORRIENTE");
        }
    }

    private CuentaResponse map(Cuenta c) {
        return new CuentaResponse(
                c.getNumeroCuenta(),
                c.getTipoCuenta().name(),
                c.getSaldoInicial(),
                c.getEstado(),
                c.getCliente().getId()
        );
    }
}
