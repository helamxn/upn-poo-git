# Sistema de Gestión de Clientes y Ventas

Proyecto académico — Técnicas de Programación Orientada a Objetos  
Universidad Privada del Norte | Ingeniería de Sistemas Computacionales | 2025-1

## Descripción

Sistema de escritorio en Java (Swing) para registrar clientes, productos y ventas de una microempresa. Implementa los conceptos de POO: herencia, polimorfismo, encapsulamiento, manejo de excepciones propias, colecciones y persistencia en base de datos SQL Server mediante JDBC.

## Tecnologías

- Java 21 (JDK 21)
- Maven 3.x
- SQL Server / SQL Server Express
- JDBC (Microsoft SQL Server JDBC Driver 12.4)
- Swing (UI)
- NetBeans IDE

## Requisitos previos

1. JDK 21 instalado y configurado en `JAVA_HOME`
2. Maven instalado (`mvn -version` debe responder)
3. SQL Server o SQL Server Express instalado y en ejecución
4. SQL Server Management Studio (SSMS)

## Configuración de la base de datos

### 1. Crear el schema

Abrir SSMS, conectarse al servidor local y ejecutar:

```
SistemaRegistro/schema.sql
```

Esto crea la base de datos `SistemaRegistroDB` con las tablas `Cliente`, `Producto`, `Venta` y `DetalleVenta`.

### 2. Insertar datos de prueba

Ejecutar en SSMS:

```
SistemaRegistro/data.sql
```

### 3. Configurar la conexión JDBC

Editar el archivo `src/main/java/persistencia/Conexion.java` y ajustar:

```java
private static final String SERVER     = "localhost";        // nombre del servidor
private static final String DATABASE   = "SistemaRegistroDB";
private static final String USUARIO    = "sa";               // usuario SQL Server
private static final String CONTRASENA = "TuContrasena123"; // contraseña
```

> Si usas una instancia nombrada (ej. SQLEXPRESS), cambia SERVER a `"localhost\\SQLEXPRESS"`.  
> Asegúrate de que el usuario `sa` esté habilitado o usa otro login con acceso a `SistemaRegistroDB`.

## Compilar y ejecutar

```bash
cd S05-Sobrecarga-Errores-Colecciones/SistemaRegistro
mvn compile
mvn exec:java
```

O desde NetBeans: clic derecho en el proyecto → **Run**.

## Estructura del proyecto

```
SistemaRegistro/
├── schema.sql                    Scripts de base de datos
├── data.sql
├── pom.xml
└── src/main/java/
    ├── com/mycompany/sistemaregistro/
    │   ├── SistemaRegistro.java  Aplicacion consola
    │   └── SistemaRegistroUI.java Aplicacion Swing (main)
    ├── modelo/
    │   ├── Cliente.java
    │   ├── Producto.java         Clase abstracta
    │   ├── ProductoElectronico.java
    │   ├── ProductoAlimento.java
    │   ├── ProductoRopa.java
    │   ├── Venta.java
    │   └── DetalleVenta.java
    ├── servicio/
    │   ├── ClienteServicio.java
    │   ├── ProductoServicio.java
    │   └── VentaServicio.java
    ├── persistencia/
    │   ├── Conexion.java         Singleton JDBC
    │   ├── ClienteDAO.java       CRUD Cliente
    │   ├── ProductoDAO.java      CRUD Producto (jerarquía)
    │   ├── VentaDAO.java         Insertar y listar ventas
    │   └── ArchivoClientes.java  (legado — reemplazado por DAO)
    └── excepciones/
        ├── ClienteException.java
        ├── ProductoException.java
        └── VentaException.java
```

## Requerimientos implementados

| RF | Descripción | Clase |
|---|---|---|
| RF01 | Registrar cliente | `ClienteServicio`, `ClienteDAO` |
| RF02 | Validar código único | `ClienteServicio` |
| RF03 | Validar formato de email | `ClienteServicio` |
| RF04 | Actualizar cliente | `ClienteServicio`, `ClienteDAO` |
| RF05 | Eliminar cliente | `ClienteServicio`, `ClienteDAO` |
| RF06 | Buscar cliente por código | `ClienteServicio` |
| RF07 | Listar clientes | `ClienteServicio`, `ClienteDAO` |
| RF08 | Registrar producto electrónico | `ProductoServicio`, `ProductoDAO` |
| RF09 | Registrar producto alimento | `ProductoServicio`, `ProductoDAO` |
| RF10 | Registrar producto ropa | `ProductoServicio`, `ProductoDAO` |
| RF11 | Validar precio > 0 | `ProductoServicio` → `ProductoException` |
| RF12 | Calcular precio final electrónico (+10% con garantía) | `ProductoElectronico` |
| RF13 | Calcular precio final alimento (−10% si perecible) | `ProductoAlimento` |
| RF14 | Calcular precio final ropa (−20% fuera de temporada) | `ProductoRopa` |
| RF15 | Actualizar producto | `ProductoServicio`, `ProductoDAO` |
| RF16 | Eliminar producto | `ProductoServicio`, `ProductoDAO` |
| RF17 | Buscar producto por código | `ProductoServicio` |
| RF18 | Listar productos | `ProductoServicio`, `ProductoDAO` |
| RF19 | Registrar venta con cliente válido | `VentaServicio`, `VentaDAO` |
| RF20 | Agregar productos con cantidad a venta | `VentaServicio` |
| RF21 | Calcular subtotal por ítem | `DetalleVenta` |
| RF22 | Calcular total de venta | `Venta` |
| RF23 | Validar venta con al menos un detalle | `VentaServicio` → `VentaException` |
| RF24 | Listar ventas registradas | `VentaServicio` |
| RF25 | Ver detalle de venta seleccionada | `VentaServicio` |
| RF26 | Persistir clientes en SQL Server | `ClienteDAO` |
| RF27 | Persistir productos en SQL Server | `ProductoDAO` |
| RF28 | Persistir ventas y detalles en SQL Server | `VentaDAO` |
| RF29 | Cargar datos desde BD al iniciar | Constructores de servicios |
| RF30 | Transacción al registrar venta (rollback en error) | `VentaDAO.insertar()` |

## Diagrama de clases (resumen)

```
Producto (abstract)
  ├── ProductoElectronico  (+10% si tieneGarantia)
  ├── ProductoAlimento     (−10% si esPerecible)
  └── ProductoRopa         (−20% si !enTemporada)

Venta ◆── DetalleVenta ──→ Producto
Venta ──→ Cliente

ClienteServicio ──→ ClienteDAO ──→ SQL Server
ProductoServicio ──→ ProductoDAO ──→ SQL Server
VentaServicio ──→ VentaDAO ──→ SQL Server
                Conexion (singleton JDBC)
```

## Control de versiones

Repositorio Git con flujo Git Flow:
- `main` — rama de producción con merges por entregable
- `feature/s0X-*` — ramas de desarrollo por semana

Historial: 26+ commits documentados desde S01 hasta S13.
