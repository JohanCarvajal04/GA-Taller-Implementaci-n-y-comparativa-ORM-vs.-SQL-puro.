package ec.edu.uteq.taller.servicio;

import ec.edu.uteq.taller.modelo.Producto;
import ec.edu.uteq.taller.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Capa de servicio: aqui va la logica de negocio (la que hubiera, en
 * este taller es minima) y se apoya solo en el repositorio. El
 * controlador nunca llama al repositorio directamente.
 */
@Service
public class ProductoService {

    private final ProductoRepository repositorio;

    public ProductoService(ProductoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Producto> listar() {
        return repositorio.findAll();
    }

    /**
     * Igual que listar(), pero además mide cuanto tarda con
     * System.nanoTime(), tal como se pide en el Paso 14 de la guia
     * para el modulo JDBC. Sirve para llenar la fila (2) de la tabla
     * comparativa.
     */
    public ResultadoListado listarConMedicion() {
        long inicio = System.nanoTime();
        List<Producto> productos = repositorio.findAll();
        long fin = System.nanoTime();
        double tiempoMs = (fin - inicio) / 1_000_000.0;
        return new ResultadoListado(productos, tiempoMs);
    }

    public Producto obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }

    /**
     * Busqueda por nombre. Al ser una consulta derivada de Spring
     * Data, Hibernate la traduce siempre a un PreparedStatement
     * parametrizado: intentar una inyeccion SQL aqui (por ejemplo
     * pasando "' OR '1'='1") no tiene ningun efecto distinto a buscar
     * ese texto literal, a diferencia de la version concatenada del
     * Paso 11 de la guia.
     */
    public List<Producto> buscarPorNombre(String nombre) {
        return repositorio.findByNombre(nombre);
    }

    public Producto crear(String nombre, BigDecimal precio, Integer stock) {
        Producto nuevo = new Producto(nombre, precio, stock);
        return repositorio.save(nuevo);
    }

    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ProductoNoEncontradoException(id);
        }
        repositorio.deleteById(id);
    }
}
