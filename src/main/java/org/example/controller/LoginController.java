package org.example.controller;

import org.example.view.Main;
import org.example.model.Cliente;
import org.example.model.Empleados;
import org.example.service.ClienteService;
import org.example.service.EmpleadoService;

import javax.swing.*;

/**
 * CONTROLADOR DE LOGIN
 * Conecta los botones de Interfaz_Grafica_Kampets con los servicios de P2
 *
 * Cómo usarlo en Interfaz_Grafica_Kampets.java:
 * Reemplaza las acciones de los botones entrarButton y entrarAdminButton
 * por llamadas a este controlador
 */
public class LoginController {

    private final ClienteService clienteService = new ClienteService();
    private final EmpleadoService empleadoService = new EmpleadoService();

    // ─────────────────────────────────────────────────────────
    // LOGIN CLIENTE
    // Llamar desde entrarButton en Interfaz_Grafica_Kampets.java
    // Retorna el Cliente si el login es exitoso, null si falla
    // ─────────────────────────────────────────────────────────
    public Cliente loginCliente(String correo, String contrasena, JPanel panel) {
        try {
            Cliente cliente = clienteService.loginCliente(correo, contrasena);
            // Login exitoso → cambiar a pantalla cliente
            Main.cambiarPantalla("panelCliente");
            return cliente;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel,
                    e.getMessage(),
                    "Error de acceso",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────
    // LOGIN ADMIN
    // Llamar desde entrarAdminButton en Interfaz_Grafica_Kampets.java
    // ─────────────────────────────────────────────────────────
    public void loginAdmin(String correo, String contrasena, JPanel panel) {
        try {
            boolean esAdmin = empleadoService.loginAdmin(correo, contrasena);
            if (esAdmin) {
                // Login admin exitoso → cambiar a pantalla admin
                Main.cambiarPantalla("panelAdmin");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel,
                    e.getMessage(),
                    "Error de acceso",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
