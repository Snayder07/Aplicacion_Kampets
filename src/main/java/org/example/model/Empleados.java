package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "EMPLEADOS")
public class Empleados extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // nombre, correo y telefono vienen de Persona
    // solo mapeamos las columnas específicas de EMPLEADOS

    @Column(name = "nombre_emp", nullable = false, length = 100)
    private String nombreEmp;

    @Column(name = "apellido_emp", length = 100)
    private String apellidoEmp;

    @Column(name = "telefono_emp", length = 20)
    private String telefonoEmp;

    @Column(name = "correo_emp", length = 100)
    private String correoEmp;

    @Column(name = "cargo", length = 50)
    private String cargo;

    @Column(name = "contrasena_emp", length = 255)
    private String contrasena;

    public Empleados() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    // Sobreescribimos getters/setters para mapear a columnas de BD
    @Override
    public String getNombre() {
        if (apellidoEmp != null && !apellidoEmp.trim().isEmpty())
            return nombreEmp + " " + apellidoEmp;
        return nombreEmp;
    }
    @Override
    public void setNombre(String nombre) { this.nombreEmp = nombre; }

    public String getApellido() { return apellidoEmp; }
    public void setApellido(String apellido) { this.apellidoEmp = apellido; }

    @Override
    public String getTelefono() { return telefonoEmp; }
    @Override
    public void setTelefono(String telefono) { this.telefonoEmp = telefono; }

    @Override
    public String getCorreo() { return correoEmp; }
    @Override
    public void setCorreo(String correo) { this.correoEmp = correo; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}