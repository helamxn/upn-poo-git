package ef_poo.modelo;

public class ProductoUtilEscolar extends Producto {

    private boolean enCampaniaEscolar;

    public ProductoUtilEscolar(int codigo, String nombre, double precioBase, boolean enCampaniaEscolar) {
        super(codigo, nombre, precioBase);
        this.enCampaniaEscolar = enCampaniaEscolar;
    }

    public void setEnCampaniaEscolar(boolean enCampaniaEscolar) {
        this.enCampaniaEscolar = enCampaniaEscolar;
    }

    public boolean isEnCampaniaEscolar() {
        return enCampaniaEscolar;
    }

    @Override
    public double calcularPrecioFinal() {
        if (enCampaniaEscolar) {
            return getPrecioBase() * 0.90;
        }
        return getPrecioBase();
    }

    @Override
    public String getTipo() {
        return "UtilEscolar";
    }

    @Override
    public String getCaracteristicaEspecial() {
        return enCampaniaEscolar ? "En campania escolar" : "Fuera de campania escolar";
    }

    @Override
    public String mostrarInformacion() {
        return getCodigo() + " - " + getNombre() + " [" + getTipo() + "] "
                + getCaracteristicaEspecial() + " (S/ " + calcularPrecioFinal() + ")";
    }
}
