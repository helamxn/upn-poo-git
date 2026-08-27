package ef_poo.modelo;

public class DetalleVenta {

    private Producto producto;
    private int cantidad;
    private double subtotal;

    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    public DetalleVenta(Producto producto, int cantidad, double subtotal) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public double calcularSubtotal() {
        return producto.calcularPrecioFinal() * cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    @Override
    public String toString() {
        return cantidad + " x " + producto.getNombre() + " = S/ " + subtotal;
    }
}
