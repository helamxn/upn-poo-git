package ef_poo.persistencia;

import ef_poo.modelo.Cliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public static void insertar(Cliente c) throws SQLException {
        String sql = "INSERT INTO Cliente (codigo, nombre, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = Conexion.obtener().prepareStatement(sql)) {
            stmt.setInt(1, c.getCodigo());
            stmt.setString(2, c.getNombre());
            stmt.setString(3, c.getEmail());
            stmt.executeUpdate();
        }
    }

    public static void actualizar(Cliente c) throws SQLException {
        String sql = "UPDATE Cliente SET nombre=?, email=? WHERE codigo=?";
        try (PreparedStatement stmt = Conexion.obtener().prepareStatement(sql)) {
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getEmail());
            stmt.setInt(3, c.getCodigo());
            stmt.executeUpdate();
        }
    }

    public static void eliminar(int codigo) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE codigo=?";
        try (PreparedStatement stmt = Conexion.obtener().prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        }
    }

    public static List<Cliente> listarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT codigo, nombre, email FROM Cliente ORDER BY codigo";
        try (Statement stmt = Conexion.obtener().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getInt("codigo"),
                        rs.getString("nombre"),
                        rs.getString("email")));
            }
        }
        return lista;
    }

    public static Cliente buscarPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT codigo, nombre, email FROM Cliente WHERE codigo=?";
        try (PreparedStatement stmt = Conexion.obtener().prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("codigo"),
                            rs.getString("nombre"),
                            rs.getString("email"));
                }
            }
        }
        return null;
    }
}
