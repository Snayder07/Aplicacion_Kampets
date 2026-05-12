package org.example.controller;

import org.example.view.Main;
import org.example.model.Cliente;
import org.example.model.Empleados;
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
            Main.clienteActual  = cliente;
            Main.empleadoActual = null;
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
            // Primero validar credenciales
            boolean esAdmin = empleadoService.loginAdmin(correo, contrasena);
            if (esAdmin) {
                // Guardar el empleado logueado
                Empleados empleado = empleadoService.buscarPorCorreo(correo.trim());
                Main.clienteActual  = null;
                Main.empleadoActual = empleado;
                Main.cambiarPantalla("panelAdmin");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel,
                    e.getMessage(), "Error de acceso", JOptionPane.ERROR_MESSAGE);
        }
    }
}
