package modelo.pedido;

import java.time.LocalDate;
import java.util.List;
import modelo.cliente.Cliente;
import modelo.vendedor.Vendedor;

/**
 *
 * @author rodri
 */

public class Pedido {

    private int id;  // ID del pedido
    private LocalDate fecha;  // Usamos LocalDate para una fecha sin hora
    private Vendedor vendedor;
    private Cliente cliente;
    private EstadoPedido estado;  // Asegúrate de que EstadoPedido sea un enum
    private double totalPedido;
    private List<DetallePedido> detalles;

    // Constructor
    public Pedido(LocalDate fecha, Vendedor vendedor, Cliente cliente, EstadoPedido estado, double totalPedido, List<DetallePedido> detalles) {
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.estado = estado;
        this.totalPedido = totalPedido;
        this.detalles = detalles;
    }

    public Pedido(LocalDate fecha, Vendedor vendedor, Cliente cliente, EstadoPedido estado,
            double total, List<DetallePedido> detalles, int id) {
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.estado = estado;
        this.totalPedido = totalPedido;
        this.detalles = detalles;
        this.id = id;  // Asignamos el ID del pedido
    }
    
    public Pedido(int id, LocalDate fecha, Vendedor vendedor, Cliente cliente, EstadoPedido estado, double totalPedido, List<DetallePedido> detalles) {
        this.id = id;  // Asegúrate de que el id se asigne correctamente aquí
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.estado = estado;
        this.totalPedido = totalPedido;
        this.detalles = detalles;
    }
    
    // Getters y setters
    public int getId() {
        return id;  // Método para obtener el ID del pedido
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public double getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(double totalPedido) {
        this.totalPedido = totalPedido;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    // Métodos adicionales para obtener los ID de vendedor y cliente
    public int getIdVendedor() {
        return vendedor.getId();  // Asumiendo que el vendedor tiene un método getId()
    }

    public int getIdCliente() {
        return cliente.getId();  // Asumiendo que el cliente tiene un método getId()
    }
    
    @Override
    public String toString() {
        return "ID Pedido: " + id
                + ", Estado: " + estado
                + ", Fecha: " + fecha
                + ", Total Pedido: " + totalPedido;
    }
}