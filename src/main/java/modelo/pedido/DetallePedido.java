package modelo.pedido;

import modelo.producto.Producto;

/**
 *
 * @author rodri
 */

public class DetallePedido {

    private int idProducto;  // El id del producto
    private Producto producto;
    private int cantidad;
    private double precio;
    private int descuento;
    private double total;  // Para almacenar el total calculado

    // Constructor con 4 parámetros
    public DetallePedido(Producto producto, int cantidad, double precio, int descuento) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
    }

    // Constructor con los parámetros adecuados
    public DetallePedido(Producto producto, int cantidad, double precio, int descuento, double total) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
        this.total = total; // Asignar el total calculado
    }
    
    // Constructor de DetallePedido
    public DetallePedido(int idProducto, int cantidad, double precio, int descuento, double total) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
        this.total = total;
    }
    
    // Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }
    
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    // Método para calcular el total
    public double getTotal() {
        double totalSinDescuento = cantidad * precio;
        double descuentoAplicado = totalSinDescuento * (descuento / 100.0);
        return totalSinDescuento - descuentoAplicado;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    @Override
    public String toString() {
        return "Producto ID: " + producto.getId()
                + ", Cantidad: " + cantidad
                + ", Descuento: " + descuento + "%"
                + ", Total: " + total;
    }
}