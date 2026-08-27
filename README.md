# Sistema de Gestión de Clientes y Ventas — Librería y Bazar "HAS"

Proyecto académico — Técnicas de Programación Orientada a Objetos
Universidad Privada del Norte | Ingeniería de Sistemas Computacionales | 2026-1

## Descripción

Sistema de escritorio en Java (Swing) para registrar clientes, productos y ventas de una librería y bazar escolar. Implementa los pilares de la POO (abstracción, encapsulamiento, herencia y polimorfismo), manejo de excepciones propias, colecciones y persistencia en SQL Server mediante JDBC con el patrón DAO.

Proyecto vigente: **`S05-Sobrecarga-Errores-Colecciones/EF_POO`** (Maven, paquete raíz `ef_poo`). El proyecto anterior, `SistemaRegistro`, se mantiene en el repositorio como referencia histórica del desarrollo semana a semana, pero ya no es el sistema de entrega.

## Tecnologías

- Java (Maven, `maven.compiler.release=26`)
- SQL Server / SQL Server Express
- JDBC (Microsoft SQL Server JDBC Driver `13.4.0.jre11`, dependencia Maven estándar — está disponible en Maven Central, no requiere `.jar` local)
- Swing (UI), con `.form` de NetBeans para el GUI Builder
- NetBeans IDE

## Requisitos previos

1. JDK compatible con Maven instalado y configurado
2. Maven instalado (`mvn -version` debe responder)
3. SQL Server o SQL Server Express instalado y en ejecución
4. SQL Server Management Studio (SSMS)

## Configuración de la base de datos

### 1. Ejecutar schema.sql

Abrir SSMS, conectarse al servidor local y ejecutar:

```
S05-Sobrecarga-Errores-Colecciones/EF_POO/schema.sql
```

Este script crea la base de datos `LibreriaBazarDB`, sus cuatro tablas (`Cliente`, `Producto`, `Venta`, `DetalleVenta`) y los datos de prueba de las cuatro tablas en un solo paso (no hace falta un `data.sql` aparte).

### 2. Driver JDBC

El driver se declara en `pom.xml` como una dependencia Maven normal:

```xml
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>13.4.0.jre11</version>
</dependency>
```

Maven lo descarga solo desde Maven Central; no hay que copiar ningún `.jar` a mano.

### 3. Conexión JDBC

`src/main/java/ef_poo/persistencia/Conexion.java` se conecta con un login de SQL Server dedicado (no autenticación de Windows), para mantener la configuración simple:

```java
private static final String SERVIDOR = "localhost";
private static final String BASE_DE_DATOS = "LibreriaBazarDB";
private static final String USUARIO = "efpoo";
private static final String CONTRASENA = "Efpoo123!";
```

Para usar este proyecto contra tu propia instancia de SQL Server:
1. Crea un login SQL Server con ese usuario y contraseña (o cambia las constantes en `Conexion.java` por las tuyas).
2. Asegúrate de que el servidor tenga habilitado el modo de autenticación mixto ("SQL Server and Windows Authentication mode").
3. Si tu instancia no es la default (`localhost`), cambia `SERVIDOR` (por ejemplo `"localhost\\SQLEXPRESS"`).

> Nota: el usuario y la contraseña quedan como constantes en el código fuente. Es una simplificación deliberada para un entorno académico local; no se recomienda para un entorno de producción.

## Compilar y ejecutar

```bash
cd S05-Sobrecarga-Errores-Colecciones/EF_POO
mvn compile
mvn exec:java
```

> El `pom.xml` de este proyecto lo gestiona el autor manualmente. Si `mvn exec:java` no encuentra la clase principal, verifica que la propiedad `exec.mainClass` apunte a `ef_poo.ui.SistemaRegistroUI` (la única clase con `main()` en el proyecto).

También puede ejecutarse desde NetBeans: clic derecho en el proyecto → **Run**.

## Estructura del proyecto

```
EF_POO/
├── pom.xml
├── schema.sql                     DDL + datos de prueba (4 tablas)
└── src/main/java/ef_poo/
    ├── ui/
    │   ├── SistemaRegistroUI.java  Aplicacion Swing (main)
    │   └── SistemaRegistroUI.form  Descriptor del GUI Builder de NetBeans
    ├── modelo/
    │   ├── Cliente.java
    │   ├── Producto.java            Clase abstracta
    │   ├── ProductoUtilEscolar.java (-10% en campaña escolar)
    │   ├── ProductoPapeleria.java   (-12% por venta al por mayor)
    │   ├── ProductoBazar.java       (-20% fuera de temporada alta)
    │   ├── Venta.java
    │   └── DetalleVenta.java
    ├── servicio/
    │   ├── ClienteServicio.java
    │   ├── ProductoServicio.java
    │   └── VentaServicio.java
    ├── persistencia/
    │   ├── Conexion.java           Singleton JDBC (login SQL dedicado)
    │   ├── ClienteDAO.java         CRUD Cliente
    │   ├── ProductoDAO.java        CRUD Producto (herencia por tabla única, sin instanceof)
    │   └── VentaDAO.java           Insertar (transaccional) y listar ventas
    └── excepciones/
        ├── ClienteException.java
        ├── ProductoException.java
        └── VentaException.java
```

## Requerimientos implementados

| Módulo | Descripción | Clases principales |
|---|---|---|
| Clientes | Registrar, buscar, actualizar, eliminar; validación de código numérico, nombre y email | `ClienteServicio`, `ClienteDAO`, `ClienteException` |
| Productos | Registrar, buscar, actualizar, eliminar; catálogo con herencia (`UtilEscolar`/`Papeleria`/`Bazar`) y precio final calculado polimórficamente | `ProductoServicio`, `ProductoDAO`, `Producto` y subtipos |
| Ventas | Registro transaccional con detalle, cálculo automático de subtotales y total, consulta por código y por cliente (INNER JOIN) | `VentaServicio`, `VentaDAO`, `Venta`, `DetalleVenta` |
| Interfaz | Navegación por pestañas (Clientes, Productos, Registrar Venta, Consultar Ventas), validación de campos y mensajes de error/confirmación | `SistemaRegistroUI` |

El detalle completo de requerimientos funcionales y no funcionales (RF-01 a RF-40, RNF-01 a RNF-06) está en `Informe-Final-T3/Informe-Final-EF.md`.

## Diagrama de clases (resumen)

```
Producto (abstract)
  ├── ProductoUtilEscolar  (-10% en campaña escolar)
  ├── ProductoPapeleria    (-12% por venta al por mayor)
  └── ProductoBazar        (-20% fuera de temporada alta)

Venta ◆── DetalleVenta ──→ Producto
Venta ──→ Cliente

ClienteServicio ──→ ClienteDAO ──→ SQL Server
ProductoServicio ──→ ProductoDAO ──→ SQL Server
VentaServicio ──→ VentaDAO ──→ SQL Server
                Conexion (singleton JDBC)
```

El diagrama de clases completo (con servicios, DAOs y excepciones) está en `Informe-Final-T3/diagrama-clases-actualizado.puml`.

## Documentación

El informe final del proyecto, con el análisis de negocio, los requerimientos, el diseño OO y la integración con SQL Server, está en:

```
Informe-Final-T3/Informe-Final-EF.md
```
