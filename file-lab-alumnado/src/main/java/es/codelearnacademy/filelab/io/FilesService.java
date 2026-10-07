package es.codelearnacademy.filelab.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * Operaciones sobre el disco con la clase de utilidades {@link Files} (NIO.2).
 *
 * <p>A diferencia de {@code Path}, estos métodos SÍ tocan el disco y por eso los de
 * {@code Files} lanzan {@link IOException} (excepción checked: obliga a try/catch o
 * {@code throws}). Aquí se captura y se traduce a {@code Optional.empty()} o
 * {@code false}, para que quien llama no tenga que tratar excepciones.</p>
 *
 * <p>Se captura {@code IOException} y no {@code Exception}: así solo se tratan los
 * errores de E/S esperados y los errores de programación (p. ej. NullPointerException)
 * no se esconden.</p>
 */
public class FilesService {

    /**
     * @param path ruta
     * @return {@code true} si existe en disco
     * @throws NullPointerException si {@code path} es nulo
     */
    public boolean existe(Path path) {
        comprobarNoNulo(path);
        return Files.exists(path);
    }

    /**
     * Crea un único directorio. Falla si el padre no existe o si ya existe.
     *
     * @param path directorio a crear
     * @return la ruta creada, o vacío si no se pudo crear
     * @throws NullPointerException si {@code path} es nulo
     */
    public Optional<Path> crearDirectorio(Path path) {
        comprobarNoNulo(path);
        try {
            return Optional.of(Files.createDirectory(path));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Crea el directorio y todos los padres que falten (como {@code mkdir -p}).
     * No falla si ya existe.
     *
     * @param path directorio a crear
     * @return la ruta creada, o vacío si no se pudo crear
     * @throws NullPointerException si {@code path} es nulo
     */
    public Optional<Path> crearDirectorios(Path path) {
        comprobarNoNulo(path);
        try {
            return Optional.of(Files.createDirectories(path));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Crea un fichero vacío. Falla si ya existe ({@code FileAlreadyExistsException}).
     *
     * @param path fichero a crear
     * @return la ruta creada, o vacío si no se pudo crear
     * @throws NullPointerException si {@code path} es nulo
     */
    public Optional<Path> crearArchivo(Path path) {
        comprobarNoNulo(path);
        try {
            return Optional.of(Files.createFile(path));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Copia un fichero. Sin opciones, falla si el destino ya existe.
     *
     * @param origen  fichero original
     * @param destino ruta de la copia
     * @return la ruta de destino, o vacío si no se pudo copiar
     * @throws NullPointerException si algún argumento es nulo
     */
    public Optional<Path> copiar(Path origen, Path destino) {
        comprobarNoNulos(origen, destino);
        try {
            return Optional.of(Files.copy(origen, destino));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Mueve o renombra un fichero. Sin opciones, falla si el destino ya existe.
     *
     * @param origen  fichero original
     * @param destino nueva ruta
     * @return la ruta de destino, o vacío si no se pudo mover
     * @throws NullPointerException si algún argumento es nulo
     */
    public Optional<Path> mover(Path origen, Path destino) {
        comprobarNoNulos(origen, destino);
        try {
            return Optional.of(Files.move(origen, destino));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Elimina un fichero o directorio vacío. {@code Files.delete} lanza
     * {@code NoSuchFileException} si no existe (a diferencia de {@code deleteIfExists}).
     *
     * @param path ruta a eliminar
     * @return {@code true} si se eliminó; {@code false} si no existía o no se pudo
     * @throws NullPointerException si {@code path} es nulo
     */
    public boolean eliminar(Path path) {
        comprobarNoNulo(path);
        try {
            Files.delete(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Devuelve el tamaño del fichero en bytes.
     *
     * <p>{@link OptionalLong} es la versión de Optional para el primitivo {@code long}
     * (evita el autoboxing a {@code Long}).</p>
     *
     * @param path fichero
     * @return tamaño en bytes, o vacío si no existe o no se puede leer
     * @throws NullPointerException si {@code path} es nulo
     */
    public OptionalLong tamanio(Path path) {
        comprobarNoNulo(path);
        try {
            return OptionalLong.of(Files.size(path));
        } catch (IOException e) {
            return OptionalLong.empty();
        }
    }

    private void comprobarNoNulo(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }
    }

    private void comprobarNoNulos(Path origen, Path destino) {
        if (origen == null || destino == null) {
            throw new NullPointerException("El origen y el destino no pueden ser nulos");
        }
    }
}
