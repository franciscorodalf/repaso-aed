package es.codelearnacademy.filelab.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import es.codelearnacademy.filelab.model.Vehiculo;
import java.util.ArrayList;
import java.util.List;

/**
 * Envoltorio XML para la lista de vehículos: raíz {@code <vehiculos>} y un
 * {@code <vehiculo>} por elemento. Ver {@link DocumentoProductos}.
 */
@JacksonXmlRootElement(localName = "vehiculos")
public class DocumentoVehiculos {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "vehiculo")
    private List<Vehiculo> vehiculos = new ArrayList<>();

    /** Constructor vacío requerido por Jackson. */
    public DocumentoVehiculos() {
    }

    /**
     * @param vehiculos vehículos que contendrá el documento
     */
    public DocumentoVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    /**
     * @return lista de vehículos del documento
     */
    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    /**
     * @param vehiculos lista de vehículos
     */
    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }
}
