package servicio;

import excepciones.ProductoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import modelo.Producto;
import persistencia.ProductoDAO;

public class ProductoServicio {

    private final List<Producto> productos = new ArrayList<>();
    private final HashMap<Integer, Producto> indice = new HashMap<>();

    public ProductoServicio() {
        cargarDesdeDB();
    }

    private void cargarDesdeDB() {
        try {
            for (Producto p : ProductoDAO.listarTodos()) {
                productos.add(p);
                indice.put(p.getCodigo(), p);
            }
        } catch (SQLException e) {
            System.err.println("Aviso: no se pudo cargar productos desde BD: " + e.getMessage());
        }
    }

    public void validar(Producto p) throws ProductoException {
        if (p == null) {
            throw new ProductoException("El producto no puede ser nulo");
        }
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new ProductoException("El nombre del producto no puede estar vacio");
        }
        if (p.getPrecioBase() <= 0) {
            throw new ProductoException("El precio debe ser mayor que cero");
        }
    }

    public void registrar(Producto p) throws ProductoException {
        validar(p);
        if (indice.containsKey(p.getCodigo())) {
            throw new ProductoException("Ya existe un producto con ese codigo");
        }
        try {
            ProductoDAO.insertar(p);
        } catch (SQLException e) {
            throw new ProductoException("Error al guardar en base de datos: " + e.getMessage());
        }
        productos.add(p);
        indice.put(p.getCodigo(), p);
    }

    public void actualizar(Producto p) throws ProductoException {
        validar(p);
        Producto actual = indice.get(p.getCodigo());
        if (actual == null) {
            throw new ProductoException("No existe un producto con ese codigo");
        }
        try {
            ProductoDAO.actualizar(p);
        } catch (SQLException e) {
            throw new ProductoException("Error al actualizar en base de datos: " + e.getMessage());
        }
        int posicion = productos.indexOf(actual);
        productos.set(posicion, p);
        indice.put(p.getCodigo(), p);
    }

    public void eliminar(int codigo) throws ProductoException {
        Producto producto = indice.remove(codigo);
        if (producto == null) {
            throw new ProductoException("No existe un producto con ese codigo");
        }
        productos.remove(producto);
        try {
            ProductoDAO.eliminar(codigo);
        } catch (SQLException e) {

            productos.add(producto);
            indice.put(codigo, producto);
            throw new ProductoException("No se puede eliminar: el producto tiene ventas registradas.");
        }
    }

    public void listar() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        for (Producto p : productos) {
            System.out.println(p);
        }
    }

    public Producto buscarPorCodigo(int codigo) {
        return indice.get(codigo);
 