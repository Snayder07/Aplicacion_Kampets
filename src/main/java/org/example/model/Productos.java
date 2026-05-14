package org.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCTOS")
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "foto_pro", columnDefinition = "BYTEA")
    private byte[] foto;

    @Column(name = "nombre_pro", nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo_pro", length = 50)
    private String tipo;

    @Column(name = "marca_pro", length = 50)
    private String marca;

    @Column(name = "precio_pro", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "stock_pro", nullable = false)
    private Integer stock;

    public Productos() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public byte[] getFoto() { return foto; }
    public void setFoto(byte[] foto) { this.foto = foto; }
}