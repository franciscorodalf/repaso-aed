package es.codelearnacademy.filelab.service;

import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.IProductoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Consultas y cálculos de negocio sobre los productos.
 *
 * <p>Recibe un {@link IProductoRepository} por el constructor (inyección de
 * dependencias): no sabe si los datos vienen de CSV, JSON, XML o memoria. Por eso los
 * tests pueden usar un repositorio en memoria.</p>
 */
public class ProductoService {

    private final IProductoRepository repository;

    /**
     * @param repository repositorio del que se leen los productos
     */
    public ProductoService(IProductoRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca el producto más caro.
     *
     * <p>Patrón "máximo": se empieza con {@code null} y se sustituye cuando el actual es
     * mayor. Con {@code masCaro == null ||} el primer producto siempre entra (el
     * {@code ||} no evalúa la segunda parte si la primera es true, así no hay NPE).</p>
     *
     * @return el producto con mayor precio, o vacío si no hay productos
     */
    public Optional<Producto> maximoPrecio() {
        List<Producto> productos = repository.findAll();
        Producto masCaro = null;
        for (Producto producto : productos) {
            if (masCaro == null || producto.precio() > masCaro.precio()) {
                masCaro = producto;
            }
        }
        // ofNullable porque masCaro sigue siendo null si la lista estaba vacía
        return Optional.ofNullable(masCaro);
    }

    /**
     * @return el producto con menor precio, o vacío si no hay productos
     */
    public Optional<Producto> minimoPrecio() {
        List<Producto> productos = repository.findAll();
        Producto masBarato = null;
        for (Producto producto : productos) {
            if (masBarato == null || producto.precio() < masBarato.precio()) {
                masBarato = producto;
            }
        }
        return Optional.ofNullable(masBarato);
    }

    /**
     * @return el producto con más unidades, o vacío si no hay productos
     */
    public Optional<Producto> maximoStock() {
        List<Producto> productos = repository.findAll();
        Producto masStock = null;
        for (Producto producto : productos) {
            if (masStock == null || producto.stock() > masStock.stock()) {
                masStock = producto;
            }
        }
        return Optional.ofNullable(masStock);
    }

    /**
     * @return el producto con menos unidades, o vacío si no hay productos
     */
    public Optional<Producto> minimoStock() {
        List<Producto> productos = repository.findAll();
        Producto menosStock = null;
        for (Producto producto : productos) {
            if (menosStock == null || producto.stock() < menosStock.stock()) {
                menosStock = producto;
            }
        }
        return Optional.ofNullable(menosStock);
    }

    /**
     * Suma el stock de todos los productos (patrón acumulador).
     *
     * @return unidades totales; 0 si no hay productos
     */
    public int stockTotal() {
        List<Producto> productos = repository.findAll();
        int totalStock = 0;
        for (Producto producto : productos) {
            totalStock += producto.stock();
        }
        return totalStock;
    }

    /**
     * Calcula el valor del inventario: suma de precio × stock de cada producto.
     *
     * @return valor total; 0 si no hay productos
     */
    public double valorInventario() {
        List<Producto> productos = repository.findAll();
        double totalInventario = 0;
        for (Producto producto : productos) {
            // variable local al bucle: solo se necesita dentro de cada vuelta
            double valorProducto = producto.precio() * producto.stock();
            totalInventario += valorProducto;
        }
        return totalInventario;
    }

    /**
     * Filtra los productos agotados (patrón filtro: lista nueva + add dentro del if).
     *
     * @return productos con stock 0; lista vacía si no hay ninguno
     */
    public List<Producto> sinStock() {
        List<Producto> productos = repository.findAll();
        List<Producto> productosSinStock = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.stock() == 0) {
                productosSinStock.add(producto);
            }
        }
        return productosSinStock;
    }

    /**
     * Busca productos cuyo nombre contenga el texto, sin distinguir mayúsculas.
     *
     * <p>Se pasan ambos textos a minúsculas antes de {@code contains}. El texto buscado
     * se convierte una sola vez, fuera del bucle.</p>
     *
     * @param texto texto a buscar
     * @return productos coincidentes; lista vacía si ninguno coincide
     * @throws IllegalArgumentException si el texto es nulo o está en blanco
     */
    public List<Producto> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El texto no puede ser nulo o vacío");
        }
        String textoBuscado = texto.toLowerCase();
        List<Producto> productos = repository.findAll();
        List<Producto> encontrados = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.nombre().toLowerCase().contains(textoBuscado)) {
                encontrados.add(producto);
            }
        }
        return encontrados;
    }
}
