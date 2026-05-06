package org.example.view;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static JFrame frame      = new JFrame("Kampets");
    public static JPanel contenedor = new JPanel(new CardLayout());

    // ── Instancias de cada panel ──────────────────────────
    // Se guardan como variables para poder llamar setTema() en todas
    private static PanelCliente       panelCliente       = new PanelCliente();
    private static PanelAdmin         panelAdmin         = new PanelAdmin();
    private static PanelAdminCitas    panelAdminCitas    = new PanelAdminCitas();
    private static PanelAdminMascotas panelAdminMascotas = new PanelAdminMascotas();
    private static PanelAdminVacunas  panelAdminVacunas  = new PanelAdminVacunas();
    private static PanelAdminInventario panelAdminInventario = new PanelAdminInventario();
    private static PanelAdminReportes panelAdminReportes = new PanelAdminReportes();
    private static PanelMisCitas      panelMisCitas      = new PanelMisCitas();
    private static PanelHistorial     panelHistorial     = new PanelHistorial();
    private static PanelAlimentos     panelAlimentos     = new PanelAlimentos();
    private static PanelVacunas       panelVacunas       = new PanelVacunas();
    private static PanelAgendarCita   panelAgendarCita   = new PanelAgendarCita();

    // ── Lista de administradores ──────────────────────────
    private static final String[][] ADMINS = {
            {"admin@kampets.com", "admin123"}
    };

    public static boolean esAdmin(String correo, String clave) {
        for (String[] admin : ADMINS) {
            if (admin[0].equals(correo) && admin[1].equals(clave)) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        // ── Agregar todas las pantallas al contenedor ─────
        contenedor.add(new Interfaz_Grafica_Kampets().panel, "login");
        contenedor.add(new CrearCuenta().panel,              "crearCuenta");
        contenedor.add(panelCliente.panel,                   "panelCliente");
        contenedor.add(panelAdmin.panel,                     "panelAdmin");
        contenedor.add(panelAdminCitas.panel,                "adminCitas");
        contenedor.add(panelAdminMascotas.panel,             "adminMascotas");
        contenedor.add(panelAdminVacunas.panel,              "adminVacunas");
        contenedor.add(panelAdminInventario.panel,           "adminInventario");
        contenedor.add(panelAdminReportes.panel,             "adminReportes");
        contenedor.add(panelMisCitas.panel,                  "misCitas");
        contenedor.add(panelHistorial.panel,                 "historial");
        contenedor.add(panelAlimentos.panel,                 "alimentos");
        contenedor.add(panelVacunas.panel,                   "vacunas");
        contenedor.add(panelAgendarCita.panel,               "agendarCita");

        frame.setContentPane(contenedor);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 520);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        cambiarPantalla("login");
    }

    public static void expandirVentana() {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    // ── Cambiar pantalla ──────────────────────────────────
    public static void cambiarPantalla(String nombre) {
        CardLayout cl = (CardLayout) contenedor.getLayout();
        cl.show(contenedor, nombre);

        // Ajustar tamaño de ventana según la pantalla
        switch (nombre) {
            case "login":
            case "crearCuenta":
                frame.setExtendedState(JFrame.NORMAL);
                frame.setSize(420, 520);
                frame.setLocationRelativeTo(null);
                break;
            default:
                expandirVentana();
                break;
        }
    }

    // ── Sincronizar tema en todos los paneles ─────────────
    // Llama esto desde PanelAdmin o PanelCliente cuando el usuario cambia el tema
    public static void aplicarTemaGlobal(boolean oscuro) {
        panelCliente.setTema(oscuro);
        panelAdminCitas.setTema(oscuro);
        panelAdminMascotas.setTema(oscuro);
        panelAdminVacunas.setTema(oscuro);
        panelAdminInventario.setTema(oscuro);
        panelAdminReportes.setTema(oscuro);
        panelMisCitas.setTema(oscuro);
        panelHistorial.setTema(oscuro);
        panelAlimentos.setTema(oscuro);
        panelVacunas.setTema(oscuro);
        panelAgendarCita.setTema(oscuro);
    }
}