package es.codelearnacademy.filelab.validation;

import es.codelearnacademy.filelab.model.Producto;

/**
 * Reglas de validez de un {@link Producto}.
 *
 * <p>Clase de utilidad: {@code final} (no se hereda) y constructor privado (no se
 * instancia); se usa con {@code ProductoValidator.validar(producto)}.</p>
 */
public final class ProductoValidator {

    private ProductoValidator() {
    }

    /**
     * Comprueba que el producto es válido; si no lo es lanza una excepción.
     *
     * <p>Un {@code if} por regla para que el mensaje diga exactamente qué falla.
     * El orden importa: primero se comprueba que no es {@code null}, porque si no
     * {@code producto.id()} lanzaría NullPointerException. Lo mismo con
     * {@code nombre() == null} antes de {@code isBlank()}.</p>
     *
     * @param producto producto a validar
     * @throws IllegalArgumentException si es nulo, id &lt;= 0, nombre nulo o en blanco,
     *                                  precio negativo o stock negativo
     */
    public static void validar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (producto.id() <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor que 0");
        }
        if (producto.nombre() == null || producto.nombre().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (producto.precio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (producto.stock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}
