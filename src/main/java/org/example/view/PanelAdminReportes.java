package org.example.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;

public class PanelAdminReportes {
    public JPanel panel;
    private boolean temaOscuro = false;

    private final Color[] CLARO = {
            new Color(240,246,252),new Color(26,74,122),Color.WHITE,new Color(42,90,138),
            new Color(230,240,250),Color.WHITE,new Color(15,40,80),new Color(100,116,139),
            new Color(234,88,12),new Color(208,228,244),new Color(15,53,96),new Color(180,210,235),
            new Color(220,38,38),new Color(22,163,74),new Color(210,228,245),
    };
    private final Color[] OSCURO = {
            new Color(18,24,38),new Color(13,18,30),new Color(26,34,52),new Color(37,55,90),
            new Color(32,42,64),Color.WHITE,new Color(226,232,240),new Color(148,163,184),
            new Color(251,146,60),new Color(30,41,59),new Color(9,14,24),new Color(122,175,212),
            new Color(239,68,68),new Color(34,197,94),new Color(15,23,42),
    };
    private Color[] C = CLARO;

    public PanelAdminReportes() { panel = new JPanel(new BorderLayout()); construir(); }
    public void setTema(boolean o) { if(o!=temaOscuro){temaOscuro=o;construir();} }

    private void construir() {
        panel.removeAll(); C = temaOscuro ? OSCURO : CLARO;
        panel.setBackground(C[0]);
        panel.add(SidebarAdmin.crear(C, temaOscuro, "adminReportes", panel), BorderLayout.WEST);
        panel.add(crearContenido(), BorderLayout.CENTER);
        panel.revalidate(); panel.repaint();
    }

    private JLabel lbl(String t,int sz,int st,Color c){JLabel l=new JLabel(t);l.setFont(new Font("Arial",st,sz));l.setForeground(c);return l;}

    private JPanel crearContenido() {
        JPanel c = new JPanel(new BorderLayout()); c.setBackground(C[0]);

        JPanel tb = new JPanel(new BorderLayout()); tb.setBackground(C[2]);
        tb.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,C[9]),BorderFactory.createEmptyBorder(16,28,16,28)));
        JPanel tl = new JPanel(new GridLayout(2,1)); tl.setBackground(C[2]);
        tl.add(lbl("Reportes",22,Font.BOLD,C[6]));
        tl.add(lbl("Genera y descarga reportes del sistema",12,Font.PLAIN,C[7]));
        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); tr.setBackground(C[2]);
        JButton btnTema = new JButton(temaOscuro?"☀  Claro":"🌙  Oscuro"); estilizarTema(btnTema);
        btnTema.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { temaOscuro=!temaOscuro; Main.aplicarTemaGlobal(temaOscuro); construir(); }
        });
        tr.add(btnTema); tb.add(tl,BorderLayout.WEST); tb.add(tr,BorderLayout.EAST); c.add(tb,BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0,24)); body.setBackground(C[0]); body.setBorder(BorderFactory.createEmptyBorder(24,28,28,28));

        // Stats
        JPanel stats = new JPanel(new GridLayout(1,4,16,0)); stats.setBackground(C[0]);
        Object[][] st = {
                {"Citas completadas","48",C[13]},{"Nuevos usuarios","21",C[1]},
                {"Vacunas aplicadas","34",C[1]}, {"Cancelaciones","6",C[12]},
        };
        for (Object[] s : st) {
            JPanel card = new JPanel(new BorderLayout(0,4)); card.setBackground(C[2]);
            card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(C[9],1),BorderFactory.createEmptyBorder(18,20,18,20)));
            card.add(lbl((String)s[0],11,Font.PLAIN,C[7]),BorderLayout.NORTH);
            card.add(lbl((String)s[1],28,Font.BOLD,(Color)s[2]),BorderLayout.CENTER);
            stats.add(card);
        }
        body.add(stats,BorderLayout.NORTH);

        // Tarjetas de reportes
        JPanel grid = new JPanel(new GridLayout(2,3,16,16)); grid.setBackground(C[0]);

        // Reporte de citas
        grid.add(crearTarjetaReporte("📋","Reporte de citas","Todas las citas del mes con estados y detalles","reporte_citas"));
        // Reporte de vacunas
        grid.add(crearTarjetaReporte("💉","Reporte de vacunas","Estado del plan de vacunación de todas las mascotas","reporte_vacunas"));
        // Reporte de mascotas
        grid.add(crearTarjetaReporte("🐾","Reporte de mascotas","Listado completo de mascotas registradas","reporte_mascotas"));
        // Reporte de inventario
        grid.add(crearTarjetaReporte("📦","Reporte de inventario","Stock actual de medicamentos y productos","reporte_inventario"));
        // Reporte de usuarios
        grid.add(crearTarjetaReporte("👤","Reporte de usuarios","Clientes registrados y actividad reciente","reporte_usuarios"));
        // Reporte general
        grid.add(crearTarjetaReporte("📊","Reporte general","Resumen ejecutivo completo del mes","reporte_general"));

        JScrollPane scroll = new JScrollPane(grid); scroll.setBorder(null); scroll.getViewport().setBackground(C[0]);
        body.add(scroll,BorderLayout.CENTER); c.add(body,BorderLayout.CENTER); return c;
    }

    private JPanel crearTarjetaReporte(String icono, String nombre, String descripcion, String archivo) {
        JPanel card = new JPanel(new BorderLayout(0,10)); card.setBackground(C[2]);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(C[9],1),BorderFactory.createEmptyBorder(22,22,18,22)));

        JPanel top = new JPanel(new BorderLayout(12,0)); top.setBackground(C[2]);
        JLabel ico = new JLabel(icono); ico.setFont(new Font("Arial",Font.PLAIN,28)); ico.setPreferredSize(new Dimension(44,44));
        JPanel textos = new JPanel(new GridLayout(2,1,0,4)); textos.setBackground(C[2]);
        textos.add(lbl(nombre,14,Font.BOLD,C[6]));
        textos.add(lbl(descripcion,11,Font.PLAIN,C[7]));
        top.add(ico,BorderLayout.WEST); top.add(textos,BorderLayout.CENTER);

        JButton descBtn = new JButton("Descargar PDF");
        descBtn.setFont(new Font("Arial",Font.BOLD,12));
        descBtn.setBackground(C[1]); descBtn.setForeground(C[5]);
        descBtn.setOpaque(true); descBtn.setBorderPainted(false);
        descBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        descBtn.setBorder(BorderFactory.createEmptyBorder(9,16,9,16));
        descBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { descargarReporte(nombre, archivo); }
        });

        card.add(top,BorderLayout.CENTER);
        card.add(descBtn,BorderLayout.SOUTH);
        return card;
    }

    private void descargarReporte(String nombre, String archivo) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar " + nombre);
        chooser.setSelectedFile(new File(archivo + ".txt"));
        if (chooser.showSaveDialog(panel) != JFileChooser.APPROVE_OPTION) return;
        try (PrintWriter pw = new PrintWriter(new FileWriter(chooser.getSelectedFile()))) {
            pw.println("========================================");
            pw.println("  " + nombre.toUpperCase() + " — KAMPETS VETERINARIA");
            pw.println("========================================");
            pw.println("Fecha de generación: " + LocalDate.now());
            pw.println();
            pw.println("RESUMEN DEL MES");
            pw.println("  Citas completadas:  48");
            pw.println("  Nuevos usuarios:    21");
            pw.println("  Vacunas aplicadas:  34");
            pw.println("  Cancelaciones:       6");
            pw.println();
            pw.println("========================================");
            pw.println("  Generado por Kampets · Sistema interno");
            pw.println("========================================");
            JOptionPane.showMessageDialog(panel, "✅ Reporte exportado:\n" + chooser.getSelectedFile().getAbsolutePath(), "Listo", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(panel, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void estilizarTema(JButton b){
        b.setFont(new Font("Arial",Font.PLAIN,13)); b.setBackground(C[2]); b.setForeground(C[6]);
        b.setOpaque(true); b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(C[9],1),BorderFactory.createEmptyBorder(7,14,7,14)));
    }
}
