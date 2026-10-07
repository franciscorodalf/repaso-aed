package es.codelearnacademy.filelab.service;

import es.codelearnacademy.filelab.config.PropertiesConfig;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Ejecuta una conversión cuyos datos (formatos y rutas) vienen de un fichero .properties.
 *
 * <p>Claves esperadas: {@code input.format}, {@code input.file}, {@code output.format}
 * y {@code output.file}.</p>
 */
public class ConfiguredDataBridge {

    private final PropertiesConfig config;
    private final DataBridgeService bridge;

    /**
     * @param config configuración de la que se leen formatos y rutas
     * @param bridge servicio que realiza la conversión
     */
    public ConfiguredDataBridge(PropertiesConfig config, DataBridgeService bridge) {
        this.config = config;
        this.bridge = bridge;
    }

    /**
     * Lee la configuración y lanza la conversión.
     *
     * <p>Si falta alguna clave o un formato no es válido devuelve 0 (ningún producto
     * convertido) en vez de lanzar una excepción, igual que hace el resto de la API.</p>
     *
     * @return número de productos convertidos, o 0 si la configuración es incompleta o incorrecta
     */
    public int execute() {
        // 1-4. Leer los cuatro valores de la configuración
        Optional<String> formatoEntrada = config.get("input.format");
        Optional<String> archivoEntrada = config.get("input.file");
        Optional<String> formatoSalida = config.get("output.format");
        Optional<String> archivoSalida = config.get("output.file");

        // Si falta cualquiera no se puede convertir
        if (formatoEntrada.isEmpty() || archivoEntrada.isEmpty()
                || formatoSalida.isEmpty() || archivoSalida.isEmpty()) {
            return 0;
        }

        try {
            // 5. Texto -> enum (puede lanzar IllegalArgumentException si el formato no existe)
            FileFormat origenFormato = FileFormat.from(formatoEntrada.get());
            FileFormat destinoFormato = FileFormat.from(formatoSalida.get());
            // 6. Texto -> Path
            Path origen = Path.of(archivoEntrada.get());
            Path destino = Path.of(archivoSalida.get());
            // 7-8. Convertir y devolver cuántos productos se han convertido
            return bridge.convert(origenFormato, origen, destinoFormato, destino);
        } catch (IllegalArgumentException e) {
            // InvalidPathException también hereda de IllegalArgumentException
            return 0;
        }
    }
}
