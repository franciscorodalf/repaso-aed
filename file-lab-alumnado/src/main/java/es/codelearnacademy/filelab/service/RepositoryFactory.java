package es.codelearnacademy.filelab.service;

import es.codelearnacademy.filelab.csv.ProductoCsvRepository;
import es.codelearnacademy.filelab.json.ProductoJsonRepository;
import es.codelearnacademy.filelab.repository.IProductoRepository;
import es.codelearnacademy.filelab.xml.ProductoXmlRepository;
import java.nio.file.Path;

/**
 * Fábrica (patrón Factory) que crea el repositorio de productos adecuado para cada formato.
 *
 * <p>Es el único sitio que conoce las clases concretas CSV/JSON/XML. El resto del código
 * trabaja con la interfaz {@link IProductoRepository} y no sabe qué formato hay debajo.</p>
 */
public class RepositoryFactory {

    /**
     * Crea un repositorio de productos para el formato y fichero indicados.
     *
     * <p>Devuelve el tipo de la interfaz, no la clase concreta: así quien lo usa puede
     * cambiar de formato sin cambiar su código (polimorfismo).</p>
     *
     * @param format formato del fichero
     * @param path   ruta del fichero; se pasa tal cual al repositorio
     * @return repositorio CSV, JSON o XML según {@code format}
     * @throws NullPointerException si el formato es nulo
     */
    public IProductoRepository create(FileFormat format, Path path) {
        if (format == null) {
            throw new NullPointerException("El formato no puede ser nulo");
        }
        // switch sobre enum: el compilador comprueba que cubrimos todos los casos
        return switch (format) {
            case CSV -> new ProductoCsvRepository(path);
            case JSON -> new ProductoJsonRepository(path);
            case XML -> new ProductoXmlRepository(path);
        };
    }
}
