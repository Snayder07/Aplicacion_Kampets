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
