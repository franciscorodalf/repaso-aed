package es.codelearnacademy.filelab.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Lectura y escritura de ficheros de texto en UTF-8.
 *
 * <p>Siempre se indica {@link StandardCharsets#UTF_8} para que tildes y eñes se lean
 * igual en cualquier sistema operativo, sin depender de su codificación por defecto.</p>
 *
 * <p>Sin opciones, {@code Files.writeString}/{@code Files.write} usan
 * {@code CREATE, TRUNCATE_EXISTING, WRITE}: crean el fichero si no existe y, si existe,
 * borran su contenido anterior.</p>
 */
public class TextFileService {

    /**
     * Escribe un texto en el fichero, sustituyendo lo que hubiera.
     *
     * @param path      fichero
     * @param contenido texto a escribir
     * @return {@code true} si se escribió; {@code false} si hubo error de E/S
     * @throws NullPointerException si algún argumento es nulo
     */
    public boolean escribir(Path path, String contenido) {
        if (path == null || contenido == null) {
            throw new NullPointerException("El path y el contenido no pueden ser nulos");
        }
        try {
            Files.writeString(path, contenido, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Lee el fichero completo como un único texto.
     *
     * @param path fichero
     * @return contenido, o "" si no existe o no se puede leer
     * @throws NullPointerException si {@code path} es nulo
     */
    public String leer(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Escribe una línea por cada elemento de la lista (añade el salto de línea).
     *
     * @param path   fichero
     * @param lineas líneas a escribir
     * @return {@code true} si se escribió; {@code false} si hubo error de E/S
     * @throws NullPointerException si algún argumento es nulo
     */
    public boolean escribirLineas(Path path, List<String> lineas) {
        if (path == null || lineas == null) {
            throw new NullPointerException("El path y las líneas no pueden ser nulos");
        }
        try {
            Files.write(path, lineas, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Lee el fichero y devuelve cada línea como un elemento de la lista.
     *
     * @param path fichero
     * @return líneas leídas, o {@code List.of()} (vacía) si no se puede leer
     * @throws NullPointerException si {@code path} es nulo
     */
    public List<String> leerLineas(Path path) {
        if (path == null) {
            throw new NullPointerException("El path no puede ser nulo");
        }
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return List.of();
        }
    }

    /**
     * Añade texto al final del fichero sin borrar lo anterior.
     *
     * <p>Solo con {@code APPEND} el fichero debe existir; si se quisiera crear cuando no
     * existe habría que añadir también {@code StandardOpenOption.CREATE}.</p>
     *
     * @param path      fichero
     * @param contenido texto a añadir
     * @return {@code true} si se añadió; {@code false} si hubo error de E/S
     * @throws NullPointerException si algún argumento es nulo
     */
    public boolean anexar(Path path, String contenido) {
        if (path == null || contenido == null) {
            throw new NullPointerException("El path y el contenido no pueden ser nulos");
        }
        try {
            Files.writeString(path, contenido, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
