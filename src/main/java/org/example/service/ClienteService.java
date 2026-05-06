package org.example.service;

import org.example.model.Cliente;
import org.example.repository.ClienteRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

/**
 * PERSONA 2 — Servicio de Clientes
 * Versión actualizada compatible con Cliente.java de P1
 */
public class ClienteService {

    private final ClienteRepositoryImpl repositorio = new ClienteRepositoryImpl();

    // ─────────────────────────────────────────────────────────
    // REGISTRAR CLIENTE
    // ─────────────────────────────────────────────────────────
    public void registrar(String nombre, String apellido, String correo,
                          String telefono, String contrasena,
                          String confirmar) throws Exception {

        // Validar campos vacíos
        if (nombre == null || nombre.trim().isEmpty())
            throw new Exception("El nombre es obligatorio.");
        if (correo == null || correo.trim().isEmpty())
            throw new Exception("El correo es obligatorio.");
        if (contrasena == null || contrasena.trim().isEmpty())
            throw new Exception("La contraseña es obligatoria.");

        // Validar que las contraseñas coincidan
        if (!contrasena.equals(confirmar))
            throw new Exception("Las contraseñas no coinciden.");

        // Validar que el correo no esté ya registrado
        if (buscarPorCorreo(correo) != null)
            throw new Exception("Ya existe una cuenta con ese correo.");

        // Crear y guardar el cliente
        Cliente cliente = new Cliente();
        cliente.setNombre((nombre + " " + apellido).trim());
        cliente.setCorreo(correo.trim().toLowerCase());
        cliente.setTelefono(telefono);
        cliente.setDireccion("");
        cliente.setFechaRegistro(LocalDate.now());
        cliente.setContrasena(contrasena); // ← usa el campo real de BD

        repositorio.guardar(cliente);
    }

    // ─────────────────────────────────────────────────────────
    // LOGIN CLIENTE
    // ─────────────────────────────────────────────────────────
    public Cliente loginCliente(String correo, String contrasena) throws Exception {

        if (correo == null || correo.trim().isEmpty())
            throw new Exception("Ingresa tu correo.");
        if (contrasena == null || contrasena.trim().isEmpty())
            throw new Exception("Ingresa tu contraseña.");

        // Buscar el cliente por correo
        Cliente cliente = buscarPorCorreo(correo.trim().toLowerCase());

        if (cliente == null)
            throw new Exception("El correo no está registrado.");

        // Verificar contraseña real de la BD
        if (!cliente.getContrasena().equals(contrasena))
            throw new Exception("Contraseña incorrecta.");

        return cliente;
    }

    // ─────────────────────────────────────────────────────────
    // BUSCAR POR CORREO
    // ─────────────────────────────────────────────────────────
    public Cliente buscarPorCorreo(String correo) {
        List<Cliente> todos = repositorio.buscarTodos();
        for (Cliente c : todos) {
            if (c.getCorreo() != null && c.getCorreo().equalsIgnoreCase(correo)) {
                return c;
            }
        }
        return null;
    }

    // ─────────────────────────────────────────────────────────
    // LISTAR TODOS
    // ─────────────────────────────────────────────────────────
    public List<Cliente> listarTodos() {
        return repositorio.buscarTodos();
    }
}
