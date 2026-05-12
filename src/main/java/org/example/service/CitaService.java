package org.example.service;

import org.example.model.Citas;
import org.example.repository.CitaRepositoryImpl;

import java.util.List;

/**
 * PERSONA 2 — Servicio de Citas
 * Lógica para crear, cancelar y consultar citas
 */
public class CitaService {

    private final CitaRepositoryImpl repositorio = new CitaRepositoryImpl();

    // ─────────────────────────────────────────────────────────
    // GUARDAR CITA
    // P3 llama a este método cuando el cliente agenda una cita
    // ─────────────────────────────────────────────────────────
    public void guardarCita(Citas cita) throws Exception {
        if (cita == null) {
            throw new Exception("La cita no puede estar vacía.");
        }
        repositorio.guardar(cita);
    }

    // ─────────────────────────────────────────────────────────
    // LISTAR TODAS LAS CITAS
    // El admin puede ver todas (RF-02, RF-07)
    // ─────────────────────────────────────────────────────────
    public List<Citas> listarTodas() {
        return repositorio.buscarTodos();
    }

    public List<Citas> listarPorCliente(Integer clienteId) {
        return repositorio.buscarPorCliente(clienteId);
    }

    public List<Citas> listarPasadasPorCliente(Integer clienteId) {
        return repositorio.buscarPasadasPorCliente(clienteId);
    }

    public List<Citas> listarDeHoy() {
        return repositorio.buscarDeHoy();
    }

    // ─────────────────────────────────────────────────────────
    // BUSCAR CITA POR ID
    // ─────────────────────────────────────────────────────────
    public Citas buscarPorId(Integer id) throws Exception {
        if (id == null) {
            throw new Exception("El ID de cita no puede estar vacío.");
        }
        return repositorio.buscarPorId(id);
    }

    // ─────────────────────────────────────────────────────────
    // CANCELAR CITA
    // P3 llama a esto cuando el cliente cancela (RF-03)
    // ─────────────────────────────────────────────────────────
    public void cancelarCita(Integer idCita) throws Exception {
        Citas cita = repositorio.buscarPorId(idCita);
        if (cita == null) {
            throw new Exception("No se encontró la cita con ID: " + idCita);
        }
        repositorio.eliminar(idCita);
    }
}
