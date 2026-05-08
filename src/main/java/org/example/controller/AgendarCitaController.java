package org.example.controller;

import org.example.model.Citas;
import org.example.model.Empleados;
import org.example.model.EstadoCita;
import org.example.model.Mascotas;
import org.example.service.CitaService;
import org.example.service.EmpleadoService;
import org.example.service.MascotaService;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

public class AgendarCitaController {

    private final CitaService     citaService     = new CitaService();
    private final MascotaService  mascotaService  = new MascotaService();
    private final EmpleadoService empleadoService = new EmpleadoService();

    // ── Listar mascotas para el combo ─────────────────────
    public List<Mascotas> listarMascotas() {
        try {
            return mascotaService.listarTodas();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // ── Listar veterinarios para el combo ─────────────────
    public List<Empleados> listarVeterinarios() {
        try {
            return empleadoService.listarTodos();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // ── Guardar cita en BD ────────────────────────────────
    public boolean guardarCita(Mascotas mascota, Empleados empleado,
                               String fechaStr, String horaStr, JPanel panel) {
        try {
            if (mascota  == null) throw new Exception("Selecciona una mascota.");
            if (empleado == null) throw new Exception("Selecciona un veterinario.");
            if (fechaStr == null || fechaStr.trim().isEmpty())
                throw new Exception("Ingresa la fecha (yyyy-MM-dd).");
            if (horaStr  == null || horaStr.trim().isEmpty())
                throw new Exception("Selecciona una hora.");

            LocalDate fecha;
            LocalTime hora;
            try {
                fecha = LocalDate.parse(fechaStr.trim());
            } catch (DateTimeParseException e) {
                throw new Exception("Formato de fecha inválido. Usa: yyyy-MM-dd");
            }
            try {
                hora = LocalTime.parse(horaStr.trim());
            } catch (DateTimeParseException e) {
                throw new Exception("Formato de hora inválido. Usa: HH:mm");
            }

            Citas cita = new Citas();
            cita.setMascota(mascota);
            cita.setEmpleado(empleado);
            cita.setFechaCita(fecha);
            cita.setHoraCita(hora);
            cita.setEstadoCita(EstadoCita.CONFIRMADA);

            citaService.guardarCita(cita);

            JOptionPane.showMessageDialog(panel,
                    "✅ Cita agendada exitosamente!\n\n" +
                            "Mascota:     " + mascota.getNombre()  + "\n" +
                            "Veterinario: " + empleado.getNombre() + "\n" +
                            "Fecha:       " + fecha                + "\n" +
                            "Hora:        " + hora,
                    "Cita confirmada", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel,
                    e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}