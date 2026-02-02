package com.devsu.test.domain.dto.request;

import jakarta.validation.constraints.*;

public class ClienteCreateRequest {

    @NotBlank(message = "nombre es requerido")
    private String nombre;

    @NotBlank(message = "genero es requerido")
    private String genero;

    @NotNull(message = "edad es requerida")
    @Min(0)
    @Max(120)
    private Integer edad;

    @NotBlank(message = "identificacion es requerida")
    private String identificacion;

    @NotBlank(message = "direccion es requerida")
    private String direccion;

    @NotBlank(message = "telefono es requerido")
    private String telefono;

    @NotBlank(message = "contrasena es requerida")
    private String contrasena;

    // getters y setters
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
}
