package es.codelearnacademy.filelab.io;

import java.nio.file.Path;

/**
 * Operaciones sobre rutas con {@link Path} (API moderna NIO.2).
 *
 * <p>Un {@code Path} es solo una ruta: crearlo o manipularlo NO toca el disco ni
 * comprueba que exista. Todos los métodos lanzan {@link NullPointerException} si
 * reciben argumentos nulos (fallar pronto con un mensaje claro).</p>
 */
public class PathService {

    /**
     * Construye una ruta a partir de una o varias partes.
     *
     * <p>{@code String...} es un parámetro varargs: se pueden pasar 0, 1 o más textos
     * y dentro del método es un array.</p>
     *
     * @param primero primera parte de la ruta
     * @param partes  resto de partes (opcionales)
     * @return la ruta resultante, p. ej. {@code crear("data", "productos.csv")} → data/productos.csv
     * @throws NullPointerException si {@code primero} es nulo
     */
    public Path crear(String primero, String... partes) {
        if (primero == null) {
            throw new NullPointerException("La primera parte de la ruta no puede ser nula");
        }
        return Path.of(primero, partes);
    }

    /**
     * Devuelve el nombre del último elemento de la ruta (el fichero o carpeta final).
     *
     * <p>{@code getFileName()} devuelve {@code null} para la raíz ("/"), por eso se comprueba.</p>
     *
     * @param path ruta
     * @return nombre final como texto, p. ej. "productos.csv"
     * @throws NullPointerException     si {@code path} es nulo
     * @throws IllegalArgumentException si la ruta no tiene nombre (raíz)
     */
    public String nombre(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }
        Path nombreArchivo = path.getFileName();
        if (nombreArchivo == null) {
            throw new IllegalArgumentException("La ruta no tiene nombre de fichero");
        }
        return nombreArchivo.toString();
    }

    /**
     * Devuelve la ruta padre (la carpeta que contiene el elemento).
     *
     * @param path ruta
     * @return ruta padre, o {@code null} si no tiene (p. ej. "productos.csv" sin carpeta)
     * @throws NullPointerException si {@code path} es nulo
     */
    public Path padre(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }
        return path.getParent();
    }

    /**
     * Convierte la ruta en absoluta, anteponiendo el directorio de trabajo actual.
     *
     * @param path ruta (relativa o absoluta)
     * @return ruta absoluta
     * @throws NullPointerException si {@code path} es nulo
     */
    public Path absoluto(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }
        return path.toAbsolutePath();
    }

    /**
     * Elimina los elementos redundantes "." y ".." de la ruta.
     *
     * @param path ruta
     * @return ruta normalizada, p. ej. data/./tmp/../productos.csv → data/productos.csv
     * @throws NullPointerException si {@code path} es nulo
     */
    public Path normalizar(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }
        return path.normalize();
    }

    /**
     * Indica si la ruta es absoluta (empieza por la raíz).
     *
     * @param path ruta
     * @return {@code true} si es absoluta
     * @throws NullPointerException si {@code path} es nulo
     */
    public boolean esAbsoluto(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }
        return path.isAbsolute();
    }

    /**
     * Añade una parte al final de una ruta base.
     *
     * @param base ruta base
     * @param otro parte a añadir
     * @return base + otro, p. ej. resolver(data, "productos.csv") → data/productos.csv
     * @throws NullPointerException si algún argumento es nulo
     */
    public Path resolver(Path base, String otro) {
        if (base == null || otro == null) {
            throw new NullPointerException("La base y la ruta a añadir no pueden ser nulas");
        }
        return base.resolve(otro);
    }

    /**
     * Calcula la ruta relativa para ir desde {@code base} hasta {@code destino}
     * (operación inversa de {@code resolve}).
     *
     * @param base    ruta de partida
     * @param destino ruta de llegada
     * @return ruta relativa, p. ej. de data a data/csv/p.csv → csv/p.csv
     * @throws NullPointerException si algún argumento es nulo
     */
    public Path relativizar(Path base, Path destino) {
        if (base == null || destino == null) {
            throw new NullPointerException("La base y el destino no pueden ser nulos");
        }
        return base.relativize(destino);
    }

    /**
     * Devuelve la extensión del fichero (lo que va después del último punto).
     *
     * <p>Se usa {@code lastIndexOf} para que "copia.tar.gz" devuelva "gz".</p>
     *
     * @param path ruta del fichero
     * @return extensión sin el punto, o "" si no tiene
     * @throws NullPointerException si {@code path} es nulo
     */
    public String extension(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }
        String archivo = nombre(path);
        int punto = archivo.lastIndexOf('.');
        if (punto == -1) {
            return "";
        }
        return archivo.substring(punto + 1);
    }
}
