package org.example.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PanelAlimentos {
    public JPanel panel;
    private boolean temaOscuro = false;

    private final Color[] CLARO = {
            new Color(240, 246, 252), new Color(26, 74, 122), Color.WHITE,
            new Color(42, 90, 138),   new Color(230, 240, 250), Color.WHITE,
            new Color(26, 58, 90),    new Color(138, 170, 200), new Color(224, 112, 32),
            new Color(208, 228, 244), new Color(15, 53, 96),    new Color(122, 175, 212),
            new Color(168, 200, 232), new Color(168, 212, 245),
    };
    private final Color[] OSCURO = {
            new Color(18, 24, 38),  new Color(13, 18, 30),  new Color(26, 34, 52),
            new Color(37, 55, 90),  new Color(32, 42, 64),  Color.WHITE,
            new Color(226, 232, 240), new Color(100, 116, 139), new Color(251, 146, 60),
            new Color(30, 41, 59),  new Color(9, 14, 24),   new Color(122, 175, 212),
            new Color(80, 120, 170), new Color(100, 160, 210),
    };
    private Color[] C = CLARO;

    public PanelAlimentos() { panel = new JPanel(new BorderLayout()); construir(); }

    public void setTema(boolean oscuro) { if (oscuro != temaOscuro) { temaOscuro = oscuro; construir(); } }

    private void construir() {
        panel.removeAll(); C = temaOscuro ? OSCURO : CLARO;
        panel.setBackground(C[0]);
        panel.add(crearSidebar(), BorderLayout.WEST);
        panel.add(crearContenido(), BorderLayout.CENTER);
        panel.revalidate(); panel.repaint();
    }

    private JLabel lbl(String t, int sz, int st, Color c) {
        JLabel l = new JLabel(t); l.setFont(new Font("Arial", st, sz)); l.setForeground(c); return l;
    }
    private JButton btn(String t, Color bg, Color fg, boolean borde) {
        JButton b = new JButton(t); b.setFont(new Font("Arial", Font.PLAIN, 13));
        b.setBackground(bg); b.setForeground(fg); b.setOpaque(true);
        b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (borde) b.setBorder(BorderFactory.createLineBorder(fg,1)); else b.setBorderPainted(false);
        return b;
    }

    private JPanel crearSidebar() {
        JPanel sb = new JPanel();
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBackground(C[1]); sb.setPreferredSize(new Dimension(220,0));
        sb.setBorder(BorderFactory.createEmptyBorder(20,12,20,12));

        JLabel logo;
        try { ImageIcon ic = new ImageIcon(getClass().getResource("/logo.png"));
            logo = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(160,55,Image.SCALE_SMOOTH)));
        } catch (Exception e) { logo = lbl("Kampets", 18, Font.BOLD, C[5]); }
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sb.add(logo); sb.add(Box.createVerticalStrut(16));
        agregarSep(sb);

        agregarSeccion(sb, "PRINCIPAL");
        String[] mp = {"Inicio","Mis citas","Historial"};
        for (int i = 0; i < mp.length; i++) {
            JButton b = btn(mp[i], C[1], C[5], false);
            b.setFont(new Font("Arial", Font.PLAIN, 13));
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE,38));
            b.setHorizontalAlignment(SwingConstants.LEFT);
            if (i == 0) b.addActionListener(e -> Main.cambiarPantalla("panelCliente"));
            if (i == 1) b.addActionListener(e -> Main.cambiarPantalla("misCitas"));
            if (i == 2) b.addActionListener(e -> Main.cambiarPantalla("historial"));
            sb.add(b); sb.add(Box.createVerticalStrut(3));
        }
        sb.add(Box.createVerticalStrut(12));
        agregarSeccion(sb, "SERVICIOS");
        String[] ms = {"Alimentos","Vacunas"};
        for (int i = 0; i < ms.length; i++) {
            JButton b = btn(ms[i], i == 0 ? C[2] : C[1], i == 0 ? C[1] : C[5], false);
            b.setFont(new Font("Arial", i == 0 ? Font.BOLD : Font.PLAIN, 13));
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE,38));
            b.setHorizontalAlignment(SwingConstants.LEFT);
            if (i == 1) b.addActionListener(e -> Main.cambiarPantalla("vacunas"));
            sb.add(b); sb.add(Box.createVerticalStrut(3));
        }
        sb.add(Box.createVerticalGlue());

        JButton cerrar = btn("Cerrar sesion", C[1], C[12], true);
        cerrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        cerrar.setMaximumSize(new Dimension(Integer.MAX_VALUE,36));
        cerrar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(panel,"Deseas cerrar sesion?","Cerrar sesion",
                    JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                Main.frame.setSize(420,520); Main.frame.setLocationRelativeTo(null);
                Main.cambiarPantalla("login");
            }
        });
        sb.add(cerrar); sb.add(Box.createVerticalStrut(8));

        JPanel up = new JPanel(new BorderLayout(8,0));
        up.setBackground(C[10]); up.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        up.setMaximumSize(new Dimension(Integer.MAX_VALUE,55)); up.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel av = lbl("MF",13,Font.BOLD,C[1]); av.setBackground(C[5]); av.setOpaque(true);
        av.setPreferredSize(new Dimension(34,34)); av.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel ui = new JPanel(new GridLayout(2,1)); ui.setBackground(C[10]);
        ui.add(lbl("Maria Fernanda",12,Font.BOLD,C[5]));
        ui.add(lbl("Cliente",10,Font.PLAIN,C[11]));
        up.add(av,BorderLayout.WEST); up.add(ui,BorderLayout.CENTER);
        sb.add(up);
        return sb;
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(C[0]);

        // Topbar
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(C[2]);
        topbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,C[9]),
                BorderFactory.createEmptyBorder(16,24,16,24)));
        JPanel topLeft = new JPanel(new GridLayout(2,1)); topLeft.setBackground(C[2]);
        topLeft.add(lbl("Alimentos para mascotas", 20, Font.BOLD, C[6]));
        topLeft.add(lbl("Productos recomendados para tus mascotas", 12, Font.PLAIN, C[7]));

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topRight.setBackground(C[2]);
        JButton btnTema = new JButton(temaOscuro ? "☀  Claro" : "🌙  Oscuro");
        btnTema.setFont(new Font("Arial", Font.PLAIN, 13));
        btnTema.setBackground(C[0]); btnTema.setForeground(C[6]); btnTema.setOpaque(true);
        btnTema.setFocusPainted(false); btnTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTema.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9],1), BorderFactory.createEmptyBorder(7,14,7,14)));
        btnTema.addActionListener(e -> { temaOscuro = !temaOscuro; construir(); });
        topRight.add(btnTema);
        topbar.add(topLeft, BorderLayout.WEST);
        topbar.add(topRight, BorderLayout.EAST);
        contenido.add(topbar, BorderLayout.NORTH);

        // Cuerpo
        JPanel cuerpo = new JPanel(new BorderLayout(0,20));
        cuerpo.setBackground(C[0]);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(24,28,28,28));

        // Categorías
        JPanel cats = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
        cats.setBackground(C[0]);
        String[] categorias = {"Todos", "Perros", "Gatos", "Aves", "Peces"};
        for (int i = 0; i < categorias.length; i++) {
            JButton f = new JButton(categorias[i]);
            f.setFont(new Font("Arial", Font.PLAIN, 12));
            f.setBackground(i == 0 ? C[1] : C[2]);
            f.setForeground(i == 0 ? C[5] : C[7]);
            f.setOpaque(true); f.setFocusPainted(false); f.setCursor(new Cursor(Cursor.HAND_CURSOR));
            f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(i == 0 ? C[1] : C[9],1),
                    BorderFactory.createEmptyBorder(6,14,6,14)));
            cats.add(f);
        }
        cuerpo.add(cats, BorderLayout.NORTH);

        // Grid de productos
        String[][] productos = {
                {"🐶", "Royal Canin Adulto",      "Perros · 15kg",     "$185.000", "★ 4.8", "Para perros adultos de razas medianas."},
                {"🐶", "Purina Pro Plan",          "Perros · 3kg",      "$62.000",  "★ 4.6", "Alto contenido proteico para razas pequeñas."},
                {"🐱", "Whiskas Adulto",           "Gatos · 1.5kg",     "$28.000",  "★ 4.5", "Nutrición completa para gatos adultos."},
                {"🐱", "Hill's Science Diet",      "Gatos · 2kg",       "$74.000",  "★ 4.9", "Apoya la salud renal y urinaria."},
                {"🐦", "Vitakraft Periquitos",     "Aves · 500g",       "$18.000",  "★ 4.4", "Mezcla de semillas enriquecidas con vitaminas."},
                {"🐟", "Tetra Goldfish",           "Peces · 250ml",     "$22.000",  "★ 4.3", "Alimento en escamas para peces de agua fría."},
        };

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setBackground(C[0]);

        for (String[] p : productos) {
            JPanel card = new JPanel(new BorderLayout(0, 8));
            card.setBackground(C[2]);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(C[9],1),
                    BorderFactory.createEmptyBorder(18,18,18,18)));

            // Ícono grande
            JLabel ico = new JLabel(p[0], SwingConstants.CENTER);
            ico.setFont(new Font("Arial", Font.PLAIN, 38));
            ico.setBackground(C[4]); ico.setOpaque(true);
            ico.setPreferredSize(new Dimension(0, 60));

            JPanel info = new JPanel(new GridLayout(4,1,0,3));
            info.setBackground(C[2]);
            info.add(lbl(p[1], 13, Font.BOLD, C[6]));
            info.add(lbl(p[2], 11, Font.PLAIN, C[7]));
            info.add(lbl(p[5], 10, Font.PLAIN, C[11]));

            JPanel bottom = new JPanel(new BorderLayout());
            bottom.setBackground(C[2]);
            bottom.add(lbl(p[3], 14, Font.BOLD, C[1]), BorderLayout.WEST);
            bottom.add(lbl(p[4], 11, Font.PLAIN, new Color(234,179,8)), BorderLayout.EAST);

            JButton agregar = btn("Agregar al carrito", C[1], C[5], false);
            agregar.setFont(new Font("Arial", Font.BOLD, 12));
            agregar.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));

            card.add(ico,     BorderLayout.NORTH);
            card.add(info,    BorderLayout.CENTER);
            card.add(bottom,  BorderLayout.SOUTH);

            JPanel wrap = new JPanel(new BorderLayout(0,8));
            wrap.setBackground(C[2]);
            wrap.add(card,    BorderLayout.CENTER);
            wrap.add(agregar, BorderLayout.SOUTH);
            grid.add(wrap);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null); scroll.getViewport().setBackground(C[0]);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        cuerpo.add(scroll, BorderLayout.CENTER);
        contenido.add(cuerpo, BorderLayout.CENTER);
        return contenido;
    }

    private void agregarSeccion(JPanel p, String t) {
        JLabel l = lbl(t, 10, Font.PLAIN, C[11]);
        l.setBorder(BorderFactory.createEmptyBorder(8,0,4,0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE,24)); p.add(l);
    }
    private void agregarSep(JPanel p) {
        JSeparator s = new JSeparator(); s.setForeground(C[3]);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE,1)); s.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(Box.createVerticalStrut(6)); p.add(s); p.add(Box.createVerticalStrut(6));
    }
}