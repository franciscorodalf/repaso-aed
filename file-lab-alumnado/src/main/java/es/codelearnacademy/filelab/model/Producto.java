package es.codelearnacademy.filelab.model;

/**
 * Producto del inventario.
 *
 * <p>Es un {@code record}: clase inmutable cuyo constructor, getters ({@code id()},
 * {@code nombre()}...), {@code equals}, {@code hashCode} y {@code toString} genera Java.
 * Jackson sabe leer y escribir records directamente.</p>
 *
 * @param id     identificador único (mayor que 0)
 * @param nombre nombre del producto
 * @param precio precio unitario (no negativo)
 * @param stock  unidades disponibles (no negativo)
 */
public record Producto(long id, String nombre, double precio, int stock) {
}
