/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;

/**
 *
 * @author Helaman
 */
public class ArchivoClientes {

    private static final String ARCHIVO = "clientes.txt";

    public void guardar(List<Cliente> clientes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Cliente c : clientes) {
                String email = (c.getEmail() == null) ? "" : c.getEmail();
                bw.write(c.getCodigo() + ";" + c.getNombre() + ";" + email);
                bw.newLine();
            }
            System.out.println("Clientes guardados en " + ARCHIVO);
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    public List<Cliente> cargar() {
        List<Cliente> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 2) {
                    int codigo = Integer.parseInt(datos[0]);
                    String nombre = datos[1];
                    String email = (datos.length >= 3 && !datos[2].isEmpty()) ? datos[2] : null;
                    lista.add(new Cliente(codigo, nombre, email));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer: " + e.getMessage());
        }
        return lista;
    }