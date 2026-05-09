package org.example.view;

import org.example.controller.RegistroController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrearCuenta {
    public JPanel panel;
    private JTextField nombreField;
    private JTextField apellidoField;
    private JTextField correoField;
    private JTextField telefonoField;
    private JPasswordField passwordField;
    private JPasswordField confirmarPasswordField;
    private JButton registrarButton;
    private JButton volverButton;

    // ── Conectado al RegistroController ──────────────────
    private final RegistroController registroController = new RegistroController();

    public CrearCuenta() {

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 244));
        panel.setPreferredSize(new Dimension(420, 520));

        // Título
        JLabel titulo = new JLabel("Crear cuenta");
        titulo.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        titulo.setForeground(new Color(29, 158, 117));
        titulo.setBounds(40, 20, 340, 40);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titulo);

        JLabel subtitulo = new JLabel("Completa tus datos para registrarte");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(150, 150, 150));
        subtitulo.setBounds(40, 58, 340, 20);
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(subtitulo);

        JSeparator sep = new JSeparator();
        sep.setBounds(40, 85, 340, 2);
        sep.setForeground(new Color(29, 158, 117));
        panel.add(sep);

        // Nombre
        JLabel labelNombre = new JLabel("Nombre");
        labelNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        labelNombre.setForeground(new Color(80, 80, 80));
        labelNombre.setBounds(40, 100, 160, 20);
        panel.add(labelNombre);

        nombreField = new JTextField();
        nombreField.setFont(new Font("Arial", Font.PLAIN, 13));
        nombreField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 200), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        nombreField.setBounds(40, 122, 160, 35);
        panel.add(nombreField);

        // Apellido
        JLabel labelApellido = new JLabel("Apellido");
        labelApellido.setFont(new Font("Arial", Font.PLAIN, 12));
        labelApellido.setForeground(new Color(80, 80, 80));
        labelApellido.setBounds(220, 100, 160, 20);
        panel.add(labelApellido);

        apellidoField = new JTextField();
        apellidoField.setFont(new Font("Arial", Font.PLAIN, 13));
        apellidoField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 200), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        apellidoField.setBounds(220, 122, 160, 35);
        panel.add(apellidoField);

        // Correo
        JLabel labelCorreo = new JLabel("Correo electrónico");
        labelCorreo.setFont(new Font("Arial", Font.PLAIN, 12));
        labelCorreo.setForeground(new Color(80, 80, 80));
        labelCorreo.setBounds(40, 170, 200, 20);
        panel.add(labelCorreo);

        correoField = new JTextField();
        correoField.setFont(new Font("Arial", Font.PLAIN, 13));
        correoField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 200), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        correoField.setBounds(40, 192, 340, 35);
        panel.add(correoField);

        // Teléfono
        JLabel labelTelefono = new JLabel("Teléfono");
        labelTelefono.setFont(new Font("Arial", Font.PLAIN, 12));
        labelTelefono.setForeground(new Color(80, 80, 80));
        labelTelefono.setBounds(40, 240, 200, 20);
        panel.add(labelTelefono);

        telefonoField = new JTextField();
        telefonoField.setFont(new Font("Arial", Font.PLAIN, 13));
        telefonoField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 200), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        telefonoField.setBounds(40, 262, 340, 35);
        panel.add(telefonoField);

        // Contraseña
        JLabel labelPass = new JLabel("Contraseña");
        labelPass.setFont(new Font("Arial", Font.PLAIN, 12));
        labelPass.setForeground(new Color(80, 80, 80));
        labelPass.setBounds(40, 310, 160, 20);
        panel.add(labelPass);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 200), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        passwordField.setBounds(40, 332, 160, 35);
        panel.add(passwordField);

        // Confirmar contraseña
        JLabel labelConfirmar = new JLabel("Confirmar contraseña");
        labelConfirmar.setFont(new Font("Arial", Font.PLAIN, 12));
        labelConfirmar.setForeground(new Color(80, 80, 80));
        labelConfirmar.setBounds(220, 310, 160, 20);
        panel.add(labelConfirmar);

        confirmarPasswordField = new JPasswordField();
        confirmarPasswordField.setFont(new Font("Arial", Font.PLAIN, 13));
        confirmarPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 200), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        confirmarPasswordField.setBounds(220, 332, 160, 35);
        panel.add(confirmarPasswordField);

        // Botón Crear cuenta
        registrarButton = new JButton("Crear cuenta");
        registrarButton.setFont(new Font("Arial", Font.BOLD, 14));
        registrarButton.setBackground(new Color(29, 158, 117));
        registrarButton.setForeground(Color.WHITE);
        registrarButton.setOpaque(true);
        registrarButton.setBorderPainted(false);
        registrarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registrarButton.setBounds(40, 395, 340, 40);
        panel.add(registrarButton);

        // Botón Volver
        volverButton = new JButton("Volver al login");
        volverButton.setFont(new Font("Arial", Font.PLAIN, 13));
        volverButton.setBackground(new Color(240, 248, 244));
        volverButton.setForeground(new Color(29, 158, 117));
        volverButton.setOpaque(true);
        volverButton.setBorder(BorderFactory.createLineBorder(new Color(29, 158, 117), 1));
        volverButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        volverButton.setBounds(40, 445, 340, 38);
        panel.add(volverButton);

        // ── Acciones ──────────────────────────────────────
        registrarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre    = nombreField.getText().trim();
                String apellido  = apellidoField.getText().trim();
                String correo    = correoField.getText().trim();
                String telefono  = telefonoField.getText().trim();
                String pass      = new String(passwordField.getPassword());
                String confirmar = new String(confirmarPasswordField.getPassword());

                // ← Llama al RegistroController que guarda en BD
                registroController.registrar(
                        nombre, apellido, correo, telefono, pass, confirmar, panel);
            }
        });

        volverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.cambiarPantalla("login");
            }
        });
    }
}
