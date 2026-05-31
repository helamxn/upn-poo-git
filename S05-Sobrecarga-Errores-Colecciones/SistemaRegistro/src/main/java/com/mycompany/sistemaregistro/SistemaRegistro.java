package com.mycompany.sistemaregistro;

import java.util.List;
import modelo.Cliente;
import modelo.Producto;
import modelo.Venta;
import servicio.ClienteServicio;
import servicio.ProductoServicio;
import servicio.VentaServicio;
import persistencia.ArchivoClientes;
import excepciones.ProductoException;
import excepciones.VentaException;

public class SistemaRegistro {

    public static void main(String[] args) {
        ClienteServicio clienteServicio = new ClienteServicio();
        ProductoServicio productoServicio = new ProductoServicio();
        VentaServicio ventaServicio = new VentaServicio();

        // ---- Clientes ----
        clienteServicio.registrar(new Cliente(101, "Ana Torres", "ana@correo.com"));
        clienteServicio.registrar(102, "Luis Rojas");

        // ---- Productos ----
        try {
            productoServicio.registrar(new Producto(201, "Cuaderno", 5.50));
            productoServicio.registrar(new Producto(202, "Lapicero", 1.20));
            productoServicio.registrar(new Producto(203, "Mochila", 0)); // invalido
        } catch (ProductoException e) {
            System.out.println("Error producto: " + e.getMessage());
        }

        System.out.println("\n--- Productos registrados ---");
        productoServicio.listar();

        // ---- Venta con detalles ----
        try {
            Cliente cliente = clienteServicio.buscarPorCodigo(101);
            Venta venta = new Venta(1, cliente, "2026-05-31");
            ventaServicio.agregarDetalle(venta, productoServicio.buscarPorCodigo(201), 3);
            ventaServicio.agregarDetalle(venta, productoServicio.buscarPorCodigo(202), 5);
            ventaServicio.registrarVenta(venta);

            System.out.println("\n--- Detalle de la venta ---");
            venta.getDetalles().forEach(System.out::println);
            System.out.println("TOTAL: S/ " + venta.getTotal());
        } catch (VentaException e) {
            System.out.println("Error venta: " + e.getMessage());
        }

        System.out.println("\n--- Ventas registradas ---");
        ventaServicio.listar();

        // ---- Manejo de archivos ----
        System.out.println("\n--- Gestion de archivos ---");
        ArchivoClientes archivo = new ArchivoClientes();
        archivo.guardar(clienteServicio.getClientes());

        List<Cliente> leidos = archivo.cargar();
        System.out.println("Clientes leidos desde el archivo:");
        for (Cliente c : leidos) {
            System.out.println(c);
        }
    }
}
