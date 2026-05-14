package org.example.service;

import org.example.model.Productos;
import org.example.repository.ProductoRepositoryImpl;
import java.math.BigDecimal;
import java.util.List;

public class ProductoService {

    private final ProductoRepositoryImpl repositorio = new ProductoRepositoryImpl();

    public void agregar(String nombre, String tipo, String marca,
                        String precio, String stock, byte[] foto) throws Exception {
        if (nombre == null || nombre.trim().isEmpty())
            throw new Exception("El nombre del producto es obligatorio.");
        if (precio == null || precio.trim().isEmpty())
            throw new Exception("El precio es obligatorio.");
        if (stock == null || stock.trim().isEmpty())
            throw new Exception("El stock es obligatorio.");

        Productos p = new Productos();
        p.setNombre(nombre.trim());
        p.setTipo(tipo  != null ? tipo.trim()  : null);
        p.setMarca(marca != null ? marca.trim() : null);
        try {
            p.setPrecio(new BigDecimal(precio.trim()));
        } catch (NumberFormatException e) {
            throw new Exception("El precio debe ser un número válido (ej: 9.99).");
        }
        try {
            p.setStock(Integer.parseInt(stock.trim()));
        } catch (NumberFormatException e) {
            throw new Exception("El stock debe ser un número entero.");
        }
        if (foto != null) p.setFoto(foto);
        repositorio.guardar(p);
    }

    public List<Productos> listarTodos() {
        return repositorio.buscarTodos();
    }

    public void eliminar(Integer id) throws Exception {
        if (id == null) throw new Exception("ID de producto inválido.");
        repositorio.eliminar(id);
    }

    public void actualizar(Productos producto) throws Exception {
        if (producto == null) throw new Exception("El producto no puede ser nulo.");
        repositorio.actualizar(producto);
    }
}