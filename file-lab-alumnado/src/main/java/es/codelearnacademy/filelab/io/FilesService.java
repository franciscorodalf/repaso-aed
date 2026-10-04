package es.codelearnacademy.filelab.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.OptionalLong;

public class FilesService {

    public boolean existe(Path path) {
        if (path == null) {
            throw new NullPointerException("Path no puede ser nulo");
        }

        return Files.exists(path);
    }

    public Optional<Path> crearDirectorio(Path path) {
        if (path == null) {
            throw new NullPointerException("Path no puede ser nulo");
        }
        try {
            Path directorio = Files.createDirectory(path);
            return Optional.of(directorio);
        } catch (IOException e) {
            return Optional.empty();
        }

    }

    public Optional<Path> crearDirectorios(Path path) {
        if (path == null) {
            throw new NullPointerException("Path no puede ser nulo");
        }

        try {
            Path directorio = Files.createDirectories(path);
            return Optional.of(directorio);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<Path> crearArchivo(Path path) {
        if (path == null) {
            throw new NullPointerException("Path no puede ser nulo");
        }

        try {
            Path archivoCreado = Files.createFile(path);
            return Optional.of(archivoCreado);
        } catch (IOException e) {
            return Optional.empty();
        }

    }

    public Optional<Path> copiar(Path origen, Path destino) {
        if (origen == null || destino == null) {
            throw new NullPointerException("Origen o Destino no pueden ser nulos");
        }

        try {
            Path archivoCopiado = Files.copy(origen, destino);
            return Optional.of(archivoCopiado);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<Path> mover(Path origen, Path destino) {
        if (origen == null || destino == null) {
            throw new NullPointerException("Origen o Destino no pueden ser nulos");
        }

        try {
            Path archivoMovido = Files.move(origen, destino);
            return Optional.of(archivoMovido);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public boolean eliminar(Path path) {
        if (path == null) {
            throw new NullPointerException("Path no puede ser nulo");
        }

        try {
            Files.delete(path);
          return true;
        } catch (IOException e) {
            return false;
        }
    }

    public OptionalLong tamanio(Path path) {
        if (path == null) {
            throw new NullPointerException("Path no puede ser nulo");
        }

        try {
            long tamanioArchivo = Files.size(path);
            return OptionalLong.of(tamanioArchivo);
        } catch (IOException e) {
            return OptionalLong.empty();
        }

    }
}
