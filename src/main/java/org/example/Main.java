package org.example;

import org.example.model.Cliente;
import org.example.repository.ClienteRepositoryImpl;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ClienteRepositoryImpl repo = new ClienteRepositoryImpl();

        // Trae todos los clientes de la BD
        List<Cliente> clientes = repo.buscarTodos();

        if (clientes.isEmpty()) {
            System.out.println("Conexión exitosa! No hay clientes aún.");
        } else {
            for (Cliente c : clientes) {
                System.out.println("Cliente: " + c.getNombre());
            }
        }
    }
}