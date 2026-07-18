# Taller GA — JDBC puro con PreparedStatement

Proyecto Maven generado a partir de la guía `GuiaTaller_JDBC.pdf` (UTEQ,
Aplicaciones Web, PPA 2026–2027). Contiene el CRUD completo, la demo de
inyección SQL y las mediciones de tiempo descritas en los pasos 8 a 17
de la guía.

## Estructura

```
taller-jdbc-puro/
├── pom.xml
└── src/main/java/ec/edu/uteq/taller/
    ├── Producto.java                    (Paso 8 — modelo POJO / record)
    ├── Conexion.java                    (Paso 9 — utilidad de conexión JDBC)
    ├── ProductoRepositorioJdbc.java     (Pasos 10 y 12 — CRUD seguro + búsqueda parametrizada)
    ├── ProductoRepositorioInseguro.java (Paso 11 — versión vulnerable, solo demo)
    └── Main.java                       (Paso 16 — demo SQLi + mediciones + CRUD)
```

## Requisitos previos (los de la guía, sección 2)

1. PostgreSQL 16 corriendo en `localhost:5432`.
2. Base `taller_db` y rol `taller` / contraseña `taller` (Paso 2).
3. Tabla `productos` con 100 filas sembradas (Paso 3).
4. Java 21 y Maven instalados.

## Cómo compilar y ejecutar

Puedes abrir la carpeta directamente en IntelliJ IDEA (File → Open →
selecciona `pom.xml`) y dejar que recargue las dependencias, tal como
indica el Paso 6 de la guía. O desde línea de comandos, una vez tengas
Maven y la base de datos arriba:

```bash
mvn compile
mvn exec:java -Dexec.mainClass="ec.edu.uteq.taller.Main"
```

(Añadí el plugin `exec-maven-plugin` al `pom.xml` para poder correrlo
así desde terminal; si solo usas IntelliJ no lo necesitas, basta con
pulsar el triángulo verde sobre `main` en `Main.java`.)

## Nota importante sobre este entorno

Generé y revisé este código directamente desde el contenido de tu PDF,
pero **no pude compilarlo ni ejecutarlo aquí**: esta sandbox no tiene
acceso a Maven Central (para bajar `postgresql-42.7.4.jar` y
`spring-core-6.1.14.jar`) ni una instancia de PostgreSQL corriendo. Vas
a necesitar compilarlo y probarlo tú en tu máquina con IntelliJ, siguiendo
los pasos 5 al 17 de la guía. Si al ejecutarlo te sale algún error,
revisa primero el Anexo 10 de la guía (errores comunes) — ahí están
cubiertos los casos típicos (conexión rechazada, driver no encontrado,
credenciales, etc.).

## Sobre la parte Spring Data JPA (Paso 13 y sección 9)

La guía deja esa parte a tu cargo. El fragmento mínimo que necesitas
para la tabla comparativa es:

```java
public interface ProductoRepository
        extends JpaRepository<Producto, Long> {

    List<Producto> findByNombre(String nombre); // parametrizado por Hibernate
}
```

Si quieres, puedo ayudarte a armar también ese segundo módulo Maven con
Spring Boot + Spring Data JPA (entidad `@Entity`, repositorio, y un
`Main`/test con `StopWatch` para medir `findAll()`) — solo dime y lo
preparo igual que este.
