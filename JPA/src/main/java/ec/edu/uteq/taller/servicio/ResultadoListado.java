package ec.edu.uteq.taller.servicio;

import ec.edu.uteq.taller.modelo.Producto;

import java.util.List;

/**
 * Envuelve el resultado de un listado junto con cuanto tardo, para
 * poder reportar el mismo dato que mide el modulo JDBC (Paso 14/15 de
 * la guia) sin repetir la logica de medicion en cada lugar que la
 * necesite (consola y REST).
 */
public record ResultadoListado(List<Producto> productos, double tiempoMs) {
}
