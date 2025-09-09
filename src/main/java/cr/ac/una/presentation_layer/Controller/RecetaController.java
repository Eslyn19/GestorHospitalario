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

    public void confeccionarReceta(Receta receta) {
        receta.setEstado("Confeccionada");
        receta.setFechaConfeccion(LocalDate.now());
        recetaService.agregar(receta);
    }

    public void addObserver(cr.ac.una.service_layer.IServiceObserver<Receta> observer) {
        recetaService.addObserver(observer);
    }
}
