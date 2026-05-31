/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.ArrayList;
import java.util.List;
import modelo.Venta;
import modelo.Producto;
import modelo.DetalleVenta;
import excepciones.VentaException;

/**
 *
 * @author Helaman
 */
public class VentaServicio {
    private List<Venta> ventas = new ArrayList<>();

    public void agregarDetalle(Venta venta, Producto producto, int cantidad) throws VentaException {
        if (producto == null)
            throw new VentaException("El producto del detalle no existe");
        if (cantidad <= 0)
            throw new VentaException("La cantidad debe ser mayor que cero");
        venta.agregarDetalle(new DetalleVenta(producto, cantidad));
    }
    public void registrarVenta(Venta venta) throws VentaException {
        if (venta.getCliente() == null)
            throw new VentaException("La venta debe estar asociada a un cliente valido");
        if (venta.getDetalles().isEmpty())
            throw new VentaException("La venta debe tener al menos un detalle");
        ventas.add(venta);
    }
    public void listar() {
        if (ventas.isEmpty()) { System.out.println("No hay ventas registradas."); return; }
        for (Venta v : ventas) {
            System.out.println("Venta " + v.getCodigo() + " | Cliente: " + v.getCliente().getNombre()
                + " | Fecha: " + v.getFecha() + " | Total: S/ " + v.getTotal());
        }
    }
    public int total() { return ventas.size(); }
}