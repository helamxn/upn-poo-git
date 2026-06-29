/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Helaman
 */
public class ProductoElectronico extends Producto {

    private boolean garantia;

    public ProductoElectronico(int codigo, String nombre, double precioBase, boolean garantia) {
        super(codigo, nombre, precioBase);
        this.garantia = garantia;
    }

    public boolean isGarantia() {
        return garantia;
    }

    public void setGarantia(boolean garantia) {
        this.garantia = garantia;
    }

    @Override
    public double calcularPrecioFinal() {

        if (garantia) {
            return getPrecioBase() * 1.10;
        }
        return getPrecioBase();
    }

    @Override
    public String getTipo() {
        return "Electronico";
    }

    @Override
    public String getCaracteristicaEspecial() {
        return garantia ? "Con garantia" : "Sin garantia";
    }

    @Override
    public String mostrarInformacion() {
        return getCodigo() + " - " + getNombre() + " [" + getTipo() + "] "
                + getCaracteristicaEspecial() + " (S/ " + calcularPrecioFinal() + ")";
    }
}
