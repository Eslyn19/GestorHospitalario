package cr.ac.una.presentation_layer.Controller;

import cr.ac.una.domain_layer.Receta;
import cr.ac.una.data_access_layer.RecetaFileStore;

import java.io.File;
import java.util.List;

public class RecetaController {

    private final RecetaFileStore recetaFileStore;

    public RecetaController(File xmlFile) {
        this.recetaFileStore = new RecetaFileStore(xmlFile);
    }

    public RecetaController() {
        this(new File("data/recetas.xml"));
    }

    public List<Receta> leerTodos() {
        return recetaFileStore.leerTodos();
    }

    public Receta leerPorId(String id) {
        return recetaFileStore.leerPorId(id);
    }
}
