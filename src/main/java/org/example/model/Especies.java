package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ESPECIES")
public class Especies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_especie", nullable = false, length = 50)
    private String nombre;

    public Especies() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}