package es.codelearnacademy.filelab.repository;

import java.util.List;
import java.util.Optional;

/**
 * Contrato genérico de un repositorio CRUD (crear, leer, actualizar, borrar).
 *
 * <p>Describe qué se puede hacer con las entidades, no cómo se guardan. Ningún método
 * lanza excepciones de E/S: los errores se expresan con listas vacías,
 * {@link Optional#empty()} o {@code false}.</p>
 *
 * @param <T>  tipo de la entidad (por ejemplo {@code Producto})
 * @param <ID> tipo de su identificador (por ejemplo {@code Long})
 */
public interface IRepository<T, ID> {

    /**
     * Recupera todas las entidades almacenadas.
     *
     * @return lista con las entidades disponibles o una lista vacía si no existen
     *         datos o no pueden recuperarse
     */
    List<T> findAll();

    /**
     * Busca una entidad utilizando su identificador.
     *
     * @param id identificador de la entidad que se desea localizar
     * @return la entidad encontrada o {@link Optional#empty()} si no existe,
     *         el identificador es nulo o no pueden recuperarse los datos
     */
    Optional<T> findById(ID id);

    /**
     * Crea una nueva entidad si no existe otra con el mismo identificador.
     *
     * @param entity entidad que se desea crear
     * @return true si la entidad se ha persistido correctamente; false si es nula,
     *         su identificador ya existe o no ha podido guardarse
     */
    boolean create(T entity);

    /**
     * Sustituye una entidad existente por la indicada, localizándola por su identificador.
     *
     * @param entity entidad con los datos actualizados
     * @return true si existía y se ha guardado el cambio; false si es nula,
     *         no existe ninguna entidad con ese identificador o no ha podido guardarse
     */
    boolean update(T entity);

    /**
     * Elimina la entidad con el identificador indicado.
     *
     * @param id identificador de la entidad que se desea eliminar
     * @return true si existía y se ha eliminado; false si el identificador es nulo,
     *         no existe o no ha podido guardarse el cambio
     */
    boolean delete(ID id);
}
