package servicio;

import excepciones.ClienteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import modelo.Cliente;
import persistencia.ClienteDAO;

public class ClienteServicio {

    private final List<Cliente> clientes = new ArrayList<>();
    private final HashMap<Integer, Cliente> indice = new HashMap<>();

    public ClienteServicio() {
        cargarDesdeDB();
    }

    private void cargarDesdeDB() {
        try {
            for (Cliente c : ClienteDAO.listarTodos()) {
                clientes.add(c);
                indice.put(c.getCodigo(), c);
            }
        } catch (SQLException e) {
            System.err.println("Aviso: no se pudo cargar clientes desde BD: " + e.getMessage());
        }
    }

    public void registrar(Cliente c) throws ClienteException {
        agregar(c);
    }

    public void registrar(int codigo, String nombre) throws ClienteException {
        agregar(new Cliente(codigo, nombre));
    }

    public void registrar(int codigo, String nombre, String email) throws ClienteException {
        agregar(new Cliente(codigo, nombre, email));
    }

    public void validarCodigo(String codigo) throws ClienteException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new ClienteException("No se puede guardar cliente con codigo nulo");
        }
        for (int i = 0; i < codigo.length(); i++) {
            if (!Character.isDigit(codigo.charAt(i))) {
                throw new ClienteException("No se puede guardar cliente con codigo diferente a numeros");
            }
        }
    }

    public void validarNombre(String nombre) throws ClienteException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ClienteException("El nombre del cliente no puede estar vacio");
        }
    }

    public void validarEmail(String email) throws ClienteException {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new ClienteException("El email no tiene un dominio valido");
        }
    }

    public void agregar(Cliente c) throws ClienteException {
        if (c == null) {
            throw new ClienteException("El cliente no puede ser nulo");
        }
        validarCodigo(String.valueOf(c.getCodigo()));
        validarNombre(c.getNombre());
        if (c.getEmail() != null && !c.getEmail().trim().isEmpty()) {
            validarEmail(c.getEmail());
        }
        if (indice.containsKey(c.getCodigo())) {
            throw new ClienteException("Ya existe un cliente con ese codigo");
        }

        try {
            ClienteDAO.insertar(c);
        } catch (SQLException e) {
            throw new ClienteException("Error al guardar en base de datos: " + e.getMessage());
        }
        clientes.add(c);
        indice.put(c.getCodigo(), c);
    }

    public void actualizar(int codigo, String nombre, String email) throws ClienteException {
        Cliente cliente = buscarPorCodigo(codigo);
        if (cliente == null) {
            throw new ClienteException("No existe un cliente con ese codigo");
        }
        validarNombre(nombre);
        String emailNuevo = (email != null && !email.trim().isEmpty()) ? email.trim() : null;
        if (emailNuevo != null) {
            validarEmail(emailNuevo);
        }

        cliente.setNombre(nombre.trim());
        cliente.setEmail(emailNuevo);
        try {
            ClienteDAO.actualizar(cliente);
        } catch (SQLException e) {
            throw new ClienteException("Error al actualizar en base de datos: " + e.getMessage());
        }
    }

    public void eliminar(int codigo) throws ClienteException {
        Cliente cliente = indice.remove(codigo);
        if (cliente == null) {
            throw new ClienteException("No existe un cliente con ese codigo");
        }
        clientes.remove(cliente);
        try {
            ClienteDAO.eliminar(codigo);
        } catch (SQLException e) {

            clientes.add(cliente);
            indice.put(codigo, cliente);
            throw new ClienteException("Error al eliminar en base de datos: " + e.getMessage());
        }
    }

    public void listar() {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    public Cliente buscarPorCodigo(int codigo) {
        return indice.get(codigo);
    }

    public List<Cliente> getClientes