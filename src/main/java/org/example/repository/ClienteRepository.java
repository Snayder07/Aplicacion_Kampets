package org.example.repository;

import org.example.model.Cliente;
import java.util.List;

public interface ClienteRepository {
    void guardar(Cliente cliente);
    Cliente buscarPorId(Integer id);
    List<Cliente> buscarTodos();
    void eliminar(Integer id);
}