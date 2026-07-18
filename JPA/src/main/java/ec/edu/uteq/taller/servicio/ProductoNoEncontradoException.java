package ec.edu.uteq.taller.servicio;

/**
 * Se lanza cuando se pide eliminar o consultar un producto cuyo id no
 * existe en la tabla 'productos'. El controlador la traduce a un
 * 404 Not Found.
 */
public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(Long id) {
        super("No existe un producto con id = " + id);
    }
}
