package cr.ac.una.service_layer;

import cr.ac.una.data_access_layer.IFileStore;

import cr.ac.una.domain_layer.Farmaceuta;
import cr.ac.una.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class FarmaceutaService implements IService<Farmaceuta>{
    private final IFileStore<Farmaceuta> farmaceutafileStore;
    private final List<IServiceObserver<Farmaceuta>> listenerFarmaceutas = new ArrayList<>();

    public FarmaceutaService(IFileStore<Farmaceuta> farmaceutafileStore) {
        this.farmaceutafileStore = farmaceutafileStore;
    }

    @Override
    public void agregar(Farmaceuta entity) {
        List<Farmaceuta> farmaceutas = farmaceutafileStore.readAll();
        farmaceutas.add(entity);
        farmaceutafileStore.writeAll(farmaceutas);
        notifyObservers(ChangeType.CREATED, entity);
    }

    @Override
    public void borrar(int id) {
        List<Farmaceuta> farmaceutas = farmaceutafileStore.readAll();
        Farmaceuta removed = null;
        for (int i = 0; i < farmaceutas.size(); i++) {
            if (farmaceutas.get(i).getID() == id) { removed = farmaceutas.remove(i); break; }
        }
        farmaceutafileStore.writeAll(farmaceutas);
        if (removed != null) notifyObservers(ChangeType.DELETED, removed);
    }

    @Override
    public void actualizar(Farmaceuta entity) {
        List<Farmaceuta> farmaceutas = farmaceutafileStore.readAll();
        for (int i = 0; i < farmaceutas.size(); i++) {
            if (farmaceutas.get(i).getID() == entity.getID()) {
                farmaceutas.set(i, entity);
                break;
            }
        }
        farmaceutafileStore.writeAll(farmaceutas);
        notifyObservers(ChangeType.UPDATED, entity);
    }

    @Override
    public List<Farmaceuta> leerTodos() {
        return farmaceutafileStore.readAll();
    }

    @Override
    public Farmaceuta leerPorId(int id) {
        return farmaceutafileStore.readAll().stream().filter(x -> x.getID() == id).findFirst().orElse(null);
    }

    @Override
    public void addObserver(IServiceObserver<Farmaceuta> listener) {
        if (listener != null) listenerFarmaceutas.add(listener);
    }

    private void notifyObservers(ChangeType type, Farmaceuta entity) {
        for (IServiceObserver<Farmaceuta> l : listenerFarmaceutas) l.onDataChanged(type, entity);
    }

}
