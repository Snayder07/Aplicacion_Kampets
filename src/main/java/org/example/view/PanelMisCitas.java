package org.example.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PanelMisCitas {
    public JPanel panel;
    private boolean temaOscuro = false;

    private final Color[] CLARO = {
            new Color(240, 246, 252),  // 0 FONDO
            new Color(26,  74,  122),  // 1 SIDEBAR_BG
            Color.WHITE,               // 2 CARD_BG
            new Color(42,  90,  138),  // 3 AZUL_MEDIO
            new Color(230, 240, 250),  // 4 AZUL_CLARO
            Color.WHITE,               // 5 TEXTO_BLANCO
            new Color(26,  58,   90),  // 6 TEXTO_OSCURO
            new Color(138, 170, 200),  // 7 TEXTO_GRIS
            new Color(224, 112,  32),  // 8 NARANJA
            new Color(208, 228, 244),  // 9 BORDE
            new Color(15,  53,   96),  // 10 USER_BG
            new Color(122, 175, 212),  // 11 SIDEBAR_DIM
            new Color(168, 200, 232),  // 12 BORDE_CERRAR
            new Color(168, 212, 245),  // 13 AZUL_LABEL
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

    public PanelMisCitas() {
        panel = new JPanel(new BorderLayout());
        construir();
    }

    public void setTema(boolean oscuro) {
        if (oscuro != temaOscuro) { temaOscuro = oscuro; construir(); }
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

    // ── Helpers ───────────────────────────────────────────
    private JLabel lbl(String t, int sz, int st, Color c) {
        JLabel l = new JLabel(t); l.setFont(new Font("Arial", st, sz)); l.setForeground(c); return l;
    }
    private JButton btn(String t, Color bg, Color fg, boolean borde) {
        JButton b = new JButton(t); b.setFont(new Font("Arial", Font.PLAIN, 13));
        b.setBackground(bg); b.setForeground(fg); b.setOpaque(true);
        b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (borde) b.setBorder(BorderFactory.createLineBorder(fg, 1)); else b.setBorderPainted(false);
        return b;
    }

    // ── Sidebar ───────────────────────────────────────────
    private JPanel crearSidebar() {
        JPanel sb = new JPanel();
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBackground(C[1]);
        sb.setPreferredSize(new Dimension(220, 0));
        sb.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

        JLabel logo;
        try {
            ImageIcon ic = new ImageIcon(getClass().getResource("/logo.png"));
            logo = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(160, 55, Image.SCALE_SMOOTH)));
        } catch (Exception e) { logo = lbl("Kampets", 18, Font.BOLD, C[5]); }
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sb.add(logo); sb.add(Box.createVerticalStrut(16));
        agregarSep(sb);

        agregarSeccion(sb, "PRINCIPAL");
        String[] mp = {"Inicio", "Mis citas", "Historial"};
        for (int i = 0; i < mp.length; i++) {
            JButton b = btn(mp[i], i == 1 ? C[2] : C[1], i == 1 ? C[1] : C[5], false);
            b.setFont(new Font("Arial", i == 1 ? Font.BOLD : Font.PLAIN, 13));
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            b.setHorizontalAlignment(SwingConstants.LEFT);
            if (i == 0) b.addActionListener(e -> Main.cambiarPantalla("panelCliente"));
            if (i == 2) b.addActionListener(e -> Main.cambiarPantalla("historial"));
            sb.add(b); sb.add(Box.createVerticalStrut(3));
        }
        sb.add(Box.createVerticalStrut(12));
        agregarSeccion(sb, "SERVICIOS");
        String[] ms = {"Alimentos", "Vacunas"};
        for (String item : ms) {
            JButton b = btn(item, C[1], C[5], false);
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            b.setHorizontalAlignment(SwingConstants.LEFT);
            if (item.equals("Alimentos")) b.addActionListener(e -> Main.cambiarPantalla("alimentos"));
            if (item.equals("Vacunas"))   b.addActionListener(e -> Main.cambiarPantalla("vacunas"));
            sb.add(b); sb.add(Box.createVerticalStrut(3));
        }
        sb.add(Box.createVerticalGlue());

        JButton cerrar = btn("Cerrar sesion", C[1], C[12], true);
        cerrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        cerrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cerrar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(panel, "Deseas cerrar sesion?", "Cerrar sesion",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Main.frame.setSize(420, 520); Main.frame.setLocationRelativeTo(null);
                Main.cambiarPantalla("login");
            }
        });
        sb.add(cerrar); sb.add(Box.createVerticalStrut(8));

        JPanel up = new JPanel(new BorderLayout(8, 0));
        up.setBackground(C[10]); up.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        up.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        up.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel av = lbl("MF", 13, Font.BOLD, C[1]); av.setBackground(C[5]); av.setOpaque(true);
        av.setPreferredSize(new Dimension(34,34)); av.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel ui = new JPanel(new GridLayout(2,1)); ui.setBackground(C[10]);
        ui.add(lbl("Maria Fernanda", 12, Font.BOLD, C[5]));
        ui.add(lbl("Cliente", 10, Font.PLAIN, C[11]));
        up.add(av, BorderLayout.WEST); up.add(ui, BorderLayout.CENTER);
        sb.add(up);
        return sb;
    }

    // ── Contenido ─────────────────────────────────────────
    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(C[0]);

        // Topbar
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(C[2]);
        topbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0, C[9]),
                BorderFactory.createEmptyBorder(16,24,16,24)));

        JPanel topLeft = new JPanel(new GridLayout(2,1));
        topLeft.setBackground(C[2]);
        topLeft.add(lbl("Mis citas", 20, Font.BOLD, C[6]));
        topLeft.add(lbl("Gestiona y revisa todas tus citas", 12, Font.PLAIN, C[7]));

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topRight.setBackground(C[2]);

        String iconoTema = temaOscuro ? "☀  Claro" : "🌙  Oscuro";
        JButton btnTema = new JButton(iconoTema);
        btnTema.setFont(new Font("Arial", Font.PLAIN, 13));
        btnTema.setBackground(C[0]); btnTema.setForeground(C[6]); btnTema.setOpaque(true);
        btnTema.setFocusPainted(false); btnTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTema.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1),
                BorderFactory.createEmptyBorder(7,14,7,14)));
        btnTema.addActionListener(e -> { temaOscuro = !temaOscuro; construir(); });

        JButton btnAgendar = btn("+ Agendar cita", C[1], C[5], false);
        btnAgendar.setFont(new Font("Arial", Font.BOLD, 13));
        btnAgendar.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        btnAgendar.addActionListener(e -> Main.cambiarPantalla("agendarCita"));

        topRight.add(btnTema); topRight.add(btnAgendar);
        topbar.add(topLeft, BorderLayout.WEST);
        topbar.add(topRight, BorderLayout.EAST);
        contenido.add(topbar, BorderLayout.NORTH);

        // Cuerpo con scroll
        JPanel cuerpo = new JPanel(new BorderLayout(0, 20));
        cuerpo.setBackground(C[0]);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));

        // Filtros
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filtros.setBackground(C[0]);
        String[] filtroNombres = {"Todas", "Confirmadas", "Pendientes", "Canceladas"};
        Color[] filtroColores  = {C[1], new Color(22,163,74), new Color(234,88,12), new Color(220,38,38)};
        for (int i = 0; i < filtroNombres.length; i++) {
            JButton f = new JButton(filtroNombres[i]);
            f.setFont(new Font("Arial", Font.PLAIN, 12));
            f.setBackground(i == 0 ? C[1] : C[2]);
            f.setForeground(i == 0 ? C[5] : C[7]);
            f.setOpaque(true); f.setFocusPainted(false);
            f.setCursor(new Cursor(Cursor.HAND_CURSOR));
            f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(i == 0 ? C[1] : C[9], 1),
                    BorderFactory.createEmptyBorder(6,14,6,14)));
            filtros.add(f);
        }
        cuerpo.add(filtros, BorderLayout.NORTH);

        // Lista de citas
        String[][] citas = {
                {"Valentin — Chequeo general",  "Mar 6 · 10:30 AM",  "Dr. Ramírez",  "Consulta general", "Confirmada",    "#1a4a7a"},
                {"Sacha — Vacunación",           "Vie 9 · 3:00 PM",   "Dr. Gómez",    "Vacunación",       "Pendiente",     "#e07020"},
                {"Mia — Revisión dental",        "Lun 12 · 9:00 AM",  "Dra. Torres",  "Odontología",      "Por confirmar", "#aaaaaa"},
                {"Valentin — Control de peso",   "Jue 15 · 11:00 AM", "Dr. Ramírez",  "Consulta general", "Confirmada",    "#1a4a7a"},
                {"Sacha — Desparasitación",      "Mar 20 · 2:00 PM",  "Dr. Gómez",    "Prevención",       "Pendiente",     "#e07020"},
        };

        JPanel lista = new JPanel(new GridLayout(citas.length, 1, 0, 10));
        lista.setBackground(C[0]);

        for (String[] c : citas) {
            JPanel card = new JPanel(new BorderLayout(12, 0));
            card.setBackground(C[2]);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, Color.decode(c[5])),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(C[9], 1),
                            BorderFactory.createEmptyBorder(16, 18, 16, 18))));

            // Info izquierda
            JPanel izq = new JPanel(new GridLayout(3, 1, 0, 3));
            izq.setBackground(C[2]);
            izq.add(lbl(c[0], 14, Font.BOLD, C[6]));
            izq.add(lbl(c[1] + "  ·  " + c[2], 12, Font.PLAIN, C[7]));
            izq.add(lbl("Tipo: " + c[3], 11, Font.PLAIN, C[11]));

            // Derecha: badge + botones
            JPanel der = new JPanel(new GridLayout(3, 1, 0, 4));
            der.setBackground(C[2]);

            JLabel badge = lbl(c[4], 11, Font.BOLD, Color.decode(c[5]));
            badge.setHorizontalAlignment(SwingConstants.RIGHT);

            JLabel cancelar = lbl("Cancelar", 11, Font.PLAIN,
                    temaOscuro ? new Color(80,110,150) : new Color(176,200,224));
            cancelar.setHorizontalAlignment(SwingConstants.RIGHT);
            cancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel reprogramar = lbl("Reprogramar", 11, Font.PLAIN,
                    temaOscuro ? new Color(59,130,200) : C[1]);
            reprogramar.setHorizontalAlignment(SwingConstants.RIGHT);
            reprogramar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            der.add(badge); der.add(reprogramar); der.add(cancelar);
            card.add(izq, BorderLayout.CENTER);
            card.add(der, BorderLayout.EAST);
            lista.add(card);
        }

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(C[0]);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        cuerpo.add(scroll, BorderLayout.CENTER);

        contenido.add(cuerpo, BorderLayout.CENTER);
        return contenido;
    }

    private void agregarSeccion(JPanel p, String t) {
        JLabel l = lbl(t, 10, Font.PLAIN, C[11]);
        l.setBorder(BorderFactory.createEmptyBorder(8,0,4,0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        p.add(l);
    }
    private void agregarSep(JPanel p) {
        JSeparator s = new JSeparator(); s.setForeground(C[3]);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(Box.createVerticalStrut(6)); p.add(s); p.add(Box.createVerticalStrut(6));
    }
}