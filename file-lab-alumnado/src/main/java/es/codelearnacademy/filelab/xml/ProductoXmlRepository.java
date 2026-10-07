package es.codelearnacademy.filelab.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IProductoRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de productos en XML con {@link XmlMapper} (Jackson para XML).
 *
 * <p>Se lee y escribe un {@link DocumentoProductos}, no una lista directamente,
 * porque el XML necesita un elemento raíz.</p>
 */
public class ProductoXmlRepository
        extends AbstractFileRepository<Producto, Long>
        implements IProductoRepository {

    private final Path path;
    private final XmlMapper mapper;

    /**
     * @param path fichero XML
     */
    public ProductoXmlRepository(Path path) {
        this(path, new XmlMapper());
    }

    /**
     * @param path   fichero XML
     * @param mapper mapper XML configurado
     */
    public ProductoXmlRepository(Path path, XmlMapper mapper) {
        this.path = path;
        this.mapper = mapper;
    }

    @Override
    protected Long getId(Producto producto) {
        return producto.id();
    }

    /**
     * Lee el documento y devuelve su lista. Aquí no hace falta TypeReference: se lee
     * una clase no genérica ({@code DocumentoProductos.class}). {@code readValue}
     * devuelve un objeto y el getter se llama sobre ese objeto, no sobre la clase.
     */
    @Override
    protected List<Producto> readAll() throws IOException {
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        DocumentoProductos documento = mapper.readValue(path.toFile(), DocumentoProductos.class);
        List<Producto> productos = documento.getProductos();
        // un <productos/> vacío puede dejar la lista a null
        return productos != null ? productos : new ArrayList<>();
    }

    /** Envuelve la lista en un documento y lo escribe (writeValue = Java → fichero). */
    @Override
    protected void writeAll(List<Producto> productos) throws IOException {
        DocumentoProductos documento = new DocumentoProductos(productos);
        mapper.writeValue(path.toFile(), documento);
    }
}
