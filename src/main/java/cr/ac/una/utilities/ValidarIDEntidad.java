package cr.ac.una.utilities;

import cr.ac.una.data_access_layer.IFileStore;
import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.domain_layer.Farmaceuta;
import cr.ac.una.domain_layer.Paciente;

import java.util.List;

public class ValidarIDEntidad {
    
    private final IFileStore<Paciente> pacienteFileStore;
    private final IFileStore<Doctor> doctorFileStore;
    private final IFileStore<Farmaceuta> farmaceutaFileStore;
    
    public ValidarIDEntidad(IFileStore<Paciente> pacienteFileStore,
                            IFileStore<Doctor> doctorFileStore,
                            IFileStore<Farmaceuta> farmaceutaFileStore) {
        this.pacienteFileStore = pacienteFileStore;
        this.doctorFileStore = doctorFileStore;
        this.farmaceutaFileStore = farmaceutaFileStore;
    }
    
    public void validarIdUnico(int id, String entityType) {
        // Verificar en pacientes
        List<Paciente> pacientes = pacienteFileStore.readAll();
        boolean existeEnPacientes = pacientes.stream().anyMatch(p -> p.getID() == id);
        if (existeEnPacientes) {
            throw new IllegalArgumentException("Ya existe un paciente con el ID: " + id);
        }
        
        // Verificar en doctores
        List<Doctor> doctores = doctorFileStore.readAll();
        boolean existeEnDoctores = doctores.stream().anyMatch(d -> d.getID() == id);
        if (existeEnDoctores) {
            throw new IllegalArgumentException("Ya existe un doctor con el ID: " + id);
        }
        
        // Verificar en farmacéuticos
        List<Farmaceuta> farmaceutas = farmaceutaFileStore.readAll();
        boolean existeEnFarmaceutas = farmaceutas.stream().anyMatch(f -> f.getID() == id);
        if (existeEnFarmaceutas) {
            throw new IllegalArgumentException("Ya existe un farmacéutico con el ID: " + id);
        }
    }

    public void validarIdUnicoParaActualizacion(int id, String entityType, int excludeEntityId) {
        // Verificar en pacientes (excluyendo el paciente actual si es una actualización de paciente)
        List<Paciente> pacientes = pacienteFileStore.readAll();
        boolean existeEnPacientes = pacientes.stream()
            .anyMatch(p -> p.getID() == id && p.getID() != excludeEntityId);
        if (existeEnPacientes) {
            throw new IllegalArgumentException("Ya existe un paciente con el ID: " + id);
        }
        
        // Verificar en doctores (excluyendo el doctor actual si es una actualización de doctor)
        List<Doctor> doctores = doctorFileStore.readAll();
        boolean existeEnDoctores = doctores.stream()
            .anyMatch(d -> d.getID() == id && d.getID() != excludeEntityId);
        if (existeEnDoctores) {
            throw new IllegalArgumentException("Ya existe un doctor con el ID: " + id);
        }
        // Verificar en farmacéuticos (excluyendo el farmacéutico actual si es una actualización de farmacéutico)
        List<Farmaceuta> farmaceutas = farmaceutaFileStore.readAll();
        boolean existeEnFarmaceutas = farmaceutas.stream()
            .anyMatch(f -> f.getID() == id && f.getID() != excludeEntityId);
        if (existeEnFarmaceutas) {
            throw new IllegalArgumentException("Ya existe un farmacéutico con el ID: " + id);
        }
    }
}
