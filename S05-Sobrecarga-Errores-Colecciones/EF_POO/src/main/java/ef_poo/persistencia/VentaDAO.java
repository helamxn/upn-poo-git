package ef_poo.persistencia;

import ef_poo.modelo.Cliente;
import ef_poo.modelo.DetalleVenta;
import ef_poo.modelo.Producto;
import ef_poo.modelo.Venta;
import ef_poo.servicio.ClienteServicio;
import ef_poo.servicio.ProductoServicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public static void insertar(Venta venta) throws SQLException {
        Connection conn = Conexion.obtener();
        conn.setAutoCommit(false);
        try {
            String sqlVenta = "INSERT INTO Venta (codigo, codigo_cliente, fecha, total) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlVenta)) {
                stmt.setInt(1, venta.getCodigo());
                stmt.setInt(2, venta.getCliente().getCodigo());
                stmt.setString(3, venta.getFecha());
                stmt.setDouble(4, venta.getTotal());
                stmt.executeUpdate();
            }

            String sqlDetalle = "INSERT INTO DetalleVenta (codigo_venta, codigo_producto, cantidad, subtotal) "
                    + "VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDetalle)) {
                for (DetalleVenta d : venta.getDetalles()) {
                    stmt.setInt(1, venta.getCodigo());
                    stmt.setInt(2, d.getProducto().getCodigo());
                    stmt.setInt(3, d.getCantidad());
                    stmt.setDouble(4, d.getSubtotal());
                    stmt.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public static List<Venta> listarTodas(ClienteServicio clienteServicio,
            ProductoServicio productoServicio) throws SQLException {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT codigo, codigo_cliente, fecha FROM Venta ORDER BY codigo";
        try (Statement stmt = Conexion.obtener().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int codigoVenta = rs.getInt("codigo");
                int codigoCliente = rs.getInt("codigo_cliente");
                Cliente cliente = clienteServicio.buscarPorCodigo(codigoCliente);
                if (cliente == null) {
                    continue;
                }

                Venta venta = new Venta(codigoVenta, cliente, rs.getString("fecha"));
                cargarDetalles(venta, productoServicio);
                lista.add(venta);
            }
        }
        return lista;
    }

    public static List<Venta> listarPorCliente(int codigoCliente,
            ProductoServicio productoServicio) throws SQLException {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT v.codigo, v.fecha, c.codigo AS cli_codigo, "
                + "c.nombre AS cli_nombre, c.email AS cli_email "
                + "FROM Venta v "
                + "INNER JOIN Cliente c ON v.codigo_cliente = c.codigo "
                + "WHERE v.codigo_cliente = ? "
                + "ORDER BY v.codigo";
        try (PreparedStatement stmt = Conexion.obtener().prepareStatement(sql)) {
            stmt.setInt(1, codigoCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getInt("cli_codigo"),
                            rs.getString("cli_nombre"),
                            rs.getString("cli_email"));
                    Venta venta = new Venta(rs.getInt("codigo"), cliente, rs.getString("fecha"));
                    cargarDetalles(venta, productoServicio);
                    lista.add(venta);
                }
            }
        }
        return lista;
    }

    private static void cargarDetalles(Venta venta, ProductoServicio productoServicio) throws SQLException {
        String sql = "SELECT codigo_producto, cantidad, subtotal FROM DetalleVenta WHERE codigo_venta=?";
        try (PreparedStatement stmt = Conexion.obtener().prepareStatement(sql)) {
            stmt.setInt(1, venta.getCodigo());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Producto producto = productoServicio.buscarPorCodigo(rs.getInt("codigo_producto"));
                    if (producto == null) {
                        continue;
                    }
                    venta.agregarDetalle(new DetalleVenta(
                            producto,
                            rs.getInt("cantidad"),
                            rs.getDouble("subtotal")));
                }
            }
        }
    }
}
