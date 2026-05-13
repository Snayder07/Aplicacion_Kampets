package org.example.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

/**
 * Servicio para enviar correos electrónicos usando Gmail SMTP.
 *
 * CONFIGURACIÓN NECESARIA:
 * Reemplaza EMAIL_REMITENTE y EMAIL_PASSWORD con las credenciales
 * de la cuenta de Gmail que usará Kampets para enviar correos.
 *
 * IMPORTANTE: En Gmail debes activar "Contraseñas de aplicación":
 * Cuenta Google → Seguridad → Verificación en 2 pasos → Contraseñas de aplicación
 * Genera una contraseña para "Correo" y úsala en EMAIL_PASSWORD.
 */
public class CorreoService {

    // ── CONFIGURA AQUÍ TUS CREDENCIALES ──────────────────────────────
    private static final String EMAIL_REMITENTE = "kampets.veterinaria@gmail.com"; // Tu correo Gmail
    private static final String EMAIL_PASSWORD  = "xxxx xxxx xxxx xxxx";            // Contraseña de aplicación Gmail
    // ─────────────────────────────────────────────────────────────────

    /**
     * Envía el código de recuperación al correo del cliente.
     */
    public static void enviarCodigoRecuperacion(String destinatario,
                                                String nombreCliente,
                                                String codigo) throws Exception {
        String cuerpo = construirCuerpoRecuperacion(nombreCliente, codigo);
        enviar(destinatario, "Código de recuperación - Kampets", cuerpo);
    }

    /**
     * Envía un correo general con asunto y cuerpo HTML personalizados.
     * Usado por el calendario para notificar cambios de cita al cliente.
     */
    public static void enviarCorreoGeneral(String destinatario,
                                           String nombreCliente,
                                           String asunto,
                                           String cuerpoHtml) throws Exception {
        enviar(destinatario, asunto, cuerpoHtml);
    }

    // ── Método interno de envío ───────────────────────────
    private static void enviar(String destinatario, String asunto, String cuerpoHtml) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");
        props.put("mail.smtp.ssl.trust",       "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_REMITENTE, EMAIL_PASSWORD);
            }
        });

        Message mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress(EMAIL_REMITENTE, "Kampets Veterinaria"));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensaje.setSubject(asunto);
        mensaje.setContent(cuerpoHtml, "text/html; charset=UTF-8");

        Transport.send(mensaje);
    }

    /** Plantilla HTML del correo de recuperación de contraseña */
    private static String construirCuerpoRecuperacion(String nombre, String codigo) {
        return "<!DOCTYPE html><html><body style='font-family:Arial,sans-serif;" +
                "background:#f0f8f4;margin:0;padding:20px'>" +
                "<div style='max-width:480px;margin:auto;background:#fff;" +
                "border-radius:12px;padding:32px;box-shadow:0 2px 8px rgba(0,0,0,0.08)'>" +
                "<h2 style='color:#1d9e75;margin-top:0'>Kampets Veterinaria</h2>" +
                "<p style='color:#444'>Hola <strong>" + nombre + "</strong>,</p>" +
                "<p style='color:#444'>Recibimos una solicitud para recuperar tu contraseña. " +
                "Usa el siguiente código de verificación:</p>" +
                "<div style='text-align:center;margin:28px 0'>" +
                "<span style='font-size:36px;font-weight:bold;letter-spacing:10px;" +
                "color:#1d9e75;background:#e8f7f1;padding:16px 28px;border-radius:8px'>" +
                codigo + "</span></div>" +
                "<p style='color:#777;font-size:13px'>Este código es válido sólo para " +
                "esta sesión. Si no solicitaste este cambio, ignora este correo.</p>" +
                "<hr style='border:none;border-top:1px solid #e0e0e0;margin:20px 0'/>" +
                "<p style='color:#aaa;font-size:11px;text-align:center'>" +
                "© Kampets Veterinaria</p></div></body></html>";
    }
}
