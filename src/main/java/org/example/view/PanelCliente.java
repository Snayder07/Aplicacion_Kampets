package org.example.view;

import org.example.controller.CitaAdminController;
import org.example.model.Citas;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PanelCliente {
    public JPanel panel;
    private boolean temaOscuro = false;

    private final CitaAdminController ctrl = new CitaAdminController();

    private final Color[] CLARO = {
            new Color(240, 246, 252), new Color(26,  74,  122), Color.WHITE,
            new Color(42,  90,  138), new Color(230, 240, 250), Color.WHITE,
            new Color(26,  58,   90), new Color(138, 170, 200), new Color(224, 112,  32),
            new Color(208, 228, 244), new Color(15,  53,   96), new Color(122, 175, 212),
            new Color(168, 200, 232), new Color(168, 212, 245),
    };
    private final Color[] OSCURO = {
            new Color(18,  24,  38),  new Color(13,  18,  30),  new Color(26,  34,  52),
            new Color(37,  55,  90),  new Color(32,  42,  64),  Color.WHITE,
            new Color(226, 232, 240), new Color(100, 116, 139), new Color(251, 146,  60),
            new Color(30,  41,  59),  new Color(9,   14,  24),  new Color(122, 175, 212),
            new Color(80,  120, 170), new Color(100, 160, 210),
    };
    private Color[] C = CLARO;

    public PanelCliente() {
        panel = new JPanel(new BorderLayout());
        construir();
    }

    public void setTema(boolean oscuro) {
        if (oscuro != temaOscuro) { temaOscuro = oscuro; construir(); }
    }

    public void recargar() { construir(); }

    private void construir() {
        panel.removeAll();
        C = temaOscuro ? OSCURO : CLARO;
        panel.setBackground(C[0]);
        panel.add(crearSidebar(),   BorderLayout.WEST);
        panel.add(crearContenido(), BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private JButton crearBoton(String texto, Color fondo, Color textColor, boolean borde) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.PLAIN, 15));
        btn.setBackground(fondo); btn.setForeground(textColor);
        btn.setOpaque(true); btn.setCursor(Main.cursorHover != null ? Main.cursorHover : new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        if (borde) btn.setBorder(BorderFactory.createLineBorder(textColor, 1));
        else btn.setBorderPainted(false);
        return btn;
    }

    private JLabel crearLabel(String texto, int size, int style, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", style, size + 2));
        lbl.setForeground(color);
        return lbl;
    }

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(C[1]);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

        JLabel logo;
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/logo.png"));
            logo = new JLabel(new ImageIcon(logoIcon.getImage().getScaledInstance(160, 55, Image.SCALE_SMOOTH)));
        } catch (Exception e) { logo = crearLabel("Kampets", 18, Font.BOLD, C[5]); }
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo); sidebar.add(Box.createVerticalStrut(16));

        JSeparator sep = new JSeparator();
        sep.setForeground(C[3]); sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(sep); sidebar.add(Box.createVerticalStrut(12));

        JLabel secPrincipal = crearLabel("PRINCIPAL", 10, Font.PLAIN, C[11]);
        secPrincipal.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(secPrincipal); sidebar.add(Box.createVerticalStrut(5));

        // Inicio — resaltado
        JButton btnInicio = crearBoton("Inicio", C[2], C[1], false);
        btnInicio.setFont(new Font("Arial", Font.BOLD, 13));
        btnInicio.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnInicio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnInicio.setHorizontalAlignment(SwingConstants.LEFT);
        btnInicio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("panelCliente"); }
        });
        sidebar.add(btnInicio); sidebar.add(Box.createVerticalStrut(3));

        // Mis mascotas
        JButton btnMisMascotas = crearBoton("Mis mascotas", C[1], C[5], false);
        btnMisMascotas.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnMisMascotas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnMisMascotas.setHorizontalAlignment(SwingConstants.LEFT);
        btnMisMascotas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("misMascotas"); }
        });
        sidebar.add(btnMisMascotas); sidebar.add(Box.createVerticalStrut(3));

        // Mis citas
        JButton btnMisCitas = crearBoton("Mis citas", C[1], C[5], false);
        btnMisCitas.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnMisCitas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnMisCitas.setHorizontalAlignment(SwingConstants.LEFT);
        btnMisCitas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("misCitas"); }
        });
        sidebar.add(btnMisCitas); sidebar.add(Box.createVerticalStrut(3));

        // Historial
        JButton btnHistorial = crearBoton("Historial", C[1], C[5], false);
        btnHistorial.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnHistorial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnHistorial.setHorizontalAlignment(SwingConstants.LEFT);
        btnHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("historial"); }
        });
        sidebar.add(btnHistorial); sidebar.add(Box.createVerticalStrut(12));

        JLabel secServicios = crearLabel("SERVICIOS", 10, Font.PLAIN, C[11]);
        secServicios.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(secServicios); sidebar.add(Box.createVerticalStrut(5));

        JButton btnAlimentos = crearBoton("Alimentos", C[1], C[5], false);
        btnAlimentos.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAlimentos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnAlimentos.setHorizontalAlignment(SwingConstants.LEFT);
        btnAlimentos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("alimentos"); }
        });
        sidebar.add(btnAlimentos); sidebar.add(Box.createVerticalStrut(3));

        JButton btnVacunas = crearBoton("Vacunas", C[1], C[5], false);
        btnVacunas.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVacunas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnVacunas.setHorizontalAlignment(SwingConstants.LEFT);
        btnVacunas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("vacunas"); }
        });
        sidebar.add(btnVacunas); sidebar.add(Box.createVerticalStrut(3));
        sidebar.add(Box.createVerticalGlue());

        JButton btnCerrar = crearBoton("Cerrar sesion", C[1], C[12], true);
        btnCerrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCerrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int conf = JOptionPane.showConfirmDialog(panel,
                        "Deseas cerrar sesion?", "Cerrar sesion", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    Main.clienteActual = null;
                    Main.frame.setExtendedState(JFrame.NORMAL);
                    Main.frame.setSize(420, 520);
                    Main.frame.setLocationRelativeTo(null);
                    Main.cambiarPantalla("login");
                }
            }
        });
        sidebar.add(btnCerrar); sidebar.add(Box.createVerticalStrut(8));

        String nombreCliente = Main.clienteActual != null ? Main.clienteActual.getNombre() : "Cliente";
        String[] partes = nombreCliente.split(" ");
        String iniciales = partes.length >= 2 ?
                String.valueOf(partes[0].charAt(0)) + String.valueOf(partes[1].charAt(0)) : "C";

        JPanel userPanel = new JPanel(new BorderLayout(8, 0));
        userPanel.setBackground(C[10]);
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        userPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel avatarLbl = crearLabel(iniciales, 13, Font.BOLD, C[1]);
        avatarLbl.setBackground(C[5]); avatarLbl.setOpaque(true);
        avatarLbl.setPreferredSize(new Dimension(40, 40));
        avatarLbl.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        userInfo.setBackground(C[10]);
        userInfo.add(crearLabel(nombreCliente, 12, Font.BOLD, C[5]));
        userInfo.add(crearLabel("Cliente", 10, Font.PLAIN, C[11]));
        userPanel.add(avatarLbl, BorderLayout.WEST);
        userPanel.add(userInfo,  BorderLayout.CENTER);
        sidebar.add(userPanel);
        return sidebar;
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(C[0]);

        // Topbar
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(C[2]);
        topbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, C[9]),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)));

        JPanel topLeft = new JPanel(new GridLayout(2, 1));
        topLeft.setBackground(C[2]);
        String nombreCliente = Main.clienteActual != null ? Main.clienteActual.getNombre() : "Cliente";
        String fechaHoy = LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", new Locale("es")));
        fechaHoy = Character.toUpperCase(fechaHoy.charAt(0)) + fechaHoy.substring(1);
        topLeft.add(crearLabel("Bienvenido, " + nombreCliente.split(" ")[0], 20, Font.PLAIN, C[6]));
        topLeft.add(crearLabel(fechaHoy, 12, Font.PLAIN, C[7]));

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topRight.setBackground(C[2]);

        // ── Botón Mis mascotas ────────────────────────────
        JButton btnMascotas = crearBoton("Mis mascotas", C[4], C[1], true);
        btnMascotas.setFont(new Font("Arial", Font.PLAIN, 13));
        btnMascotas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C[9], 1), BorderFactory.createEmptyBorder(7, 14, 7, 14)));
        btnMascotas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("misMascotas"); }
        });

        // ── Botón Agendar cita ────────────────────────────
        JButton btnAgendar = crearBoton("+ Agendar cita", C[1], C[5], false);
        btnAgendar.setFont(new Font("Arial", Font.BOLD, 13));
        btnAgendar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAgendar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("agendarCita"); }
        });

        topRight.add(btnMascotas); topRight.add(btnAgendar);
        topbar.add(topLeft, BorderLayout.WEST); topbar.add(topRight, BorderLayout.EAST);
        contenido.add(topbar, BorderLayout.NORTH);

        // Centro
        JPanel centro = new JPanel(new BorderLayout(0, 16));
        centro.setBackground(C[0]);
        centro.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Próxima cita
        List<Citas> citas = Main.clienteActual != null
                ? ctrl.listarPorCliente(Main.clienteActual.getId())
                : Collections.emptyList();
        JPanel proximaCard = new JPanel(new BorderLayout());
        proximaCard.setBackground(C[1]);
        proximaCard.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        JPanel proximaIzq = new JPanel(new GridLayout(3, 1));
        proximaIzq.setBackground(C[1]);

        if (!citas.isEmpty()) {
            Citas primera = citas.get(0);
            String mascota = primera.getMascota()  != null ? primera.getMascota().getNombre()  : "—";
            String vet     = primera.getEmpleado() != null ? primera.getEmpleado().getNombre() : "—";
            String fecha   = primera.getFechaCita() != null ? primera.getFechaCita().toString() : "—";
            String hora    = primera.getHoraCita()  != null ? primera.getHoraCita().toString()  : "—";
            proximaIzq.add(crearLabel("PROXIMA CITA", 10, Font.PLAIN, C[13]));
            proximaIzq.add(crearLabel(mascota, 17, Font.BOLD, C[5]));
            proximaIzq.add(crearLabel(vet, 12, Font.PLAIN, C[12]));
            JPanel proximaDer = new JPanel(new GridLayout(2, 1));
            proximaDer.setBackground(C[1]);
            JLabel lFecha = crearLabel(fecha, 12, Font.BOLD, C[5]);
            lFecha.setHorizontalAlignment(SwingConstants.RIGHT);
            JLabel lHora = crearLabel(hora, 12, Font.PLAIN, C[13]);
            lHora.setHorizontalAlignment(SwingConstants.RIGHT);
            proximaDer.add(lFecha); proximaDer.add(lHora);
            proximaCard.add(proximaIzq, BorderLayout.CENTER);
            proximaCard.add(proximaDer, BorderLayout.EAST);
        } else {
            proximaIzq.add(crearLabel("PROXIMA CITA", 10, Font.PLAIN, C[13]));
            proximaIzq.add(crearLabel("No tienes citas programadas", 15, Font.BOLD, C[5]));
            proximaIzq.add(crearLabel("Agenda tu primera cita", 12, Font.PLAIN, C[12]));
            proximaCard.add(proximaIzq, BorderLayout.CENTER);
        }

        // Lista citas
        JPanel citasPanel = new JPanel(new BorderLayout(0, 10));
        citasPanel.setBackground(C[0]);
        JPanel citasHeader = new JPanel(new BorderLayout());
        citasHeader.setBackground(C[0]);
        citasHeader.add(crearLabel("MIS CITAS", 11, Font.PLAIN, C[7]), BorderLayout.WEST);
        JButton verTodo = crearBoton("Ver todas", C[0], C[1], false);
        verTodo.setFont(new Font("Arial", Font.BOLD, 12));
        verTodo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Main.cambiarPantalla("misCitas"); }
        });
        citasHeader.add(verTodo, BorderLayout.EAST);
        citasPanel.add(citasHeader, BorderLayout.NORTH);

        int max = Math.min(citas.size(), 3);
        JPanel listaCitas = new JPanel();
        listaCitas.setLayout(new BoxLayout(listaCitas, BoxLayout.Y_AXIS));
        listaCitas.setBackground(C[0]);
        listaCitas.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        if (citas.isEmpty()) {
            JLabel sinCitas = crearLabel("No tienes citas registradas aún.", 13, Font.PLAIN, C[7]);
            sinCitas.setHorizontalAlignment(SwingConstants.CENTER);
            sinCitas.setAlignmentX(Component.LEFT_ALIGNMENT);
            listaCitas.add(sinCitas);
        } else {
            for (int i = 0; i < max; i++) {
                Citas c = citas.get(i);
                String mascota = c.getMascota()  != null ? c.getMascota().getNombre()  : "—";
                String vet     = c.getEmpleado() != null ? c.getEmpleado().getNombre() : "—";
                String fecha   = c.getFechaCita() != null ? c.getFechaCita().toString() : "—";
                String estado  = c.getEstadoCita() != null ? c.getEstadoCita().toString() : "—";

                Color colorEstado = C[1];
                if (c.getEstadoCita() != null) {
                    switch (c.getEstadoCita()) {
                        case CONFIRMADA: colorEstado = new Color(22, 163, 74); break;
                        case PENDIENTE:  colorEstado = new Color(234, 88, 12); break;
                        case CANCELADA:  colorEstado = new Color(220, 38, 38); break;
                        default: colorEstado = C[1];
                    }
                }

                JPanel card = new JPanel(new BorderLayout(10, 0));
                card.setBackground(C[2]);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 4, 0, 0, colorEstado),
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(C[9], 1),
                                BorderFactory.createEmptyBorder(14, 16, 14, 16))));

                JPanel info = new JPanel(new GridLayout(2, 1, 0, 4));
                info.setBackground(C[2]);
                info.add(crearLabel(mascota, 14, Font.BOLD, C[6]));
                info.add(crearLabel(fecha + "  ·  " + vet, 12, Font.PLAIN, C[7]));

                JPanel derecha = new JPanel(new GridLayout(2, 1, 0, 6));
                derecha.setBackground(C[2]);
                JLabel badge = crearLabel(estado, 11, Font.BOLD, colorEstado);
                badge.setHorizontalAlignment(SwingConstants.RIGHT);
                final Integer idCita = c.getId();
                JLabel cancelar = crearLabel("Cancelar cita", 11, Font.PLAIN,
                        temaOscuro ? new Color(80, 110, 150) : new Color(176, 200, 224));
                cancelar.setHorizontalAlignment(SwingConstants.RIGHT);
                cancelar.setCursor(Main.cursorHover != null ? Main.cursorHover : new Cursor(Cursor.HAND_CURSOR));
                cancelar.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        int conf = JOptionPane.showConfirmDialog(panel,
                                "¿Cancelar esta cita?", "Confirmar", JOptionPane.YES_NO_OPTION);
                        if (conf == JOptionPane.YES_OPTION) {
                            ctrl.cancelarCita(idCita, panel);
                            construir();
                        }
                    }
                });
                derecha.add(badge); derecha.add(cancelar);
                card.add(info, BorderLayout.CENTER); card.add(derecha, BorderLayout.EAST);
                listaCitas.add(card);
                if (i < max - 1) listaCitas.add(Box.createVerticalStrut(8));
            }
        }

        citasPanel.add(listaCitas, BorderLayout.CENTER);
        centro.add(proximaCard, BorderLayout.NORTH);
        centro.add(citasPanel,  BorderLayout.CENTER);
        JScrollPane outerScroll = new JScrollPane(centro);
        outerScroll.setBorder(null); outerScroll.getViewport().setBackground(C[0]);
        outerScroll.getVerticalScrollBar().setUnitIncrement(16);
        outerScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contenido.add(outerScroll, BorderLayout.CENTER);
        return contenido;
    }
}