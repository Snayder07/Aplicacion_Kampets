package org.example.view;

import org.example.controller.CitaAdminController;
import org.example.model.Citas;
import org.example.model.EstadoCita;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PanelAdminCitas {
    public JPanel panel;
    private boolean temaOscuro = false;
    private String filtroActual = "Todas";

    private final CitaAdminController ctrl = new CitaAdminController();

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

    public PanelAdminCitas() { panel = new JPanel(new BorderLayout()); construir(); }
    public void setTema(boolean o) { if (o!=temaOscuro){temaOscuro=o;construir();} }
    public void recargar() { construir(); }

    private void construir() {
        panel.removeAll(); C = temaOscuro ? OSCURO : CLARO;
        panel.setBackground(C[0]);
        panel.add(SidebarAdmin.crear(C, temaOscuro, "adminCitas", panel), BorderLayout.WEST);
        panel.add(crearContenido(), BorderLayout.CENTER);
        panel.revalidate(); panel.repaint();
    }

    private JLabel lbl(String t, int sz, int st, Color c) {
        JLabel l = new JLabel(t); l.setFont(new Font("Arial",st,sz)); l.setForeground(c); return l;
    }

    private JPanel crearContenido() {
        JPanel c = new JPanel(new BorderLayout()); c.setBackground(C[0]);

        // Topbar
        JPanel tb = new JPanel(new BorderLayout()); tb.setBackground(C[2]);
        tb.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,C[9]),
                BorderFactory.createEmptyBorder(16,28,16,28)));
        JPanel tl = new JPanel(new GridLayout(2,1)); tl.setBackground(C[2]);
        tl.add(lbl("Todas las citas",22,Font.BOLD,C[6]));
        tl.add(lbl("Gestión y seguimiento de citas",12,Font.PLAIN,C[7]));
        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); tr.setBackground(C[2]);
        JButton btnTema = new JButton(temaOscuro?"☀  Claro":"🌙  Oscuro");
        estilizarTema(btnTema);
        btnTema.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                temaOscuro=!temaOscuro; Main.aplicarTemaGlobal(temaOscuro); construir();
            }
        });
        JButton btnNueva = new JButton("+ Nueva cita");
        btnNueva.setFont(new Font("Arial",Font.BOLD,13));
        btnNueva.setBackground(new Color(22,163,74)); btnNueva.setForeground(Color.WHITE);
        btnNueva.setOpaque(true); btnNueva.setBorderPainted(false);
        btnNueva.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNueva.setBorder(BorderFactory.createEmptyBorder(9,18,9,18));
        tr.add(btnTema); tr.add(btnNueva);
        tb.add(tl,BorderLayout.WEST); tb.add(tr,BorderLayout.EAST);
        c.add(tb,BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel(new BorderLayout(0,16));
        body.setBackground(C[0]);
        body.setBorder(BorderFactory.createEmptyBorder(24,28,28,28));

        // ── Filtros funcionales ───────────────────────────
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
        filtros.setBackground(C[0]);

        String[] fnombres = {"Todas","Confirmadas","Pendientes","Canceladas","En espera","Completadas"};
        JButton[] botonesF = new JButton[fnombres.length];

        // Panel contenedor de tabla para reemplazar al filtrar
        JPanel[] tablaHolder = new JPanel[1];
        tablaHolder[0] = crearTabla(filtroActual);
        tablaHolder[0].setBackground(C[2]);

        for (int i = 0; i < fnombres.length; i++) {
            final String nombreFiltro = fnombres[i];
            final int idx = i;
            JButton f = new JButton(fnombres[i]);
            f.setFont(new Font("Arial",Font.PLAIN,12));
            boolean esActivo = filtroActual.equals(nombreFiltro);
            f.setBackground(esActivo?C[1]:C[2]);
            f.setForeground(esActivo?C[5]:C[7]);
            f.setOpaque(true); f.setFocusPainted(false);
            f.setCursor(new Cursor(Cursor.HAND_CURSOR));
            f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(esActivo?C[1]:C[9],1),
                    BorderFactory.createEmptyBorder(6,14,6,14)));
            botonesF[idx] = f;

            f.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    filtroActual = nombreFiltro;
                    // Actualizar estilos de botones
                    for (int j = 0; j < botonesF.length; j++) {
                        boolean activo = fnombres[j].equals(filtroActual);
                        botonesF[j].setBackground(activo?C[1]:C[2]);
                        botonesF[j].setForeground(activo?C[5]:C[7]);
                        botonesF[j].setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(activo?C[1]:C[9],1),
                                BorderFactory.createEmptyBorder(6,14,6,14)));
                    }
                    // Reemplazar tabla filtrada
                    body.remove(tablaHolder[0]);
                    tablaHolder[0] = crearTabla(filtroActual);
                    body.add(tablaHolder[0], BorderLayout.CENTER);
                    body.revalidate(); body.repaint();
                }
            });
            filtros.add(f);
        }
        body.add(filtros, BorderLayout.NORTH);
        body.add(tablaHolder[0], BorderLayout.CENTER);
        c.add(body, BorderLayout.CENTER);
        return c;
    }

    // ── Crear tabla con filtro aplicado ───────────────────
    private JPanel crearTabla(String filtro) {
        // Cargar citas desde BD en orden inverso (última primero)
        List<Citas> todasCitas = ctrl.listarTodas();
        Collections.reverse(todasCitas); // última pedida = primera en la lista

        // Filtrar según selección
        List<Citas> citasFiltradas = new ArrayList<>();
        for (Citas cita : todasCitas) {
            if (filtro.equals("Todas")) {
                citasFiltradas.add(cita);
            } else {
                EstadoCita estadoBuscado = null;
                switch (filtro) {
                    case "Confirmadas":  estadoBuscado = EstadoCita.CONFIRMADA;  break;
                    case "Pendientes":   estadoBuscado = EstadoCita.PENDIENTE;   break;
                    case "Canceladas":   estadoBuscado = EstadoCita.CANCELADA;   break;
                    case "En espera":    estadoBuscado = EstadoCita.EN_ESPERA;   break;
                    case "Completadas":  estadoBuscado = EstadoCita.COMPLETADA;  break;
                }
                if (estadoBuscado != null && cita.getEstadoCita() == estadoBuscado) {
                    citasFiltradas.add(cita);
                }
            }
        }

        // Construir datos para la tabla
        String[] cols = {"Mascota","Veterinario","Fecha","Hora","Estado"};
        Object[][] datos = new Object[citasFiltradas.size()][5];
        for (int i = 0; i < citasFiltradas.size(); i++) {
            Citas cita = citasFiltradas.get(i);
            datos[i][0] = cita.getMascota()  != null ? cita.getMascota().getNombre()  : "—";
            datos[i][1] = cita.getEmpleado() != null ? cita.getEmpleado().getNombre() : "—";
            datos[i][2] = cita.getFechaCita() != null ? cita.getFechaCita().toString() : "—";
            datos[i][3] = cita.getHoraCita()  != null ? cita.getHoraCita().toString()  : "—";
            datos[i][4] = cita.getEstadoCita() != null ? cita.getEstadoCita().toString() : "—";
        }

        DefaultTableModel modelo = new DefaultTableModel(datos,cols) {
            public boolean isCellEditable(int r,int cc){return false;}
        };
        JTable tabla = new JTable(modelo);
        tabla.setBackground(C[2]); tabla.setForeground(C[6]);
        tabla.setFont(new Font("Arial",Font.PLAIN,13)); tabla.setRowHeight(40);
        tabla.setShowGrid(false); tabla.setIntercellSpacing(new Dimension(0,0));
        tabla.setSelectionBackground(C[3]); tabla.setFillsViewportHeight(true);

        JTableHeader th = tabla.getTableHeader();
        th.setBackground(C[14]); th.setForeground(temaOscuro?C[7]:C[1]);
        th.setFont(new Font("Arial",Font.BOLD,11));
        th.setReorderingAllowed(false); th.setPreferredSize(new Dimension(0,36));

        // Renderer columna Estado
        tabla.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int col){
                JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,s,f,r,col);
                l.setFont(new Font("Arial",Font.BOLD,12)); l.setHorizontalAlignment(SwingConstants.CENTER);
                String estado = v == null ? "" : v.toString();
                switch(estado){
                    case "CONFIRMADA":  l.setForeground(C[13]); l.setText("✅ Confirmada");  break;
                    case "PENDIENTE":   l.setForeground(C[8]);  l.setText("⏳ Pendiente");   break;
                    case "CANCELADA":   l.setForeground(C[12]); l.setText("❌ Cancelada");   break;
                    case "EN_ESPERA":   l.setForeground(C[8]);  l.setText("⏰ En espera");   break;
                    case "COMPLETADA":  l.setForeground(C[13]); l.setText("✔ Completada");  break;
                    default:            l.setForeground(C[7]);
                }
                l.setBackground(s?C[3]:(r%2==0?C[2]:C[4])); l.setOpaque(true); return l;
            }
        });

        DefaultTableCellRenderer base = new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int col){
                super.getTableCellRendererComponent(t,v,s,f,r,col);
                setForeground(C[6]); setBackground(r%2==0?C[2]:C[4]);
                if(s)setBackground(C[3]);
                setFont(new Font("Arial",Font.PLAIN,13)); setOpaque(true);
                setBorder(BorderFactory.createEmptyBorder(0,14,0,14)); return this;
            }
        };
        for(int i=0;i<4;i++) tabla.getColumnModel().getColumn(i).setCellRenderer(base);

        // Anchos de columna
        int[] anchos = {130, 160, 110, 80, 140};
        for (int i=0;i<anchos.length;i++) tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(null); sp.getViewport().setBackground(C[2]);
        JPanel wrapper = new JPanel(new BorderLayout()); wrapper.setBackground(C[2]);
        wrapper.add(sp, BorderLayout.CENTER);

        // Mensaje si no hay citas con ese filtro
        if (citasFiltradas.isEmpty()) {
            JPanel sinCitas = new JPanel(new GridBagLayout());
            sinCitas.setBackground(C[2]);
            JLabel msg = new JLabel("No hay citas con estado \"" + filtro + "\"");
            msg.setFont(new Font("Arial", Font.PLAIN, 14));
            msg.setForeground(C[7]);
            sinCitas.add(msg);
            wrapper.add(sinCitas, BorderLayout.CENTER);
            wrapper.remove(sp);
        }

        return wrapper;
    }

    private void estilizarTema(JButton b){
        b.setFont(new Font("Arial",Font.PLAIN,13)); b.setBackground(C[2]); b.setForeground(C[6]);
        b.setOpaque(true); b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9],1),BorderFactory.createEmptyBorder(7,14,7,14)));
    }
}
