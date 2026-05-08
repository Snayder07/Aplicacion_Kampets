package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "CLIENTES")
public class Cliente extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // nombre, correo y telefono vienen de Persona
    // solo mapeamos las columnas específicas de CLIENTES

    @Column(name = "nombre_c", nullable = false, length = 100)
    private String nombreC;

    @Column(name = "correo_c", length = 100)
    private String correoC;

    @Column(name = "telefono_c", length = 20)
    private String telefonoC;

    @Column(name = "direccion_c", length = 150)
    private String direccion;

    @Column(name = "contrasena", length = 255)
    private String contrasena;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    public Cliente() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    // Sobreescribimos getters/setters para mapear a columnas de BD
    @Override
    public String getNombre() { return nombreC; }
    @Override
    public void setNombre(String nombre) { this.nombreC = nombre; }

    @Override
    public String getCorreo() { return correoC; }
    @Override
    public void setCorreo(String correo) { this.correoC = correo; }

    @Override
    public String getTelefono() { return telefonoC; }
    @Override
    public void setTelefono(String telefono) { this.telefonoC = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}