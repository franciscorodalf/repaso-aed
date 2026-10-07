package es.codelearnacademy.filelab.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import es.codelearnacademy.filelab.model.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase "envoltorio" que representa el documento XML completo.
 *
 * <p>Un XML necesita un único elemento raíz, así que no se puede escribir una
 * {@code List} directamente como en JSON. Resultado:</p>
 * <pre>
 * &lt;productos&gt;
 *   &lt;producto&gt;&lt;id&gt;1&lt;/id&gt;...&lt;/producto&gt;
 *   &lt;producto&gt;...&lt;/producto&gt;
 * &lt;/productos&gt;
 * </pre>
 */
@JacksonXmlRootElement(localName = "productos") // nombre del elemento raíz
public class DocumentoProductos {

    /**
     * {@code useWrapping = false}: no añade otra etiqueta alrededor de la lista
     * (sin ella saldría &lt;productos&gt;&lt;productos&gt;&lt;producto&gt;...).
     * {@code localName = "producto"}: nombre de cada elemento de la lista.
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "producto")
    private List<Producto> productos = new ArrayList<>();

    /** Constructor vacío: Jackson lo necesita para crear el objeto al leer. */
    public DocumentoProductos() {
    }

    /**
     * @param productos productos que contendrá el documento
     */
    public DocumentoProductos(List<Producto> productos) {
        this.productos = productos;
    }

    /**
     * Getter usado por Jackson al escribir y por el repositorio al leer.
     *
     * @return lista de productos del documento
     */
    public List<Producto> getProductos() {
        return productos;
    }

    /**
     * Setter usado por Jackson al leer.
     *
     * @param productos lista de productos
     */
    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
