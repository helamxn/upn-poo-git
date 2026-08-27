package ef_poo.servicio;

import ef_poo.excepciones.ProductoException;
import ef_poo.modelo.Producto;
import ef_poo.persistencia.ProductoDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoServicio {

    public void validar(Producto p) throws ProductoException {
        if (p == null) {
            throw new ProductoException("El producto no puede ser nulo");
        }
    }

    public void registrar(Producto p) throws ProductoException {
        validar(p);
        try {
            if (ProductoDAO.buscarPorCodigo(p.getCodigo()) != null) {
                throw new ProductoException("Ya existe un producto con ese codigo");
            }
            ProductoDAO.insertar(p);
        } catch (SQLException e) {
            throw new ProductoException("Error al guardar en base de datos: " + e.getMessage());
        }
    }

    public void actualizar(Producto p) throws ProductoException {
        validar(p);
        try {
            if (ProductoDAO.buscarPorCodigo(p.getCodigo()) == null) {
                throw new ProductoException("No existe un producto con ese codigo");
            }
            ProductoDAO.actualizar(p);
        } catch (SQLException e) {
            throw new ProductoException("Error al actualizar en base de datos: " + e.getMessage());
        }
    }

    public void eliminar(int codigo) throws ProductoException {
        try {
            if (ProductoDAO.buscarPorCodigo(codigo) == null) {
                throw new ProductoException("No existe un producto con ese codigo");
            }
            if (ProductoDAO.tieneVentasAsociadas(codigo)) {
                throw new ProductoException("No se puede eliminar: el producto tiene ventas registradas.");
            }
            ProductoDAO.eliminar(codigo);
        } catch (SQLException e) {
            if (e.getErrorCode() == 547) {
                throw new ProductoException("No se puede eliminar: el producto tiene ventas registradas.");
            }
            throw new ProductoException("Error al eliminar en base de datos: " + e.getMessage());
        }
    }

    public void listar() {
        List<Producto> productos = getProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        for (Producto p : productos) {
            System.out.println(p);
        }
    }

    public Producto buscarPorCodigo(int codigo) {
        try {
            return ProductoDAO.buscarPorCodigo(codigo);
        } catch (SQLException e) {
            System.err.println("Aviso: no se pudo buscar el producto en base de datos: " + e.getMessage());
            return null;
        }
    }

    public List<Producto> getProductos() {
        try {
            return ProductoDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("Aviso: no se pudo listar productos desde base de datos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public int total() {
        return getProductos().size();
    }
}
