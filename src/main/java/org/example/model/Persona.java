package org.example.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

@MappedSuperclass
public abstract class Persona {

    // Campos transient — Hibernate los ignora
    // Las subclases los sobreescriben con sus propias columnas
    @Transient
    private String nombre;

    @Transient
    private String telefono;

    @Transient
    private String correo;

    public Persona() {}

    public Persona(String nombre, String telefono, String correo) {
        this.nombre   = nombre;
        this.telefono = telefono;
        this.correo   = correo;
    }

    public String getNombre()            { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono()              { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo()            { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
