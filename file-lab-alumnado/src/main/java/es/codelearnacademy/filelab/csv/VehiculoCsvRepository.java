package es.codelearnacademy.filelab.csv;

import es.codelearnacademy.filelab.model.Vehiculo;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IVehiculoRepository;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 * Repositorio de vehículos en CSV. Igual que {@link ProductoCsvRepository} pero con
 * otra entidad: demuestra que {@link AbstractFileRepository} es genérico
 * (aquí {@code T = Vehiculo}, {@code ID = String}).
 */
public class VehiculoCsvRepository
        extends AbstractFileRepository<Vehiculo, String>
        implements IVehiculoRepository {

    private final Path path;

    /** Lectura: la primera línea es la cabecera y no se trata como dato. */
    private final CSVFormat inputFormat = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .get();

    /** Escritura: cabecera fija. */
    private final CSVFormat outputFormat = CSVFormat.DEFAULT.builder()
            .setHeader("matricula", "marca", "modelo", "anio")
            .get();

    /**
     * @param path fichero CSV
     */
    public VehiculoCsvRepository(Path path) {
        this.path = path;
    }

    /** El identificador de un vehículo es su matrícula. */
    @Override
    protected String getId(Vehiculo vehiculo) {
        return vehiculo.matricula();
    }

    /** Lee el CSV; si no existe devuelve una lista vacía. */
    @Override
    protected List<Vehiculo> readAll() throws IOException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        if (!Files.exists(path)) {
            return vehiculos;
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
             CSVParser parser = inputFormat.parse(reader)) {
            for (CSVRecord fila : parser) {
                vehiculos.add(new Vehiculo(
                        fila.get("matricula"),
                        fila.get("marca"),
                        fila.get("modelo"),
                        Integer.parseInt(fila.get("anio"))));
            }
        }
        return vehiculos;
    }

    /** Reescribe el CSV completo con cabecera. */
    @Override
    protected void writeAll(List<Vehiculo> vehiculos) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, outputFormat)) {
            for (Vehiculo vehiculo : vehiculos) {
                printer.printRecord(vehiculo.matricula(), vehiculo.marca(), vehiculo.modelo(), vehiculo.anio());
            }
        }
    }
}
