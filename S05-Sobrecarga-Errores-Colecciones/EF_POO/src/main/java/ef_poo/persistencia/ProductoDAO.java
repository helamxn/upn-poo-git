package ef_poo.persistencia;

import ef_poo.modelo.Producto;
import ef_poo.modelo.ProductoBazar;
import ef_poo.modelo.ProductoPapeleria;
import ef_poo.modelo.ProductoUtilEscolar;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public static void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO Producto "
                + "(codigo, nombre, precio_base, tipo, en_campania_escolar, venta_por_mayor, categoria, temporada_alta) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = Conexion.obtener().prepareStatement(sql)) {
            stmt.setInt(1, p.getCodigo());
            stmt.setString(2, p.getNombre());
            stmt.setDouble(3, p.getPrecioBase());
            stmt.setString(4, p.getTipo());
            switch (p.getTipo()) {
                case "UtilEscolar" -> {
                    stmt.setBoolean(5, ((ProductoUtilEscolar) p).isEnCampaniaEscolar());
                    stmt.setNull(6, Types.BIT);
                    stmt.setString(7, null);
                    stmt.setNull(8, Types.BIT);
                }
                case "Papeleria" -> {
                    stmt.setNull(5, Types.BIT);
                    stmt.setBoolean(6, ((ProductoPapeleria) p).isVentaPorMayor());
                    stmt.setString(7, null);
                    stmt.setNull(8, Types.BIT);
                }
                case "Bazar" -> {
                    ProductoBazar bazar = (ProductoBazar) p;
                    stmt.setNull(5, Types.BIT);
                    stmt.setNull(6, Types.BIT);
                    stmt.setString(7, bazar.getCategoria());
                    stmt.setBoolean(8, bazar.isTemporadaAlta());
                }
                default ->
                    throw new SQLException("Tipo de producto no soportado: " + p.getTipo());
            }
            stmt.executeUpdate();
        }
    }

    public static void actualizar(Producto p) throws SQLException {
        String sql = "UPDATE Producto SET nombre=?, precio_base=?, tipo=?, "
                + "en_campania_escolar=?, venta_por_mayor=?, categoria=?, temporada_alta=? "
                + "WHERE codigo=?";
        try (PreparedStatement stmt = Conexion.obtener().prepareStatement(sql)) {
            stmt.setString(1, p.getNombre());
            stmt.setDouble(2, p.getPrecioBase());
            stmt.setString(3, p.getTipo());
            switch (p.getTipo()) {
                case "UtilEscolar" -> {
                    stmt.setBoolean(4, ((ProductoUtilEscolar) p).isEnCampaniaEscolar());
                    stmt.setNull(5, Types.BIT);
                    stmt.setString(6, null);
                    stmt.setNull(7, Types.BIT);
                }
                case "Papeleria" -> {
                    stmt.setNull(4, Types.BIT);
                    stmt.setBoolean(5, ((ProductoPapeleria) p).isVentaPorMayor());
                    stmt.setString(6, null);
                    stmt.setNull(7, Types.BIT);
                }
                case "Bazar" -> {
                    ProductoBazar bazar = (ProductoBazar) p;
                    stmt.setNull(4, Types.BIT);
                    stmt.setNull(5, Types.BIT);
                    stmt.setString(6, bazar.getCategoria());
                    stmt.setBoolean(7, bazar.isTemporadaAlta());
                }
                default ->
                    throw new SQLException("Tipo de producto no soportado: " + p.getTipo());
            }
            stmt.setInt(8, p.getCodigo());
            stmt.executeUpdate();
        }
    }

    public static void eliminar(int codigo) throws SQLException {
        String sql = "DELETE FROM Producto WHERE codigo=?";
        try (PreparedStatement stmt = Conexion.obtener().prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        }
    }

    public static boolean tieneVentasAsociadas(int codigoProducto) throws SQLException {
        String sql = "SELECT TOP 1 1 FROM DetalleVenta WHERE codigo_producto = ?";
        try (PreparedStatement stmt = Conexion.obtener().prepareStatement(sql)) {
            stmt.setInt(1, codigoProducto);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static List<Producto> listarTodos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Producto ORDER BY codigo";
        try (Statement stmt = Conexion.obtener().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Producto p = construirProducto(rs);
                if (p != null) {
                    lista.add(p);
                }
            }
        }
        return lista;
    }

    public static Producto buscarPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM Producto WHERE codigo=?";
        try (PreparedStatement stmt = Conexion.obtener().prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirProducto(rs);
                }
            }
        }
        return null;
    }

    private static Producto construirProducto(ResultSet rs) throws SQLException {
        int codigo = rs.getInt("codigo");
        String nombre = rs.getString("nombre");
        double precio = rs.getDouble("precio_base");
        String tipo = rs.getString("tipo");
        return switch (tipo) {
            case "UtilEscolar" ->
                new ProductoUtilEscolar(codigo, nombre, precio,
                rs.getBoolean("en_campania_escolar"));
            case "Papeleria" ->
                new ProductoPapeleria(codigo, nombre, precio,
                rs.getBoolean("venta_por_mayor"));
            case "Bazar" ->
                new ProductoBazar(codigo, nombre, precio,
                rs.getString("categoria"),
                rs.getBoolean("temporada_alta"));
            default ->
                null;
        };
    }
}
