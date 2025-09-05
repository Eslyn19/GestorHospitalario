package cr.ac.una.service_layer;

import cr.ac.una.data_access_layer.IFileStore;
import cr.ac.una.domain_layer.Receta;
import cr.ac.una.utilities.ChangeType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecetaService implements IService<Receta> {
    private final IFileStore<Receta> recetaFileStore;
    private final List<IServiceObserver<Receta>> listenerRecetas = new ArrayList<>();

    public RecetaService(IFileStore<Receta> recetaFileStore) {
        this.recetaFileStore = recetaFileStore;
    }

    @Override
    public void agregar(Receta entity) {
        List<Receta> recetas = recetaFileStore.readAll();
        recetas.add(entity);
        recetaFileStore.writeAll(recetas);
        notifyObservers(ChangeType.CREATED, entity);
    }

    @Override
    public void borrar(int id) {
        List<Receta> recetas = recetaFileStore.readAll();
        Receta removed = null;
        for (int i = 0; i < recetas.size(); i++) {
            if (recetas.get(i).getId().equals(String.valueOf(id))) { 
                removed = recetas.remove(i); 
                break; 
            }
        }
        recetaFileStore.writeAll(recetas);
        if (removed != null) notifyObservers(ChangeType.DELETED, removed);
    }

    public void borrarPorId(String id) {
        List<Receta> recetas = recetaFileStore.readAll();
        Receta removed = null;
        for (int i = 0; i < recetas.size(); i++) {
            if (recetas.get(i).getId().equals(id)) { 
                removed = recetas.remove(i); 
                break; 
            }
        }
        recetaFileStore.writeAll(recetas);
        if (removed != null) notifyObservers(ChangeType.DELETED, removed);
    }

    @Override
    public void actualizar(Receta entity) {
        List<Receta> recetas = recetaFileStore.readAll();
        for (int i = 0; i < recetas.size(); i++) {
            if (recetas.get(i).getId().equals(entity.getId())) {
                recetas.set(i, entity);
                break;
            }
        }
        recetaFileStore.writeAll(recetas);
        notifyObservers(ChangeType.UPDATED, entity);
    }

    @Override
    public List<Receta> leerTodos() {
        return recetaFileStore.readAll();
    }

    @Override
    public Receta leerPorId(int id) {
        return recetaFileStore.readAll().stream()
                .filter(x -> x.getId().equals(String.valueOf(id)))
                .findFirst().orElse(null);
    }

    public Receta leerPorIdString(String id) {
        return recetaFileStore.readAll().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst().orElse(null);
    }

    public List<Receta> leerPorPaciente(String paciente) {
        return recetaFileStore.readAll().stream()
                .filter(x -> x.getPaciente().toLowerCase().contains(paciente.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Receta> leerPorMedico(String medico) {
        return recetaFileStore.readAll().stream()
                .filter(x -> x.getMedico().toLowerCase().contains(medico.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Receta> leerPorEstado(String estado) {
        return recetaFileStore.readAll().stream()
                .filter(x -> x.getEstado().toLowerCase().contains(estado.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Receta> leerPorFechaConfeccion(LocalDate fecha) {
        return recetaFileStore.readAll().stream()
                .filter(x -> x.getFechaConfeccion().equals(fecha))
                .collect(Collectors.toList());
    }

    public List<Receta> leerPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return recetaFileStore.readAll().stream()
                .filter(x -> x.getFechaConfeccion().isAfter(fechaInicio.minusDays(1)) && 
                           x.getFechaConfeccion().isBefore(fechaFin.plusDays(1)))
                .collect(Collectors.toList());
    }

    public void cambiarEstado(String id, String nuevoEstado) {
        Receta receta = leerPorIdString(id);
        if (receta != null) {
            receta.setEstado(nuevoEstado);
            actualizar(receta);
        }
    }

    public void marcarComoRetirada(String id, LocalDate fechaRetiro) {
        Receta receta = leerPorIdString(id);
        if (receta != null) {
            receta.setEstado("Retirada");
            receta.setFechaRetiro(fechaRetiro);
            actualizar(receta);
        }
    }

    @Override
    public void addObserver(IServiceObserver<Receta> listener) {
        if (listener != null) listenerRecetas.add(listener);
    }

    private void notifyObservers(ChangeType type, Receta entity) {
        for (IServiceObserver<Receta> l : listenerRecetas) l.onDataChanged(type, entity);
    }
}
