/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Helaman
 */
public class ProductoAlimento extends Producto {

    private boolean perecible;

    public ProductoAlimento(int codigo, String nombre, double precioBase, boolean perecible) {
        super(codigo, nombre, precioBase);
        this.perecible = perecible;
    }

    public boolean isPerecible() {
        return perecible;
    }

    public void setPerecible(boolean perecible) {
        this.perecible = perecible;
    }

    @Override
    public double calcularPrecioFinal() {

        if (perecible) {
            return getPrecioBase() * 0.90;
        }
        return getPrecioBase();
    }

    @Override
    public String getTipo() {
        return "Alimento";
    }

    @Override
    public String getCaracteristicaEspecial() {
        return perecible ? "Perecible" : "No perecible";
    }

    @Override
    public String mostrarInformacion() {
        return getCodigo() + " - " + getNombre() + " [" + getTipo() + "] "
                + getCaracteristicaEspecial() + " (S/ " + calcularPrecioFinal() + ")";
    }
}
