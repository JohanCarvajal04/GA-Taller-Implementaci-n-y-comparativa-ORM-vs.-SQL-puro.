package ec.edu.uteq.taller.controlador;

import java.math.BigDecimal;

/**
 * Cuerpo esperado en POST /api/productos. Se separa de la entidad
 * Producto para no exponer el mapeo JPA directamente en la API REST.
 */
public record ProductoRequest(String nombre, BigDecimal precio, Integer stock) {
}
