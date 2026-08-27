package ef_poo.servicio;

import ef_poo.excepciones.VentaException;
import ef_poo.modelo.DetalleVenta;
import ef_poo.modelo.Producto;
import ef_poo.modelo.Venta;
import ef_poo.persistencia.VentaDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class VentaServicio {

    private final List<Venta> ventas = new ArrayList<>();
    private int siguienteCodigo = 1;
    private ProductoServicio productoServicio;

    public VentaServicio() {
    }

    public VentaServicio(ClienteServicio clienteServicio, ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
        cargarDesdeDB(clienteServicio, productoServicio);
    }

    private void cargarDesdeDB(ClienteServicio clienteServicio, ProductoServicio productoServicio) {
        try {
            List<Venta> lista = VentaDAO.listarTodas(clienteServicio, productoServicio);
            for (Venta v : lista) {
                ventas.add(v);
                if (v.getCodigo() >= siguienteCodigo) {
                    siguienteCodigo = v.getCodigo() + 1;
                }
            }
        } catch (SQLException e) {
            System.err.println("Aviso: no se pudo cargar ventas desde BD: " + e.getMessage());
            siguienteCodigo = 1;
        }
    }

    public int getSiguienteCodigo() {
        return siguienteCodigo++;
    }

    public void agregarDetalle(Venta venta, Producto producto, int cantidad) throws VentaException {
        if (producto == null) {
            throw new VentaException("El producto del detalle no existe");
        }
        if (cantidad <= 0) {
            throw new VentaException("La cantidad debe ser mayor que cero");
        }
        venta.agregarDetalle(new DetalleVenta(producto, cantidad));
    }

    public void registrarVenta(Venta venta) throws VentaException {
        if (venta.getCliente() == null) {
            throw new VentaException("La venta debe estar asociada a un cliente valido");
        }
        if (venta.getDetalles().isEmpty()) {
            throw new VentaException("La venta debe tener al menos un detalle");
        }
        try {
            VentaDAO.insertar(venta);
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
                throw new VentaException("El codigo de venta generado ya existia en la base de datos. "
                        + "Intente registrar la venta nuevamente.");
            }
            throw new VentaException("Error al guardar la venta en base de datos: " + e.getMessage());
        }
        ventas.add(venta);
    }

    public void listar() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        for (Venta v : ventas) {
            System.out.println("Venta " + v.getCodigo()
                    + " | Cliente: " + v.getCliente().getNombre()
                    + " | Fecha: " + v.getFecha()
                    + " | Total: S/ " + v.getTotal());
        }
    }

    public List<Venta> listarPorCliente(int codigoCliente) {
        if (productoServicio != null) {
            try {
                return VentaDAO.listarPorCliente(codigoCliente, productoServicio);
            } catch (SQLException e) {
                System.err.println("Aviso: filtro por cliente desde BD fallo, "
                        + "se usa la coleccion en memoria: " + e.getMessage());
            }
        }
        List<Venta> resultado = new ArrayList<>();
        for (Venta v : ventas) {
            if (v.getCliente() != null && v.getCliente().getCodigo() == codigoCliente) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public List<Venta> listarPorFecha(String fecha) {
        List<Venta> resultado = new ArrayList<>();
        for (Venta v : ventas) {
            if (v.getFecha() != null && v.getFecha().equals(fecha)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public double calcularTotal(List<Venta> ventasACalcular) {
        double totalVentas = 0;
        for (Venta v : ventasACalcular) {
            totalVentas += v.getTotal();
        }
        return totalVentas;
    }

    public Venta buscarPorCodigo(int codigo) {
        for (Venta v : ventas) {
            if (v.getCodigo() == codigo) {
                return v;
            }
        }
        return null;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public int total() {
        return ventas.size();
    }
}
