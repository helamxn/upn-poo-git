/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Helaman
 */
public class Cliente {

    private int codigo;
    private String nombre;
    private String email;
    private boolean activo;

    public Cliente() {
    }

    public Cliente(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.activo = true;
    }

    public Cliente(int codigo, String nombre, String email) {
        this(codigo, nombre);
        this.email = email;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + (email != null ? " (" + email + ")" : "");
    }
}
