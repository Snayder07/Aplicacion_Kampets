package org.example.view;

import org.example.controller.CitaAdminController;
import org.example.model.Citas;
import org.example.model.Control_vacunas;
import org.example.service.ControlVacunaService;
import org.example.service.EmpleadoService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PanelAdmin {
    public JPanel panel;
    private boolean temaOscuro = false;

    private final EmpleadoService      empleadoService  = new EmpleadoService();
    private final CitaAdminController  citaCtrl         = new CitaAdminController();
    private final ControlVacunaService vacunaService    = new ControlVacunaService();

    private final Color[] CLARO = {
            new Color(240,253,244), new Color(22,101,52),   Color.WHITE,
            new Color(34,120,70),   new Color(220,245,230), Color.WHITE,
            new Color(15,60,30),    new Color(100,130,110), new Color(234,88,12),
            new Color(187,224,200), new Color(15,60,30),    new Color(134,190,155),
            new Color(220,38,38),   new Color(22,163,74),   new Color(210,240,220),
    };
    private final Color[] OSCURO = {
            new Color(18,24,38),    new Color(13,18,30),   new Color(26,34,52),
            new Color(37,55,90),    new Color(32,42,64),   Color.WHITE,
            new Color(226,232,240), new Color(148,163,184),new Color(251,146,60),
            new Color(30,41,59),    new Color(9,14,24),    new Color(122,175,212),
            new Color(239,68,68),   new Color(34,197,94),  new Color(15,23,42),
    };
    private Color[] C = CLARO;

    public PanelAdmin() { panel = new JPanel(new BorderLayout()); construir(); }
    public void setTema(boolean o) { if (o != temaOscuro) { temaOscuro = o; construir(); } }
    public void recargar() { construir(); }

    private void construir() {
        panel.removeAll(); C = temaOscuro ? OSCURO : CLARO;
        panel.setBackground(C[0]);
        panel.add(SidebarAdmin.crear(C, temaOscuro, "panelAdmin", panel), BorderLayout.WEST);
        panel.add(crearContenido(), BorderLayout.CENTER);
        panel.revalidate(); panel.repaint();
    }

    private JLabel lbl(String t, int sz, int st, Color c) {
        JLabel l = new JLabel(t); l.setFont(new Font("Arial",st,sz + 2)); l.setForeground(c); return l;
    }
    private JButton btn(String t, Color bg, Color fg) {
        JButton b = new JButton(t); b.setFont(new Font("Arial",Font.BOLD,15));
        b.setBackground(bg); b.setForeground(fg); b.setOpaque(true);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Main.cursorHover != null ? Main.cursorHover : new Cursor(Cursor.HAND_CURSOR)); return b;
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(C[0]);
        contenido.add(crearTopbar(), BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(crearCentro());
        scroll.setBorder(null); scroll.getViewport().setBackground(C[0]);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        contenido.add(scroll, BorderLayout.CENTER);
        return contenido;
    }

    private JPanel crearTopbar() {
        JPanel tb = new JPanel(new BorderLayout());
        tb.setBackground(C[2]);
        tb.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,C[9]),
                BorderFactory.createEmptyBorder(16,28,16,28)));

        JPanel izq = new JPanel();
        izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));
        izq.setBackground(C[2]);

        JLabel titulo = new JLabel("Inicio");
        titulo.setFont(new Font("Arial",Font.BOLD,22)); titulo.setForeground(C[6]);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES")));
        fechaHoy = Character.toUpperCase(fechaHoy.charAt(0)) + fechaHoy.substring(1);
        JLabel fecha = new JLabel(fechaHoy + " · Kampets Veterinaria");
        fecha.setFont(new Font("Arial",Font.PLAIN,12)); fecha.setForeground(C[11]);
        fecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        izq.add(titulo); izq.add(fecha);

        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));
        der.setBackground(C[2]);

        JButton btnExportar = btn("⬇  Exportar reporte", C[2], C[1]);
        btnExportar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9],1), BorderFactory.createEmptyBorder(8,16,8,16)));
        btnExportar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { exportarReporte(); }
        });

        JButton btnNuevo = btn("+ Nuevo admin", new Color(22,163,74), Color.WHITE);
        btnNuevo.setBorder(BorderFactory.createEmptyBorder(9,18,9,18));
        btnNuevo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { mostrarFormularioNuevoAdmin(); }
        });

        der.add(btnExportar); der.add(btnNuevo);
        tb.add(izq, BorderLayout.WEST); tb.add(der, BorderLayout.EAST);
        return tb;
    }

    private void mostrarFormularioNuevoAdmin() {
        JDialog dlg = new JDialog(Main.frame, "Registrar nuevo administrador", true);
        dlg.setSize(440, 480);
        dlg.setLocationRelativeTo(Main.frame);
        dlg.setResizable(false);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(C[2]);
        form.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel titulo = lbl("Nuevo administrador", 16, Font.BOLD, C[6]);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub = lbl("Solo los admins pueden crear cuentas de administrador", 11, Font.PLAIN, C[7]);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(titulo); form.add(Box.createVerticalStrut(4)); form.add(sub);
        form.add(Box.createVerticalStrut(20));

        // Nombre
        JLabel lNombre = lbl("Nombre", 12, Font.BOLD, C[6]);
        lNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lNombre); form.add(Box.createVerticalStrut(6));
        JTextField tfNombre = new JTextField();
        tfNombre.setFont(new Font("Arial", Font.PLAIN, 13));
        tfNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tfNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1), BorderFactory.createEmptyBorder(6,10,6,10)));
        form.add(tfNombre); form.add(Box.createVerticalStrut(12));

        // Apellido
        JLabel lApellido = lbl("Apellido", 12, Font.BOLD, C[6]);
        lApellido.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lApellido); form.add(Box.createVerticalStrut(6));
        JTextField tfApellido = new JTextField();
        tfApellido.setFont(new Font("Arial", Font.PLAIN, 13));
        tfApellido.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tfApellido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1), BorderFactory.createEmptyBorder(6,10,6,10)));
        form.add(tfApellido); form.add(Box.createVerticalStrut(12));

        // Correo
        JLabel lCorreo = lbl("Correo electrónico", 12, Font.BOLD, C[6]);
        lCorreo.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lCorreo); form.add(Box.createVerticalStrut(6));
        JTextField tfCorreo = new JTextField();
        tfCorreo.setFont(new Font("Arial", Font.PLAIN, 13));
        tfCorreo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tfCorreo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1), BorderFactory.createEmptyBorder(6,10,6,10)));
        form.add(tfCorreo); form.add(Box.createVerticalStrut(12));

        // Contraseña
        JLabel lPass = lbl("Contraseña", 12, Font.BOLD, C[6]);
        lPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lPass); form.add(Box.createVerticalStrut(6));
        JPasswordField tfPass = new JPasswordField();
        tfPass.setFont(new Font("Arial", Font.PLAIN, 13));
        tfPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tfPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1), BorderFactory.createEmptyBorder(6,10,6,10)));
        form.add(tfPass); form.add(Box.createVerticalStrut(12));

        // Cargo
        JLabel lCargo = lbl("Cargo", 12, Font.BOLD, C[6]);
        lCargo.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lCargo); form.add(Box.createVerticalStrut(6));
        JComboBox<String> cbCargo = new JComboBox<>(new String[]{
                "Administrador", "Veterinario", "Recepcionista"
        });
        cbCargo.setFont(new Font("Arial", Font.PLAIN, 13));
        cbCargo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(cbCargo); form.add(Box.createVerticalStrut(24));

        // Botones
        JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bots.setBackground(C[2]); bots.setAlignmentX(Component.LEFT_ALIGNMENT);
        bots.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnCancelar.setBackground(C[4]); btnCancelar.setForeground(C[6]);
        btnCancelar.setOpaque(true); btnCancelar.setBorderPainted(false);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1), BorderFactory.createEmptyBorder(8,16,8,16)));
        btnCancelar.setCursor(Main.cursorHover != null ? Main.cursorHover : new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { dlg.dispose(); }
        });

        JButton btnGuardar = btn("Registrar admin", new Color(22,163,74), Color.WHITE);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(9,18,9,18));
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre    = tfNombre.getText().trim();
                String apellido  = tfApellido.getText().trim();
                String correo = tfCorreo.getText().trim();
                String pass   = new String(tfPass.getPassword());
                String cargo  = (String) cbCargo.getSelectedItem();

                if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(dlg,
                            "Todos los campos son obligatorios.",
                            "Campos vac\u00edos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    empleadoService.registrarAdmin(nombre, apellido, correo, pass, cargo);
                    JOptionPane.showMessageDialog(dlg,
                            "✅ Administrador registrado exitosamente.\n\nCorreo: " + correo,
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dlg.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dlg,
                            ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bots.add(btnCancelar); bots.add(btnGuardar);
        form.add(bots);

        dlg.setContentPane(form);
        dlg.setVisible(true);
    }

    private JPanel crearCentro() {
        JPanel centro = new JPanel(new BorderLayout(0,20));
        centro.setBackground(C[0]);
        centro.setBorder(BorderFactory.createEmptyBorder(24,28,28,28));
        centro.add(crearFilaStats(), BorderLayout.NORTH);
        JPanel inferior = new JPanel(new BorderLayout(0,20));
        inferior.setBackground(C[0]);
        inferior.add(crearTablaCitas(),   BorderLayout.NORTH);
        inferior.add(crearTablaVacunas(), BorderLayout.CENTER);
        centro.add(inferior, BorderLayout.CENTER);
        return centro;
    }

    private JPanel crearFilaStats() {
        // Datos reales de BD
        List<Citas> citasHoy;
        List<Control_vacunas> todasVacunas;
        try { citasHoy      = citaCtrl.listarDeHoy();           } catch (Exception e) { citasHoy      = Collections.emptyList(); }
        try { todasVacunas  = vacunaService.listarTodas();      } catch (Exception e) { todasVacunas  = Collections.emptyList(); }

        long pendientes = todasVacunas.stream().filter(cv -> {
            String est = vacunaService.calcularEstado(cv.getProximaDosis());
            return est.equals("Vencida") || est.equals("Próxima");
        }).count();

        JPanel fila = new JPanel(new GridLayout(1,2,16,0));
        fila.setBackground(C[0]);
        Object[][] stats = {
                {"Citas hoy",         String.valueOf(citasHoy.size()),  "Programadas para hoy",  C[13]},
                {"Vacunas pendientes", String.valueOf(pendientes),       "Vencidas o próximas",   C[8]},
        };
        for (Object[] s : stats) {
            JPanel card = new JPanel(new BorderLayout(0,6));
            card.setBackground(C[2]);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(C[9],1),
                    BorderFactory.createEmptyBorder(20,22,20,22)));
            card.add(lbl((String)s[0],12,Font.PLAIN,C[7]),       BorderLayout.NORTH);
            card.add(lbl((String)s[1],32,Font.BOLD,C[6]),        BorderLayout.CENTER);
            card.add(lbl((String)s[2],11,Font.PLAIN,(Color)s[3]),BorderLayout.SOUTH);
            fila.add(card);
        }
        return fila;
    }

    private JPanel crearTablaCitas() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(C[2]); header.setBorder(BorderFactory.createEmptyBorder(16,20,14,20));
        JLabel titulo = lbl("Citas de hoy",15,Font.BOLD,C[6]);
        JButton verTodas = btn("Ver todas",C[4],C[1]);
        verTodas.setBorder(BorderFactory.createEmptyBorder(6,14,6,14));
        verTodas.setFont(new Font("Arial",Font.PLAIN,12));
        verTodas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("adminCitas"); }
        });
        header.add(titulo,BorderLayout.WEST); header.add(verTodas,BorderLayout.EAST);

        // Datos reales desde BD
        List<Citas> citasHoy;
        try { citasHoy = citaCtrl.listarDeHoy(); } catch (Exception ex) { citasHoy = Collections.emptyList(); }

        String[] cols = {"Cliente/Mascota","Hora","Vet","Estado"};
        Object[][] datos;
        if (citasHoy.isEmpty()) {
            datos = new Object[][]{{"Sin citas para hoy","—","—","—"}};
        } else {
            datos = new Object[citasHoy.size()][4];
            for (int i = 0; i < citasHoy.size(); i++) {
                Citas c = citasHoy.get(i);
                String clienteNombre = c.getMascota() != null && c.getMascota().getCliente() != null
                        ? c.getMascota().getCliente().getNombre() : "—";
                String[] partes = clienteNombre.split(" ");
                String clienteCorto = partes.length >= 2
                        ? partes[0] + " " + partes[1].charAt(0) + "."
                        : clienteNombre;
                String mascotaNombre = c.getMascota() != null ? c.getMascota().getNombre() : "—";
                String hora   = c.getHoraCita()   != null ? c.getHoraCita().toString()                     : "—";
                String vet    = c.getEmpleado()   != null ? c.getEmpleado().getNombre()                    : "—";
                String estado = c.getEstadoCita() != null ? c.getEstadoCita().toString()                   : "—";
                datos[i] = new Object[]{clienteCorto + " – " + mascotaNombre, hora, vet, estado};
            }
        }

        JTable tabla = construirTabla(cols, datos, 3);
        JScrollPane sp = new JScrollPane(tabla); sp.setBorder(null); sp.getViewport().setBackground(C[2]);
        JPanel wrapper = new JPanel(new BorderLayout()); wrapper.setBackground(C[2]);
        wrapper.add(header,BorderLayout.NORTH); wrapper.add(sp,BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel crearTablaVacunas() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(C[2]); header.setBorder(BorderFactory.createEmptyBorder(16,20,14,20));
        JLabel titulo = lbl("Vacunas pendientes",15,Font.BOLD,C[6]);
        JButton verTodas = btn("Ver todas",C[4],C[1]);
        verTodas.setBorder(BorderFactory.createEmptyBorder(6,14,6,14));
        verTodas.setFont(new Font("Arial",Font.PLAIN,12));
        verTodas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("adminVacunas"); }
        });
        header.add(titulo,BorderLayout.WEST); header.add(verTodas,BorderLayout.EAST);

        // Datos reales desde BD — solo Vencidas o Próximas
        List<Control_vacunas> todas;
        try { todas = vacunaService.listarTodas(); } catch (Exception ex) { todas = Collections.emptyList(); }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("es"));
        List<Object[]> filas = new ArrayList<>();
        for (Control_vacunas cv : todas) {
            String estado = vacunaService.calcularEstado(cv.getProximaDosis());
            if (!estado.equals("Vencida") && !estado.equals("Próxima")) continue;
            String mascota = cv.getMascota() != null ? cv.getMascota().getNombre() : "—";
            String duenio  = cv.getMascota() != null && cv.getMascota().getCliente() != null
                    ? cv.getMascota().getCliente().getNombre() : "—";
            String tipo    = cv.getVacuna()  != null ? cv.getVacuna().getNombre()  : "—";
            String ultima  = cv.getFechaAplicacion() != null ? cv.getFechaAplicacion().format(fmt)  : "—";
            String proxima = cv.getProximaDosis()    != null ? cv.getProximaDosis().format(fmt)      : "—";
            filas.add(new Object[]{mascota, duenio, tipo, ultima, proxima, estado});
        }

        String[] cols = {"Mascota","Dueño","Tipo de vacuna","Última aplicación","Próxima fecha","Estado"};
        Object[][] datos = filas.isEmpty()
                ? new Object[][]{{"Sin vacunas pendientes","—","—","—","—","—"}}
                : filas.toArray(new Object[0][]);

        JTable tabla = construirTabla(cols, datos, 5);
        int[] anchos = {90,160,140,130,130,100};
        for (int i=0;i<anchos.length;i++) tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        tabla.setRowHeight(40);
        JScrollPane sp = new JScrollPane(tabla); sp.setBorder(null); sp.getViewport().setBackground(C[2]);
        JPanel wrapper = new JPanel(new BorderLayout()); wrapper.setBackground(C[2]);
        wrapper.add(header,BorderLayout.NORTH); wrapper.add(sp,BorderLayout.CENTER);
        return wrapper;
    }

    private JTable construirTabla(String[] cols, Object[][] datos, int colEstado) {
        DefaultTableModel modelo = new DefaultTableModel(datos,cols) {
            public boolean isCellEditable(int r,int c){return false;}
        };
        JTable tabla = new JTable(modelo);
        tabla.setBackground(C[2]); tabla.setForeground(C[6]);
        tabla.setFont(new Font("Arial",Font.PLAIN,13)); tabla.setRowHeight(38);
        tabla.setShowGrid(false); tabla.setIntercellSpacing(new Dimension(0,0));
        tabla.setSelectionBackground(C[3]); tabla.setFillsViewportHeight(true);
        JTableHeader th = tabla.getTableHeader();
        th.setBackground(C[14]); th.setForeground(temaOscuro?C[7]:C[1]);
        th.setFont(new Font("Arial",Font.BOLD,11)); th.setReorderingAllowed(false);
        th.setPreferredSize(new Dimension(0,36));
        tabla.getColumnModel().getColumn(colEstado).setCellRenderer(new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int col){
                JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,s,f,r,col);
                l.setFont(new Font("Arial",Font.BOLD,12)); l.setHorizontalAlignment(SwingConstants.CENTER);
                switch(v==null?"":v.toString()){
                    case "Confirmada": l.setForeground(C[13]); break;
                    case "Pendiente":  l.setForeground(C[8]);  break;
                    case "Cancelada":  l.setForeground(C[12]); break;
                    case "Vencida":    l.setForeground(C[12]); break;
                    case "Próxima":    l.setForeground(C[8]);  break;
                    default:           l.setForeground(C[7]);
                }
                l.setBackground(s?C[3]:(r%2==0?C[2]:C[4])); l.setOpaque(true); return l;
            }
        });
        DefaultTableCellRenderer base = new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int col){
                super.getTableCellRendererComponent(t,v,s,f,r,col); setForeground(C[6]);
                setFont(new Font("Arial",Font.PLAIN,13)); setBackground(r%2==0?C[2]:C[4]);
                if(s)setBackground(C[3]); setOpaque(true);
                setBorder(BorderFactory.createEmptyBorder(0,14,0,14)); return this;
            }
        };
        for(int i=0;i<colEstado;i++) tabla.getColumnModel().getColumn(i).setCellRenderer(base);
        return tabla;
    }

    private void exportarReporte() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar reporte");
        chooser.setSelectedFile(new File("reporte_kampets.txt"));
        if (chooser.showSaveDialog(panel) != JFileChooser.APPROVE_OPTION) return;

        List<Citas> citasHoy;
        List<Control_vacunas> todasVacunas;
        try { citasHoy     = citaCtrl.listarDeHoy();      } catch (Exception e) { citasHoy     = Collections.emptyList(); }
        try { todasVacunas = vacunaService.listarTodas(); } catch (Exception e) { todasVacunas = Collections.emptyList(); }
        long pendientes = todasVacunas.stream().filter(cv -> {
            String est = vacunaService.calcularEstado(cv.getProximaDosis());
            return est.equals("Vencida") || est.equals("Próxima");
        }).count();

        try (PrintWriter pw = new PrintWriter(new FileWriter(chooser.getSelectedFile()))) {
            pw.println("========================================");
            pw.println("       REPORTE KAMPETS VETERINARIA      ");
            pw.println("========================================");
            pw.println("Fecha: " + LocalDate.now());
            pw.println(); pw.println("ESTADÍSTICAS DEL DÍA");
            pw.println("  Citas hoy:           " + citasHoy.size());
            pw.println("  Vacunas pendientes:  " + pendientes);
            pw.println();
            if (!citasHoy.isEmpty()) {
                pw.println("CITAS DE HOY:");
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
                for (Citas c : citasHoy) {
                    String mascota = c.getMascota() != null ? c.getMascota().getNombre() : "—";
                    String vet     = c.getEmpleado() != null ? c.getEmpleado().getNombre() : "—";
                    String hora    = c.getHoraCita() != null ? c.getHoraCita().format(fmt) : "—";
                    String estado  = c.getEstadoCita() != null ? c.getEstadoCita().toString() : "—";
                    pw.println("  " + hora + " | " + mascota + " | " + vet + " | " + estado);
                }
                pw.println();
            }
            pw.println("========================================");
            pw.println("  Generado por Kampets · Sistema interno");
            pw.println("========================================");
            JOptionPane.showMessageDialog(panel,"Reporte exportado:\n"+chooser.getSelectedFile().getAbsolutePath(),"Listo",JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(panel,"Error: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
