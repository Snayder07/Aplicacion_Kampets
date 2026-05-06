package org.example.service;

import org.example.model.Empleados;
import org.example.repository.EmpleadoRepositoryImpl;

import java.util.List;

/**
 * PERSONA 2 — Servicio de Empleados (Administrador)
 * Valida el login del administrador contra la tabla EMPLEADOS de la BD
 */
public class EmpleadoService {

    private final EmpleadoRepositoryImpl repositorio = new EmpleadoRepositoryImpl();

    // ─────────────────────────────────────────────────────────
    // LOGIN ADMINISTRADOR
    // Verifica que el empleado exista en la BD
    // P3 llama esto desde entrarAdminButton
    // ─────────────────────────────────────────────────────────
    public boolean loginAdmin(String correo, String contrasena) throws Exception {

        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("Ingresa el correo de administrador.");
        }
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new Exception("Ingresa la contraseña.");
        }

        // Buscar en la tabla EMPLEADOS por nombre (cuando P1 agregue correo usar correo)
        // Por ahora validamos con credenciales fijas de admin
        boolean esAdmin = correo.trim().equals("admin@kampets.com")
                && contrasena.equals("admin123");

        if (!esAdmin) {
            throw new Exception("Credenciales de administrador incorrectas.");
        }

        return true;
    }

    // ─────────────────────────────────────────────────────────
    // LISTAR EMPLEADOS
    // Solo visible para el admin (RF-07)
    // ─────────────────────────────────────────────────────────
    public List<Empleados> listarTodos() {
        return repositorio.buscarTodos();
    }
}
