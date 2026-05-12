package org.example.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class SidebarAdmin {

    public static JPanel crear(Color[] C, boolean temaOscuro, String pantallaActual, JPanel panelRef) {
        JPanel sb = new JPanel();
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBackground(C[1]);
        sb.setPreferredSize(new Dimension(220, 0));

        // ── Logo ─────────────────────────────────────────
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(C[1]);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(22, 20, 10, 20));
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel lt = new JPanel(new GridLayout(2, 1));
        lt.setBackground(C[1]);
        JLabel ln = new JLabel("Kampets");
        ln.setFont(new Font("Arial", Font.BOLD, 20));
        ln.setForeground(Color.WHITE);
        JLabel ls = new JLabel("Panel administrativo");
        ls.setFont(new Font("Arial", Font.PLAIN, 10));
        ls.setForeground(C[11]);
        lt.add(ln); lt.add(ls);
        logoPanel.add(lt, BorderLayout.CENTER);
        sb.add(logoPanel);
        agregarSep(sb, C);

        // ── GENERAL ──────────────────────────────────────
        agregarSeccion(sb, "GENERAL", C);

        JButton btnInicio = crearItem("⊞  Inicio", pantallaActual.equals("panelAdmin"), C);
        btnInicio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("panelAdmin"); }
        });
        sb.add(btnInicio); sb.add(Box.createVerticalStrut(2));

        JButton btnCitas = crearItem("📅  Todas las citas", pantallaActual.equals("adminCitas"), C);
        btnCitas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("adminCitas"); }
        });
        sb.add(btnCitas); sb.add(Box.createVerticalStrut(2));

        JButton btnMascotas = crearItem("🐾  Mascotas registradas", pantallaActual.equals("adminMascotas"), C);
        btnMascotas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("adminMascotas"); }
        });
        sb.add(btnMascotas); sb.add(Box.createVerticalStrut(2));

        agregarSep(sb, C);

        // ── CLÍNICA ───────────────────────────────────────
        agregarSeccion(sb, "CLÍNICA", C);

        JButton btnVacunas = crearItem("💉  Vacunas pendientes", pantallaActual.equals("adminVacunas"), C);
        btnVacunas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("adminVacunas"); }
        });
        sb.add(btnVacunas); sb.add(Box.createVerticalStrut(2));

        JButton btnInventario = crearItem("📦  Inventario", pantallaActual.equals("adminInventario"), C);
        btnInventario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("adminInventario"); }
        });
        sb.add(btnInventario); sb.add(Box.createVerticalStrut(2));

        JButton btnReportes = crearItem("📊  Reportes", pantallaActual.equals("adminReportes"), C);
        btnReportes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("adminReportes"); }
        });
        sb.add(btnReportes); sb.add(Box.createVerticalStrut(2));

        agregarSep(sb, C);
        sb.add(Box.createVerticalGlue());

        // ── Cerrar sesión ─────────────────────────────────
        JButton cerrar = new JButton("Cerrar sesión");
        cerrar.setFont(new Font("Arial", Font.PLAIN, 13));
        cerrar.setBackground(new Color(127, 29, 29));
        cerrar.setForeground(new Color(252, 165, 165));
        cerrar.setOpaque(true); cerrar.setBorderPainted(false); cerrar.setFocusPainted(false);
        cerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cerrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cerrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        cerrar.setHorizontalAlignment(SwingConstants.LEFT);
        cerrar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        cerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int r = JOptionPane.showConfirmDialog(panelRef, "¿Deseas cerrar sesión?",
                        "Cerrar sesión", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) {
                    Main.empleadoActual = null;
                    Main.clienteActual  = null;
                    Main.frame.setExtendedState(JFrame.NORMAL);
                    Main.frame.setSize(420, 520);
                    Main.frame.setLocationRelativeTo(null);
                    Main.cambiarPantalla("login");
                }
            }
        });
        sb.add(cerrar); sb.add(Box.createVerticalStrut(8));

        // ── Usuario — nombre real del admin logueado ──────
        String nombreAdmin = Main.empleadoActual != null ?
                Main.empleadoActual.getNombre() : "Administrador";
        String cargoAdmin  = Main.empleadoActual != null ?
                Main.empleadoActual.getCargo()   : "Admin · Kampets";

        // Iniciales del nombre
        String[] partes = nombreAdmin.split(" ");
        String iniciales = partes.length >= 2 ?
                String.valueOf(partes[0].charAt(0)) + String.valueOf(partes[1].charAt(0)) :
                String.valueOf(nombreAdmin.charAt(0));

        JPanel up = new JPanel(new BorderLayout(10, 0));
        up.setBackground(C[10]);
        up.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        up.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        up.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel av = new JLabel(iniciales);
        av.setFont(new Font("Arial", Font.BOLD, 13));
        av.setForeground(C[1]); av.setBackground(Color.WHITE); av.setOpaque(true);
        av.setPreferredSize(new Dimension(34, 34));
        av.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel ui = new JPanel(new GridLayout(2, 1));
        ui.setBackground(C[10]);
        JLabel uName = new JLabel(nombreAdmin);
        uName.setFont(new Font("Arial", Font.BOLD, 12)); uName.setForeground(Color.WHITE);
        JLabel uRole = new JLabel(cargoAdmin + " · Kampets");
        uRole.setFont(new Font("Arial", Font.PLAIN, 10)); uRole.setForeground(C[11]);
        ui.add(uName); ui.add(uRole);
        up.add(av, BorderLayout.WEST); up.add(ui, BorderLayout.CENTER);
        sb.add(up);

        return sb;
    }

    private static JButton crearItem(String texto, boolean activo, Color[] C) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Arial", activo ? Font.BOLD : Font.PLAIN, 13));
        b.setBackground(activo ? new Color(42, 90, 148) : C[1]);
        b.setForeground(activo ? Color.WHITE : C[7]);
        b.setOpaque(true); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 12));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!activo) b.setBackground(new Color(42, 90, 148));
            }
            public void mouseExited(MouseEvent e) {
                if (!activo) b.setBackground(C[1]);
            }
        });
        return b;
    }

    private static void agregarSeccion(JPanel p, String t, Color[] C) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Arial", Font.PLAIN, 10)); l.setForeground(C[11]);
        l.setBorder(BorderFactory.createEmptyBorder(10, 20, 4, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        p.add(l);
    }

    private static void agregarSep(JPanel p, Color[] C) {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(255, 255, 255, 30));
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(Box.createVerticalStrut(6)); p.add(s); p.add(Box.createVerticalStrut(6));
    }
}
