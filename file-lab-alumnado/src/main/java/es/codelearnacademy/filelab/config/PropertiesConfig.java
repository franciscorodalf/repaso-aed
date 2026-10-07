package es.codelearnacademy.filelab.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Acceso a un fichero de configuración {@code .properties} (pares clave=valor).
 *
 * <p>Cada operación vuelve a leer el fichero del disco, así siempre se trabaja con
 * su contenido actual. Ningún método público lanza {@link IOException}: los errores
 * se traducen en {@link Optional#empty()}, un mapa vacío o {@code false}.</p>
 */
public class PropertiesConfig {

    /** Ruta del fichero .properties con el que trabaja esta instancia. */
    private final Path path;

    /**
     * Crea el acceso a la configuración. No lee ni crea el fichero todavía.
     *
     * @param path ruta del fichero .properties
     */
    public PropertiesConfig(Path path) {
        this.path = path;
    }

    /**
     * Obtiene el valor de una propiedad.
     *
     * <p>Se usa {@code Optional.ofNullable} porque {@code getProperty} devuelve
     * {@code null} cuando la clave no existe; {@code Optional.of(null)} lanzaría
     * NullPointerException.</p>
     *
     * @param key clave buscada
     * @return el valor, o {@link Optional#empty()} si no existe o no se puede leer el fichero
     */
    public Optional<String> get(String key) {
        try {
            Properties properties = cargar();
            return Optional.ofNullable(properties.getProperty(key));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Obtiene el valor de una propiedad o un valor por defecto.
     *
     * <p>Reutiliza {@link #get(String)} y {@code orElse} saca el valor del Optional
     * o devuelve el valor por defecto si está vacío.</p>
     *
     * @param key          clave buscada
     * @param defaultValue valor que se devuelve si la clave no existe
     * @return el valor de la propiedad o {@code defaultValue}
     */
    public String getOrDefault(String key, String defaultValue) {
        return get(key).orElse(defaultValue);
    }

    /**
     * Devuelve todas las propiedades del fichero.
     *
     * <p>Se copian a un {@code Map<String, String>} porque {@link Properties} hereda de
     * {@code Hashtable<Object, Object>}; {@code stringPropertyNames()} da las claves
     * como String.</p>
     *
     * @return mapa clave-valor; {@code Map.of()} si el fichero no existe o falla la lectura
     */
    public Map<String, String> findAll() {
        try {
            Properties properties = cargar();
            Map<String, String> resultado = new HashMap<>();
            for (String clave : properties.stringPropertyNames()) {
                resultado.put(clave, properties.getProperty(clave));
            }
            return resultado;
        } catch (IOException e) {
            return Map.of();
        }
    }

    /**
     * Añade o sustituye una propiedad y guarda el fichero completo.
     *
     * <p>Patrón leer-modificar-escribir: se cargan las propiedades existentes para no
     * perderlas, se cambia una y se reescribe todo. Funciona aunque el fichero no exista
     * (se crea al guardar).</p>
     *
     * @param key   clave de la propiedad
     * @param value nuevo valor
     * @return {@code true} si se ha guardado; {@code false} si hay un error de E/S
     */
    public boolean put(String key, String value) {
        try {
            Properties properties = cargar();
            properties.setProperty(key, value);
            guardar(properties);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Elimina una propiedad y guarda el cambio.
     *
     * @param key clave que se quiere eliminar
     * @return {@code true} si la operación se completa (aunque la clave no existiera);
     *         {@code false} si hay un error de E/S
     */
    public boolean remove(String key) {
        try {
            Properties properties = cargar();
            properties.remove(key);
            guardar(properties);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Lee el fichero y devuelve sus propiedades. Si el fichero no existe devuelve un
     * {@link Properties} vacío en vez de fallar: así {@code put} puede crear el fichero
     * desde cero.
     *
     * @return propiedades leídas (posiblemente vacías)
     * @throws IOException si el fichero existe pero no puede leerse
     */
    private Properties cargar() throws IOException {
        Properties properties = new Properties();
        if (Files.exists(path)) {
            // try-with-resources: cierra el Reader aunque haya excepción
            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                properties.load(reader);
            }
        }
        return properties;
    }

    /**
     * Escribe todas las propiedades en el fichero (lo crea o lo sobrescribe).
     *
     * @param properties propiedades que se guardan
     * @throws IOException si no puede escribirse
     */
    private void guardar(Properties properties) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            // El segundo argumento es un comentario que se escribe como "#..." al principio
            properties.store(writer, null);
        }
    }
}
