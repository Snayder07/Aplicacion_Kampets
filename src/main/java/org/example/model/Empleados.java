package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "EMPLEADOS")
public class Empleados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_emp", nullable = false, length = 100)
    private String nombre;

    @Column(name = "telefono_emp", length = 20)
    private String telefono;

    @Column(name = "cargo", length = 50)
    private String cargo;

    @Column(name = "correo_emp", length = 100)
    private String correo;

    @Column(name = "contrasena_emp", length = 255)
    private String contrasena;

    public Empleados() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
