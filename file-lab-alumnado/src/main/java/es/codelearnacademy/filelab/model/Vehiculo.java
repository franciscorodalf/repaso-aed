package es.codelearnacademy.filelab.model;

/**
 * Vehículo identificado por su matrícula. Record inmutable, como {@link Producto}.
 *
 * @param matricula identificador único
 * @param marca     marca
 * @param modelo    modelo
 * @param anio      año de fabricación
 */
public record Vehiculo(String matricula, String marca, String modelo, int anio) {
}
