package cr.ac.una.presentation_layer.Controller;

import cr.ac.una.domain_layer.PrescripcionMedicamento;
import cr.ac.una.service_layer.PrescripcionService;

import java.util.List;

public class PrescripcionController {
    private final PrescripcionService prescripcionService;

    public PrescripcionController(PrescripcionService prescripcionService) {
        this.prescripcionService = prescripcionService;
    }

    public void agregar(PrescripcionMedicamento prescripcion) {
        prescripcionService.agregar(prescripcion);
    }

    public void actualizar(PrescripcionMedicamento prescripcion) {
        prescripcionService.actualizar(prescripcion);
    }

    public void borrar(String medicamento) {
        prescripcionService.borrarPorMedicamento(medicamento);
    }

    public List<PrescripcionMedicamento> leerTodos() {
        return prescripcionService.leerTodos();
    }

    public PrescripcionMedicamento leerPorMedicamento(String medicamento) {
        return prescripcionService.leerPorMedicamento(medicamento);
    }

    public List<PrescripcionMedicamento> buscarPorIndicaciones(String indicaciones) {
        return prescripcionService.leerPorIndicaciones(indicaciones);
    }

    public void addObserver(cr.ac.una.service_layer.IServiceObserver<PrescripcionMedicamento> observer) {
        prescripcionService.addObserver(observer);
    }
}
