package cr.ac.una.service_layer;

import cr.ac.una.utilities.ChangeType;

public interface IServiceObserver<T> {
    void onDataChanged(ChangeType type, T entity);
}
