package org.example.view;


import javax.swing.*;
import java.awt.*;

public class Interfaz_Grafica_Kampets {
    public JPanel panel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton entrarButton;
    private JButton entrarAdminButton;
    private JButton crearCuentaButton;

    public Interfaz_Grafica_Kampets() {

        // ── Panel principal ──────────────────────────────
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 244));
        panel.setPreferredSize(new Dimension(420, 440));

        // ── Título ───────────────────────────────────────
        JLabel titulo = new JLabel("Kampets");
        titulo.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        titulo.setForeground(new Color(29, 158, 117));
        titulo.setBounds(130, 25, 200, 50);
        panel.add(titulo);

        JLabel subtitulo = new JLabel("Veterinaria");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(120, 120, 120));
        subtitulo.setBounds(175, 70, 120, 20);
        panel.add(subtitulo);

        // ── Separador ────────────────────────────────────
        JSeparator sep = new JSeparator();
        sep.setBounds(40, 100, 340, 2);
        sep.setForeground(new Color(29, 158, 117));
        panel.add(sep);

        // ── Label Correo ─────────────────────────────────
        JLabel labelCorreo = new JLabel("Correo electrónico");
        labelCorreo.setFont(new Font("Arial", Font.PLAIN, 12));
        labelCorreo.setForeground(new Color(80, 80, 80));
        labelCorreo.setBounds(40, 115, 180, 20);
        panel.add(labelCorreo);

        // ── Campo Correo ─────────────────────────────────
        textField1 = new JTextField();
        textField1.setFont(new Font("Arial", Font.PLAIN, 13));
        textField1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 200), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        textField1.setBounds(40, 138, 340, 35);
        panel.add(textField1);

        // ── Label Contraseña ─────────────────────────────
        JLabel labelPass = new JLabel("Contraseña");
        labelPass.setFont(new Font("Arial", Font.PLAIN, 12));
        labelPass.setForeground(new Color(80, 80, 80));
        labelPass.setBounds(40, 185, 180, 20);
        panel.add(labelPass);

        // ── Campo Contraseña ─────────────────────────────
        passwordField1 = new JPasswordField();
        passwordField1.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordField1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 200), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        passwordField1.setBounds(40, 208, 340, 35);
        panel.add(passwordField1);

        // ── Botón Entrar ─────────────────────────────────
        entrarButton = new JButton("Entrar");
        entrarButton.setFont(new Font("Arial", Font.BOLD, 14));
        entrarButton.setBackground(new Color(29, 158, 117));
        entrarButton.setForeground(Color.WHITE);
        entrarButton.setOpaque(true);
        entrarButton.setBorderPainted(false);
        entrarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        entrarButton.setBounds(40, 265, 340, 40);
        panel.add(entrarButton);

        // ── Botón Entrar como Administrador ───────────────
        entrarAdminButton = new JButton("Entrar como administrador");
        entrarAdminButton.setFont(new Font("Arial", Font.PLAIN, 13));
        entrarAdminButton.setBackground(new Color(15, 110, 86));
        entrarAdminButton.setForeground(Color.WHITE);
        entrarAdminButton.setOpaque(true);
        entrarAdminButton.setBorderPainted(false);
        entrarAdminButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        entrarAdminButton.setBounds(40, 315, 340, 38);
        panel.add(entrarAdminButton);

        // ── Texto primera vez ─────────────────────────────
        JLabel labelSep = new JLabel("¿Es tu primera vez aquí?");
        labelSep.setFont(new Font("Arial", Font.PLAIN, 12));
        labelSep.setForeground(new Color(150, 150, 150));
        labelSep.setBounds(40, 362, 340, 20);
        labelSep.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(labelSep);

        // ── Botón Crear cuenta ────────────────────────────
        crearCuentaButton = new JButton("Crear cuenta");
        crearCuentaButton.setFont(new Font("Arial", Font.PLAIN, 13));
        crearCuentaButton.setBackground(new Color(240, 248, 244));
        crearCuentaButton.setForeground(new Color(29, 158, 117));
        crearCuentaButton.setOpaque(true);
        crearCuentaButton.setBorder(BorderFactory.createLineBorder(new Color(29, 158, 117), 1));
        crearCuentaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        crearCuentaButton.setBounds(40, 385, 340, 38);
        panel.add(crearCuentaButton);

        // ── Acciones de los botones ───────────────────────
        entrarButton.addActionListener(e -> {
            String correo = textField1.getText();
            String clave = new String(passwordField1.getPassword());
            if (correo.equals("cliente@kampets.com") && clave.equals("1234")) {
                Main.expandirVentana(); // ← expande la ventana
                Main.cambiarPantalla("panelCliente");
            } else {
                JOptionPane.showMessageDialog(panel, "Correo o contraseña incorrectos",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        entrarAdminButton.addActionListener(e -> {
            String correo = textField1.getText();
            String clave = new String(passwordField1.getPassword());
            if (Main.esAdmin(correo, clave)) {
                Main.expandirVentana();
                Main.cambiarPantalla("panelAdmin");
            } else {
                JOptionPane.showMessageDialog(panel, "Credenciales de administrador incorrectas",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        crearCuentaButton.addActionListener(e ->
                Main.cambiarPantalla("crearCuenta"));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Kampets - Login");
        frame.setContentPane(new Interfaz_Grafica_Kampets().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

