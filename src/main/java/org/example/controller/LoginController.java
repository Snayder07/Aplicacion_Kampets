package org.example.controller;

import org.example.view.Main;
import org.example.model.Cliente;
import org.example.service.ClienteService;
import org.example.service.EmpleadoService;

import javax.swing.*;

public class LoginController {

    private final ClienteService  clienteService  = new ClienteService();
    private final EmpleadoService empleadoService = new EmpleadoService();

    // ── LOGIN CLIENTE ─────────────────────────────────────
    public Cliente loginCliente(String correo, String contrasena, JPanel panel) {
        try {
            Cliente cliente = clienteService.loginCliente(correo, contrasena);
            // Guardar cliente en sesión para usarlo en todas las pantallas
            Main.clienteActual = cliente;
            Main.cambiarPantalla("panelCliente");
            return cliente;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel,
                    e.getMessage(), "Error de acceso", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // ── LOGIN ADMIN ───────────────────────────────────────
    public void loginAdmin(String correo, String contrasena, JPanel panel) {
        try {
            boolean esAdmin = empleadoService.loginAdmin(correo, contrasena);
            if (esAdmin) {
                Main.clienteActual = null; // limpiar cliente al entrar como admin
                Main.cambiarPantalla("panelAdmin");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel,
                    e.getMessage(), "Error de acceso", JOptionPane.ERROR_MESSAGE);
        }
    }
}