# Trabajo de Campo – S10: Control de Requerimientos e Implementación en Java

## DESARROLLO

Esta semana documenta el control de los **15 requerimientos** implementados del
**Sistema de gestión de clientes y ventas**, relacionándolos con sus historias
de usuario, sus criterios de aceptación y los archivos Java donde se realizaron.

### 1. Tabla de control de requerimientos y criterios de aceptación

| Requerimiento                    | Historia de usuario | Criterio de aceptación                                                             | Estado actual |
| -------------------------------- | ------------------- | ---------------------------------------------------------------------------------- | ------------- |
| RF-01 Registrar cliente          | HU-0001             | Dado un cliente con datos válidos, el sistema lo agrega y lo muestra en el listado | Implementado  |
| RF-02 Validar código numérico    | HU-0001             | Si el código contiene caracteres no numéricos, el sistema rechaza el registro      | Implementado  |
| RF-03 Validar código no nulo     | HU-0001             | Si el código es nulo o vacío, el sistema rechaza el registro                       | Implementado  |
| RF-04 Validar email              | HU-0001             | Si el email no contiene "@" y ".", el sistema lo rechaza                           | Implementado  |
| RF-07 Buscar cliente por código  | HU-0001             | Dado un código existente, el sistema retorna el cliente correspondiente            | Implementado  |
| RF-11 Registrar producto         | HU-0002             | Dado un producto válido, el sistema lo agrega al catálogo                          | Implementado  |
| RF-12 Validar nombre de producto | HU-0002             | Si el nombre está vacío, el sistema rechaza el registro                            | Implementado  |
| RF-13 Validar precio             | HU-0002             | Si el precio es cero o negativo, el sistema rechaza el registro                    | Implementado  |
| RF-15 Código de producto único   | HU-0002             | Si el código de producto ya existe, el sistema no permite el duplicado             | Implementado  |
| RF-17 Buscar producto por código | HU-0002             | Dado un código existente, el sistema retorna el producto                           | Implementado  |
| RF-21 Registrar venta            | HU-0003             | Dada una venta con cliente y al menos un detalle, el sistema la registra           | Implementado  |
| RF-24 Agregar detalle a la venta | HU-0003             | El sistema permite agregar productos con su cantidad a la venta                    | Implementado  |
| RF-27 Calcular subtotal          | HU-0003             | Cada detalle calcula su subtotal como precio × cantidad                            | Implementado  |
| RF-28 Calcular total de la venta | HU-0003             | El total de la venta es la suma de los subtotales de sus detalles                  | Implementado  |
| RF-29 Validar venta con detalle  | HU-0003             | Si la venta no tiene al menos un detalle, el sistema la rechaza                    | Implementado  |

---

### 2. Relación implementación Java y requerimientos realizados

| Requerimiento                    | Historia de usuario | Nombre archivo Java     | Estado actual |
| -------------------------------- | ------------------- | ----------------------- | ------------- |
| RF-01 Registrar cliente          | HU-0001             | `ClienteServicio.java`  | Implementado  |
| RF-02 Validar código numérico    | HU-0001             | `ClienteServicio.java`  | Implementado  |
| RF-03 Validar código no nulo     | HU-0001             | `ClienteServicio.java`  | Implementado  |
| RF-04 Validar email              | HU-0001             | `ClienteServicio.java`  | Implementado  |
| RF-07 Buscar cliente por código  | HU-0001             | `ClienteServicio.java`  | Implementado  |
| RF-11 Registrar producto         | HU-0002             | `ProductoServicio.java` | Implementado  |
| RF-12 Validar nombre de producto | HU-0002             | `ProductoServicio.java` | Implementado  |
| RF-13 Validar precio             | HU-0002             | `ProductoServicio.java` | Implementado  |
| RF-15 Código de producto único   | HU-0002             | `ProductoServicio.java` | Implementado  |
| RF-17 Buscar producto por código | HU-0002             | `ProductoServicio.java` | Implementado  |
| RF-21 Registrar venta            | HU-0003             | `VentaServicio.java`    | Implementado  |
| RF-24 Agregar detalle a la venta | HU-0003             | `VentaServicio.java`    | Implementado  |
| RF-27 Calcular subtotal          | HU-0003             | `DetalleVenta.java`     | Implementado  |
| RF-28 Calcular total de la venta | HU-0003             | `Venta.java`            | Implementado  |
| RF-29 Validar venta con detalle  | HU-0003             | `VentaServicio.java`    | Implementado  |

---

### Evidencia de la implementación

Los 15 requerimientos se ejecutan y validan en la clase principal
`SistemaRegistro.java`. La evidencia de ejecución (registro de clientes y
productos, cálculo del total de la venta y manejo de archivos) corresponde a las
capturas registradas en S09.

---

_Universidad Privada del Norte – Facultad de Ingeniería – 2026-1_
