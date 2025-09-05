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
        Doctor newDoctor = new Doctor(id, nombre, apellido, String.valueOf(id), especialidad);
        doctorIService.agregar(newDoctor);
    }

    public void Actualizar(int id, String nombre, String apellido, String especialidad){
        validarId(id);
        validarNombre(nombre, apellido);
        Doctor newDoctor = new Doctor(id, nombre, apellido, String.valueOf(id), especialidad);
        doctorIService.actualizar(newDoctor);
    }
    
    public void ActualizarConClave(int id, String nombre, String apellido, String clave, String especialidad){
        validarId(id);
        validarNombre(nombre, apellido);
        validarClave(clave);
        Doctor newDoctor = new Doctor(id, nombre, apellido, clave, especialidad);
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

    public boolean autenticar(int id, String password) {
        try {
            Doctor doctor = leerPorId(id);
            if (doctor != null) {
                return doctor.getClave().equals(password);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerNombreDoctor(int id) {
        try {
            Doctor doctor = leerPorId(id);
            if (doctor != null) {
                return doctor.getNombre() + " " + doctor.getApellido();
            }
            return "Doctor";
        } catch (Exception e) {
            return "Doctor";
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
