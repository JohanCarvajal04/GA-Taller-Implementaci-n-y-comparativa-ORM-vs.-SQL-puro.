package ec.edu.uteq.taller.config;

import ec.edu.uteq.taller.modelo.Producto;
import ec.edu.uteq.taller.servicio.ProductoService;
import ec.edu.uteq.taller.servicio.ResultadoListado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.util.List;

/**
 * Reproduce en consola, al arrancar la aplicacion, la misma secuencia
 * de demo/medicion que el Main.java del modulo taller-jdbc-puro
 * (Paso 16 de la guia), pero pasando siempre por la capa de servicio
 * en lugar de llamar al repositorio directamente. Se puede desactivar
 * con la propiedad 'taller.demo.enabled=false' en application.properties
 * si solo quieres usar la API REST (por ejemplo, para probarla con
 * Postman) sin que se ejecute esta demo en cada arranque.
 */
@Component
public class DemoArranque implements CommandLineRunner {

    private final ProductoService servicio;
    private final boolean demoHabilitada;

    public DemoArranque(ProductoService servicio,
                         @Value("${taller.demo.enabled:true}") boolean demoHabilitada) {
        this.servicio = servicio;
        this.demoHabilitada = demoHabilitada;
    }

    @Override
    public void run(String... args) {
        if (!demoHabilitada) {
            return;
        }

        // ------------------------------------------------------------
        // 1) Intento de inyeccion SQL sobre la consulta derivada
        //    findByNombre(...). Hibernate siempre la traduce a un
        //    PreparedStatement parametrizado.
        // ------------------------------------------------------------
        String ataque = "' OR '1'='1";
        System.out.println("=== SPRING DATA JPA (Hibernate) ===");
        List<Producto> filas = servicio.buscarPorNombre(ataque);
        System.out.println("Filas devueltas al atacante: "
                + filas.size()
                + " (correcto: 0 — Hibernate parametriza siempre)");

        // ------------------------------------------------------------
        // 2) Medicion de listar() (findAll) con System.nanoTime(),
        //    ya envuelta por el servicio en ResultadoListado
        // ------------------------------------------------------------
        ResultadoListado resultado = servicio.listarConMedicion();
        System.out.printf("nanoTime : %d filas en %.3f ms %n",
                resultado.productos().size(), resultado.tiempoMs());

        // ------------------------------------------------------------
        // 3) Medicion de listar() con Spring StopWatch
        // ------------------------------------------------------------
        StopWatch sw = new StopWatch("listar-jpa");
        sw.start("SELECT * FROM productos (Hibernate)");
        List<Producto> lista2 = servicio.listar();
        sw.stop();
        System.out.printf("StopWatch: %d filas en %.3f ms %n",
                lista2.size(),
                sw.getTotalTimeNanos() / 1_000_000.0);
        System.out.println(sw.prettyPrint());

        // ------------------------------------------------------------
        // 4) Crear un producto nuevo a traves del servicio
        // ------------------------------------------------------------
        Producto guardado = servicio.crear(
                "Producto de prueba", new BigDecimal("99.99"), 5);
        System.out.println("Creado con id = " + guardado.getId());

        // ------------------------------------------------------------
        // 5) Eliminarlo para dejar la base como estaba
        // ------------------------------------------------------------
        servicio.eliminar(guardado.getId());
        System.out.println("Eliminado: true");
    }
}
