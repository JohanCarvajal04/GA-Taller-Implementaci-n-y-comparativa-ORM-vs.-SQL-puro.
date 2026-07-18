package ec.edu.uteq.taller.controlador;

import ec.edu.uteq.taller.modelo.Producto;
import ec.edu.uteq.taller.servicio.ProductoNoEncontradoException;
import ec.edu.uteq.taller.servicio.ProductoService;
import ec.edu.uteq.taller.servicio.ResultadoListado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService servicio;

    public ProductoController(ProductoService servicio) {
        this.servicio = servicio;
    }

    // GET /api/productos  -> listar()
    @GetMapping
    public List<Producto> listar() {
        return servicio.listar();
    }

    // GET /api/productos/medicion -> listar() + tiempo en ms, para la
    // tabla comparativa del Paso 8 (fila 2)
    @GetMapping("/medicion")
    public Map<String, Object> listarConMedicion() {
        ResultadoListado resultado = servicio.listarConMedicion();
        return Map.of(
                "totalFilas", resultado.productos().size(),
                "tiempoMs", resultado.tiempoMs(),
                "productos", resultado.productos()
        );
    }

    // GET /api/productos/{id}
    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) {
        return servicio.obtenerPorId(id);
    }

    // GET /api/productos/buscar?nombre=...  -> demo de la seccion 5:
    // prueba a pasar aqui el ataque ' OR '1'='1 y compara el resultado
    // con el de la version JDBC concatenada (ProductoRepositorioInseguro).
    @GetMapping("/buscar")
    public List<Producto> buscarPorNombre(@RequestParam String nombre) {
        return servicio.buscarPorNombre(nombre);
    }

    // POST /api/productos  -> crear(Producto p)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody ProductoRequest request) {
        return servicio.crear(request.nombre(), request.precio(), request.stock());
    }

    // DELETE /api/productos/{id}  -> eliminar(long id)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarNoEncontrado(
            ProductoNoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
}
