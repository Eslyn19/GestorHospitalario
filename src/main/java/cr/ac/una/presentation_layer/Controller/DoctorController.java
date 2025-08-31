package cr.ac.una.presentation_layer.Controller;

import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.service_layer.IService;

import java.util.List;

public class DoctorController {

    private final IService<Doctor> doctorIService;

    public DoctorController(IService<Doctor> doctorIService) {
        this.doctorIService = doctorIService;
    }

    public void agregar(int id, String nombre, String apellido, String especialidad){
        validarId(id);
        validarNombre(nombre, apellido);
        Doctor newDoctor = new Doctor(id, nombre, apellido, "",especialidad);
        doctorIService.agregar(newDoctor);
    }

    public void Actualizar(int id, String nombre, String apellido, String especialidad){
        validarId(id);
        validarNombre(nombre, apellido);
        Doctor newDoctor = new Doctor(id, nombre, apellido, "", especialidad);
        doctorIService.actualizar(newDoctor);
    }

    public void eliminar(int id){
        validarId(id);
        doctorIService.borrar(id);
    }

    public List<Doctor> leerTodos() {
        return doctorIService.leerTodos();
    }

    public Doctor leerPorId(int id) {
        validarId(id);
        return doctorIService.leerPorId(id);
    }

    // --- Validaciones mínimas ---
    private void validarId(int id) {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser mayor que 0.");
    }

    private void validarNombre(String nombre, String apellido) {
        if (nombre == null || apellido == null || nombre.trim().isEmpty() || apellido.trim().isEmpty())
            throw new IllegalArgumentException("El nombre y apellido es obligatorio.");
    }
}
