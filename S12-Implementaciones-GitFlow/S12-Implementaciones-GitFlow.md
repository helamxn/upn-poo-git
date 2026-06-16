# Trabajo de Campo – S12: Implementaciones y Git Flow

## DESARROLLO

### 1. Tabla de control de las implementaciones realizadas

| Requerimiento | Historia de usuario | Archivo(s) Java | Estado actual |
|---|---|---|---|
| RF-01 Registrar cliente | HU-0001 | `ClienteServicio.java`, `Cliente.java` | Implementado |
| RF-02 Validar código numérico | HU-0001 | `ClienteServicio.java` | Implementado |
| RF-03 Validar código no nulo | HU-0001 | `ClienteServicio.java` | Implementado |
| RF-04 Validar email | HU-0001 | `ClienteServicio.java` | Implementado |
| RF-07 Buscar cliente por código | HU-0001 | `ClienteServicio.java` | Implementado |
| RF-11 Registrar producto | HU-0002 | `ProductoServicio.java`, `Producto.java` | Implementado |
| RF-12 Validar nombre de producto | HU-0002 | `ProductoServicio.java` | Implementado |
| RF-13 Validar precio | HU-0002 | `ProductoServicio.java` | Implementado |
| RF-15 Código de producto único | HU-0002 | `ProductoServicio.java` | Implementado |
| RF-17 Buscar producto por código | HU-0002 | `ProductoServicio.java` | Implementado |
| RF-21 Registrar venta | HU-0003 | `VentaServicio.java`, `Venta.java` | Implementado |
| RF-24 Agregar detalle a la venta | HU-0003 | `VentaServicio.java`, `DetalleVenta.java` | Implementado |
| RF-27 Calcular subtotal | HU-0003 | `DetalleVenta.java` | Implementado |
| RF-28 Calcular total de la venta | HU-0003 | `Venta.java` | Implementado |
| RF-29 Validar venta con detalle | HU-0003 | `VentaServicio.java` | Implementado |

---

### 2. Git Flow del proceso de desarrollo

El proyecto se versionó en Git siguiendo un flujo de trabajo basado en **Git Flow**, que organiza el desarrollo en ramas con propósitos definidos.

![Git Flow del proyecto](capturas/git-flow.png)

**Ramas del modelo Git Flow:**

| Rama | Propósito |
|---|---|
| `main` (master) | Contiene únicamente versiones estables y publicables. Cada versión se etiqueta (v0.1, v0.2, v1.0). |
| `develop` | Rama de integración donde se reúnen las funcionalidades antes de una versión. |
| `feature/*` | Una por cada funcionalidad nueva. Nace de `develop` y se integra de vuelta en `develop`. |
| `release/*` | Prepara una versión para publicarse; se integra en `main` y en `develop`. |
| `hotfix/*` | Corrige errores urgentes en producción; nace de `main` y se integra en `main` y `develop`. |

**Flujo aplicado en este proyecto.** Por tratarse de un proyecto académico individual, se aplicó una versión simplificada de Git Flow: se trabajó cada entregable semanal en su propia rama `feature/sNN-tema`, que luego se integró a `main` mediante un merge sin avance rápido (`--no-ff`) para conservar la trazabilidad de cada integración. Las ramas `release` y `hotfix` corresponden a escenarios de equipos más grandes y se documentan aquí como parte del modelo.

Comandos usados en cada semana:
```bash
git checkout -b feature/sNN-tema
git add -A
git commit -m "feat|docs: ... SNN"
git checkout main
git merge --no-ff feature/sNN-tema -m "merge: integra SNN a main"
git push origin main
```

**Correspondencia con el historial real del repositorio:**

| Rama de funcionalidad | Entregable integrado |
|---|---|
| `feature/guia-...` (S01–S04) | Guías de Git |
| `feature/s05-sistema-registro` | Proyecto Java inicial |
| `feature/s06-realidad-problematica` | Realidad problemática y antecedentes |
| `feature/s07-restricciones-objetivos-alcance` | Restricciones, objetivos y alcance |
| `feature/s08-requerimientos-historias` | Requerimientos e historias de usuario |
| `feature/s09-diagrama-clases-archivos` | Diagrama de clases y manejo de archivos |
| `feature/s10-control-requerimientos` | Tablas de control |
| `feature/s11-responsabilidad-social` | Informe de responsabilidad social |
| `feature/s12-implementaciones-gitflow` | Este entregable |

---

### 3. Anexos de las actividades realizadas

**ANEXO 1:** historial de commits del repositorio (`git log --oneline --graph --all`), mostrando las ramas de funcionalidad y sus merges a `main`.

**ANEXO 2:** listado de ramas del proyecto (`git branch -a`).

**ANEXO 3:** vista del repositorio en GitHub (pestaña de commits o red/network del repositorio).

---

*Universidad Privada del Norte – Facultad de Ingeniería – 2026-1*
