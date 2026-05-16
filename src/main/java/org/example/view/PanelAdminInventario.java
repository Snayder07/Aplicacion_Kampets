package org.example.view;

import org.example.controller.InventarioController;
import org.example.model.Productos;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;

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
        List<Productos> lista = ctrl.listarTodos();
        long total     = lista.size();
        long stockBajo = lista.stream().filter(p -> p.getStock() != null && p.getStock() < 10).count();
        long tipos     = lista.stream().map(Productos::getTipo).filter(Objects::nonNull).distinct().count();
        long sinStock  = lista.stream().filter(p -> p.getStock() != null && p.getStock() == 0).count();

        JPanel c = new JPanel(new BorderLayout()); c.setBackground(C[0]);

        // ── Topbar ────────────────────────────────────────
        JPanel tb = new JPanel(new BorderLayout()); tb.setBackground(C[2]);
        tb.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,C[9]),
                BorderFactory.createEmptyBorder(16,28,16,28)));
        JPanel tl = new JPanel(new GridLayout(2,1)); tl.setBackground(C[2]);
        tl.add(lbl("Inventario",22,Font.BOLD,C[6]));
        tl.add(lbl("Medicamentos, vacunas y productos disponibles",12,Font.PLAIN,C[7]));
        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); tr.setBackground(C[2]);
        JButton btnAgregar = new JButton("+ Agregar producto");
        btnAgregar.setFont(new Font("Arial",Font.BOLD,13));
        btnAgregar.setBackground(new Color(22,163,74)); btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setOpaque(true); btnAgregar.setBorderPainted(false);
        btnAgregar.setCursor(Main.cursorHover != null ? Main.cursorHover : new Cursor(Cursor.HAND_CURSOR));
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(9,18,9,18));
        btnAgregar.addActionListener(e -> mostrarFormAgregar());
        tr.add(btnAgregar);
        tb.add(tl,BorderLayout.WEST); tb.add(tr,BorderLayout.EAST);
        c.add(tb,BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0,20));
        body.setBackground(C[0]);
        body.setBorder(BorderFactory.createEmptyBorder(24,28,28,28));

        // ── Stats ─────────────────────────────────────────
        JPanel stats = new JPanel(new GridLayout(1,4,16,0)); stats.setBackground(C[0]);
        Object[][] st = {
                {"Total productos",  String.valueOf(total),     C[1]},
                {"Stock bajo (<10)", String.valueOf(stockBajo), C[12]},
                {"Sin stock",        String.valueOf(sinStock),  C[8]},
                {"Categorias",       String.valueOf(tipos),     C[1]},
        };
        for (Object[] s : st) {
            JPanel card = new JPanel(new BorderLayout(0,4)); card.setBackground(C[2]);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(C[9],1),
                    BorderFactory.createEmptyBorder(16,20,16,20)));
            card.add(lbl((String)s[0],11,Font.PLAIN,C[7]),BorderLayout.NORTH);
            card.add(lbl((String)s[1],28,Font.BOLD,(Color)s[2]),BorderLayout.CENTER);
            stats.add(card);
        }
        body.add(stats,BorderLayout.NORTH);

        // ── Tabla con foto + eliminar ─────────────────────
        // Columnas: Foto | Producto | Tipo | Marca | Precio | Stock | Estado | Eliminar
        String[] cols = {"Foto","Producto","Tipo","Marca","Precio","Stock","Estado","Eliminar"};

        Object[][] datos = new Object[lista.size()][8];
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
            // Foto: guardamos los bytes para el renderer
            datos[i] = new Object[]{p.getFoto(), nombre, tipo, marca, precio, stock, estado, "Eliminar"};
        }

        DefaultTableModel modelo = new DefaultTableModel(datos, cols) {
            public boolean isCellEditable(int r, int cc) { return cc == 7; } // solo col Eliminar
            public Class<?> getColumnClass(int col) {
                if (col == 0) return byte[].class;
                return String.class;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setBackground(C[2]); tabla.setForeground(C[6]);
        tabla.setFont(new Font("Arial",Font.PLAIN,13)); tabla.setRowHeight(48);
        tabla.setShowGrid(false); tabla.setIntercellSpacing(new Dimension(0,0));
        tabla.setSelectionBackground(C[3]); tabla.setFillsViewportHeight(true);
        JTableHeader th = tabla.getTableHeader();
        th.setBackground(C[14]); th.setForeground(temaOscuro?C[7]:C[1]);
        th.setFont(new Font("Arial",Font.BOLD,11));
        th.setReorderingAllowed(false); th.setPreferredSize(new Dimension(0,36));

        // Anchos
        int[] anchos = {52, 180, 100, 110, 100, 70, 100, 90};
        for (int i = 0; i < anchos.length; i++)
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);

        // ── Renderer: Foto (col 0) ────────────────────────
        tabla.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int col) {
                JLabel lbl = new JLabel();
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBackground(s ? C[3] : (r%2==0 ? C[2] : C[4]));
                lbl.setOpaque(true);
                if (v instanceof byte[] && ((byte[])v).length > 0) {
                    try {
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream((byte[])v));
                        if (img != null) {
                            Image scaled = img.getScaledInstance(38, 38, Image.SCALE_SMOOTH);
                            lbl.setIcon(new ImageIcon(scaled));
                        }
                    } catch (Exception ex) { lbl.setText("🖼"); }
                } else {
                    lbl.setText("—");
                    lbl.setForeground(C[7]);
                }
                return lbl;
            }
        });

        // ── Renderer: Estado (col 6) ──────────────────────
        tabla.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t,v,s,f,r,col);
                l.setFont(new Font("Arial",Font.BOLD,12));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                switch(v != null ? v.toString() : "") {
                    case "Sin stock":  l.setForeground(C[12]); break;
                    case "Stock bajo": l.setForeground(C[8]);  break;
                    default:           l.setForeground(C[13]);
                }
                l.setBackground(s ? C[3] : (r%2==0 ? C[2] : C[4]));
                l.setOpaque(true); return l;
            }
        });

        // ── Renderer + Editor: Eliminar (col 7) ───────────
        tabla.getColumnModel().getColumn(7).setCellRenderer(new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int col) {
                JButton btn = new JButton(" Eliminar");
                btn.setFont(new Font("Arial", Font.BOLD, 11));
                btn.setBackground(new Color(220, 38, 38));
                btn.setForeground(Color.WHITE);
                btn.setOpaque(true); btn.setBorderPainted(false);
                btn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return btn;
            }
        });

        tabla.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private JButton btn = new JButton(" Eliminar");
            {
                btn.setFont(new Font("Arial", Font.BOLD, 11));
                btn.setBackground(new Color(220, 38, 38));
                btn.setForeground(Color.WHITE);
                btn.setOpaque(true); btn.setBorderPainted(false);
                btn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                btn.setCursor(Main.cursorHover != null ? Main.cursorHover : new Cursor(Cursor.HAND_CURSOR));
                btn.addActionListener(e -> {
                    int row = tabla.getSelectedRow();
                    if (row < 0) return;
                    Productos prod = lista.get(row);
                    int confirm = JOptionPane.showConfirmDialog(panel,
                            "¿Eliminar \"" + prod.getNombre() + "\" del inventario?",
                            "Confirmar eliminacion", JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        fireEditingStopped();
                        ctrl.eliminarProducto(prod.getId(), panel);
                        construir();
                    } else {
                        fireEditingCanceled();
                    }
                });
            }
            public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int col) { return btn; }
            public Object getCellEditorValue() { return "Eliminar"; }
        });

        // ── Renderer base columnas de texto ───────────────
        DefaultTableCellRenderer base = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int col) {
                super.getTableCellRendererComponent(t,v,s,f,r,col);
                setForeground(C[6]);
                setBackground(r%2==0 ? C[2] : C[4]);
                if (s) setBackground(C[3]);
                setFont(new Font("Arial",Font.PLAIN,13));
                setOpaque(true);
                setBorder(BorderFactory.createEmptyBorder(0,14,0,14));
                return this;
            }
        };
        for (int i = 1; i <= 5; i++) tabla.getColumnModel().getColumn(i).setCellRenderer(base);

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(null); sp.getViewport().setBackground(C[2]);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(C[2]); wrapper.add(sp, BorderLayout.CENTER);
        body.add(wrapper, BorderLayout.CENTER);

        JScrollPane outerScroll = new JScrollPane(body);
        outerScroll.setBorder(null); outerScroll.getViewport().setBackground(C[0]);
        outerScroll.getVerticalScrollBar().setUnitIncrement(16);
        outerScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        c.add(outerScroll, BorderLayout.CENTER);
        return c;
    }

    // ── Formulario agregar producto ───────────────────────
    private void mostrarFormAgregar() {
        JDialog dlg = new JDialog();
        dlg.setTitle("Agregar producto");
        dlg.setModal(true);
        dlg.setResizable(false);
        dlg.setLocationRelativeTo(panel);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(new Color(240,253,244));
        root.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        Color verde  = new Color(22,101,52);
        Color gris   = new Color(100,116,139);
        Color borde  = new Color(187,224,200);

        // Titulo
        JLabel tit = new JLabel("Nuevo producto");
        tit.setFont(new Font("Arial",Font.BOLD,16)); tit.setForeground(verde);
        tit.setAlignmentX(Component.LEFT_ALIGNMENT); root.add(tit);
        root.add(Box.createVerticalStrut(16));

        // Campos
        JTextField tfNombre = tf(); JTextField tfTipo   = tf();
        JTextField tfMarca  = tf(); JTextField tfPrecio = tf();
        JTextField tfStock  = tf();

        root.add(campo(root, "Nombre *",   tfNombre, gris, borde));
        root.add(Box.createVerticalStrut(8));
        root.add(campo(root, "Tipo",       tfTipo,   gris, borde));
        root.add(Box.createVerticalStrut(8));
        root.add(campo(root, "Marca",      tfMarca,  gris, borde));
        root.add(Box.createVerticalStrut(8));
        root.add(campo(root, "Precio *",   tfPrecio, gris, borde));
        root.add(Box.createVerticalStrut(8));
        root.add(campo(root, "Stock *",    tfStock,  gris, borde));
        root.add(Box.createVerticalStrut(14));

        // ── Selector de foto ──────────────────────────────
        JLabel lblFoto = new JLabel("Foto del producto (opcional)");
        lblFoto.setFont(new Font("Arial",Font.PLAIN,12));
        lblFoto.setForeground(gris); lblFoto.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(lblFoto); root.add(Box.createVerticalStrut(6));

        JPanel fotoPanel = new JPanel(new BorderLayout(10,0));
        fotoPanel.setBackground(new Color(240,253,244));
        fotoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fotoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel previewFoto = new JLabel("Sin foto");
        previewFoto.setPreferredSize(new Dimension(70, 70));
        previewFoto.setHorizontalAlignment(SwingConstants.CENTER);
        previewFoto.setFont(new Font("Arial",Font.PLAIN,10));
        previewFoto.setForeground(gris);
        previewFoto.setOpaque(true);
        previewFoto.setBackground(Color.WHITE);
        previewFoto.setBorder(BorderFactory.createLineBorder(borde,1));

        final byte[][] fotoBytes = {null};

        JButton btnFoto = new JButton("Seleccionar imagen");
        btnFoto.setFont(new Font("Arial",Font.PLAIN,12));
        btnFoto.setBackground(Color.WHITE); btnFoto.setForeground(verde);
        btnFoto.setBorder(BorderFactory.createLineBorder(verde,1));
        btnFoto.setFocusPainted(false);
        btnFoto.setCursor(Main.cursorHover != null ? Main.cursorHover : new Cursor(Cursor.HAND_CURSOR));
        btnFoto.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Seleccionar foto del producto");
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Imagenes (jpg, png, gif)", "jpg","jpeg","png","gif","bmp"));
            if (fc.showOpenDialog(dlg) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                try {
                    fotoBytes[0] = Files.readAllBytes(f.toPath());
                    BufferedImage img = ImageIO.read(f);
                    if (img != null) {
                        Image scaled = img.getScaledInstance(66, 66, Image.SCALE_SMOOTH);
                        previewFoto.setIcon(new ImageIcon(scaled));
                        previewFoto.setText("");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dlg, "No se pudo cargar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel fotoDer = new JPanel();
        fotoDer.setLayout(new BoxLayout(fotoDer, BoxLayout.Y_AXIS));
        fotoDer.setBackground(new Color(240,253,244));
        fotoDer.add(btnFoto);
        fotoDer.add(Box.createVerticalStrut(6));
        JLabel hint = new JLabel("JPG, PNG, GIF (max 5MB)");
        hint.setFont(new Font("Arial",Font.PLAIN,10)); hint.setForeground(gris);
        fotoDer.add(hint);

        fotoPanel.add(previewFoto, BorderLayout.WEST);
        fotoPanel.add(fotoDer, BorderLayout.CENTER);
        root.add(fotoPanel);
        root.add(Box.createVerticalStrut(20));

        // ── Botones ───────────────────────────────────────
        JPanel bots = new JPanel(new GridLayout(1,2,10,0));
        bots.setBackground(new Color(240,253,244));
        bots.setAlignmentX(Component.LEFT_ALIGNMENT);
        bots.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial",Font.PLAIN,13));
        btnCancelar.setBackground(Color.WHITE); btnCancelar.setForeground(verde);
        btnCancelar.setBorder(BorderFactory.createLineBorder(verde,1));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(Main.cursorHover != null ? Main.cursorHover : new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dlg.dispose());

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Arial",Font.BOLD,13));
        btnGuardar.setBackground(verde); btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setOpaque(true); btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(Main.cursorHover != null ? Main.cursorHover : new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> {
            boolean ok = ctrl.agregarProducto(
                    tfNombre.getText(), tfTipo.getText(),
                    tfMarca.getText(), tfPrecio.getText(),
                    tfStock.getText(), fotoBytes[0], panel);
            if (ok) { dlg.dispose(); recargar(); }
        });

        bots.add(btnCancelar); bots.add(btnGuardar);
        root.add(bots);

        dlg.add(root);
        dlg.pack();
        dlg.setMinimumSize(new Dimension(380, dlg.getHeight()));
        dlg.setLocationRelativeTo(panel);
        dlg.setVisible(true);
    }

    private JPanel campo(JPanel root, String label, JTextField tf, Color gris, Color borde) {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(240,253,244)); p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial",Font.PLAIN,12)); l.setForeground(gris);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l); p.add(Box.createVerticalStrut(3)); p.add(tf);
        return p;
    }

    private JTextField tf() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Arial",Font.PLAIN,13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(187,224,200),1),
                BorderFactory.createEmptyBorder(4,10,4,10)));
        return tf;
    }

    private void estilizarTema(JButton b){
        b.setFont(new Font("Arial",Font.PLAIN,13)); b.setBackground(C[2]); b.setForeground(C[6]);
        b.setOpaque(true); b.setFocusPainted(false);
        b.setCursor(Main.cursorHover != null ? Main.cursorHover : new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9],1),BorderFactory.createEmptyBorder(7,14,7,14)));
    }
}
