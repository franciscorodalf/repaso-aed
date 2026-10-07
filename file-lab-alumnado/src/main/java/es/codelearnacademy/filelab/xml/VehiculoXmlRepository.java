package es.codelearnacademy.filelab.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import es.codelearnacademy.filelab.model.Vehiculo;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IVehiculoRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de vehículos en XML (mismo esquema que {@link ProductoXmlRepository}).
 */
public class VehiculoXmlRepository
        extends AbstractFileRepository<Vehiculo, String>
        implements IVehiculoRepository {

    private final Path path;
    private final XmlMapper mapper = new XmlMapper();

    /**
     * @param path fichero XML
     */
    public VehiculoXmlRepository(Path path) {
        this.path = path;
    }

    @Override
    protected String getId(Vehiculo vehiculo) {
        return vehiculo.matricula();
    }

    /** Lee el documento {@code <vehiculos>} y devuelve su lista. */
    @Override
    protected List<Vehiculo> readAll() throws IOException {
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        DocumentoVehiculos documento = mapper.readValue(path.toFile(), DocumentoVehiculos.class);
        List<Vehiculo> vehiculos = documento.getVehiculos();
        return vehiculos != null ? vehiculos : new ArrayList<>();
    }

    /** Envuelve la lista en el documento y lo escribe. */
    @Override
    protected void writeAll(List<Vehiculo> vehiculos) throws IOException {
        DocumentoVehiculos documento = new DocumentoVehiculos(vehiculos);
        mapper.writeValue(path.toFile(), documento);
    }
}
