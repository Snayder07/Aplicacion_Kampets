package org.example.controller;

import org.example.view.Main;
import org.example.model.Cliente;
import org.example.service.ClienteService;

import javax.swing.*;
import java.time.LocalDate;

/**
 * CONTROLADOR DE REGISTRO
 * Conecta el botón registrarButton de CrearCuenta.java con ClienteService
 */
public class RegistroController {

    private final ClienteService clienteService = new ClienteService();

    // ─────────────────────────────────────────────────────────
    // REGISTRAR CLIENTE
    // Llamar desde registrarButton en CrearCuenta.java
    // ─────────────────────────────────────────────────────────
    public void registrar(String nombre, String apellido, String correo,
                          String telefono, String contrasena,
                          String confirmar, JPanel panel) {
        try {
            // Unir nombre y apellido
            String nombreCompleto = nombre.trim() + " " + apellido.trim();

            // Llamar al servicio de P2 para registrar
            clienteService.registrar(
                    nombreCompleto,
                    correo,
                    telefono,
                    "",   // dirección vacía por ahora
                    contrasena,
                    confirmar
            );

            // Registro exitoso
            JOptionPane.showMessageDialog(panel,
                    "¡Cuenta creada exitosamente! Ya puedes iniciar sesión.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE);

            // Volver al login
            Main.cambiarPantalla("login");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel,
                    e.getMessage(),
                    "Error al registrar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
