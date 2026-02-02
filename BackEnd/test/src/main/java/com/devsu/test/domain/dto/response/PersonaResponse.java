package com.devsu.test.domain.dto.response;

public class PersonaResponse {
    private Long id;
    private String nombre;
    private String genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;

    public PersonaResponse(Long id, String nombre, String genero, Integer edad,
                           String identificacion, String direccion, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getGenero() { return genero; }
    public Integer getEdad() { return edad; }
    public String getIdentificacion() { return identificacion; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
}
