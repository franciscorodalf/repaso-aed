package es.codelearnacademy.filelab.io;

import java.io.File;
import java.nio.file.Path;

/**
 * Operaciones con la clase antigua {@link File} (java.io) y conversión entre
 * {@code File} y {@code Path}.
 *
 * <p>{@code File} es la API clásica; se sigue viendo en código antiguo. Sus métodos
 * devuelven {@code boolean}/{@code null} en lugar de lanzar excepciones con detalle,
 * por eso hoy se prefiere {@code Path} + {@code Files}.</p>
 */
public class FileService {

    /**
     * @param file fichero o carpeta
     * @return {@code true} si existe en disco
     * @throws NullPointerException si {@code file} es nulo
     */
    public boolean existe(File file) {
        comprobarNoNulo(file);
        return file.exists();
    }

    /**
     * @param file fichero o carpeta
     * @return {@code true} si existe y es un fichero normal
     * @throws NullPointerException si {@code file} es nulo
     */
    public boolean esArchivo(File file) {
        comprobarNoNulo(file);
        return file.isFile();
    }

    /**
     * @param file fichero o carpeta
     * @return {@code true} si existe y es un directorio
     * @throws NullPointerException si {@code file} es nulo
     */
    public boolean esDirectorio(File file) {
        comprobarNoNulo(file);
        return file.isDirectory();
    }

    /**
     * @param file fichero o carpeta
     * @return último elemento de la ruta, p. ej. "productos.csv"
     * @throws NullPointerException si {@code file} es nulo
     */
    public String nombre(File file) {
        comprobarNoNulo(file);
        return file.getName();
    }

    /**
     * @param file fichero o carpeta
     * @return carpeta padre como {@code File}, o {@code null} si no tiene
     * @throws NullPointerException si {@code file} es nulo
     */
    public File padre(File file) {
        comprobarNoNulo(file);
        return file.getParentFile();
    }

    /**
     * Pasa de la API antigua a la moderna.
     *
     * @param file fichero
     * @return el mismo fichero como {@link Path}
     * @throws NullPointerException si {@code file} es nulo
     */
    public Path convertirAPath(File file) {
        comprobarNoNulo(file);
        return file.toPath();
    }

    /**
     * Pasa de la API moderna a la antigua (útil con librerías que piden un {@code File},
     * como {@code mapper.readValue(File, ...)}).
     *
     * @param path ruta
     * @return la misma ruta como {@link File}
     * @throws NullPointerException si {@code path} es nulo
     */
    public File convertirAFile(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }
        return path.toFile();
    }

    /** Evita repetir la misma comprobación en todos los métodos. */
    private void comprobarNoNulo(File file) {
        if (file == null) {
            throw new NullPointerException("El file no puede ser nulo");
        }
    }
}
