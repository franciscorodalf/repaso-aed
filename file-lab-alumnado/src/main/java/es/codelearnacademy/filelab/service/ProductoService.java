package es.codelearnacademy.filelab.service;

import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.IProductoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoService {

    private final IProductoRepository repository;

    public ProductoService(IProductoRepository repository) {
        this.repository = repository;
    }

    public Optional<Producto> maximoPrecio() {

        List<Producto> productos = repository.findAll();
        Producto masCaro = null;
        for (Producto producto : productos) {
            if (masCaro == null || producto.precio() > masCaro.precio()) {
                masCaro = producto;
            }
        }
        return Optional.ofNullable(masCaro);
    }

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

    public int stockTotal() {
        List<Producto> productos = repository.findAll();
        int totalStock = 0;
        for (Producto producto : productos) {
            totalStock += producto.stock();
        }
        return totalStock;
    }

    public double valorInventario() {
        List<Producto> productos = repository.findAll();
        double totalInventario = 0;
        double calculo = 0;
        for (Producto producto : productos) {
            calculo = producto.precio() * producto.stock();
            totalInventario += calculo;
        }
        return totalInventario;
    }

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

    public List<Producto> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El texto no puede ser nulo o vacio");
        }
       String textoBuscar =  texto.toLowerCase();
        List<Producto> productos = repository.findAll();
        List<Producto> productoBuscar = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.nombre().toLowerCase().contains(textoBuscar)) {
                productoBuscar.add(producto);
            }
        }
        return productoBuscar;

    }
}
