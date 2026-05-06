package org.example.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PanelAgendarCita {
    public JPanel panel;
    private boolean temaOscuro = false;

    private final Color[] CLARO = {
            new Color(240, 246, 252),  // 0 FONDO
            new Color(26,  74,  122),  // 1 AZUL
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

    public PanelAgendarCita() {
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
    private JTextField campo(String placeholder) {
        JTextField tf = new JTextField(placeholder);
        tf.setFont(new Font("Arial", Font.PLAIN, 13));
        tf.setForeground(C[7]);
        tf.setBackground(C[2]);
        tf.setCaretColor(C[6]);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (tf.getText().equals(placeholder)) {
                    tf.setText(""); tf.setForeground(C[6]);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(placeholder); tf.setForeground(C[7]);
                }
            }
        });
        return tf;
    }
    private JComboBox<String> combo(String[] opciones) {
        JComboBox<String> cb = new JComboBox<>(opciones);
        cb.setFont(new Font("Arial", Font.PLAIN, 13));
        cb.setBackground(C[2]);
        cb.setForeground(C[6]);
        cb.setBorder(BorderFactory.createLineBorder(C[9], 1));
        return cb;
    }

    // ════════════════════════════════════════════════════
    // SIDEBAR
    // ════════════════════════════════════════════════════
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
            JButton b = btn(mp[i], C[1], C[5], false);
            b.setFont(new Font("Arial", Font.PLAIN, 13));
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            b.setHorizontalAlignment(SwingConstants.LEFT);
            if (i == 0) b.addActionListener(e -> Main.cambiarPantalla("panelCliente"));
            if (i == 1) b.addActionListener(e -> Main.cambiarPantalla("misCitas"));
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
                Main.frame.setSize(420, 520);
                Main.frame.setLocationRelativeTo(null);
                Main.cambiarPantalla("login");
            }
        });
        sb.add(cerrar); sb.add(Box.createVerticalStrut(8));

        JPanel up = new JPanel(new BorderLayout(8, 0));
        up.setBackground(C[10]); up.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        up.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        up.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel av = lbl("MF", 13, Font.BOLD, C[1]); av.setBackground(C[5]); av.setOpaque(true);
        av.setPreferredSize(new Dimension(34, 34)); av.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel ui = new JPanel(new GridLayout(2, 1)); ui.setBackground(C[10]);
        ui.add(lbl("Maria Fernanda", 12, Font.BOLD, C[5]));
        ui.add(lbl("Cliente", 10, Font.PLAIN, C[11]));
        up.add(av, BorderLayout.WEST); up.add(ui, BorderLayout.CENTER);
        sb.add(up);
        return sb;
    }

    // ════════════════════════════════════════════════════
    // CONTENIDO
    // ════════════════════════════════════════════════════
    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(C[0]);

        // ── Topbar ────────────────────────────────────────
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(C[2]);
        topbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, C[9]),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)));

        JPanel topLeft = new JPanel(new GridLayout(2, 1));
        topLeft.setBackground(C[2]);
        topLeft.add(lbl("Agendar cita", 20, Font.BOLD, C[6]));
        topLeft.add(lbl("Completa los datos para reservar tu cita", 12, Font.PLAIN, C[7]));

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topRight.setBackground(C[2]);

        JButton btnTema = new JButton(temaOscuro ? "☀  Claro" : "🌙  Oscuro");
        btnTema.setFont(new Font("Arial", Font.PLAIN, 13));
        btnTema.setBackground(C[0]); btnTema.setForeground(C[6]); btnTema.setOpaque(true);
        btnTema.setFocusPainted(false); btnTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTema.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1),
                BorderFactory.createEmptyBorder(7, 14, 7, 14)));
        btnTema.addActionListener(e -> {
            temaOscuro = !temaOscuro;
            Main.aplicarTemaGlobal(temaOscuro);
            construir();
        });

        JButton btnVolver = btn("← Volver", C[0], C[1], true);
        btnVolver.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1),
                BorderFactory.createEmptyBorder(7, 14, 7, 14)));
        btnVolver.addActionListener(e -> Main.cambiarPantalla("panelCliente"));

        topRight.add(btnTema);
        topRight.add(btnVolver);
        topbar.add(topLeft,  BorderLayout.WEST);
        topbar.add(topRight, BorderLayout.EAST);
        contenido.add(topbar, BorderLayout.NORTH);

        // ── Formulario centrado ───────────────────────────
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(C[0]);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(C[2]);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1),
                BorderFactory.createEmptyBorder(32, 36, 32, 36)));

        // Título del formulario
        JLabel tituloForm = lbl("Nueva cita", 18, Font.BOLD, C[6]);
        tituloForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subForm = lbl("Selecciona mascota, servicio, veterinario, fecha y hora", 12, Font.PLAIN, C[7]);
        subForm.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(tituloForm);
        form.add(Box.createVerticalStrut(4));
        form.add(subForm);
        form.add(Box.createVerticalStrut(24));

        // ── Fila 1: Mascota ───────────────────────────────
        agregarCampoLabel(form, "Mascota");
        JComboBox<String> cbMascota = combo(new String[]{
                "Selecciona una mascota...", "🐶 Valentin — Perro", "🐶 Sacha — Perro", "🐱 Mia — Gato"
        });
        cbMascota.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbMascota.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        form.add(cbMascota);
        form.add(Box.createVerticalStrut(16));

        // ── Fila 2: Tipo de servicio ──────────────────────
        agregarCampoLabel(form, "Tipo de servicio");
        JComboBox<String> cbServicio = combo(new String[]{
                "Selecciona un servicio...",
                "Consulta general", "Vacunación", "Odontología",
                "Desparasitación", "Cirugía", "Control de peso", "Urgencias"
        });
        cbServicio.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbServicio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        form.add(cbServicio);
        form.add(Box.createVerticalStrut(16));

        // ── Fila 3: Veterinario ───────────────────────────
        agregarCampoLabel(form, "Veterinario");
        JComboBox<String> cbVet = combo(new String[]{
                "Selecciona un veterinario...",
                "Dr. Ramírez — Medicina general",
                "Dra. Torres — Odontología",
                "Dr. Gómez — Vacunación y prevención"
        });
        cbVet.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbVet.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        form.add(cbVet);
        form.add(Box.createVerticalStrut(16));

        // ── Fila 4: Fecha y Hora (en la misma fila) ───────
        JPanel filaFechaHora = new JPanel(new GridLayout(1, 2, 16, 0));
        filaFechaHora.setBackground(C[2]);
        filaFechaHora.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaFechaHora.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel colFecha = new JPanel();
        colFecha.setLayout(new BoxLayout(colFecha, BoxLayout.Y_AXIS));
        colFecha.setBackground(C[2]);
        agregarCampoLabel(colFecha, "Fecha");
        JTextField tfFecha = campo("dd/mm/aaaa");
        tfFecha.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfFecha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        colFecha.add(tfFecha);

        JPanel colHora = new JPanel();
        colHora.setLayout(new BoxLayout(colHora, BoxLayout.Y_AXIS));
        colHora.setBackground(C[2]);
        agregarCampoLabel(colHora, "Hora");
        JComboBox<String> cbHora = combo(new String[]{
                "Selecciona hora...",
                "8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM",
                "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM",
                "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM",
                "4:00 PM", "4:30 PM"
        });
        cbHora.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbHora.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        colHora.add(cbHora);

        filaFechaHora.add(colFecha);
        filaFechaHora.add(colHora);
        form.add(filaFechaHora);
        form.add(Box.createVerticalStrut(16));

        // ── Fila 5: Motivo / Notas ────────────────────────
        agregarCampoLabel(form, "Motivo de la consulta (opcional)");
        JTextArea taMotivo = new JTextArea(3, 1);
        taMotivo.setFont(new Font("Arial", Font.PLAIN, 13));
        taMotivo.setForeground(C[7]);
        taMotivo.setBackground(C[2]);
        taMotivo.setCaretColor(C[6]);
        taMotivo.setLineWrap(true);
        taMotivo.setWrapStyleWord(true);
        taMotivo.setText("Describe brevemente el motivo de la consulta...");
        taMotivo.addFocusListener(new java.awt.event.FocusAdapter() {
            final String placeholder = "Describe brevemente el motivo de la consulta...";
            public void focusGained(java.awt.event.FocusEvent e) {
                if (taMotivo.getText().equals(placeholder)) {
                    taMotivo.setText(""); taMotivo.setForeground(C[6]);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (taMotivo.getText().isEmpty()) {
                    taMotivo.setText(placeholder); taMotivo.setForeground(C[7]);
                }
            }
        });
        JScrollPane scrollMotivo = new JScrollPane(taMotivo);
        scrollMotivo.setBorder(BorderFactory.createLineBorder(C[9], 1));
        scrollMotivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollMotivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        form.add(scrollMotivo);
        form.add(Box.createVerticalStrut(28));

        // ── Botones ───────────────────────────────────────
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        botones.setBackground(C[2]);
        botones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCancelar = btn("Cancelar", C[4], C[1], false);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1),
                BorderFactory.createEmptyBorder(10, 22, 10, 22)));
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnCancelar.addActionListener(e -> Main.cambiarPantalla("panelCliente"));

        JButton btnConfirmar = btn("Confirmar cita", C[1], C[5], false);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 13));
        btnConfirmar.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btnConfirmar.addActionListener(e -> {
            // Validación básica
            if (cbMascota.getSelectedIndex() == 0 ||
                    cbServicio.getSelectedIndex() == 0 ||
                    cbVet.getSelectedIndex() == 0 ||
                    cbHora.getSelectedIndex() == 0 ||
                    tfFecha.getText().equals("dd/mm/aaaa") ||
                    tfFecha.getText().isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Por favor completa todos los campos obligatorios.",
                        "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(panel,
                    "✅ Cita agendada exitosamente!\n\n" +
                            "Mascota: "      + cbMascota.getSelectedItem()  + "\n" +
                            "Servicio: "     + cbServicio.getSelectedItem() + "\n" +
                            "Veterinario: "  + cbVet.getSelectedItem()      + "\n" +
                            "Fecha: "        + tfFecha.getText()            + "\n" +
                            "Hora: "         + cbHora.getSelectedItem(),
                    "Cita confirmada", JOptionPane.INFORMATION_MESSAGE);
            Main.cambiarPantalla("misCitas");
        });

        botones.add(btnCancelar);
        botones.add(btnConfirmar);
        form.add(botones);

        // Limitar ancho del formulario
        form.setPreferredSize(new Dimension(560, form.getPreferredSize().height));
        form.setMaximumSize(new Dimension(560, Integer.MAX_VALUE));

        outer.add(form);
        JScrollPane scrollOuter = new JScrollPane(outer);
        scrollOuter.setBorder(null);
        scrollOuter.getViewport().setBackground(C[0]);
        scrollOuter.getVerticalScrollBar().setUnitIncrement(12);
        contenido.add(scrollOuter, BorderLayout.CENTER);

        return contenido;
    }

    // ── Helpers de formulario ─────────────────────────────
    private void agregarCampoLabel(JPanel p, String texto) {
        JLabel l = lbl(texto, 12, Font.BOLD, C[6]);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        p.add(l);
    }

    private void agregarSep(JPanel p) {
        JSeparator s = new JSeparator(); s.setForeground(C[3]);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(Box.createVerticalStrut(6)); p.add(s); p.add(Box.createVerticalStrut(6));
    }

    private void agregarSeccion(JPanel p, String t) {
        JLabel l = lbl(t, 10, Font.PLAIN, C[11]);
        l.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        p.add(l);
    }
}