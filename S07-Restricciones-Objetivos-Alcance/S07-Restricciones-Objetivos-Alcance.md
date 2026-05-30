# Trabajo de Campo – S07: Restricciones, Alternativas, Objetivos y Alcance

## DESARROLLO

Este documento continúa la planificación del proyecto **Sistema de gestión de clientes y ventas para una microempresa**, definido en S06.

### 1. Restricciones del proyecto

Las restricciones son los límites reales dentro de los cuales debe desarrollarse el proyecto. Se identifican las siguientes:

| Tipo | Restricción |
|---|---|
| Tiempo | El desarrollo debe completarse dentro de un ciclo académico, lo que limita el número de funcionalidades. |
| Recursos humanos | Equipo reducido de desarrollo con disponibilidad parcial (estudiantes). |
| Presupuesto | Sin presupuesto para licencias; se usan únicamente herramientas gratuitas o de código abierto. |
| Tecnología | Aplicación de escritorio en Java (NetBeans) con base de datos local; sin infraestructura en la nube. |
| Datos | No se dispone de datos de una empresa real; se trabaja con datos de prueba. |
| Alcance | El sistema cubre la gestión de clientes y ventas; no incluye contabilidad, planillas ni tributación. |
| Conocimiento | El equipo está en formación, por lo que se priorizan técnicas vistas en el curso. |

---

### 2. Propuesta de alternativas de solución

A partir de las restricciones anteriores, se evaluaron alternativas realistas:

| Alternativa | Ventajas | Desventajas | Decisión |
|---|---|---|---|
| Aplicación de escritorio en Java | Tecnología vista en el curso, sin costo, funciona sin internet | Instalación por equipo, no accesible de forma remota | **Elegida** |
| Aplicación web | Acceso multiplataforma | Requiere servidor/hosting y conocimientos no cubiertos en el curso | Descartada |
| Aplicación móvil | Movilidad | Fuera del alcance técnico y de tiempo del curso | Descartada |
| Continuar con Excel/papel | Costo nulo, ya conocido | Mantiene el problema actual (errores, datos dispersos) | Descartada |

**Justificación:** la aplicación de escritorio en Java es la alternativa más realista porque se ajusta a las restricciones de tiempo, presupuesto y conocimiento del equipo, a la vez que resuelve el problema central de centralizar y validar la información de clientes y ventas.

---

### 3. Objetivos del proyecto

**Objetivo general**

Desarrollar un sistema de información de escritorio que mejore la gestión de clientes y ventas de una microempresa, centralizando los datos y reduciendo los errores del registro manual.

**Objetivos específicos**

1. Implementar el registro de clientes con validación de los datos de entrada (código y correo).
2. Implementar el registro de productos y de ventas, con el cálculo automático del total de cada venta a partir de sus detalles.
3. Almacenar la información en una estructura de datos consultable que evite la duplicidad.
4. Permitir la consulta y el listado de los clientes, productos y ventas registradas.
5. Aplicar control de versiones (Git) durante todo el desarrollo del proyecto.

---

### 4. Alcance del proyecto

**El sistema incluye (dentro del alcance):**

- Registro, validación y almacenamiento de clientes.
- Registro de productos con su precio.
- Registro de ventas asociadas a un cliente, con su detalle (productos y cantidades) y cálculo automático del total.
- Consulta y listado de la información registrada.
- Búsqueda de clientes por código.
- Manejo de errores ante datos inválidos.

**El sistema no incluye (fuera del alcance):**

- Módulos de contabilidad, planillas o tributación (facturación electrónica SUNAT).
- Acceso web o móvil.
- Integración con pasarelas de pago o sistemas externos.
- Gestión de usuarios con roles y permisos avanzados.

**Usuarios previstos:** el administrador o encargado de la microempresa, que registra y consulta la información comercial.

---

*Universidad Privada del Norte – Facultad de Ingeniería – 2026-1*
