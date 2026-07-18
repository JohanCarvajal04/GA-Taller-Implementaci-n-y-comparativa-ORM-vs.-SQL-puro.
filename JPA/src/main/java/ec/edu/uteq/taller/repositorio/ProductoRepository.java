package ec.edu.uteq.taller.repositorio;

import ec.edu.uteq.taller.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JpaRepository ya trae implementados findAll(), save(...),
 * deleteById(...), existsById(...), etc. No escribimos SQL a mano.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Consulta derivada: Spring Data genera el SQL a partir del nombre
    // del metodo, y Hibernate lo ejecuta siempre como PreparedStatement
    // parametrizado (igual de seguro que el Paso 12 de la guia, pero
    // sin escribir una sola linea de SQL).
    List<Producto> findByNombre(String nombre);
}
