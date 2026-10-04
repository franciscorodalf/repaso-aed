package es.codelearnacademy.filelab.io;

import java.nio.file.Path;

public class PathService {
          
    
    public Path crear(String primero, String... partes) {
        if (primero == null) {
            throw new NullPointerException("Los parametros no pueden ser nulo");
        }

        return Path.of(primero, partes);

    }

    /**
     * 
     * 
     */
    public String nombre(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }

        Path nombreArchivo = path.getFileName();
        if (nombreArchivo == null) {
            throw new IllegalArgumentException("El nombre del path no puede ser nulo");

        }

        return nombreArchivo.toString();
    }

    /**
     * 
     */
    public Path padre(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }

        return path.getParent();
    }

    /**
     * 
     */
    public Path absoluto(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }

        return path.toAbsolutePath();
    }

    public Path normalizar(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }

        return path.normalize();
    }

    /**
     * 
     */
    public boolean esAbsoluto(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }

        return path.isAbsolute();
    }

    public Path resolver(Path base, String otro) {
        if (base == null || otro == null) {
            throw new NullPointerException("Los parametros no pueden ser nulos");
        }

        return base.resolve(otro);
    }

    /**
     * 
     */
    public Path relativizar(Path base, Path destino) {
        if (base == null || destino == null) {
            throw new NullPointerException("Los parametros no pueden ser nulos");
        }

        return base.relativize(destino);
    }

    /**
     * 
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
