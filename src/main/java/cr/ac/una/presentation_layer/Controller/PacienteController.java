package cr.ac.una.presentation_layer.Controller;

import cr.ac.una.domain_layer.Paciente;
import cr.ac.una.service_layer.IService;

import java.util.List;

public class PacienteController {

    private final IService<Paciente> pacienteIService;

    public PacienteController(IService<Paciente> pacienteIService) {
        this.pacienteIService = pacienteIService;
    }

    public void agregar(int id, String nombre, String fechaNacimiento, String telefono){
        validarId(id);
        validarNombre(nombre);
        validarFechaNacimiento(fechaNacimiento);
        validarTelefono(telefono);
        Paciente newPaciente = new Paciente(id, nombre, fechaNacimiento, telefono);
        pacienteIService.agregar(newPaciente);
    }

    public void Actualizar(int id, String nombre, String fechaNacimiento, String telefono){
        validarId(id);
        validarNombre(nombre);
        validarFechaNacimiento(fechaNacimiento);
        validarTelefono(telefono);
        Paciente newPaciente = new Paciente(id, nombre, fechaNacimiento, telefono);
        pacienteIService.actualizar(newPaciente);
    }

    public void eliminar(int id){
        validarId(id);
        pacienteIService.borrar(id);
    }

    public List<Paciente> leerTodos() {
        return pacienteIService.leerTodos();
    }

    public Paciente leerPorId(int id) {
        validarId(id);
        return pacienteIService.leerPorId(id);
    }

    // --- Validaciones ---
    private void validarId(int id) {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser mayor que 0.");
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio.");
    }

    private void validarFechaNacimiento(String fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.trim().isEmpty())
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
    }

    private void validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty())
            throw new IllegalArgumentException("El teléfono es obligatorio.");
    }
}
