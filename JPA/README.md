# Taller GA — Spring Data JPA + Hibernate (proyecto Spring Boot completo)

Segundo módulo del taller, para la columna derecha de la tabla
comparativa (Paso 13 y sección 9 de la guía). Apunta a la **misma**
base `taller_db` y la **misma** tabla `productos` con 100 filas que
usa el módulo `taller-jdbc-puro`, para que la comparación sea justa.

A diferencia de la primera versión, este módulo está organizado como
un proyecto Spring Boot típico, en capas: **modelo → repositorio →
servicio → controlador**.

## Estructura

```
taller-jpa/
├── pom.xml
└── src/main/
    ├── java/ec/edu/uteq/taller/
    │   ├── TallerJpaApplication.java        (arranque Spring Boot)
    │   ├── modelo/
    │   │   └── Producto.java                 (entidad @Entity, mapea la tabla productos)
    │   ├── repositorio/
    │   │   └── ProductoRepository.java        (extends JpaRepository<Producto, Long>)
    │   ├── servicio/
    │   │   ├── ProductoService.java            (logica de negocio, unico que usa el repositorio)
    │   │   ├── ProductoNoEncontradoException.java
    │   │   └── ResultadoListado.java           (DTO: lista + tiempo medido en ms)
    │   ├── controlador/
    │   │   ├── ProductoController.java         (API REST, solo llama al servicio)
    │   │   └── ProductoRequest.java             (DTO de entrada para POST)
    │   └── config/
    │       └── DemoArranque.java               (CommandLineRunner opcional: demo SQLi + mediciones en consola)
    └── resources/
        └── application.properties              (datasource contra taller_db)
```

## Requisitos previos

Los mismos que el módulo JDBC: PostgreSQL 16 arriba, base `taller_db`,
rol `taller`/`taller`, tabla `productos` con 100 filas ya sembradas
(pasos 2 y 3 de la guía). **No** vuelvas a crear la tabla desde este
módulo: `spring.jpa.hibernate.ddl-auto=none` está puesto a propósito
para no tocar el esquema ni los datos ya sembrados.

## Cómo compilar y ejecutar

```bash
mvn spring-boot:run
```

O ábrelo en IntelliJ (File → Open → `pom.xml`) y ejecuta
`TallerJpaApplication` con el triángulo verde. Por defecto arranca en
`http://localhost:8080`.

Al arrancar, `DemoArranque` imprime en consola la misma secuencia que
el `Main.java` del módulo JDBC (intento de inyección, medición con
`nanoTime`/`StopWatch`, crear y eliminar), pero pasando por la capa de
servicio. Si solo quieres usar la API REST sin que se ejecute esta
demo cada vez, agrega en `application.properties`:

```properties
taller.demo.enabled=false
```

## Endpoints REST

Los tres del alcance funcional de la guía, más dos de apoyo:

| Método | Ruta | Equivale a |
|---|---|---|
| `GET` | `/api/productos` | `listar()` |
| `GET` | `/api/productos/{id}` | obtener uno |
| `GET` | `/api/productos/medicion` | `listar()` + tiempo en ms (fila 2 de la tabla) |
| `GET` | `/api/productos/buscar?nombre=...` | demo de la sección 5 (parametrizada) |
| `POST` | `/api/productos` | `crear(Producto p)` |
| `DELETE` | `/api/productos/{id}` | `eliminar(long id)` |

Ejemplos con `curl`:

```bash
curl http://localhost:8080/api/productos

curl "http://localhost:8080/api/productos/buscar?nombre=' OR '1'='1"
# devuelve [] — Hibernate parametriza la consulta derivada, no hay inyeccion posible

curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Producto de prueba","precio":99.99,"stock":5}'

curl -X DELETE http://localhost:8080/api/productos/101
```

## Por qué el ataque de inyección no funciona aquí

`findByNombre(String nombre)` es una **consulta derivada** (Spring
Data la genera a partir del nombre del método). Detrás de escena
Hibernate siempre traduce esto a SQL con parámetros enlazados
(`PreparedStatement` con `?`), nunca por concatenación de cadenas. Por
eso `/api/productos/buscar?nombre=' OR '1'='1` devuelve una lista
vacía, igual que la versión segura del módulo JDBC — la diferencia es
que aquí no escribiste ni una línea de SQL para conseguirlo.

## Nota sobre este entorno

Igual que con el módulo JDBC, no pude compilar ni ejecutar esto en la
sandbox: no hay acceso a Maven Central (para
`spring-boot-starter-data-jpa`, `spring-boot-starter-web`,
`spring-boot-starter-parent`, el driver de PostgreSQL, etc.) ni una
base de datos corriendo aquí. El código sigue el patrón estándar en
capas de Spring Boot que ya conoces del curso; pruébalo en tu máquina
con IntelliJ y la base `taller_db` arriba.

## Para completar la tabla comparativa

| Criterio | JDBC puro | Spring Data JPA |
|---|---|---|
| (1) Líneas de código | ~42–46 (repositorio + conexión) | entidad + repositorio + servicio + controlador son más archivos, pero cada uno es muy corto y casi todo boilerplate generable por el IDE (getters/setters, anotaciones); el acceso a datos en sí (`ProductoRepository`) son 3 líneas efectivas |
| (2) Tiempo del listado (100 filas) | tu medición con `nanoTime`/`StopWatch` | tu medición con `nanoTime`/`StopWatch` (usualmente algo más lento en la *primera* llamada por el arranque del contexto de Spring y la inicialización de Hibernate; comparable después del warm-up) |
| (3) Facilidad de mantenimiento | SQL escrito a mano, hay que tocar cada consulta si cambia el esquema | Hibernate genera el SQL; `findByNombre` se escribe sin una sola línea de SQL; la separación en capas facilita testear el servicio sin la base de datos |
| (4) Prevención de SQL Injection | depende de usar bien `PreparedStatement` | garantizada por defecto en las consultas generadas por Spring Data/Hibernate |

Anota tus tiempos reales medidos en tu máquina en la fila (2); los
demás textos ya los puedes usar tal cual para tu entrega.
