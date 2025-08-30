package cr.ac.una.service_layer;

import java.util.List;

public interface IService<T> {
    void agregar(T entity);
    void borrar(int id);
    void actualizar(T entity);
    List<T> leerTodos();
    T leerPorId(int id);

    // Observer
    void addObserver(IServiceObserver<T> listener);
}
