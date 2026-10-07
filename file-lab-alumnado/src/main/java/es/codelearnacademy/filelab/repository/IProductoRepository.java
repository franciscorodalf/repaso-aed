package es.codelearnacademy.filelab.repository;

import es.codelearnacademy.filelab.model.Producto;

/**
 * Repositorio de productos: fija los genéricos de {@link IRepository} a
 * {@code Producto} con identificador {@code Long}. Permite escribir código que
 * acepte "cualquier repositorio de productos" (CSV, JSON, XML o memoria).
 */
public interface IProductoRepository extends IRepository<Producto, Long> {
}
