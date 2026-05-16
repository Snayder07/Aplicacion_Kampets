package org.example.controller;

import org.example.model.Cliente;
import org.example.model.Especies;
import org.example.model.Mascotas;
import org.example.repository.EspecieRepositoryImpl;
import org.example.service.ClienteService;
import org.example.service.MascotaService;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

public class MascotaAdminController {

    private final MascotaService       mascotaService = new MascotaService();
    private final ClienteService       clienteService = new ClienteService();
    private final EspecieRepositoryImpl especieRepo   = new EspecieRepositoryImpl();

    public List<Mascotas> listarTodas() {
        try {
            return mascotaService.listarTodas();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Especies> listarEspecies() {
        try {
            return especieRepo.buscarTodos();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /** Busca una especie por nombre; si no existe, la crea en la BD. */
    public Especies obtenerOCrearEspecie(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return null;
        Especies existente = especieRepo.buscarPorNombre(nombre.trim());
        if (existente != null) return existente;
        Especies nueva = new Especies();
        nueva.setNombre(nombre.trim());
        especieRepo.guardar(nueva);
        return especieRepo.buscarPorNombre(nombre.trim());
    }

    public List<Cliente> listarClientes() {
        try {
            return clienteService.listarTodos();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /** Excepción especial para indicar que se necesita una característica diferenciadora */
    public static class NecesitaCaracteristicaException extends Exception {
        public NecesitaCaracteristicaException(String msg) { super(msg); }
    }

    public boolean registrarMascota(String nombre, Especies especie, Cliente cliente,
                                    String fechaNacStr, String sexo, String caracteristica,
                                    JPanel panel) {
        return registrarMascota(nombre, especie, cliente, fechaNacStr, sexo, caracteristica, panel, null);
    }

    /**
     * Versión extendida: si onNecesitaCaracteristica != null, en vez de mostrar popup
     * llama ese Runnable (la vista resalta el campo) y retorna false sin popup propio.
     */
    public boolean registrarMascota(String nombre, Especies especie, Cliente cliente,
                                    String fechaNacStr, String sexo, String caracteristica,
                                    JPanel panel, Runnable onNecesitaCaracteristica) {
        try {
            if (nombre == null || nombre.trim().isEmpty())
                throw new Exception("El nombre de la mascota es obligatorio.");
            if (especie == null) throw new Exception("Selecciona una especie.");
            if (cliente == null) throw new Exception("Selecciona un dueño.");

            LocalDate fechaNac = null;
            if (fechaNacStr != null && !fechaNacStr.trim().isEmpty()) {
                try { fechaNac = LocalDate.parse(fechaNacStr.trim()); }
                catch (DateTimeParseException e) {
                    throw new Exception("Formato de fecha inválido. Usa: yyyy-MM-dd");
                }
            }

            // ── Detección de duplicados ──────────────────────────────────
            List<Mascotas> todas = mascotaService.listarTodas();
            boolean hayConflicto = todas.stream().anyMatch(m ->
                    m.getNombre().equalsIgnoreCase(nombre.trim()) &&
                            m.getEspecie() != null &&
                            m.getEspecie().getNombre().equalsIgnoreCase(especie.getNombre())
            );
            String car = (caracteristica != null) ? caracteristica.trim() : "";
            if (hayConflicto) {
                if (car.isEmpty()) {
                    throw new NecesitaCaracteristicaException(
                            "Ya existe una mascota llamada \"" + nombre.trim() + "\" de especie " +
                                    especie.getNombre() + ".\n" +
                                    "Ingresa una característica que la distinga\n" +
                                    "(ej: color de pelaje, collar rojo, mancha en la oreja...)."
                    );
                }
                boolean carDuplicada = todas.stream().anyMatch(m ->
                        m.getNombre().equalsIgnoreCase(nombre.trim()) &&
                                m.getEspecie() != null &&
                                m.getEspecie().getNombre().equalsIgnoreCase(especie.getNombre()) &&
                                car.equalsIgnoreCase(m.getCaracteristica() != null ? m.getCaracteristica().trim() : "")
                );
                if (carDuplicada) {
                    throw new NecesitaCaracteristicaException(
                            "Ya existe una mascota con ese nombre, especie y esa misma característica.\n" +
                                    "Ingresa una característica diferente para distinguirla."
                    );
                }
            }

            Mascotas m = new Mascotas();
            m.setNombre(nombre.trim());
            m.setEspecie(especie);
            m.setCliente(cliente);
            m.setFechaNac(fechaNac);
            m.setSexo(sexo);
            m.setCaracteristica(car.isEmpty() ? null : car);
            mascotaService.registrarMascota(m);
            JOptionPane.showMessageDialog(panel, "Mascota registrada exitosamente.");
            return true;
        } catch (NecesitaCaracteristicaException e) {
            if (onNecesitaCaracteristica != null) {
                // La vista maneja el resaltado; solo mostramos el mensaje
                JOptionPane.showMessageDialog(panel, e.getMessage(), "Campo requerido", JOptionPane.WARNING_MESSAGE);
                onNecesitaCaracteristica.run();
            } else {
                JOptionPane.showMessageDialog(panel, e.getMessage(), "Campo requerido", JOptionPane.WARNING_MESSAGE);
            }
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void actualizarMascota(Mascotas mascota, String nuevoNombre,
                                  Especies nuevaEspecie, java.time.LocalDate nuevaFecha,
                                  String nuevoSexo, JPanel panel) {
        try {
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty())
                throw new Exception("El nombre no puede estar vacío.");
            mascota.setNombre(nuevoNombre.trim());
            mascota.setEspecie(nuevaEspecie);
            mascota.setFechaNac(nuevaFecha);
            mascota.setSexo(nuevoSexo);
            new org.example.repository.MascotaRepositoryImpl().actualizar(mascota);
            JOptionPane.showMessageDialog(panel, "Mascota actualizada correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarMascota(Integer id, JPanel panel) {
        try {
            mascotaService.eliminarMascota(id);
            JOptionPane.showMessageDialog(panel, "Mascota eliminada exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
