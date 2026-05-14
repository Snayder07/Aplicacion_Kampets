package org.example.view;

import org.example.model.Citas;
import org.example.service.CitaService;
import org.example.service.CorreoService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.List;

public class PanelCalendario {

    public JPanel panel;
    private boolean temaOscuro = false;

    private final CitaService citaService = new CitaService();

    private LocalDate semanaInicio = LocalDate.now().with(DayOfWeek.MONDAY);

    // ── Constantes de layout ──────────────────────────────
    private static final int HORA_INICIO  = 7;
    private static final int HORA_FIN     = 20;
    private static final int ALTO_HORA    = 64;   // px exactos por hora
    private static final int ANCHO_HORAS  = 56;   // columna etiquetas
    private static final int ANCHO_DIA    = 150;  // ancho base por día
    private static final int ALTO_BLOQUE  = ALTO_HORA - 4; // 2px arriba y 2px abajo
    private static final int SNAP_MIN     = 15;   // snap cada 15 minutos

    private JPanel  panelBloques;
    private JLabel  lblSemana;

    // Arrastre
    private Citas  citaArrastrada   = null;
    private JPanel bloqueArrastrado = null;
    private Point  offsetArrastre   = new Point();

    private final Color[] CLARO = {
            new Color(240,253,244), new Color(22,101,52),   Color.WHITE,
            new Color(34,120,70),   new Color(220,245,230), Color.WHITE,
            new Color(15,60,30),    new Color(100,130,110), new Color(234,88,12),
            new Color(187,224,200), new Color(15,60,30),    new Color(134,190,155),
            new Color(220,38,38),   new Color(22,163,74),   new Color(210,240,220),
    };
    private final Color[] OSCURO = {
            new Color(18,24,38),  new Color(13,18,30),  new Color(26,34,52),
            new Color(37,55,90),  new Color(32,42,64),  Color.WHITE,
            new Color(226,232,240),new Color(148,163,184),new Color(251,146,60),
            new Color(30,41,59),  new Color(9,14,24),   new Color(122,175,212),
            new Color(239,68,68), new Color(34,197,94), new Color(15,23,42),
    };
    private Color[] C = CLARO;

    private static final Color[] COLORES_BLOQUE = {
            new Color(34,120,70),  new Color(14,116,144), new Color(124,58,237),
            new Color(217,119,6),  new Color(190,18,60),  new Color(5,150,105),
            new Color(59,130,246), new Color(168,85,247), new Color(234,88,12),
    };

    public PanelCalendario() {
        panel = new JPanel(new BorderLayout());
        construir();
    }

    public void setTema(boolean oscuro) {
        if (oscuro != temaOscuro) { temaOscuro = oscuro; construir(); }
    }

    public void recargar() { construir(); }

    // ══════════════════════════════════════════════════════
    //  CONSTRUCCIÓN
    // ══════════════════════════════════════════════════════
    private void construir() {
        panel.removeAll();
        C = temaOscuro ? OSCURO : CLARO;
        panel.setBackground(C[0]);
        panel.add(SidebarAdmin.crear(C, temaOscuro, "adminCalendario", panel), BorderLayout.WEST);
        panel.add(crearContenido(), BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private JPanel crearContenido() {
        JPanel c = new JPanel(new BorderLayout());
        c.setBackground(C[0]);
        c.add(crearTopbar(),     BorderLayout.NORTH);
        c.add(crearCalendario(), BorderLayout.CENTER);
        return c;
    }

    // ── Topbar ────────────────────────────────────────────
    private JPanel crearTopbar() {
        JPanel tb = new JPanel(new BorderLayout());
        tb.setBackground(C[1]);
        tb.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));
        tb.setPreferredSize(new Dimension(0, 62));

        JLabel titulo = new JLabel("📅  Calendario de Citas");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        tb.add(titulo, BorderLayout.WEST);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        nav.setOpaque(false);

        lblSemana = new JLabel();
        lblSemana.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSemana.setForeground(new Color(200, 230, 210));
        actualizarLblSemana();

        JButton btnAnt = navBtn("◀  Anterior");
        JButton btnHoy = navBtn("Hoy");
        JButton btnSig = navBtn("Siguiente  ▶");

        btnAnt.addActionListener(e -> { semanaInicio = semanaInicio.minusWeeks(1); construir(); });
        btnHoy.addActionListener(e -> { semanaInicio = LocalDate.now().with(DayOfWeek.MONDAY); construir(); });
        btnSig.addActionListener(e -> { semanaInicio = semanaInicio.plusWeeks(1); construir(); });

        nav.add(lblSemana); nav.add(btnAnt); nav.add(btnHoy); nav.add(btnSig);
        tb.add(nav, BorderLayout.EAST);
        return tb;
    }

    private JButton navBtn(String t) {
        JButton b = new JButton(t);
        b.setFont(new Font("Arial", Font.PLAIN, 12));
        b.setBackground(C[3]); b.setForeground(Color.WHITE);
        b.setOpaque(true); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Main.cursorHover != null ? Main.cursorHover : new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void actualizarLblSemana() {
        if (lblSemana == null) return;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMM", new Locale("es","CO"));
        lblSemana.setText(semanaInicio.format(fmt) + " – " + semanaInicio.plusDays(6).format(fmt));
    }

    // ══════════════════════════════════════════════════════
    //  GRILLA Y CONTENEDOR
    // ══════════════════════════════════════════════════════
    private JPanel crearCalendario() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(C[0]);

        // Cabecera de días (fija, no hace scroll)
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(C[0]);
        cabecera.setBorder(BorderFactory.createMatteBorder(0,0,1,0, new Color(180,220,200)));

        JPanel espHoras = new JPanel();
        espHoras.setPreferredSize(new Dimension(ANCHO_HORAS, 48));
        espHoras.setBackground(C[0]);
        cabecera.add(espHoras, BorderLayout.WEST);

        JPanel diasPanel = new JPanel(new GridLayout(1,7,0,0));
        diasPanel.setBackground(C[0]);
        String[] nombres = {"Lun","Mar","Mié","Jue","Vie","Sáb","Dom"};
        for (int i = 0; i < 7; i++) {
            LocalDate dia = semanaInicio.plusDays(i);
            JPanel celda = new JPanel(new GridLayout(2,1));
            celda.setPreferredSize(new Dimension(ANCHO_DIA, 48));
            celda.setBackground(esHoy(dia) ? C[3] : C[0]);
            celda.setBorder(BorderFactory.createMatteBorder(0,1,0,0, new Color(180,220,200)));

            JLabel lDia = new JLabel(nombres[i], SwingConstants.CENTER);
            lDia.setFont(new Font("Arial", Font.BOLD, 11));
            lDia.setForeground(esHoy(dia) ? Color.WHITE : C[7]);

            JLabel lNum = new JLabel(String.valueOf(dia.getDayOfMonth()), SwingConstants.CENTER);
            lNum.setFont(new Font("Arial", Font.BOLD, 18));
            lNum.setForeground(esHoy(dia) ? Color.WHITE : C[6]);

            celda.add(lDia); celda.add(lNum);
            diasPanel.add(celda);
        }
        cabecera.add(diasPanel, BorderLayout.CENTER);
        wrapper.add(cabecera, BorderLayout.NORTH);

        // Área scrollable
        int altoTotal = (HORA_FIN - HORA_INICIO) * ALTO_HORA;
        int anchoTotal = ANCHO_HORAS + 7 * ANCHO_DIA;

        JLayeredPane capas = new JLayeredPane();
        capas.setPreferredSize(new Dimension(anchoTotal, altoTotal));

        GrillaFondo grilla = new GrillaFondo(altoTotal, anchoTotal);
        grilla.setBounds(0, 0, anchoTotal, altoTotal);
        capas.add(grilla, JLayeredPane.DEFAULT_LAYER);

        panelBloques = new JPanel(null);
        panelBloques.setOpaque(false);
        panelBloques.setBounds(0, 0, anchoTotal, altoTotal);
        capas.add(panelBloques, JLayeredPane.PALETTE_LAYER);

        cargarCitas();

        JScrollPane scroll = new JScrollPane(capas);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(C[0]);
        scroll.getVerticalScrollBar().setUnitIncrement(ALTO_HORA);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    // ── Grilla pintada con paintComponent ─────────────────
    private class GrillaFondo extends JPanel {
        GrillaFondo(int alto, int ancho) {
            setPreferredSize(new Dimension(ancho, alto));
            setBackground(C[0]);
            setLayout(null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int totalH = HORA_FIN - HORA_INICIO;

            // Franjas alternadas
            for (int h = 0; h < totalH; h++) {
                if (h % 2 == 0) {
                    g2.setColor(new Color(0,0,0,5));
                    g2.fillRect(ANCHO_HORAS, h * ALTO_HORA, getWidth() - ANCHO_HORAS, ALTO_HORA);
                }
            }

            // Líneas horizontales
            for (int h = 0; h <= totalH; h++) {
                int y = h * ALTO_HORA;
                // Línea de hora entera
                g2.setColor(new Color(150,200,170,150));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(0, y, getWidth(), y);

                // Etiqueta de hora
                if (h < totalH) {
                    g2.setColor(C[7]);
                    g2.setFont(new Font("Arial", Font.PLAIN, 10));
                    g2.drawString(String.format("%02d:00", HORA_INICIO + h), 4, y + 12);
                }

                // Línea de media hora (punteada)
                if (h < totalH) {
                    int yMed = y + ALTO_HORA / 2;
                    g2.setColor(new Color(150,200,170,60));
                    float[] dash = {4f,4f};
                    g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER, 10f, dash, 0f));
                    g2.drawLine(ANCHO_HORAS, yMed, getWidth(), yMed);
                    g2.setStroke(new BasicStroke(1f));
                }
            }

            // Líneas verticales exactas en el borde de cada día
            for (int d = 0; d <= 7; d++) {
                int x = ANCHO_HORAS + d * ANCHO_DIA;
                g2.setColor(new Color(150,200,170,150));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(x, 0, x, getHeight());
            }

            // Línea roja de hora actual
            LocalDate hoy = LocalDate.now();
            for (int d = 0; d < 7; d++) {
                if (semanaInicio.plusDays(d).equals(hoy)) {
                    LocalTime ahora = LocalTime.now();
                    int min = (ahora.getHour() - HORA_INICIO) * 60 + ahora.getMinute();
                    if (min >= 0 && min < (HORA_FIN - HORA_INICIO) * 60) {
                        int y   = min * ALTO_HORA / 60;
                        int xI  = ANCHO_HORAS + d * ANCHO_DIA;
                        g2.setColor(new Color(220,38,38,220));
                        g2.setStroke(new BasicStroke(2f));
                        g2.drawLine(xI, y, xI + ANCHO_DIA, y);
                        g2.fillOval(xI - 5, y - 5, 10, 10);
                    }
                }
            }
        }
    }

    // ══════════════════════════════════════════════════════
    //  CARGA DE CITAS — detección y resolución de solapamiento
    // ══════════════════════════════════════════════════════
    private void cargarCitas() {
        panelBloques.removeAll();
        List<Citas> todas = citaService.listarTodas();
        LocalDate fin = semanaInicio.plusDays(6);

        // Filtrar semana actual
        List<Citas> semana = new ArrayList<>();
        for (Citas c : todas) {
            if (c.getFechaCita() == null || c.getHoraCita() == null) continue;
            if (c.getFechaCita().isBefore(semanaInicio) || c.getFechaCita().isAfter(fin)) continue;
            semana.add(c);
        }

        // Agrupar por día
        Map<LocalDate, List<Citas>> porDia = new LinkedHashMap<>();
        for (Citas c : semana)
            porDia.computeIfAbsent(c.getFechaCita(), k -> new ArrayList<>()).add(c);

        int colorIdx = 0;

        for (Map.Entry<LocalDate, List<Citas>> entry : porDia.entrySet()) {
            LocalDate fecha  = entry.getKey();
            List<Citas> dia  = entry.getValue();
            dia.sort(Comparator.comparing(Citas::getHoraCita));

            int diaIdx = (int)(fecha.toEpochDay() - semanaInicio.toEpochDay());

            // ── Calcular columnas para solapamientos ──────
            // Cada cita dura 50 min efectivos para detectar colisión
            int n = dia.size();
            int[] col      = new int[n];
            int[] maxCols  = new int[n];
            Arrays.fill(col,     0);
            Arrays.fill(maxCols, 1);

            // Para cada cita, buscar el grupo de citas que se solapan con ella
            boolean[] procesada = new boolean[n];
            for (int i = 0; i < n; i++) {
                if (procesada[i]) continue;

                // Construir grupo de solapamiento comenzando desde i
                List<Integer> grupo = new ArrayList<>();
                grupo.add(i);

                LocalTime iIni = dia.get(i).getHoraCita();
                LocalTime iFin = iIni.plusMinutes(50);

                for (int j = i + 1; j < n; j++) {
                    LocalTime jIni = dia.get(j).getHoraCita();
                    LocalTime jFin = jIni.plusMinutes(50);
                    if (jIni.isBefore(iFin) && jFin.isAfter(iIni)) {
                        grupo.add(j);
                        procesada[j] = true;
                    }
                }
                procesada[i] = true;

                // Asignar columna dentro del grupo
                int totalGrupo = grupo.size();
                for (int k = 0; k < totalGrupo; k++) {
                    col[grupo.get(k)]     = k;
                    maxCols[grupo.get(k)] = totalGrupo;
                }
            }

            // ── Crear bloques con posición exacta ─────────
            for (int i = 0; i < n; i++) {
                Citas cita  = dia.get(i);
                Color color = COLORES_BLOQUE[colorIdx % COLORES_BLOQUE.length];
                colorIdx++;

                boolean comprimido = maxCols[i] > 1;

                // Y exacto: desde el inicio de la hora
                int minOffset = (cita.getHoraCita().getHour() - HORA_INICIO) * 60
                        + cita.getHoraCita().getMinute();
                int y = minOffset * ALTO_HORA / 60 + 2;

                // X exacto: dividir el ancho del día en tantas columnas como solapados
                int anchoDisp   = ANCHO_DIA - 4; // 2px margen cada lado
                int anchoBloque = anchoDisp / maxCols[i];
                int x = ANCHO_HORAS + diaIdx * ANCHO_DIA + 2 + col[i] * anchoBloque;

                JPanel bloque = crearBloque(cita, color, comprimido);
                bloque.setBounds(x, y, anchoBloque - 1, ALTO_BLOQUE);
                panelBloques.add(bloque);
                habilitarArrastre(bloque, cita);
            }
        }

        panelBloques.revalidate();
        panelBloques.repaint();
    }

    private JPanel crearBloque(Citas cita, Color color, boolean comprimido) {
        JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.Y_AXIS));
        b.setBackground(color);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(3, 5, 2, 4)));
        b.setCursor(new Cursor(Cursor.MOVE_CURSOR));

        String hora    = cita.getHoraCita().format(DateTimeFormatter.ofPattern("HH:mm"));
        String mascota = cita.getMascota() != null ? cita.getMascota().getNombre() : "?";
        String cliente = (cita.getMascota() != null && cita.getMascota().getCliente() != null)
                ? cita.getMascota().getCliente().getNombre() : "?";

        JLabel lH = new JLabel(hora);
        lH.setFont(new Font("Arial", Font.BOLD, comprimido ? 9 : 11));
        lH.setForeground(Color.WHITE);
        lH.setAlignmentX(0f);

        JLabel lM = new JLabel(comprimido ? mascota : "🐾 " + mascota);
        lM.setFont(new Font("Arial", Font.PLAIN, comprimido ? 9 : 10));
        lM.setForeground(new Color(230,255,240));
        lM.setAlignmentX(0f);

        b.add(lH); b.add(lM);

        if (!comprimido) {
            JLabel lC = new JLabel("👤 " + cliente);
            lC.setFont(new Font("Arial", Font.PLAIN, 10));
            lC.setForeground(new Color(230,255,240));
            lC.setAlignmentX(0f);
            b.add(lC);
        }

        b.setToolTipText("<html><b>" + mascota + "</b><br>Cliente: " + cliente
                + "<br>Hora: " + hora + "<br><i>Arrastra para mover</i></html>");
        return b;
    }

    // ══════════════════════════════════════════════════════
    //  ARRASTRE — snap pixel-perfecto
    // ══════════════════════════════════════════════════════
    private void habilitarArrastre(JPanel bloque, Citas cita) {
        bloque.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                citaArrastrada   = cita;
                bloqueArrastrado = bloque;
                offsetArrastre   = e.getPoint();
                panelBloques.setComponentZOrder(bloque, 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (citaArrastrada == null) return;

                // Snap final y calcular nueva fecha/hora
                int sx = snapX(bloque.getX());
                int sy = snapY(bloque.getY());
                bloque.setLocation(sx, sy);
                panelBloques.repaint();

                int diaIdx  = Math.max(0, Math.min(6, (sx - ANCHO_HORAS) / ANCHO_DIA));
                int minutos = Math.max(0, Math.min(
                        (HORA_FIN - HORA_INICIO) * 60 - SNAP_MIN,
                        sy * 60 / ALTO_HORA));

                LocalDate nuevaFecha = semanaInicio.plusDays(diaIdx);
                LocalTime nuevaHora  = LocalTime.of(
                        HORA_INICIO + minutos / 60,
                        (minutos % 60 / SNAP_MIN) * SNAP_MIN);

                LocalDate fAntes = citaArrastrada.getFechaCita();
                LocalTime hAntes = citaArrastrada.getHoraCita();

                if (!nuevaFecha.equals(fAntes) || !nuevaHora.equals(hAntes))
                    confirmarCambio(citaArrastrada, nuevaFecha, nuevaHora, fAntes, hAntes);

                citaArrastrada   = null;
                bloqueArrastrado = null;
            }
        });

        bloque.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (bloqueArrastrado == null) return;
                Point p = SwingUtilities.convertPoint(bloque, e.getPoint(), panelBloques);
                // Snap en tiempo real para mostrar destino exacto
                bloqueArrastrado.setLocation(
                        snapX(p.x - offsetArrastre.x),
                        snapY(p.y - offsetArrastre.y));
                panelBloques.repaint();
            }
        });
    }

    /**
     * Snap horizontal: el borde izquierdo del bloque se alinea exactamente
     * al inicio de la columna del día más cercano.
     * Fórmula: ANCHO_HORAS + diaIdx * ANCHO_DIA + 2
     */
    private int snapX(int rawX) {
        // Día que corresponde al centro del bloque
        int centro = rawX + ANCHO_DIA / 2;
        int d = (int) Math.floor((double)(centro - ANCHO_HORAS) / ANCHO_DIA);
        d = Math.max(0, Math.min(6, d));
        return ANCHO_HORAS + d * ANCHO_DIA + 2;
    }

    /**
     * Snap vertical: alineado al múltiplo de SNAP_MIN minutos más cercano.
     * Fórmula inversa de y = minutos * ALTO_HORA / 60 + 2
     */
    private int snapY(int rawY) {
        int maxMin = (HORA_FIN - HORA_INICIO) * 60 - SNAP_MIN;
        int minutos = (rawY - 2) * 60 / ALTO_HORA;
        minutos = (int) Math.round((double) minutos / SNAP_MIN) * SNAP_MIN;
        minutos = Math.max(0, Math.min(maxMin, minutos));
        return minutos * ALTO_HORA / 60 + 2;
    }

    // ── Confirmación ──────────────────────────────────────
    private void confirmarCambio(Citas cita,
                                 LocalDate nF, LocalTime nH,
                                 LocalDate aF, LocalTime aH) {
        DateTimeFormatter ff = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", new Locale("es","CO"));
        DateTimeFormatter fh = DateTimeFormatter.ofPattern("HH:mm");

        String msg = "<html><b>¿Confirmas el cambio?</b><br><br>"
                + "Mascota: <b>" + cita.getMascota().getNombre() + "</b><br>"
                + "Antes:  " + aF.format(ff) + " a las " + aH.format(fh) + "<br>"
                + "Nueva:  <b>" + nF.format(ff) + " a las " + nH.format(fh) + "</b><br><br>"
                + "Se notificará al cliente por correo.</html>";

        int r = JOptionPane.showConfirmDialog(panel, msg, "Mover cita",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (r == JOptionPane.YES_OPTION) {
            try {
                cita.setFechaCita(nF);
                cita.setHoraCita(nH);
                new org.example.repository.CitaRepositoryImpl().actualizar(cita);
                enviarNotificacion(cita, aF, aH, nF, nH);
                JOptionPane.showMessageDialog(panel,
                        "Cita actualizada. Cliente notificado por correo.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                construir();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                        "Error al guardar: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                construir();
            }
        } else {
            construir(); // revertir visual
        }
    }

    // ── Notificación por correo ───────────────────────────
    private void enviarNotificacion(Citas cita,
                                    LocalDate aF, LocalTime aH,
                                    LocalDate nF, LocalTime nH) {
        new Thread(() -> {
            try {
                String correo  = cita.getMascota().getCliente().getCorreo();
                String nombre  = cita.getMascota().getCliente().getNombre();
                String mascota = cita.getMascota().getNombre();
                DateTimeFormatter ff = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", new Locale("es","CO"));
                DateTimeFormatter fh = DateTimeFormatter.ofPattern("HH:mm");
                CorreoService.enviarCorreoGeneral(correo, nombre,
                        "Tu cita en Kampets fue reprogramada",
                        cuerpoCorreo(nombre, mascota,
                                aF.format(ff), aH.format(fh),
                                nF.format(ff), nH.format(fh)));
            } catch (Exception ex) {
                System.err.println("Error notificación: " + ex.getMessage());
            }
        }).start();
    }

    private String cuerpoCorreo(String nom, String mas, String fA, String hA, String fN, String hN) {
        return "<!DOCTYPE html><html><body style='font-family:Arial,sans-serif;"
                + "background:#f0f8f4;margin:0;padding:20px'>"
                + "<div style='max-width:520px;margin:auto;background:#fff;"
                + "border-radius:12px;padding:32px;box-shadow:0 2px 8px rgba(0,0,0,.08)'>"
                + "<h2 style='color:#1d9e75;margin-top:0'>Kampets Veterinaria</h2>"
                + "<p style='color:#444'>Hola <strong>" + nom + "</strong>,</p>"
                + "<p style='color:#444'>Tu cita para <strong>" + mas + "</strong> fue reprogramada:</p>"
                + "<table style='width:100%;border-collapse:collapse;margin:20px 0'>"
                + "<tr><td style='padding:10px;background:#fef2f2;color:#991b1b'>"
                + "<b>Antes:</b> " + fA + " — " + hA + "</td></tr>"
                + "<tr><td style='padding:10px;background:#ecfdf5;color:#065f46'>"
                + "<b>Ahora:</b> " + fN + " — " + hN + "</td></tr>"
                + "</table>"
                + "<p style='color:#777;font-size:13px'>¿Preguntas? Contáctanos.</p>"
                + "<hr style='border:none;border-top:1px solid #e0e0e0;margin:20px 0'/>"
                + "<p style='color:#aaa;font-size:11px;text-align:center'>© Kampets Veterinaria</p>"
                + "</div></body></html>";
    }

    private boolean esHoy(LocalDate f) { return f.equals(LocalDate.now()); }
}
