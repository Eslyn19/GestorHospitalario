package cr.ac.una.presentation_layer.Controller;

import cr.ac.una.domain_layer.Medicamento;
import cr.ac.una.service_layer.MedicamentoService;

import java.util.List;

public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    public MedicamentoController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    public void agregar(String codigo, String nombreMedic, String presentacion){
        validarCodigo(codigo);
        validarNombreMedic(nombreMedic);
        validarPresentacion(presentacion);
        Medicamento newMedicamento = new Medicamento(codigo, nombreMedic, presentacion);
        medicamentoService.agregar(newMedicamento);
    }

    public void actualizar(String codigo, String nombreMedic, String presentacion){
        validarCodigo(codigo);
        validarNombreMedic(nombreMedic);
        validarPresentacion(presentacion);
        Medicamento newMedicamento = new Medicamento(codigo, nombreMedic, presentacion);
        medicamentoService.actualizar(newMedicamento);
    }

    public void eliminar(String codigo){
        validarCodigo(codigo);
        medicamentoService.borrarPorCodigo(codigo);
    }

    public List<Medicamento> leerTodos() {
        return medicamentoService.leerTodos();
    }

    public Medicamento leerPorCodigo(String codigo) {
        validarCodigo(codigo);
        return medicamentoService.leerPorCodigo(codigo);
    }

    // --- Validaciones ---
    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty())
            throw new IllegalArgumentException("El código es obligatorio.");
    }

    private void validarNombreMedic(String nombreMedic) {
        if (nombreMedic == null || nombreMedic.trim().isEmpty())
            throw new IllegalArgumentException("El nombre del medicamento es obligatorio.");
    }

    private void validarPresentacion(String presentacion) {
        if (presentacion == null || presentacion.trim().isEmpty())
            throw new IllegalArgumentException("La presentación es obligatoria.");
    }
}
