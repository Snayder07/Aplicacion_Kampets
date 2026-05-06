package org.example.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class PanelAdminVacunas {
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

    public PanelAdminVacunas() { panel = new JPanel(new BorderLayout()); construir(); }
    public void setTema(boolean o) { if(o!=temaOscuro){temaOscuro=o;construir();} }

    private void construir() {
        panel.removeAll(); C = temaOscuro ? OSCURO : CLARO;
        panel.setBackground(C[0]);
        panel.add(SidebarAdmin.crear(C, temaOscuro, "adminVacunas", panel), BorderLayout.WEST);
        panel.add(crearContenido(), BorderLayout.CENTER);
        panel.revalidate(); panel.repaint();
    }

    private JLabel lbl(String t,int sz,int st,Color c){JLabel l=new JLabel(t);l.setFont(new Font("Arial",st,sz));l.setForeground(c);return l;}

    private JPanel crearContenido() {
        JPanel c = new JPanel(new BorderLayout()); c.setBackground(C[0]);

        JPanel tb = new JPanel(new BorderLayout()); tb.setBackground(C[2]);
        tb.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,C[9]),BorderFactory.createEmptyBorder(16,28,16,28)));
        JPanel tl = new JPanel(new GridLayout(2,1)); tl.setBackground(C[2]);
        tl.add(lbl("Vacunas pendientes",22,Font.BOLD,C[6]));
        tl.add(lbl("Control y seguimiento del plan de vacunación",12,Font.PLAIN,C[7]));
        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); tr.setBackground(C[2]);
        JButton btnTema = new JButton(temaOscuro?"☀  Claro":"🌙  Oscuro"); estilizarTema(btnTema);
        btnTema.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { temaOscuro=!temaOscuro; Main.aplicarTemaGlobal(temaOscuro); construir(); }
        });
        tr.add(btnTema); tb.add(tl,BorderLayout.WEST); tb.add(tr,BorderLayout.EAST); c.add(tb,BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0,20)); body.setBackground(C[0]); body.setBorder(BorderFactory.createEmptyBorder(24,28,28,28));

        // Stats
        JPanel stats = new JPanel(new GridLayout(1,3,16,0)); stats.setBackground(C[0]);
        Object[][] st = {{"Vencidas","8",C[12]},{"Próximas (30 días)","12",C[8]},{"Al día","117",C[13]}};
        for (Object[] s : st) {
            JPanel card = new JPanel(new BorderLayout(0,4)); card.setBackground(C[2]);
            card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(C[9],1),BorderFactory.createEmptyBorder(18,20,18,20)));
            JLabel titulo = lbl((String)s[0],11,Font.PLAIN,C[7]);
            JLabel valor  = lbl((String)s[1],28,Font.BOLD,(Color)s[2]);
            card.add(titulo,BorderLayout.NORTH); card.add(valor,BorderLayout.CENTER); stats.add(card);
        }
        body.add(stats,BorderLayout.NORTH);

        // Tabla
        String[] cols = {"Mascota","Dueño","Vacuna","Última aplicación","Próxima fecha","Veterinario","Estado"};
        Object[][] datos = {
                {"Rocky","Carlos M.", "Antirrábica",  "15 Mar 2024","15 Mar 2025","Dr. Ramírez","Vencida"},
                {"Luna", "Maria F.",  "Polivalente",  "01 Abr 2025","01 Abr 2026","Dr. Gómez",  "Próxima"},
                {"Mochi","Ana L.",    "Bordetella",   "20 Feb 2025","20 May 2026","Dra. Torres","Al día"},
                {"Max",  "Juan P.",   "Leptospirosis","10 Ene 2025","10 Jul 2025","Dr. Ramírez","Vencida"},
                {"Nemo", "Laura S.",  "Antirrábica",  "05 Nov 2024","05 Nov 2025","Dra. Torres","Vencida"},
                {"Coco", "María R.",  "Polivalente",  "12 Mar 2025","12 Mar 2026","Dr. Gómez",  "Al día"},
                {"Lola", "Pedro A.",  "Rabia",        "20 Dic 2024","20 Jun 2025","Dr. Ramírez","Vencida"},
                {"Simba","Clara V.",  "Triple felina","08 Feb 2025","08 Feb 2026","Dra. Torres","Próxima"},
        };

        DefaultTableModel modelo = new DefaultTableModel(datos,cols){public boolean isCellEditable(int r,int cc){return false;}};
        JTable tabla = new JTable(modelo);
        tabla.setBackground(C[2]); tabla.setForeground(C[6]);
        tabla.setFont(new Font("Arial",Font.PLAIN,13)); tabla.setRowHeight(40);
        tabla.setShowGrid(false); tabla.setIntercellSpacing(new Dimension(0,0));
        tabla.setSelectionBackground(C[3]); tabla.setFillsViewportHeight(true);
        JTableHeader th = tabla.getTableHeader(); th.setBackground(C[14]); th.setForeground(temaOscuro?C[7]:C[1]);
        th.setFont(new Font("Arial",Font.BOLD,11)); th.setReorderingAllowed(false); th.setPreferredSize(new Dimension(0,36));

        tabla.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int col){
                JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,s,f,r,col);
                l.setFont(new Font("Arial",Font.BOLD,12)); l.setHorizontalAlignment(SwingConstants.CENTER);
                switch(v.toString()){
                    case "Vencida": l.setForeground(C[12]); break;
                    case "Próxima": l.setForeground(C[8]);  break;
                    default:        l.setForeground(C[13]);
                }
                l.setBackground(s?C[3]:(r%2==0?C[2]:C[4])); l.setOpaque(true); return l;
            }
        });
        DefaultTableCellRenderer base = new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int col){
                super.getTableCellRendererComponent(t,v,s,f,r,col); setForeground(C[6]);
                setBackground(r%2==0?C[2]:C[4]); if(s)setBackground(C[3]);
                setFont(new Font("Arial",Font.PLAIN,13)); setOpaque(true); setBorder(BorderFactory.createEmptyBorder(0,14,0,14)); return this;
            }
        };
        for(int i=0;i<6;i++) tabla.getColumnModel().getColumn(i).setCellRenderer(base);

        JScrollPane sp = new JScrollPane(tabla); sp.setBorder(null); sp.getViewport().setBackground(C[2]);
        JPanel wrapper = new JPanel(new BorderLayout()); wrapper.setBackground(C[2]); wrapper.add(sp,BorderLayout.CENTER);
        body.add(wrapper,BorderLayout.CENTER); c.add(body,BorderLayout.CENTER); return c;
    }

    private void estilizarTema(JButton b){
        b.setFont(new Font("Arial",Font.PLAIN,13)); b.setBackground(C[2]); b.setForeground(C[6]);
        b.setOpaque(true); b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(C[9],1),BorderFactory.createEmptyBorder(7,14,7,14)));
    }
}