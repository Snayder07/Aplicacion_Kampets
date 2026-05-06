package org.example.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PanelCliente {
    public JPanel panel;

    private boolean temaOscuro = false;

    private final Color[] CLARO = {
            new Color(240, 246, 252),
            new Color(26,  74,  122),
            Color.WHITE,
            new Color(42,  90,  138),
            new Color(230, 240, 250),
            Color.WHITE,
            new Color(26,  58,   90),
            new Color(138, 170, 200),
            new Color(224, 112,  32),
            new Color(208, 228, 244),
            new Color(15,  53,   96),
            new Color(122, 175, 212),
            new Color(168, 200, 232),
            new Color(168, 212, 245),
    };

    private final Color[] OSCURO = {
            new Color(18,  24,  38),
            new Color(13,  18,  30),
            new Color(26,  34,  52),
            new Color(37,  55,  90),
            new Color(32,  42,  64),
            Color.WHITE,
            new Color(226, 232, 240),
            new Color(100, 116, 139),
            new Color(251, 146,  60),
            new Color(30,  41,  59),
            new Color(9,   14,  24),
            new Color(122, 175, 212),
            new Color(80,  120, 170),
            new Color(100, 160, 210),
    };

    private Color[] C = CLARO;

    public PanelCliente() {
        panel = new JPanel(new BorderLayout());
        construir();
    }

    public void setTema(boolean oscuro) {
        if (oscuro != temaOscuro) {
            temaOscuro = oscuro;
            construir();
        }
    }

    private void construir() {
        panel.removeAll();
        C = temaOscuro ? OSCURO : CLARO;
        panel.setBackground(C[0]);
        panel.add(crearSidebar(),   BorderLayout.WEST);
        panel.add(crearContenido(), BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private JButton crearBoton(String texto, Color fondo, Color textColor, boolean borde) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setBackground(fondo);
        btn.setForeground(textColor);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        if (borde) btn.setBorder(BorderFactory.createLineBorder(textColor, 1));
        else btn.setBorderPainted(false);
        return btn;
    }

    private JLabel crearLabel(String texto, int size, int style, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", style, size));
        lbl.setForeground(color);
        return lbl;
    }

    // ════════════════════════════════════════════════════
    // SIDEBAR
    // ════════════════════════════════════════════════════
    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(C[1]);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

        // Logo
        JLabel logo;
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
            Image logoImg = logoIcon.getImage().getScaledInstance(160, 55, Image.SCALE_SMOOTH);
            logo = new JLabel(new ImageIcon(logoImg));
        } catch (Exception e) {
            logo = crearLabel("Kampets", 18, Font.BOLD, C[5]);
        }
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(16));

        JSeparator sep = new JSeparator();
        sep.setForeground(C[3]);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(12));

        // ── Sección PRINCIPAL ────────────────────────────
        JLabel secPrincipal = crearLabel("PRINCIPAL", 10, Font.PLAIN, C[11]);
        secPrincipal.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(secPrincipal);
        sidebar.add(Box.createVerticalStrut(5));

        // Inicio — activo por defecto (resaltado)
        JButton btnInicio = crearBoton("Inicio", C[2], C[1], false);
        btnInicio.setFont(new Font("Arial", Font.BOLD, 13));
        btnInicio.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnInicio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnInicio.setHorizontalAlignment(SwingConstants.LEFT);
        btnInicio.addActionListener(e -> Main.cambiarPantalla("panelCliente")); // ← vuelve a inicio
        sidebar.add(btnInicio);
        sidebar.add(Box.createVerticalStrut(3));

        // Mis citas
        JButton btnMisCitas = crearBoton("Mis citas", C[1], C[5], false);
        btnMisCitas.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnMisCitas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnMisCitas.setHorizontalAlignment(SwingConstants.LEFT);
        btnMisCitas.addActionListener(e -> Main.cambiarPantalla("misCitas")); // ← va a Mis Citas
        sidebar.add(btnMisCitas);
        sidebar.add(Box.createVerticalStrut(3));

        // Historial
        JButton btnHistorial = crearBoton("Historial", C[1], C[5], false);
        btnHistorial.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnHistorial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnHistorial.setHorizontalAlignment(SwingConstants.LEFT);
        btnHistorial.addActionListener(e -> Main.cambiarPantalla("historial")); // ← va a Historial
        sidebar.add(btnHistorial);
        sidebar.add(Box.createVerticalStrut(12));

        // ── Sección SERVICIOS ────────────────────────────
        JLabel secServicios = crearLabel("SERVICIOS", 10, Font.PLAIN, C[11]);
        secServicios.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(secServicios);
        sidebar.add(Box.createVerticalStrut(5));

        // Alimentos
        JButton btnAlimentos = crearBoton("Alimentos", C[1], C[5], false);
        btnAlimentos.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAlimentos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnAlimentos.setHorizontalAlignment(SwingConstants.LEFT);
        btnAlimentos.addActionListener(e -> Main.cambiarPantalla("alimentos")); // ← va a Alimentos
        sidebar.add(btnAlimentos);
        sidebar.add(Box.createVerticalStrut(3));

        // Vacunas
        JButton btnVacunas = crearBoton("Vacunas", C[1], C[5], false);
        btnVacunas.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVacunas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnVacunas.setHorizontalAlignment(SwingConstants.LEFT);
        btnVacunas.addActionListener(e -> Main.cambiarPantalla("vacunas")); // ← va a Vacunas
        sidebar.add(btnVacunas);
        sidebar.add(Box.createVerticalStrut(3));

        sidebar.add(Box.createVerticalGlue());

        // Cerrar sesión
        JButton btnCerrar = crearBoton("Cerrar sesion", C[1], C[12], true);
        btnCerrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCerrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnCerrar.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(panel,
                    "Deseas cerrar sesion?", "Cerrar sesion", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                Main.frame.setExtendedState(JFrame.NORMAL);
                Main.frame.setSize(420, 520);
                Main.frame.setLocationRelativeTo(null);
                Main.cambiarPantalla("login");
            }
        });
        sidebar.add(btnCerrar);
        sidebar.add(Box.createVerticalStrut(8));

        // Usuario
        JPanel userPanel = new JPanel(new BorderLayout(8, 0));
        userPanel.setBackground(C[10]);
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        userPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel avatarLbl = crearLabel("MF", 13, Font.BOLD, C[1]);
        avatarLbl.setBackground(C[5]);
        avatarLbl.setOpaque(true);
        avatarLbl.setPreferredSize(new Dimension(34, 34));
        avatarLbl.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        userInfo.setBackground(C[10]);
        userInfo.add(crearLabel("Maria Fernanda", 12, Font.BOLD, C[5]));
        userInfo.add(crearLabel("Cliente", 10, Font.PLAIN, C[11]));

        userPanel.add(avatarLbl, BorderLayout.WEST);
        userPanel.add(userInfo,  BorderLayout.CENTER);
        sidebar.add(userPanel);

        return sidebar;
    }

    // ════════════════════════════════════════════════════
    // CONTENIDO PRINCIPAL
    // ════════════════════════════════════════════════════
    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(C[0]);

        // Topbar
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(C[2]);
        topbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, C[9]),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)
        ));

        JPanel topLeft = new JPanel(new GridLayout(2, 1));
        topLeft.setBackground(C[2]);
        topLeft.add(crearLabel("Bienvenida, Maria", 20, Font.PLAIN, C[6]));
        topLeft.add(crearLabel("Martes, 6 de mayo de 2026", 12, Font.PLAIN, C[7]));

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topRight.setBackground(C[2]);

        // Botón tema
        JButton btnTema = new JButton(temaOscuro ? "☀  Claro" : "🌙  Oscuro");
        btnTema.setFont(new Font("Arial", Font.PLAIN, 13));
        btnTema.setBackground(C[0]);
        btnTema.setForeground(C[6]);
        btnTema.setOpaque(true);
        btnTema.setFocusPainted(false);
        btnTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTema.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1),
                BorderFactory.createEmptyBorder(7, 14, 7, 14)
        ));
        btnTema.addActionListener(e -> {
            temaOscuro = !temaOscuro;
            Main.aplicarTemaGlobal(temaOscuro); // ← sincroniza TODAS las pantallas
            construir();
        });

        // Botón agendar
        JButton btnAgendar = crearBoton("+ Agendar cita", C[1], C[5], false);
        btnAgendar.setFont(new Font("Arial", Font.BOLD, 13));
        btnAgendar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAgendar.addActionListener(e -> Main.cambiarPantalla("agendarCita"));

        topRight.add(btnTema);
        topRight.add(btnAgendar);
        topbar.add(topLeft,  BorderLayout.WEST);
        topbar.add(topRight, BorderLayout.EAST);
        contenido.add(topbar, BorderLayout.NORTH);

        // Centro
        JPanel centro = new JPanel(new BorderLayout(0, 16));
        centro.setBackground(C[0]);
        centro.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Próxima cita
        JPanel proximaCard = new JPanel(new BorderLayout());
        proximaCard.setBackground(C[1]);
        proximaCard.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel proximaIzq = new JPanel(new GridLayout(3, 1));
        proximaIzq.setBackground(C[1]);
        proximaIzq.add(crearLabel("PROXIMA CITA", 10, Font.PLAIN, C[13]));
        proximaIzq.add(crearLabel("Valentin — Chequeo general", 17, Font.BOLD, C[5]));
        proximaIzq.add(crearLabel("Dr. Ramirez · Consulta general", 12, Font.PLAIN, C[12]));

        JPanel proximaDer = new JPanel(new GridLayout(3, 1));
        proximaDer.setBackground(C[1]);
        JLabel mes = crearLabel("MARZO", 11, Font.PLAIN, C[11]);
        mes.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel dia = crearLabel("6", 36, Font.BOLD, C[5]);
        dia.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel hora = crearLabel("10:30 AM", 12, Font.PLAIN, C[13]);
        hora.setHorizontalAlignment(SwingConstants.RIGHT);
        proximaDer.add(mes); proximaDer.add(dia); proximaDer.add(hora);
        proximaCard.add(proximaIzq, BorderLayout.CENTER);
        proximaCard.add(proximaDer, BorderLayout.EAST);

        // Lista de citas
        JPanel citasPanel = new JPanel(new BorderLayout(0, 10));
        citasPanel.setBackground(C[0]);

        JPanel citasHeader = new JPanel(new BorderLayout());
        citasHeader.setBackground(C[0]);
        citasHeader.add(crearLabel("MIS CITAS", 11, Font.PLAIN, C[7]), BorderLayout.WEST);

        JButton verTodo = crearBoton("Ver todas", C[0], C[1], false);
        verTodo.setFont(new Font("Arial", Font.BOLD, 12));
        verTodo.addActionListener(e -> Main.cambiarPantalla("misCitas")); // ← ver todas las citas
        citasHeader.add(verTodo, BorderLayout.EAST);
        citasPanel.add(citasHeader, BorderLayout.NORTH);

        JPanel listaCitas = new JPanel(new GridLayout(3, 1, 0, 8));
        listaCitas.setBackground(C[0]);
        listaCitas.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        String[][] citas = {
                {"Valentin — Chequeo general", "Mar 6 · 10:30 AM · Dr. Ramirez", "Confirmada",    "#1a4a7a"},
                {"Sacha — Vacunacion",         "Vie 9 · 3:00 PM · Dr. Gomez",    "Pendiente",     "#e07020"},
                {"Mia — Revision dental",      "Lun 12 · 9:00 AM · Dra. Torres", "Por confirmar", "#aaaaaa"}
        };

        for (String[] cita : citas) {
            JPanel card = new JPanel(new BorderLayout(10, 0));
            card.setBackground(C[2]);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, Color.decode(cita[3])),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(C[9], 1),
                            BorderFactory.createEmptyBorder(14, 16, 14, 16)
                    )
            ));

            JPanel info = new JPanel(new GridLayout(2, 1, 0, 4));
            info.setBackground(C[2]);
            info.add(crearLabel(cita[0], 14, Font.BOLD, C[6]));
            info.add(crearLabel(cita[1], 12, Font.PLAIN, C[7]));

            JPanel derecha = new JPanel(new GridLayout(2, 1, 0, 6));
            derecha.setBackground(C[2]);

            JLabel badge = crearLabel(cita[2], 11, Font.BOLD, Color.decode(cita[3]));
            badge.setHorizontalAlignment(SwingConstants.RIGHT);

            JLabel cancelar = crearLabel("Cancelar cita", 11, Font.PLAIN,
                    temaOscuro ? new Color(80, 110, 150) : new Color(176, 200, 224));
            cancelar.setHorizontalAlignment(SwingConstants.RIGHT);
            cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            derecha.add(badge);
            derecha.add(cancelar);
            card.add(info,    BorderLayout.CENTER);
            card.add(derecha, BorderLayout.EAST);
            listaCitas.add(card);
        }

        citasPanel.add(listaCitas, BorderLayout.CENTER);
        centro.add(proximaCard, BorderLayout.NORTH);
        centro.add(citasPanel,  BorderLayout.CENTER);
        contenido.add(centro,   BorderLayout.CENTER);

        return contenido;
    }
}