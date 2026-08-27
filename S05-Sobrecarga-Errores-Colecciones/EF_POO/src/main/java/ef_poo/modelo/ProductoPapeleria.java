package ef_poo.modelo;

public class ProductoPapeleria extends Producto {

    private boolean ventaPorMayor;

    public ProductoPapeleria(int codigo, String nombre, double precioBase, boolean ventaPorMayor) {
        super(codigo, nombre, precioBase);
        this.ventaPorMayor = ventaPorMayor;
    }

    public void setVentaPorMayor(boolean ventaPorMayor) {
        this.ventaPorMayor = ventaPorMayor;
    }

    public boolean isVentaPorMayor() {
        return ventaPorMayor;
    }

    @Override
    public double calcularPrecioFinal() {
        if (ventaPorMayor) {
            return getPrecioBase() * 0.88;
        }
        return getPrecioBase();
    }

    @Override
    public String getTipo() {
        return "Papeleria";
    }

    @Override
    public String getCaracteristicaEspecial() {
        return ventaPorMayor ? "Venta por mayor" : "Venta al detalle";
    }

    @Override
    public String mostrarInformacion() {
        return getCodigo() + " - " + getNombre() + " [" + getTipo() + "] "
                + getCaracteristicaEspecial() + " (S/ " + calcularPrecioFinal() + ")";
    }
}
