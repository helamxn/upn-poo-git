package ef_poo.modelo;

public class Cliente {

    private int codigo;
    private String nombre;
    private String email;

    public Cliente() {
    }

    public Cliente(int codigo, String nombre) {
        setCodigo(codigo);
        setNombre(nombre);
    }

    public Cliente(int codigo, String nombre, String email) {
        this(codigo, nombre);
        this.email = email;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El codigo del cliente debe ser mayor que cero");
        }
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacio");
        }
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + (email != null ? " (" + email + ")" : "");
    }
}
