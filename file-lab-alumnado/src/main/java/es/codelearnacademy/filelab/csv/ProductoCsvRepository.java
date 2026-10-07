package es.codelearnacademy.filelab.csv;

import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IProductoRepository;
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
 * Repositorio de productos guardados en CSV con Apache Commons CSV.
 *
 * <p>Formato: cabecera {@code id,nombre,precio,stock} y una fila por producto.
 * Solo implementa leer/escribir; el CRUD lo hereda de {@link AbstractFileRepository}.</p>
 */
public class ProductoCsvRepository
        extends AbstractFileRepository<Producto, Long>
        implements IProductoRepository {

    private final Path path;

    /**
     * Formato de lectura: {@code setHeader()} sin argumentos toma los nombres de columna
     * de la primera línea (para poder hacer {@code row.get("id")}) y
     * {@code setSkipHeaderRecord(true)} evita que la cabecera se lea como un producto.
     */
    private final CSVFormat inputFormat = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .get();

    /** Formato de escritura: escribe la cabecera indicada al principio del fichero. */
    private final CSVFormat outputFormat = CSVFormat.DEFAULT.builder()
            .setHeader("id", "nombre", "precio", "stock")
            .get();

    /**
     * @param path fichero CSV (no hace falta que exista todavía)
     */
    public ProductoCsvRepository(Path path) {
        this.path = path;
    }

    /** El id de un producto es su campo {@code id}; autoboxing long → Long. */
    @Override
    protected Long getId(Producto producto) {
        return producto.id();
    }

    /**
     * Lee el CSV. Cada {@link CSVRecord} es una fila; sus valores son texto, así que se
     * convierten con {@code Long.parseLong}, {@code Double.parseDouble} e
     * {@code Integer.parseInt}.
     */
    @Override
    protected List<Producto> readAll() throws IOException {
        List<Producto> productos = new ArrayList<>();
        // Fichero inexistente = repositorio vacío (permite crear el primer producto)
        if (!Files.exists(path)) {
            return productos;
        }
        // try-with-resources con dos recursos: se cierran en orden inverso
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
             CSVParser parser = inputFormat.parse(reader)) {
            for (CSVRecord fila : parser) {
                productos.add(new Producto(
                        Long.parseLong(fila.get("id")),
                        fila.get("nombre"),
                        Double.parseDouble(fila.get("precio")),
                        Integer.parseInt(fila.get("stock"))));
            }
        }
        return productos;
    }

    /**
     * Reescribe el CSV completo. {@code printRecord} escribe una fila y pone comillas
     * si un valor contiene comas.
     */
    @Override
    protected void writeAll(List<Producto> productos) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, outputFormat)) {
            for (Producto producto : productos) {
                printer.printRecord(producto.id(), producto.nombre(), producto.precio(), producto.stock());
            }
        }
    }
}
