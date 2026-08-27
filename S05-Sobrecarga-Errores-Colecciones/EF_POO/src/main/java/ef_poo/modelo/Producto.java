package ef_poo.modelo;

public abstract class Producto {

    private int codigo;
    private String nombre;
    private double precioBase;

    public Producto() {
    }

    public Producto(int codigo, String nombre, double precioBase) {
        setCodigo(codigo);
        setNombre(nombre);
        setPrecioBase(precioBase);
    }

    public abstract double calcularPrecioFinal();

    public abstract String getTipo();

    public abstract String mostrarInformacion();

    public abstract String getCaracteristicaEspecial();

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El codigo del producto debe ser mayor que cero");
        }
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacio");
        }
        this.nombre = nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        if (precioBase <= 0) {
            throw new IllegalArgumentException("El precio base debe ser mayor que cero");
        }
        this.precioBase = precioBase;
    }

    @Override
    public String toString() {
        return mostrarInformacion();
    }
}
