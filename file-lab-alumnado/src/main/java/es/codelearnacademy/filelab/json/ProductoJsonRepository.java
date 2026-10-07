package es.codelearnacademy.filelab.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IProductoRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de productos en JSON con Jackson.
 *
 * <p>El fichero es un array: {@code [ {"id":1,"nombre":"...","precio":..,"stock":..}, ... ]}.
 * {@link ObjectMapper} convierte objetos Java ↔ JSON.</p>
 */
public class ProductoJsonRepository
        extends AbstractFileRepository<Producto, Long>
        implements IProductoRepository {

    private final Path path;
    private final ObjectMapper mapper;

    /**
     * Crea el repositorio con un {@link ObjectMapper} por defecto.
     * {@code this(...)} llama al otro constructor para no repetir código.
     *
     * @param path fichero JSON
     */
    public ProductoJsonRepository(Path path) {
        this(path, new ObjectMapper());
    }

    /**
     * Permite pasar un mapper configurado (p. ej. con salida indentada).
     *
     * @param path   fichero JSON
     * @param mapper mapper de Jackson
     */
    public ProductoJsonRepository(Path path, ObjectMapper mapper) {
        this.path = path;
        this.mapper = mapper;
    }

    @Override
    protected Long getId(Producto producto) {
        return producto.id();
    }

    /**
     * Lee el array JSON a una lista.
     *
     * <p>{@code new TypeReference<List<Producto>>(){}} es necesario por el borrado de
     * tipos (type erasure): en tiempo de ejecución {@code List<Producto>.class} no existe,
     * solo {@code List}. La clase anónima ({@code {}}) conserva el tipo genérico completo
     * para que Jackson cree objetos {@code Producto} y no mapas.</p>
     */
    @Override
    protected List<Producto> readAll() throws IOException {
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        return mapper.readValue(path.toFile(), new TypeReference<List<Producto>>() { });
    }

    /** Serializa la lista completa como array JSON (sobrescribe el fichero). */
    @Override
    protected void writeAll(List<Producto> productos) throws IOException {
        mapper.writeValue(path.toFile(), productos);
    }
}
