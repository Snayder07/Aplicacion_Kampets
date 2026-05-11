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

    public List<Cliente> listarClientes() {
        try {
            return clienteService.listarTodos();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public boolean registrarMascota(String nombre, Especies especie, Cliente cliente,
                                    String fechaNacStr, String sexo, JPanel panel) {
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


            Mascotas m = new Mascotas();
            m.setNombre(nombre.trim());
            m.setEspecie(especie);
            m.setCliente(cliente);
            m.setFechaNac(fechaNac);
            m.setSexo(sexo);
            mascotaService.registrarMascota(m);
            JOptionPane.showMessageDialog(panel, "Mascota registrada exitosamente.");
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
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
