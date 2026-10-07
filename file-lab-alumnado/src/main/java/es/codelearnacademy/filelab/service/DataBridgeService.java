package es.codelearnacademy.filelab.service;

import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.IProductoRepository;
import java.nio.file.Path;
import java.util.List;

/**
 * Convierte productos de un formato de fichero a otro (CSV, JSON, XML).
 *
 * <p>No contiene código de CSV, JSON ni XML: pide los repositorios a
 * {@link RepositoryFactory} y trabaja solo con {@link IProductoRepository}.
 * Leer de uno y escribir en otro es suficiente para convertir.</p>
 */
public class DataBridgeService {

    /** Fábrica que decide qué repositorio concreto usar. Se inyecta por constructor. */
    private final RepositoryFactory repositoryFactory;

    /**
     * @param repositoryFactory fábrica de repositorios que se usará en las conversiones
     */
    public DataBridgeService(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
    }

    /**
     * Lee todos los productos del fichero de origen y los guarda en el de destino.
     *
     * <p>Como la interfaz pública no tiene {@code writeAll}, se guarda cada producto con
     * {@code create}. Si el destino ya contiene un producto con el mismo id, {@code create}
     * devuelve {@code false} y ese producto no se cuenta.</p>
     *
     * @param origenFormato  formato del fichero de origen
     * @param origen         ruta del fichero de origen
     * @param destinoFormato formato del fichero de destino
     * @param destino        ruta del fichero de destino
     * @return número de productos guardados correctamente en el destino
     */
    public int convert(FileFormat origenFormato, Path origen,
                       FileFormat destinoFormato, Path destino) {
        // 1-2. Repositorio de origen y lectura de todos los productos
        IProductoRepository repositorioOrigen = repositoryFactory.create(origenFormato, origen);
        List<Producto> productos = repositorioOrigen.findAll();

        // 3. Repositorio de destino (otro formato, otro fichero)
        IProductoRepository repositorioDestino = repositoryFactory.create(destinoFormato, destino);

        // 4-5. Guardar cada producto y contar los que se han guardado
        int convertidos = 0;
        for (Producto producto : productos) {
            if (repositorioDestino.create(producto)) {
                convertidos++;
            }
        }
        return convertidos;
    }
}
