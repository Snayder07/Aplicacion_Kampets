package org.example.controller;

import org.example.model.Control_vacunas;
import org.example.service.ControlVacunaService;

import java.util.Collections;
import java.util.List;

public class VacunaAdminController {

    private final ControlVacunaService service = new ControlVacunaService();

    public List<Control_vacunas> listarTodas() {
        try {
            return service.listarTodas();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public String calcularEstado(Control_vacunas cv) {
        return service.calcularEstado(cv.getProximaDosis());
    }
}
