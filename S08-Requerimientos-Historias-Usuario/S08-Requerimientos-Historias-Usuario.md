# Trabajo de Campo – S08: Requerimientos e Historias de Usuario

## DESARROLLO

Este documento continúa la planificación del **Sistema de gestión de clientes y ventas** (S06–S07), definiendo sus requerimientos y las historias de usuario sobre las cuatro entidades del proyecto: `Cliente`, `Producto`, `Venta` y `DetalleVenta`.

### 1. Diseño detallado de la historia de usuario

Una historia de usuario describe una necesidad funcional desde la perspectiva del usuario. Su estructura tiene dos partes:

**Enunciado de la historia**

> **Como** [rol], **necesito** [funcionalidad], **con la finalidad de** [razón].

**Criterios de aceptación** (uno o más escenarios que verifican la historia)

> **En caso que** [contexto], **cuando** [evento], **el sistema** [resultado esperado].

Los campos que documentan cada historia son:

| Campo | Descripción |
|---|---|
| ID | Identificador único de la historia (ej. HU-0001) |
| Rol | Actor que usa la funcionalidad |
| Característica / Funcionalidad | Qué necesita hacer |
| Razón / Resultado | Para qué lo necesita |
| N° de escenario | Número del criterio de aceptación |
| Criterio (título) | Nombre corto del escenario |
| Contexto | Condición que dispara el escenario |
| Evento | Acción que ejecuta el usuario |
| Resultado esperado | Comportamiento del sistema |

El detalle completo de las historias se encuentra en el archivo `Historias-Usuario-S08.xlsx`.

---

### 2. Recojo de información e historias de usuario

A partir del contexto del proyecto (una microempresa comercial que registra clientes, productos y ventas de forma manual), se elaboraron las siguientes historias de usuario, documentadas en detalle en el Excel adjunto:

| ID | Historia | Escenarios |
|---|---|---|
| HU-0001 | Gestionar clientes | Código numérico, código no nulo, email válido, registro exitoso |
| HU-0002 | Gestionar productos | Nombre obligatorio, precio válido, código único, registro exitoso |
| HU-0003 | Registrar ventas | Cliente existente, detalle con producto válido, cantidad válida, cálculo del total |

> **Nota:** al tratarse de un proyecto académico, el recojo de información se basa en el contexto definido del negocio (microempresa comercial), no en una empresa real.

---

### 3. Requerimientos funcionales

Los requerimientos funcionales (RF) describen lo que el sistema debe hacer.

**Módulo Clientes**

| Código | Requerimiento funcional |
|---|---|
| RF-01 | El sistema debe permitir registrar un cliente con código, nombre y email. |
| RF-02 | El sistema debe validar que el código del cliente sea numérico. |
| RF-03 | El sistema debe validar que el código del cliente no sea nulo ni vacío. |
| RF-04 | El sistema debe validar que el email del cliente tenga un formato válido. |
| RF-05 | El sistema debe impedir el registro de clientes con código duplicado. |
| RF-06 | El sistema debe listar todos los clientes registrados. |
| RF-07 | El sistema debe permitir buscar un cliente por su código. |
| RF-08 | El sistema debe permitir actualizar los datos de un cliente existente. |
| RF-09 | El sistema debe permitir eliminar un cliente. |
| RF-10 | El sistema debe mostrar un mensaje de confirmación al registrar un cliente. |

**Módulo Productos**

| Código | Requerimiento funcional |
|---|---|
| RF-11 | El sistema debe permitir registrar un producto con código, nombre y precio. |
| RF-12 | El sistema debe validar que el nombre del producto no esté vacío. |
| RF-13 | El sistema debe validar que el precio del producto sea mayor que cero. |
| RF-14 | El sistema debe validar que el precio sea un valor numérico. |
| RF-15 | El sistema debe impedir el registro de productos con código duplicado. |
| RF-16 | El sistema debe listar todos los productos registrados. |
| RF-17 | El sistema debe permitir buscar un producto por su código. |
| RF-18 | El sistema debe permitir actualizar los datos de un producto. |
| RF-19 | El sistema debe permitir eliminar un producto. |
| RF-20 | El sistema debe mostrar el precio del producto al consultarlo. |

**Módulo Ventas**

| Código | Requerimiento funcional |
|---|---|
| RF-21 | El sistema debe permitir registrar una venta asociada a un cliente. |
| RF-22 | El sistema debe validar que el cliente de la venta exista. |
| RF-23 | El sistema debe registrar la fecha de la venta. |
| RF-24 | El sistema debe permitir agregar uno o más detalles a una venta. |
| RF-25 | El sistema debe validar que el producto de cada detalle exista. |
| RF-26 | El sistema debe validar que la cantidad de cada detalle sea mayor que cero. |
| RF-27 | El sistema debe calcular el subtotal de cada detalle (precio × cantidad). |
| RF-28 | El sistema debe calcular el total de la venta sumando los subtotales. |
| RF-29 | El sistema debe validar que una venta tenga al menos un detalle. |
| RF-30 | El sistema debe mostrar el total de la venta al confirmarla. |
| RF-31 | El sistema debe listar todas las ventas registradas. |
| RF-32 | El sistema debe permitir buscar una venta por su código. |
| RF-33 | El sistema debe permitir consultar el detalle de una venta. |
| RF-34 | El sistema debe permitir listar las ventas de un cliente específico. |

**Generales**

| Código | Requerimiento funcional |
|---|---|
| RF-35 | El sistema debe presentar un menú para navegar entre los módulos de clientes, productos y ventas. |
| RF-36 | El sistema debe validar los campos obligatorios en cada formulario. |
| RF-37 | El sistema debe mostrar mensajes de error claros ante datos inválidos. |
| RF-38 | El sistema debe continuar operando sin cerrarse ante un error de validación. |
| RF-39 | El sistema debe mostrar mensajes de confirmación tras cada operación exitosa. |
| RF-40 | El sistema debe permitir salir de la aplicación de forma controlada. |

---

### 4. Requerimientos no funcionales

Los requerimientos no funcionales (RNF) describen cómo debe comportarse el sistema (cualidades).

| Código | Requerimiento no funcional |
|---|---|
| RNF-01 | **Usabilidad:** la interfaz debe ser intuitiva y permitir operar el sistema con un mínimo de entrenamiento. |
| RNF-02 | **Rendimiento:** las operaciones de registro y consulta deben responder de forma inmediata para el volumen de una microempresa. |
| RNF-03 | **Portabilidad:** el sistema debe ejecutarse en cualquier sistema operativo que cuente con la máquina virtual de Java (JVM). |
| RNF-04 | **Mantenibilidad:** el código debe estar organizado en paquetes y clases siguiendo el paradigma orientado a objetos. |
| RNF-05 | **Confiabilidad:** las validaciones deben impedir el ingreso de datos inconsistentes o corruptos. |
| RNF-06 | **Disponibilidad:** el sistema debe funcionar de manera local, sin requerir conexión a internet. |

---

*Universidad Privada del Norte – Facultad de Ingeniería – 2026-1*
