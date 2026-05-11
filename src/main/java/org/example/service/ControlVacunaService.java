package org.example.service;

import org.example.model.Control_vacunas;
import org.example.repository.ControlVacunaRepositoryImpl;
import java.time.LocalDate;
import java.util.List;

public class ControlVacunaService {

    private final ControlVacunaRepositoryImpl repositorio = new ControlVacunaRepositoryImpl();

    public List<Control_vacunas> listarTodas() {
        return repositorio.buscarTodos();
    }

    public List<Control_vacunas> listarPorCliente(Integer clienteId) {
        return repositorio.buscarPorCliente(clienteId);
    }

    public void guardar(Control_vacunas cv) throws Exception {
        if (cv == null) throw new Exception("El registro de vacuna no puede estar vacío.");
        if (cv.getMascota() == null) throw new Exception("Debe seleccionar una mascota.");
        if (cv.getVacuna()  == null) throw new Exception("Debe seleccionar una vacuna.");
        if (cv.getFechaAplicacion() == null) throw new Exception("La fecha de aplicación es obligatoria.");
        repositorio.guardar(cv);
    }

    public void eliminar(Integer id) throws Exception {
        if (id == null) throw new Exception("ID de registro inválido.");
        repositorio.eliminar(id);
    }

    public String calcularEstado(LocalDate proximaDosis) {
        if (proximaDosis == null) return "Al día";
        LocalDate hoy = LocalDate.now();
        if (proximaDosis.isBefore(hoy))              return "Vencida";
        if (proximaDosis.isBefore(hoy.plusDays(30))) return "Próxima";
        return "Al día";
    }
}