/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.sistemaregistro;

import excepciones.ClienteException;
import modelo.Cliente;
import servicio.ClienteServicio;

/**
 *
 * @author Helaman
 */
public class SistemaRegistro {

    public static void main(String[] args) {
        ClienteServicio servicio = new ClienteServicio();

        servicio.registrar(new Cliente(101, "Ana Torres", "ana@correo.com"));
        servicio.registrar(102, "Luis Rojas");
        servicio.registrar(103, "Marta Diaz", "marta@correo.com");

        String[] codigosPrueba = {"104", "abc", "", "105"};
        for (String cod : codigosPrueba) {
            try {
                servicio.validarCodigo(cod);
                servicio.registrar(Integer.parseInt(cod), "Cliente " + cod);
                System.out.println("Cliente con codigo " + cod + " registrado.");
            } catch (ClienteException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        try {
            servicio.validarEmail("correo-invalido");
        } catch (ClienteException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Lista de clientes registrados ---");
        servicio.listar();
        System.out.println("Total: " + servicio.total());

        System.out.println("\n--- Busqueda por codigo ---");
        Cliente encontrado = servicio.buscarPorCodigo(102);
        System.out.println(encontrado != null ? "Encontrado: " + encontrado : "No encontrado");
    }
}
