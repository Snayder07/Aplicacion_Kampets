package org.example.repository;

import org.example.model.Citas;
import java.util.List;

public interface CitaRepository {
    void guardar(Citas cita);
    Citas buscarPorId(Integer id);
    List<Citas> buscarTodos();
    void eliminar(Integer id);
}