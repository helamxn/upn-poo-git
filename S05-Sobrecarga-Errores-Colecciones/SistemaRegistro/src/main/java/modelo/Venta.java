/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Helaman
 */
public class Venta {
    private int codigo;
    private Cliente cliente;
    private String fecha;
    private List<DetalleVenta> detalles = new ArrayList<>();
    private double total;

    public Venta(int codigo, Cliente cliente, String fecha) {
        this.codigo = codigo; this.cliente = cliente; this.fecha = fecha;
    }
    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        total = calcularTotal();
    }
    public double calcularTotal() {
        double t = 0;
        for (DetalleVenta d : detalles) {
            t += d.getSubtotal();
        }
        return t;
    }
    public int getCodigo() { return codigo; }
    public Cliente getCliente() { return cliente; }
    public String getFecha() { return fecha; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public double getTotal() { return total; }
}
