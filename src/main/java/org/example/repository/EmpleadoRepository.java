package org.example.repository;

import org.example.model.Empleados;
import java.util.List;

public interface EmpleadoRepository {
    void guardar(Empleados empleado);
    Empleados buscarPorId(Integer id);
    Empleados buscarPorCorreo(String correo);
    List<Empleados> buscarTodos();
    void actualizar(Empleados empleado);
    void eliminar(Integer id);
}
