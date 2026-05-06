package org.example.repository;

import org.example.model.Mascotas;
import java.util.List;

public interface MascotaRepository {
    void guardar(Mascotas mascota);
    Mascotas buscarPorId(Integer id);
    List<Mascotas> buscarTodos();
    void eliminar(Integer id);
}