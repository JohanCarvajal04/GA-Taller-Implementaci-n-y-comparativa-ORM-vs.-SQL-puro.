package ec.edu.uteq.taller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion. La logica de negocio vive en
 * ec.edu.uteq.taller.servicio, el acceso a datos en
 * ec.edu.uteq.taller.repositorio, la entidad en
 * ec.edu.uteq.taller.modelo y la API REST en
 * ec.edu.uteq.taller.controlador — la estructura de capas tipica de
 * un proyecto Spring Boot.
 */
@SpringBootApplication
public class TallerJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TallerJpaApplication.class, args);
    }
}
