package com.devsu.test.domain.dto.response;

public class ClienteResponse {
    private Long clienteId;
    private String nombre;
    private String identificacion;
    private String direccion;
    private String telefono;
    private Boolean estado;

    public ClienteResponse(Long clienteId, String nombre, String identificacion, String direccion, String telefono, Boolean estado) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.identificacion = identificacion;
        this.estado = estado;
    }

    public Long getClienteId() { return clienteId; }
    public String getNombre() { return nombre; }
    public String getIdentificacion() { return identificacion; }
    public Boolean getEstado() { return estado; }
}
