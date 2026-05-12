package org.example.view;

import org.example.controller.InventarioController;
import org.example.model.Productos;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Objects;

public class PanelAdminInventario {
    public JPanel panel;
    private boolean temaOscuro = false;

    private final InventarioController ctrl = new InventarioController();

    private final Color[] CLARO = {
            new Color(240,253,244),new Color(22,101,52),Color.WHITE,new Color(34,120,70),
            new Color(220,245,230),Color.WHITE,new Color(15,60,30),new Color(100,130,110),
            new Color(234,88,12),new Color(187,224,200),new Color(15,60,30),new Color(134,190,155),
            new Color(220,38,38),new Color(22,163,74),new Color(210,240,220),
    };
    private final Color[] OSCURO = {
            new Color(18,24,38),new Color(13,18,30),new Color(26,34,52),new Color(37,55,90),
            new Color(32,42,64),Color.WHITE,new Color(226,232,240),new Color(148,163,184),
            new Color(251,146,60),new Color(30,41,59),new Color(9,14,24),new Color(122,175,212),
            new Color(239,68,68),new Color(34,197,94),new Color(15,23,42),
    };
    private Color[] C = CLARO;

    public PanelAdminInventario() { panel = new JPanel(new BorderLayout()); construir(); }
    public void setTema(boolean o) { if(o!=temaOscuro){temaOscuro=o;construir();} }
    public void recargar() { construir(); }

    private void construir() {
        panel.removeAll(); C = temaOscuro ? OSCURO : CLARO;
        panel.setBackground(C[0]);
        panel.add(SidebarAdmin.crear(C, temaOscuro, "adminInventario", panel), BorderLayout.WEST);
        panel.add(crearContenido(), BorderLayout.CENTER);
        panel.revalidate(); panel.repaint();
    }

    private JLabel lbl(String t,int sz,int st,Color c){JLabel l=new JLabel(t);l.setFont(new Font("Arial",st,sz+2));l.setForeground(c);return l;}

    private JPanel crearContenido() {
        // ── Cargar datos de BD ────────────────────────────────────────
        List<Productos> lista = ctrl.listarTodos();
        long total     = lista.size();
        long stockBajo = lista.stream().filter(p -> p.getStock() != null && p.getStock() < 10).count();
        long tipos     = lista.stream().map(Productos::getTipo).filter(Objects::nonNull).distinct().count();
        long sinStock  = lista.stream().filter(p -> p.getStock() != null && p.getStock() == 0).count();

        JPanel c = new JPanel(new BorderLayout()); c.setBackground(C[0]);

        // ── Topbar ────────────────────────────────────────────────────
        JPanel tb = new JPanel(new BorderLayout()); tb.setBackground(C[2]);
        tb.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,C[9]),BorderFactory.createEmptyBorder(16,28,16,28)));
        JPanel tl = new JPanel(new GridLayout(2,1)); tl.setBackground(C[2]);
        tl.add(lbl("Inventario",22,Font.BOLD,C[6]));
        tl.add(lbl("Medicamentos, vacunas y productos disponibles",12,Font.PLAIN,C[7]));
        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); tr.setBackground(C[2]);
        JButton btnTema = new JButton(temaOscuro?"☀  Claro":"🌙  Oscuro"); estilizarTema(btnTema);
        btnTema.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { temaOscuro=!temaOscuro; Main.aplicarTemaGlobal(temaOscuro); construir(); }
        });
        JButton btnAgregar = new JButton("+ Agregar producto");
        btnAgregar.setFont(new Font("Arial",Font.BOLD,13)); btnAgregar.setBackground(new Color(22,163,74));
        btnAgregar.setForeground(Color.WHITE); btnAgregar.setOpaque(true); btnAgregar.setBorderPainted(false);
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR)); btnAgregar.setBorder(BorderFactory.createEmptyBorder(9,18,9,18));
        btnAgregar.addActionListener(e -> mostrarFormAgregar());
        tr.add(btnTema); tr.add(btnAgregar);
        tb.add(tl,BorderLayout.WEST); tb.add(tr,BorderLayout.EAST); c.add(tb,BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0,20)); body.setBackground(C[0]); body.setBorder(BorderFactory.createEmptyBorder(24,28,28,28));

        // ── Stats ─────────────────────────────────────────────────────
        JPanel stats = new JPanel(new GridLayout(1,4,16,0)); stats.setBackground(C[0]);
        Object[][] st = {
                {"Total productos",  String.valueOf(total),     C[1]},
                {"Stock bajo (<10)", String.valueOf(stockBajo), C[12]},
                {"Sin stock",        String.valueOf(sinStock),  C[8]},
                {"Categorías",       String.valueOf(tipos),     C[1]},
        };
        for (Object[] s : st) {
            JPanel card = new JPanel(new BorderLayout(0,4)); card.setBackground(C[2]);
            card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(C[9],1),BorderFactory.createEmptyBorder(16,20,16,20)));
            card.add(lbl((String)s[0],11,Font.PLAIN,C[7]),BorderLayout.NORTH);
            card.add(lbl((String)s[1],28,Font.BOLD,(Color)s[2]),BorderLayout.CENTER);
            stats.add(card);
        }
        body.add(stats,BorderLayout.NORTH);

        // ── Tabla ─────────────────────────────────────────────────────
        String[] cols = {"Producto","Tipo","Marca","Precio","Stock","Estado"};

        Object[][] datos = new Object[lista.size()][6];
        for (int i = 0; i < lista.size(); i++) {
            Productos p = lista.get(i);
            String nombre  = p.getNombre() != null ? p.getNombre() : "—";
            String tipo    = p.getTipo()   != null ? p.getTipo()   : "—";
            String marca   = p.getMarca()  != null ? p.getMarca()  : "—";
            String precio  = p.getPrecio() != null ? "$" + p.getPrecio().toPlainString() : "—";
            String stock   = p.getStock()  != null ? String.valueOf(p.getStock()) : "0";
            String estado;
            if      (p.getStock() == null || p.getStock() == 0) estado = "Sin stock";
            else if (p.getStock() < 10)                         estado = "Stock bajo";
            else                                                 estado = "OK";
            datos[i] = new Object[]{nombre, tipo, marca, precio, stock, estado};
        }

        DefaultTableModel modelo = new DefaultTableModel(datos,cols){public boolean isCellEditable(int r,int cc){return false;}};
        JTable tabla = new JTable(modelo);
        tabla.setBackground(C[2]); tabla.setForeground(C[6]);
        tabla.setFont(new Font("Arial",Font.PLAIN,13)); tabla.setRowHeight(40);
        tabla.setShowGrid(false); tabla.setIntercellSpacing(new Dimension(0,0));
        tabla.setSelectionBackground(C[3]); tabla.setFillsViewportHeight(true);
        JTableHeader th = tabla.getTableHeader(); th.setBackground(C[14]); th.setForeground(temaOscuro?C[7]:C[1]);
        th.setFont(new Font("Arial",Font.BOLD,11)); th.setReorderingAllowed(false); th.setPreferredSize(new Dimension(0,36));

        // Anchos de columnas
        int[] anchos = {200, 120, 120, 110, 80, 100};
        for (int i = 0; i < anchos.length; i++) tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);

        // Renderer estado (col 5)
        tabla.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int col){
                JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,s,f,r,col);
                l.setFont(new Font("Arial",Font.BOLD,12)); l.setHorizontalAlignment(SwingConstants.CENTER);
                switch(v != null ? v.toString() : ""){
                    case "Sin stock":  l.setForeground(C[12]); break;
                    case "Stock bajo": l.setForeground(C[8]);  break;
                    default:           l.setForeground(C[13]);
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
        for(int i=0;i<5;i++) tabla.getColumnModel().getColumn(i).setCellRenderer(base);

        JScrollPane sp = new JScrollPane(tabla); sp.setBorder(null); sp.getViewport().setBackground(C[2]); sp.getVerticalScrollBar().setUnitIncrement(16);
        JPanel wrapper = new JPanel(new BorderLayout()); wrapper.setBackground(C[2]); wrapper.add(sp,BorderLayout.CENTER);
        body.add(wrapper,BorderLayout.CENTER);
        JScrollPane outerScroll = new JScrollPane(body);
        outerScroll.setBorder(null); outerScroll.getViewport().setBackground(C[0]);
        outerScroll.getVerticalScrollBar().setUnitIncrement(16);
        outerScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        c.add(outerScroll,BorderLayout.CENTER); return c;
    }

    private void mostrarFormAgregar() {
        JDialog dlg = new JDialog();
        dlg.setTitle("Agregar producto");
        dlg.setModal(true);
        dlg.setSize(400, 320);
        dlg.setLocationRelativeTo(panel);

        JPanel form = new JPanel(new GridLayout(6, 2, 8, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.setBackground(C[2]);

        JTextField tfNombre = new JTextField(); JTextField tfTipo = new JTextField();
        JTextField tfMarca  = new JTextField(); JTextField tfPrecio = new JTextField();
        JTextField tfStock  = new JTextField();

        form.add(new JLabel("Nombre:")); form.add(tfNombre);
        form.add(new JLabel("Tipo:"));   form.add(tfTipo);
        form.add(new JLabel("Marca:"));  form.add(tfMarca);
        form.add(new JLabel("Precio:")); form.add(tfPrecio);
        form.add(new JLabel("Stock:"));  form.add(tfStock);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(22,163,74)); btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setOpaque(true); btnGuardar.setBorderPainted(false);
        btnGuardar.addActionListener(e -> {
            boolean ok = ctrl.agregarProducto(tfNombre.getText(), tfTipo.getText(),
                    tfMarca.getText(), tfPrecio.getText(), tfStock.getText(), panel);
            if (ok) { dlg.dispose(); recargar(); }
        });
        form.add(new JLabel()); form.add(btnGuardar);

        dlg.add(form); dlg.setVisible(true);
    }

    private void estilizarTema(JButton b){
        b.setFont(new Font("Arial",Font.PLAIN,13)); b.setBackground(C[2]); b.setForeground(C[6]);
        b.setOpaque(true); b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(C[9],1),BorderFactory.createEmptyBorder(7,14,7,14)));
    }
}
