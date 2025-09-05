package cr.ac.una.presentation_layer.Controller;

import cr.ac.una.domain_layer.Receta;
import cr.ac.una.service_layer.RecetaService;

import java.time.LocalDate;
import java.util.List;

public class RecetaController {
    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    public void agregar(Receta receta) {
        recetaService.agregar(receta);
    }

    public void actualizar(Receta receta) {
        recetaService.actualizar(receta);
    }

    public void borrar(String id) {
        recetaService.borrarPorId(id);
    }

    public List<Receta> leerTodos() {
        return recetaService.leerTodos();
    }

    public Receta leerPorId(String id) {
        return recetaService.leerPorIdString(id);
    }

    public List<Receta> buscarPorPaciente(String paciente) {
        return recetaService.leerPorPaciente(paciente);
    }

    public List<Receta> buscarPorMedico(String medico) {
        return recetaService.leerPorMedico(medico);
    }

    public List<Receta> buscarPorEstado(String estado) {
        return recetaService.leerPorEstado(estado);
    }

    public List<Receta> buscarPorFechaConfeccion(LocalDate fecha) {
        return recetaService.leerPorFechaConfeccion(fecha);
    }

    public List<Receta> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return recetaService.leerPorRangoFechas(fechaInicio, fechaFin);
    }

    public void cambiarEstado(String id, String nuevoEstado) {
        recetaService.cambiarEstado(id, nuevoEstado);
    }

    public void marcarComoRetirada(String id, LocalDate fechaRetiro) {
        recetaService.marcarComoRetirada(id, fechaRetiro);
    }

    public void confeccionarReceta(Receta receta) {
        receta.setEstado("Confeccionada");
        receta.setFechaConfeccion(LocalDate.now());
        recetaService.agregar(receta);
    }

    public void addObserver(cr.ac.una.service_layer.IServiceObserver<Receta> observer) {
        recetaService.addObserver(observer);
    }
}
