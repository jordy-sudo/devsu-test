package com.devsu.test.domain.dto.request;

import jakarta.validation.constraints.*;

public class ClienteCreateRequest {

    // Persona
    @NotBlank(message = "nombre es requerido")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "genero es requerido")
    @Size(max = 20)
    private String genero;

    @NotNull(message = "edad es requerida")
    @Min(0) @Max(80)
    private Integer edad;

    @NotBlank(message = "identificacion es requerida")
    @Size(max = 13)
    private String identificacion;

    @NotBlank(message = "direccion es requerida")
    @Size(max = 200)
    private String direccion;

    @NotBlank(message = "telefono es requerido")
    @Size(max = 15)
    private String telefono;

    // Cliente
    @NotBlank(message = "contrasena es requerida")
    @Size(min = 4, max = 64, message = "contrasena debe tener entre 4 y 64 caracteres")
    private String contrasena;

    // opcional: si no llega, default true
    private Boolean estado;

    // getters/setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
