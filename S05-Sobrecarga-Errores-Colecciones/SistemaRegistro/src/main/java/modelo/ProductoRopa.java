/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Helaman
 */
public class ProductoRopa extends Producto {

    private String talla;
    private boolean enTemporada;

    public ProductoRopa(int codigo, String nombre, double precioBase, String talla, boolean enTemporada) {
        super(codigo, nombre, precioBase);
        setTalla(talla);
        this.enTemporada = enTemporada;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        if (talla == null || talla.trim().isEmpty()) {
            throw new IllegalArgumentException("La talla no puede estar vacia");
        }
        this.talla = talla;
    }

    public boolean isEnTemporada() {
        return enTemporada;
    }

    public void setEnTemporada(boolean enTemporada) {
        this.enTemporada = enTemporada;
    }

    @Override
    public double calcularPrecioFinal() {

        if (!enTemporada) {
            return getPrecioBase() * 0.80;
        }
        return getPrecioBase();
    }

    @Override
    public String getTipo() {
        return "Ropa";
    }

    @Override
    public String getCaracteristicaEspecial() {
        return "Talla " + talla + (enTemporada ? " - En temporada" : " - Fuera de temporada");
    }

    @Override
    public String mostrarInformacion() {
        return getCodigo() + " - " + getNombre() + " [" + getTipo() + "] "
                + getCaracteristicaEspecial() + " (S/ " + calcularPrecioFinal() + ")";
    }
}
