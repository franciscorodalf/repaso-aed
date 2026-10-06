package es.codelearnacademy.filelab.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFileRepository<T, ID> implements IRepository<T, ID> {

    protected abstract ID getId(T entity);

    protected abstract List<T> readAll() throws IOException;

    protected abstract void writeAll(List<T> entities) throws IOException;

    @Override
    public List<T> findAll() {
        try {
            return readAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        if (id == null) {
            return Optional.empty();
        }

        try {
            List<T> lista = readAll();
            for (T t : lista) {
                if (getId(t).equals(id)) {
                    return Optional.of(t);
                }
            }
            return Optional.empty();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean create(T entity) {
        if (entity == null) {
            return false;
        }
        try {
            List<T> lista = new ArrayList<>(readAll());
            for (T t : lista) {
                if (getId(t).equals(getId(entity))) {
                    return false;
                }
            }

            lista.add(entity);
            writeAll(lista);
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    @Override
    public boolean update(T entity) {
        if (entity == null) {
            return false;
        }

        try {
            List<T> lista = new ArrayList<>(readAll());
            for (int i = 0; i < lista.size(); i++) {
                if (getId(lista.get(i)).equals(getId(entity))) {
                    lista.set(i, entity);
                    writeAll(lista);
                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            return false;
        }

    }

    @Override
    public boolean delete(ID id) {
        if (id == null) {
            return false;
        }

        try {
            List<T> lista = new ArrayList<>(readAll());
            for (int i = 0; i < lista.size(); i++) {
                if (getId(lista.get(i)).equals(id)) {
                    lista.remove(i);
                    writeAll(lista);
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
