package org.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "CITA_SERVICIO")
public class Cita_servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cita", nullable = false)
    private Citas cita;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio servicio;

    @Column(name = "precio_cobrado", precision = 10, scale = 2)
    private BigDecimal precioCobrado;

    public Cita_servicio() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Citas getCita() { return cita; }
    public void setCita(Citas cita) { this.cita = cita; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public BigDecimal getPrecioCobrado() { return precioCobrado; }
    public void setPrecioCobrado(BigDecimal precioCobrado) { this.precioCobrado = precioCobrado; }
}