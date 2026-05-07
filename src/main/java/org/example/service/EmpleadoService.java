package org.example.service;

import org.example.model.Empleados;
import org.example.repository.EmpleadoRepositoryImpl;

import java.util.List;

public class EmpleadoService {

    private final EmpleadoRepositoryImpl repositorio = new EmpleadoRepositoryImpl();

    // ─────────────────────────────────────────────────────────
    // LOGIN ADMINISTRADOR
    // Busca el empleado en la BD por correo y valida contraseña
    // ─────────────────────────────────────────────────────────
    public boolean loginAdmin(String correo, String contrasena) throws Exception {

        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("Ingresa el correo de administrador.");
        }
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new Exception("Ingresa la contraseña.");
        }

        // Buscar empleado en la BD por correo
        Empleados empleado = repositorio.buscarPorCorreo(correo.trim());

        if (empleado == null) {
            throw new Exception("No existe un administrador con ese correo.");
        }

        if (!empleado.getContrasena().equals(contrasena)) {
            throw new Exception("Contraseña incorrecta.");
        }

        return true;
    }

    // ─────────────────────────────────────────────────────────
    // LISTAR EMPLEADOS
    // ─────────────────────────────────────────────────────────
    public List<Empleados> listarTodos() {
        return repositorio.buscarTodos();
    }
}
