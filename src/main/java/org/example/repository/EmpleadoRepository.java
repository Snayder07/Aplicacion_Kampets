package org.example.repository;

import org.example.model.Empleados;
import java.util.List;

public interface EmpleadoRepository {
    void guardar(Empleados empleado);
    Empleados buscarPorId(Integer id);
    List<Empleados> buscarTodos();
    void eliminar(Integer id);
}