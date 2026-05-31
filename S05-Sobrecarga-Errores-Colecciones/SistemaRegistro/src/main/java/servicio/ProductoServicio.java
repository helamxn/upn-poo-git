/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import modelo.Producto;
import excepciones.ProductoException;

/**
 *
 * @author Helaman
 */
public class ProductoServicio {
    private List<Producto> productos = new ArrayList<>();
    private HashMap<Integer, Producto> indice = new HashMap<>();

    public void registrar(Producto p) throws ProductoException {
        validar(p);
        if (indice.containsKey(p.getCodigo()))
            throw new ProductoException("Ya existe un producto con ese codigo");
        productos.add(p);
        indice.put(p.getCodigo(), p);
    }
    public void validar(Producto p) throws ProductoException {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty())
            throw new ProductoException("El nombre del producto no puede estar vacio");
        if (p.getPrecio() <= 0)
            throw new ProductoException("El precio debe ser mayor que cero");
    }
    public void listar() {
        if (productos.isEmpty()) { System.out.println("No hay productos registrados."); return; }
        for (Producto p : productos) System.out.println(p);
    }
    public Producto buscarPorCodigo(int codigo) { return indice.get(codigo); }
    public int total() { return productos.size(); }
}