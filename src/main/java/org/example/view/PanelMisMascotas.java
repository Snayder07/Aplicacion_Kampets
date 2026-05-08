package org.example.view;

import org.example.controller.MascotaAdminController;
import org.example.model.Cliente;
import org.example.model.Especies;
import org.example.model.Mascotas;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PanelMisMascotas {
    public JPanel panel;
    private boolean temaOscuro = false;

    private final MascotaAdminController ctrl = new MascotaAdminController();

    private final Color[] CLARO = {
            new Color(240,246,252), new Color(26,74,122),   Color.WHITE,
            new Color(42,90,138),   new Color(230,240,250), Color.WHITE,
            new Color(26,58,90),    new Color(138,170,200), new Color(224,112,32),
            new Color(208,228,244), new Color(15,53,96),    new Color(122,175,212),
            new Color(168,200,232), new Color(168,212,245),
    };
    private final Color[] OSCURO = {
            new Color(18,24,38),   new Color(13,18,30),   new Color(26,34,52),
            new Color(37,55,90),   new Color(32,42,64),   Color.WHITE,
            new Color(226,232,240),new Color(100,116,139),new Color(251,146,60),
            new Color(30,41,59),   new Color(9,14,24),    new Color(122,175,212),
            new Color(80,120,170), new Color(100,160,210),
    };
    private Color[] C = CLARO;

    public PanelMisMascotas() {
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

    private JLabel lbl(String t, int sz, int st, Color c) {
        JLabel l = new JLabel(t); l.setFont(new Font("Arial",st,sz)); l.setForeground(c); return l;
    }
    private JButton btn(String t, Color bg, Color fg, boolean borde) {
        JButton b = new JButton(t); b.setFont(new Font("Arial",Font.PLAIN,13));
        b.setBackground(bg); b.setForeground(fg); b.setOpaque(true);
        b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (borde) b.setBorder(BorderFactory.createLineBorder(fg,1)); else b.setBorderPainted(false);
        return b;
    }

    // ── Sidebar ───────────────────────────────────────────
    private JPanel crearSidebar() {
        JPanel sb = new JPanel();
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBackground(C[1]); sb.setPreferredSize(new Dimension(220,0));
        sb.setBorder(BorderFactory.createEmptyBorder(20,12,20,12));

        JLabel logo;
        try {
            ImageIcon ic = new ImageIcon(getClass().getResource("/logo.png"));
            logo = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(160,55,Image.SCALE_SMOOTH)));
        } catch (Exception e) { logo = lbl("Kampets",18,Font.BOLD,C[5]); }
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sb.add(logo); sb.add(Box.createVerticalStrut(16));
        agregarSep(sb);

        agregarSeccion(sb, "PRINCIPAL");
        String[] mp = {"Inicio","Mis citas","Historial","Mis mascotas"};
        for (int i = 0; i < mp.length; i++) {
            final int idx = i;
            JButton b = btn(mp[i], i==3?C[2]:C[1], i==3?C[1]:C[5], false);
            b.setFont(new Font("Arial", i==3?Font.BOLD:Font.PLAIN, 13));
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE,38));
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (idx==0) Main.cambiarPantalla("panelCliente");
                    if (idx==1) Main.cambiarPantalla("misCitas");
                    if (idx==2) Main.cambiarPantalla("historial");
                    if (idx==3) Main.cambiarPantalla("misMascotas");
                }
            });
            sb.add(b); sb.add(Box.createVerticalStrut(3));
        }
        sb.add(Box.createVerticalStrut(12));

        agregarSeccion(sb, "SERVICIOS");
        String[] ms = {"Alimentos","Vacunas"};
        for (String item : ms) {
            JButton b = btn(item, C[1], C[5], false);
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE,38));
            b.setHorizontalAlignment(SwingConstants.LEFT);
            if (item.equals("Alimentos")) b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("alimentos"); }
            });
            if (item.equals("Vacunas")) b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("vacunas"); }
            });
            sb.add(b); sb.add(Box.createVerticalStrut(3));
        }
        sb.add(Box.createVerticalGlue());

        JButton cerrar = btn("Cerrar sesion", C[1], C[12], true);
        cerrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        cerrar.setMaximumSize(new Dimension(Integer.MAX_VALUE,36));
        cerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(panel,"Deseas cerrar sesion?",
                        "Cerrar sesion",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                    Main.clienteActual = null;
                    Main.frame.setSize(420,520);
                    Main.frame.setLocationRelativeTo(null);
                    Main.cambiarPantalla("login");
                }
            }
        });
        sb.add(cerrar); sb.add(Box.createVerticalStrut(8));

        JPanel up = new JPanel(new BorderLayout(8,0));
        up.setBackground(C[10]); up.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        up.setMaximumSize(new Dimension(Integer.MAX_VALUE,55));
        up.setAlignmentX(Component.LEFT_ALIGNMENT);
        String nombreCliente = Main.clienteActual != null ? Main.clienteActual.getNombre() : "Cliente";
        String[] partes = nombreCliente.split(" ");
        String iniciales = partes.length >= 2 ?
                String.valueOf(partes[0].charAt(0)) + String.valueOf(partes[1].charAt(0)) :
                String.valueOf(nombreCliente.charAt(0));
        JLabel av = lbl(iniciales,13,Font.BOLD,C[1]); av.setBackground(C[5]); av.setOpaque(true);
        av.setPreferredSize(new Dimension(34,34)); av.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel ui = new JPanel(new GridLayout(2,1)); ui.setBackground(C[10]);
        ui.add(lbl(nombreCliente,12,Font.BOLD,C[5]));
        ui.add(lbl("Cliente",10,Font.PLAIN,C[11]));
        up.add(av,BorderLayout.WEST); up.add(ui,BorderLayout.CENTER);
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
                BorderFactory.createMatteBorder(0,0,1,0,C[9]),
                BorderFactory.createEmptyBorder(16,24,16,24)));
        JPanel topLeft = new JPanel(new GridLayout(2,1)); topLeft.setBackground(C[2]);
        topLeft.add(lbl("Mis mascotas",20,Font.BOLD,C[6]));
        topLeft.add(lbl("Administra tus mascotas registradas",12,Font.PLAIN,C[7]));

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));
        topRight.setBackground(C[2]);
        JButton btnTema = new JButton(temaOscuro?"☀  Claro":"🌙  Oscuro");
        btnTema.setFont(new Font("Arial",Font.PLAIN,13));
        btnTema.setBackground(C[0]); btnTema.setForeground(C[6]); btnTema.setOpaque(true);
        btnTema.setFocusPainted(false); btnTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTema.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9],1), BorderFactory.createEmptyBorder(7,14,7,14)));
        btnTema.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                temaOscuro = !temaOscuro; Main.aplicarTemaGlobal(temaOscuro); construir();
            }
        });

        JButton btnAgregar = btn("+ Agregar mascota", C[1], C[5], false);
        btnAgregar.setFont(new Font("Arial",Font.BOLD,13));
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(9,18,9,18));
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { mostrarFormularioAgregar(); }
        });

        topRight.add(btnTema); topRight.add(btnAgregar);
        topbar.add(topLeft,BorderLayout.WEST); topbar.add(topRight,BorderLayout.EAST);
        contenido.add(topbar,BorderLayout.NORTH);

        // Lista de mascotas
        JPanel cuerpo = new JPanel(new BorderLayout(0,20));
        cuerpo.setBackground(C[0]);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(24,28,28,28));

        // Filtrar mascotas del cliente logueado
        List<Mascotas> todas = ctrl.listarTodas();
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(C[0]);

        boolean hayMascotas = false;
        for (Mascotas m : todas) {
            if (Main.clienteActual != null &&
                    m.getCliente() != null &&
                    m.getCliente().getId().equals(Main.clienteActual.getId())) {

                hayMascotas = true;
                JPanel card = new JPanel(new BorderLayout(12,0));
                card.setBackground(C[2]);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0,4,0,0,C[1]),
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(C[9],1),
                                BorderFactory.createEmptyBorder(14,18,14,18))));

                String especie = m.getEspecie() != null ? m.getEspecie().getNombre() : "—";
                String fechaNac = m.getFechaNac() != null ? m.getFechaNac().toString() : "—";
                String sexo = m.getSexo() != null ? m.getSexo() : "—";

                JPanel izq = new JPanel(new GridLayout(2,1,0,4));
                izq.setBackground(C[2]);
                izq.add(lbl("🐾  " + m.getNombre(),15,Font.BOLD,C[6]));
                izq.add(lbl(especie + "  ·  Nacimiento: " + fechaNac + "  ·  Sexo: " + sexo,
                        12,Font.PLAIN,C[7]));

                card.add(izq,BorderLayout.CENTER);
                lista.add(card);
                lista.add(Box.createVerticalStrut(10));
            }
        }

        if (!hayMascotas) {
            JPanel sinMascotas = new JPanel(new GridBagLayout());
            sinMascotas.setBackground(C[0]);
            JPanel inner = new JPanel();
            inner.setLayout(new BoxLayout(inner,BoxLayout.Y_AXIS));
            inner.setBackground(C[0]);
            JLabel ico = lbl("🐾",48,Font.PLAIN,C[7]);
            ico.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel msg = lbl("No tienes mascotas registradas",16,Font.PLAIN,C[7]);
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel sub = lbl("Haz clic en '+ Agregar mascota' para registrar tu primera mascota",
                    12,Font.PLAIN,C[11]);
            sub.setAlignmentX(Component.CENTER_ALIGNMENT);
            inner.add(ico); inner.add(Box.createVerticalStrut(12));
            inner.add(msg); inner.add(Box.createVerticalStrut(8)); inner.add(sub);
            sinMascotas.add(inner);
            cuerpo.add(sinMascotas,BorderLayout.CENTER);
        } else {
            JScrollPane scroll = new JScrollPane(lista);
            scroll.setBorder(null); scroll.getViewport().setBackground(C[0]);
            scroll.getVerticalScrollBar().setUnitIncrement(12);
            cuerpo.add(scroll,BorderLayout.CENTER);
        }

        contenido.add(cuerpo,BorderLayout.CENTER);
        return contenido;
    }

    // ── Formulario agregar mascota ────────────────────────
    private void mostrarFormularioAgregar() {
        if (Main.clienteActual == null) {
            JOptionPane.showMessageDialog(panel,
                    "Debes iniciar sesión para agregar mascotas.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(panel),
                "Agregar mascota", true);
        dlg.setSize(400, 350);
        dlg.setLocationRelativeTo(panel);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(C[2]);
        form.setBorder(BorderFactory.createEmptyBorder(24,28,24,28));

        // Nombre
        form.add(lbl("Nombre de la mascota", 12, Font.BOLD, C[6]));
        form.add(Box.createVerticalStrut(6));
        JTextField tfNombre = new JTextField();
        tfNombre.setFont(new Font("Arial",Font.PLAIN,13));
        tfNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE,38));
        tfNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9],1),
                BorderFactory.createEmptyBorder(6,10,6,10)));
        form.add(tfNombre); form.add(Box.createVerticalStrut(14));

        // Especie
        form.add(lbl("Especie", 12, Font.BOLD, C[6]));
        form.add(Box.createVerticalStrut(6));
        JComboBox<Especies> cbEspecie = new JComboBox<>();
        cbEspecie.setFont(new Font("Arial",Font.PLAIN,13));
        cbEspecie.setMaximumSize(new Dimension(Integer.MAX_VALUE,38));
        List<Especies> especies = ctrl.listarEspecies();
        for (Especies esp : especies) cbEspecie.addItem(esp);
        cbEspecie.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                if (value instanceof Especies) setText(((Especies)value).getNombre());
                return this;
            }
        });
        form.add(cbEspecie); form.add(Box.createVerticalStrut(14));

        // Fecha nacimiento
        form.add(lbl("Fecha de nacimiento (yyyy-MM-dd)", 12, Font.BOLD, C[6]));
        form.add(Box.createVerticalStrut(6));
        JTextField tfFecha = new JTextField();
        tfFecha.setFont(new Font("Arial",Font.PLAIN,13));
        tfFecha.setMaximumSize(new Dimension(Integer.MAX_VALUE,38));
        tfFecha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9],1),
                BorderFactory.createEmptyBorder(6,10,6,10)));
        form.add(tfFecha); form.add(Box.createVerticalStrut(14));

        // Sexo
        form.add(lbl("Sexo", 12, Font.BOLD, C[6]));
        form.add(Box.createVerticalStrut(6));
        JComboBox<String> cbSexo = new JComboBox<>(new String[]{"Macho","Hembra"});
        cbSexo.setFont(new Font("Arial",Font.PLAIN,13));
        cbSexo.setMaximumSize(new Dimension(Integer.MAX_VALUE,38));
        form.add(cbSexo); form.add(Box.createVerticalStrut(20));

        // Botones
        JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));
        bots.setBackground(C[2]);
        JButton btnCancel = btn("Cancelar",C[4],C[1],true);
        btnCancel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9],1), BorderFactory.createEmptyBorder(8,16,8,16)));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { dlg.dispose(); }
        });

        JButton btnGuardar = btn("Guardar",C[1],C[5],false);
        btnGuardar.setFont(new Font("Arial",Font.BOLD,13));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = tfNombre.getText().trim();
                Especies especie = (Especies) cbEspecie.getSelectedItem();
                String fecha = tfFecha.getText().trim();
                String sexo = (String) cbSexo.getSelectedItem();
                Cliente cliente = Main.clienteActual;

                boolean ok = ctrl.registrarMascota(nombre, especie, cliente, fecha, sexo, panel);
                if (ok) {
                    dlg.dispose();
                    construir(); // recargar lista
                }
            }
        });

        bots.add(btnCancel); bots.add(btnGuardar);
        bots.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(bots);

        dlg.add(form);
        dlg.setVisible(true);
    }

    private void agregarSep(JPanel p) {
        JSeparator s = new JSeparator(); s.setForeground(C[3]);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE,1)); s.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(Box.createVerticalStrut(6)); p.add(s); p.add(Box.createVerticalStrut(6));
    }
    private void agregarSeccion(JPanel p, String t) {
        JLabel l = lbl(t,10,Font.PLAIN,C[11]);
        l.setBorder(BorderFactory.createEmptyBorder(8,0,4,0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE,24)); p.add(l);
    }
}