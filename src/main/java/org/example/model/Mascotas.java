package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "MASCOTAS")
public class Mascotas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_especie", nullable = false)
    private Especies especie;

    @Column(name = "nombre_m", nullable = false, length = 100)
    private String nombre;

    @Column(name = "fecha_nac")
    private LocalDate fechaNac;

    @Column(name = "sexo", length = 10)
    private String sexo;

    @Column(name = "caracteristica", length = 200)
    private String caracteristica;

    public Mascotas() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Especies getEspecie() { return especie; }
    public void setEspecie(Especies especie) { this.especie = especie; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaNac() { return fechaNac; }
    public void setFechaNac(LocalDate fechaNac) { this.fechaNac = fechaNac; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getCaracteristica() { return caracteristica; }
    public void setCaracteristica(String caracteristica) { this.caracteristica = caracteristica; }

    /**
     * Devuelve una etiqueta única para mostrar en combos y reportes.
     * Ej: "Max (Golden Retriever)" o "Max (Golden Retriever — pelaje dorado)"
     */
    public String getEtiqueta() {
        String base = nombre != null ? nombre : "?";
        String esp  = (especie != null) ? especie.getNombre() : null;
        String car  = (caracteristica != null && !caracteristica.trim().isEmpty()) ? caracteristica.trim() : null;
        if (esp != null && car != null) return base + " (" + esp + " — " + car + ")";
        if (esp != null)                return base + " (" + esp + ")";
        if (car != null)                return base + " [" + car + "]";
        return base;
    }
}