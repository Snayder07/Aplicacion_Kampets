package org.example.repository;

import org.example.model.Productos;
import java.util.List;

public interface ProductoRepository {
    void guardar(Productos producto);
    Productos buscarPorId(Integer id);
    List<Productos> buscarTodos();
    void eliminar(Integer id);
    void actualizar(Productos producto);
}