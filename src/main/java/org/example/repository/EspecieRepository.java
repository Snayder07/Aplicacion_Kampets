package org.example.repository;

import org.example.model.Especies;
import java.util.List;

public interface EspecieRepository {
    List<Especies> buscarTodos();
    Especies buscarPorId(Integer id);
    Especies buscarPorNombre(String nombre);
    void guardar(Especies e);
}