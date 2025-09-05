package cr.ac.una.presentation_layer.Controller;

import cr.ac.una.domain_layer.Farmaceuta;
import cr.ac.una.service_layer.IService;

import java.util.List;

public class FarmaceutaController {

    private final IService<Farmaceuta> farmaceutaIService;

    public FarmaceutaController(IService<Farmaceuta> farmaceutaIService) {
        this.farmaceutaIService = farmaceutaIService;
    }

    public void agregar(int id, String nombre, String apellido){
        validarId(id);
        validarNombre(nombre, apellido);
        Farmaceuta newFarmaceuta = new Farmaceuta(id, nombre, apellido, String.valueOf(id));
        farmaceutaIService.agregar(newFarmaceuta);
    }

    public void Actualizar(int id, String nombre, String apellido){
        validarId(id);
        validarNombre(nombre, apellido);
        Farmaceuta newFarmaceuta = new Farmaceuta(id, nombre, apellido, String.valueOf(id));
        farmaceutaIService.actualizar(newFarmaceuta);
    }
    
    public void ActualizarConClave(int id, String nombre, String apellido, String clave){
        validarId(id);
        validarNombre(nombre, apellido);
        validarClave(clave);
        Farmaceuta newFarmaceuta = new Farmaceuta(id, nombre, apellido, clave);
        farmaceutaIService.actualizar(newFarmaceuta);
    }

    public void eliminar(int id){
        validarId(id);
        farmaceutaIService.borrar(id);
    }

    public List<Farmaceuta> leerTodos() {
        return farmaceutaIService.leerTodos();
    }

    public Farmaceuta leerPorId(int id) {
        validarId(id);
        return farmaceutaIService.leerPorId(id);
    }

    public boolean autenticar(int id, String password) {
        try {
            Farmaceuta farmaceuta = leerPorId(id);
            if (farmaceuta != null) {
                return farmaceuta.getClave().equals(password);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerNombreFarmaceuta(int id) {
        try {
            Farmaceuta farmaceuta = leerPorId(id);
            if (farmaceuta != null) {
                return farmaceuta.getNombre() + " " + farmaceuta.getApellido();
            }
            return "Farmacéutico";
        } catch (Exception e) {
            return "Farmacéutico";
        }
    }

    // --- Validaciones mínimas ---
    private void validarId(int id) {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser mayor que 0.");
    }

    private void validarNombre(String nombre, String apellido) {
        if (nombre == null || apellido == null || nombre.trim().isEmpty() || apellido.trim().isEmpty())
            throw new IllegalArgumentException("El nombre y apellido es obligatorio.");
    }
    
    private void validarClave(String clave) {
        if (clave == null || clave.trim().isEmpty())
            throw new IllegalArgumentException("La clave es obligatoria.");
    }
}
