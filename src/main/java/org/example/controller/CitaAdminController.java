package org.example.controller;

import org.example.model.Citas;
import org.example.model.Empleados;
import org.example.model.EstadoCita;
import org.example.model.Mascotas;
import org.example.service.CitaService;
import org.example.service.CorreoService;
import org.example.service.EmpleadoService;
import org.example.service.MascotaService;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

public class CitaAdminController {

    private final CitaService     citaService     = new CitaService();
    private final MascotaService  mascotaService  = new MascotaService();
    private final EmpleadoService empleadoService = new EmpleadoService();

    public List<Citas> listarTodas() {
        try {
            return citaService.listarTodas();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Citas> listarPorCliente(Integer clienteId) {
        try {
            return citaService.listarPorCliente(clienteId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Citas> listarPasadasPorCliente(Integer clienteId) {
        try {
            return citaService.listarPasadasPorCliente(clienteId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Citas> listarDeHoy() {
        try {
            return citaService.listarDeHoy();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void cancelarCita(Integer id, JPanel panel) {
        try {
            citaService.cancelarCita(id);
            JOptionPane.showMessageDialog(panel, "Cita cancelada exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Confirma una cita: cambia estado a CONFIRMADA y envia correo al cliente.
     * Si el correo falla, igual confirma la cita y avisa al admin.
     */
    public void confirmarCita(Integer id, JPanel panel) {
        try {
            Citas cita = citaService.buscarPorId(id);
            if (cita == null) throw new Exception("No se encontró la cita.");

            citaService.cambiarEstado(id, EstadoCita.CONFIRMADA);

            // Intentar enviar correo de confirmacion
            String correoDestino = null;
            String nombreCliente = "cliente";
            String nombreMascota = cita.getMascota() != null ? cita.getMascota().getNombre() : "su mascota";
            if (cita.getMascota() != null && cita.getMascota().getCliente() != null) {
                correoDestino = cita.getMascota().getCliente().getCorreo();
                nombreCliente = cita.getMascota().getCliente().getNombre();
            }
            String fecha = cita.getFechaCita() != null ? cita.getFechaCita().toString() : "—";
            String hora  = cita.getHoraCita()  != null ? cita.getHoraCita().toString()  : "—";

            if (correoDestino != null && !correoDestino.isEmpty()) {
                String cuerpo =
                        "<div style='font-family:Arial,sans-serif;max-width:520px;margin:auto;background:#f0fdf4;border-radius:10px;padding:32px;'>" +
                                "<h2 style='color:#16a34a;margin-bottom:6px;'>✅ Cita Confirmada</h2>" +
                                "<p style='color:#374151;font-size:15px;'>Hola <b>" + nombreCliente + "</b>, tu cita en <b>Kampets Veterinaria</b> ha sido <b>confirmada</b>.</p>" +
                                "<table style='width:100%;border-collapse:collapse;margin:20px 0;'>" +
                                "<tr><td style='padding:10px 14px;background:#dcfce7;border-radius:6px 6px 0 0;color:#15803d;font-weight:bold;'>Mascota</td>" +
                                "<td style='padding:10px 14px;background:#f0fdf4;'>" + nombreMascota + "</td></tr>" +
                                "<tr><td style='padding:10px 14px;background:#dcfce7;color:#15803d;font-weight:bold;'>Fecha</td>" +
                                "<td style='padding:10px 14px;background:#f0fdf4;'>" + fecha + "</td></tr>" +
                                "<tr><td style='padding:10px 14px;background:#dcfce7;border-radius:0 0 6px 6px;color:#15803d;font-weight:bold;'>Hora</td>" +
                                "<td style='padding:10px 14px;background:#f0fdf4;'>" + hora + "</td></tr>" +
                                "</table>" +
                                "<p style='color:#6b7280;font-size:13px;'>Por favor preséntate puntualmente. Si necesitas cancelar, contáctanos con anticipación.</p>" +
                                "<p style='color:#16a34a;font-weight:bold;margin-top:24px;'>¡Hasta pronto! 🐾</p>" +
                                "</div>";
                try {
                    CorreoService.enviarCorreoGeneral(correoDestino, nombreCliente, "Confirmación de cita - Kampets", cuerpo);
                    JOptionPane.showMessageDialog(panel,
                            "Cita confirmada y correo enviado a " + correoDestino + ".",
                            "Cita confirmada", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception mailEx) {
                    JOptionPane.showMessageDialog(panel,
                            "Cita confirmada, pero no se pudo enviar el correo.\n" + mailEx.getMessage(),
                            "Cita confirmada", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(panel,
                        "Cita confirmada. El cliente no tiene correo registrado.",
                        "Cita confirmada", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cambia el estado de una cita a cualquier valor.
     */
    public void cambiarEstado(Integer id, EstadoCita nuevoEstado, JPanel panel) {
        try {
            citaService.cambiarEstado(id, nuevoEstado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Mascotas> listarMascotas() {
        try {
            return mascotaService.listarTodas();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Empleados> listarEmpleados() {
        try {
            return empleadoService.listarTodos();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public boolean guardarCita(Mascotas mascota, Empleados empleado,
                               String fechaStr, String horaStr, String estadoStr, JPanel panel) {
        try {
            if (mascota  == null) throw new Exception("Selecciona una mascota.");
            if (empleado == null) throw new Exception("Selecciona un veterinario.");
            if (fechaStr == null || fechaStr.trim().isEmpty())
                throw new Exception("Ingresa la fecha (yyyy-MM-dd).");
            if (horaStr  == null || horaStr.trim().isEmpty())
                throw new Exception("Ingresa la hora (HH:mm).");

            LocalDate fecha;
            LocalTime hora;
            try { fecha = LocalDate.parse(fechaStr.trim()); }
            catch (DateTimeParseException e) { throw new Exception("Formato de fecha inválido. Usa: yyyy-MM-dd"); }
            try { hora = LocalTime.parse(horaStr.trim()); }
            catch (DateTimeParseException e) { throw new Exception("Formato de hora inválido. Usa: HH:mm"); }

            // Convertir String a EstadoCita
            EstadoCita estado;
            try {
                estado = EstadoCita.valueOf(estadoStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                estado = EstadoCita.CONFIRMADA;
            }

            Citas cita = new Citas();
            cita.setMascota(mascota);
            cita.setEmpleado(empleado);
            cita.setFechaCita(fecha);
            cita.setHoraCita(hora);
            cita.setEstadoCita(estado);
            citaService.guardarCita(cita);
            JOptionPane.showMessageDialog(panel, "Cita guardada exitosamente.");
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
