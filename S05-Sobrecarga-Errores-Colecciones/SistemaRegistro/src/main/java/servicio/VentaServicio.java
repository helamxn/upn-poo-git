package servicio;

import excepciones.VentaException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.DetalleVenta;
import modelo.Producto;
import modelo.Venta;
import persistencia.VentaDAO;

public class VentaServicio {

    private final List<Venta> ventas = new ArrayList<>();
    private int siguienteCodigo = 1;

    public VentaServicio() {
    }

    public VentaServicio(ClienteServicio clienteServicio, ProductoServicio productoServicio) {
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

            try {
                siguienteCodigo = VentaDAO.maxCodigo() + 1;
            } catch (SQLException ex) {
                siguienteCodigo = 1;
            }
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

    public Venta buscarPorCodigo(int codigo) {
        for (Venta v : ventas) {
            if (v.getCodigo() == codigo) {
                return v;
            }
        }
        return null;
    }

    public List<