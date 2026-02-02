package com.devsu.test.domain.dto.request;

import jakarta.validation.constraints.*;

public class PersonaCreateRequest {

    @NotBlank(message = "nombre es requerido")
    @Size(max = 100, message = "nombre máximo 50 caracteres")
    private String nombre;

    @NotBlank(message = "genero es requerido")
    @Size(max = 20, message = "genero máximo 20 caracteres")
    private String genero;

    @NotNull(message = "edad es requerida")
    @Min(value = 0, message = "edad no puede ser negativa")
    @Max(value = 120, message = "edad no puede ser mayor a 80")
    private Integer edad;

    @NotBlank(message = "identificacion es requerida")
    @Size(max = 20, message = "identificacion máximo 13 caracteres")
    private String identificacion;

    @NotBlank(message = "direccion es requerida")
    @Size(max = 200, message = "direccion máximo 200 caracteres")
    private String direccion;

    @NotBlank(message = "telefono es requerido")
    @Size(max = 30, message = "telefono máximo 10 caracteres")
    private String telefono;

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
}
