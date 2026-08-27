package ef_poo.modelo;

public class ProductoBazar extends Producto {

    private String categoria;
    private boolean temporadaAlta;

    public ProductoBazar(int codigo, String nombre, double precioBase, String categoria, boolean temporadaAlta) {
        super(codigo, nombre, precioBase);
        setCategoria(categoria);
        this.temporadaAlta = temporadaAlta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoria de bazar no puede estar vacia");
        }
        this.categoria = categoria;
    }

    public void setTemporadaAlta(boolean temporadaAlta) {
        this.temporadaAlta = temporadaAlta;
    }

    public boolean isTemporadaAlta() {
        return temporadaAlta;
    }

    @Override
    public double calcularPrecioFinal() {
        if (!temporadaAlta) {
            return getPrecioBase() * 0.80;
        }
        return getPrecioBase();
    }

    @Override
    public String getTipo() {
        return "Bazar";
    }

    @Override
    public String getCaracteristicaEspecial() {
        return "Categoria " + categoria + (temporadaAlta ? " - Temporada alta" : " - Fuera de temporada alta");
    }

    @Override
    public String mostrarInformacion() {
        return getCodigo() + " - " + getNombre() + " [" + getTipo() + "] "
                + getCaracteristicaEspecial() + " (S/ " + calcularPrecioFinal() + ")";
    }
}
