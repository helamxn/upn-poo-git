package ef_poo.servicio;

import ef_poo.excepciones.ClienteException;
import ef_poo.modelo.Cliente;
import ef_poo.persistencia.ClienteDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteServicio {

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
        try {
            if (ClienteDAO.buscarPorCodigo(c.getCodigo()) != null) {
                throw new ClienteException("Ya existe un cliente con ese codigo");
            }
            ClienteDAO.insertar(c);
        } catch (SQLException e) {
            throw new ClienteException("Error al guardar en base de datos: " + e.getMessage());
        }
    }

    public void actualizar(int codigo, String nombre, String email) throws ClienteException {
        validarNombre(nombre);
        String emailNuevo = (email != null && !email.trim().isEmpty()) ? email.trim() : null;
        if (emailNuevo != null) {
            validarEmail(emailNuevo);
        }
        try {
            Cliente cliente = ClienteDAO.buscarPorCodigo(codigo);
            if (cliente == null) {
                throw new ClienteException("No existe un cliente con ese codigo");
            }
            cliente.setNombre(nombre.trim());
            cliente.setEmail(emailNuevo);
            ClienteDAO.actualizar(cliente);
        } catch (SQLException e) {
            throw new ClienteException("Error al actualizar en base de datos: " + e.getMessage());
        }
    }

    public void eliminar(int codigo) throws ClienteException {
        try {
            if (ClienteDAO.buscarPorCodigo(codigo) == null) {
                throw new ClienteException("No existe un cliente con ese codigo");
            }
            ClienteDAO.eliminar(codigo);
        } catch (SQLException e) {
            throw new ClienteException("Error al eliminar en base de datos: " + e.getMessage());
        }
    }

    public void listar() {
        List<Cliente> clientes = getClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    public Cliente buscarPorCodigo(int codigo) {
        try {
            return ClienteDAO.buscarPorCodigo(codigo);
        } catch (SQLException e) {
            System.err.println("Aviso: no se pudo buscar el cliente en base de datos: " + e.getMessage());
            return null;
        }
    }

    public List<Cliente> getClientes() {
        try {
            return ClienteDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("Aviso: no se pudo listar clientes desde base de datos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public int total() {
        return getClientes().size();
    }
}
