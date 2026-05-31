/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Helaman
 */
public class Producto {
    private int codigo;
    private String nombre;
    private double precio;

    public Producto() {}
    public Producto(int codigo, String nombre, double precio) {
        this.codigo = codigo; this.nombre = nombre; this.precio = precio;
    }
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    @Override
    public String toString() {
        return codigo + " - " + nombre + " (S/ " + precio + ")";
    }
}
