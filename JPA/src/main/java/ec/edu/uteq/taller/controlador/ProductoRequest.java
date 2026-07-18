package ec.edu.uteq.taller.controlador;

import java.math.BigDecimal;

public record ProductoRequest(String nombre, BigDecimal precio, Integer stock) {
}
