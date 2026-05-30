/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import modelo.Cliente;
import excepciones.ClienteException;

/**
 *
 * @author Helaman
 */
public class ClienteServicio {

    private List<Cliente> clientes = new ArrayList<>();
    private HashMap<Integer, Cliente> indice = new HashMap<>();

    public void registrar(Cliente c) {
        agregar(c);
    }

    public void registrar(int codigo, String nombre) {
        agregar(new Cliente(codigo, nombre));
    }

    public void registrar(int codigo, String nombre, String email) {
        agregar(new Cliente(codigo, nombre, email));
    }

    public void validarCodigo(String codigo) throws ClienteException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new ClienteException("No se puede guardar cliente con codigo nulo");
        }
        if (!codigo.matches("\\d+")) {
            throw new ClienteException("No se puede guardar cliente con codigo diferente a numeros");
        }
    }

    public void validarEmail(String email) throws ClienteException {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ClienteException("El email no tiene un dominio valido");
        }
    }

    public void agregar(Cliente c) {
        clientes.add(c);
        indice.put(c.getCodigo(), c);
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

    public int total() {
        return clientes.size();
    }
}
