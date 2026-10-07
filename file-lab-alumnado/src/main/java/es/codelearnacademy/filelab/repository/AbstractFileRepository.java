package es.codelearnacademy.filelab.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación común del CRUD para repositorios basados en fichero.
 *
 * <p>Patrón "Template Method": esta clase escribe la lógica de {@code findById},
 * {@code create}, {@code update} y {@code delete} una sola vez, apoyándose en tres
 * métodos abstractos que cada formato (CSV, JSON, XML) implementa:</p>
 * <ul>
 *   <li>{@link #getId(Object)}: cómo sacar el id de una entidad;</li>
 *   <li>{@link #readAll()}: cómo leer el fichero entero a una lista;</li>
 *   <li>{@link #writeAll(List)}: cómo escribir la lista entera en el fichero.</li>
 * </ul>
 *
 * <p>{@code readAll}/{@code writeAll} declaran {@code throws IOException} y aquí se
 * captura, para que la API pública no lance excepciones checked.</p>
 *
 * @param <T>  tipo de entidad
 * @param <ID> tipo del identificador
 */
public abstract class AbstractFileRepository<T, ID> implements IRepository<T, ID> {

    /**
     * Devuelve el identificador de una entidad.
     *
     * @param entity entidad
     * @return su identificador
     */
    protected abstract ID getId(T entity);

    /**
     * Lee todas las entidades del fichero.
     *
     * @return lista de entidades (vacía si el fichero no existe)
     * @throws IOException si el fichero existe pero no puede leerse
     */
    protected abstract List<T> readAll() throws IOException;

    /**
     * Escribe la lista completa en el fichero, sustituyendo su contenido.
     *
     * @param entities entidades que se guardan
     * @throws IOException si no puede escribirse
     */
    protected abstract void writeAll(List<T> entities) throws IOException;

    /**
     * {@inheritDoc}
     *
     * <p>Delega en {@link #readAll()}; si falla devuelve {@code List.of()} (lista vacía
     * inmutable).</p>
     */
    @Override
    public List<T> findAll() {
        try {
            return readAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Recorre la lista comparando ids con {@code equals}: los ids son objetos
     * ({@code Long}, {@code String}) y {@code ==} compararía referencias, no valores.</p>
     */
    @Override
    public Optional<T> findById(ID id) {
        if (id == null) {
            return Optional.empty();
        }
        try {
            List<T> entidades = readAll();
            for (T entidad : entidades) {
                if (getId(entidad).equals(id)) {
                    return Optional.of(entidad);
                }
            }
            return Optional.empty();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Leer → comprobar que el id no existe → añadir → escribir todo.
     * Se copia a un {@code new ArrayList<>(...)} porque {@code readAll} podría devolver
     * una lista inmutable y {@code add} lanzaría UnsupportedOperationException.</p>
     */
    @Override
    public boolean create(T entity) {
        if (entity == null) {
            return false;
        }
        try {
            List<T> entidades = new ArrayList<>(readAll());
            for (T entidad : entidades) {
                if (getId(entidad).equals(getId(entity))) {
                    return false; // id duplicado
                }
            }
            entidades.add(entity); // fuera del bucle: se añade una sola vez
            writeAll(entidades);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Se usa un for con índice porque hace falta la posición para {@code set(i, ...)}.</p>
     */
    @Override
    public boolean update(T entity) {
        if (entity == null) {
            return false;
        }
        try {
            List<T> entidades = new ArrayList<>(readAll());
            for (int i = 0; i < entidades.size(); i++) {
                if (getId(entidades.get(i)).equals(getId(entity))) {
                    entidades.set(i, entity);
                    writeAll(entidades);
                    return true;
                }
            }
            return false; // no existía
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Al encontrarlo se elimina y se sale con {@code return}, así no hay problema
     * por modificar la lista mientras se recorre.</p>
     */
    @Override
    public boolean delete(ID id) {
        if (id == null) {
            return false;
        }
        try {
            List<T> entidades = new ArrayList<>(readAll());
            for (int i = 0; i < entidades.size(); i++) {
                if (getId(entidades.get(i)).equals(id)) {
                    entidades.remove(i);
                    writeAll(entidades);
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
