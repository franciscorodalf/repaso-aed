package es.codelearnacademy.filelab.service;

/**
 * Formatos de fichero soportados para guardar productos.
 */
public enum FileFormat {
    CSV,
    JSON,
    XML;

    /**
     * Convierte un texto (por ejemplo leído de un .properties) en un valor del enum,
     * sin distinguir mayúsculas y minúsculas.
     *
     * <p>Se recorre {@code values()} (todos los valores del enum) y se compara con
     * {@code equalsIgnoreCase} usando {@code name()}, que devuelve "CSV", "JSON"...
     * Alternativa equivalente: {@code valueOf(value.trim().toUpperCase())}, que ya lanza
     * IllegalArgumentException si no existe.</p>
     *
     * @param value texto con el formato ("csv", "Json", "XML"...)
     * @return el formato correspondiente
     * @throws IllegalArgumentException si el texto es nulo o no es un formato reconocido
     */
    public static FileFormat from(String value) {
        if (value == null) {
            throw new IllegalArgumentException("El formato no puede ser nulo");
        }
        String texto = value.trim();
        for (FileFormat formato : values()) {
            if (formato.name().equalsIgnoreCase(texto)) {
                return formato;
            }
        }
        throw new IllegalArgumentException("Formato no reconocido: " + value);
    }
}
