package negocio.abm.pedido;

import java.util.List;
import modelo.cliente.Cliente;
import modelo.pedido.DetallePedido;
import modelo.pedido.Pedido;
import modelo.producto.Producto;
import modelo.vendedor.Vendedor;
import repositorio.dao.cliente.ClienteDaoImpl;
import repositorio.dao.pedido.PedidoDaoImpl;
import repositorio.dao.vendedor.VendedorDaoImpl;

/**
 *
 * @author rodri
 */

public class ABMPedido implements IABMPedido {

    private PedidoDaoImpl pedidoDao;

    public ABMPedido() {
        pedidoDao = new PedidoDaoImpl();  // Crear instancia del DAO
    }
    
    @Override
    public void crearPedido(Vendedor vendedor, Cliente cliente, List<DetallePedido> detalles) {
        // Validación de existencia de vendedor y cliente
        if (vendedor == null || cliente == null) {
            throw new IllegalArgumentException("El vendedor o el cliente no pueden ser nulos.");
        }

        // Crear un nuevo pedido
        Pedido nuevoPedido = new Pedido(
                java.time.LocalDate.now(), // Fecha actual
                vendedor, // Vendedor asociado
                cliente, // Cliente asociado
                modelo.pedido.EstadoPedido.PREPARACION, // Estado inicial
                calcularTotalPedido(detalles), // Total calculado con los detalles
                detalles // Detalles del pedido
        );

        // Insertar el nuevo pedido en la base de datos
        pedidoDao.insertarNuevoPedido(nuevoPedido);
    }

    @Override
    public DetallePedido crearDetalle(Producto producto, int cantidad, int descuento) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }

        // Crear un detalle de pedido
        double precio = producto.getPrecio();
        double total = calcularTotalDetalle(precio, cantidad, descuento);

        return new DetallePedido(producto, cantidad, precio, descuento, total);
    }

    @Override
    public List<Pedido> traerTodosLosPedidos() {
        return pedidoDao.obtenerTodosLosPedidos();
    }

    @Override
    public List<Pedido> pedidosPorVendedor(String codigo) {
        int idVendedor = obtenerIdVendedorPorCodigo(codigo);
        if (idVendedor == -1) {
            throw new IllegalArgumentException("Vendedor no encontrado.");
        }
        return pedidoDao.obtenerPedidosPorVendedor(idVendedor);
    }

    @Override
    public List<Pedido> pedidosPorCliente(String codigo) {
        int idCliente = obtenerIdClientePorCodigo(codigo);
        if (idCliente == -1) {
            throw new IllegalArgumentException("Cliente no encontrado.");
        }
        return pedidoDao.obtenerPedidosPorCliente(idCliente);
    }

    // Métodos auxiliares
    private double calcularTotalPedido(List<DetallePedido> detalles) {
        double total = 0;
        for (DetallePedido detalle : detalles) {
            total += detalle.getTotal();
        }
        return total;
    }

    private double calcularTotalDetalle(double precio, int cantidad, int descuento) {
        double descuentoAplicado = precio * (descuento / 100.0);
        double precioFinal = precio - descuentoAplicado;
        return precioFinal * cantidad;
    }

    private int obtenerIdVendedorPorCodigo(String codigo) {
        VendedorDaoImpl vendedorDao = new VendedorDaoImpl();
        Vendedor vendedor = vendedorDao.obtenerVendedor(codigo);
        return vendedor != null ? vendedor.getId() : -1;
    }

    private int obtenerIdClientePorCodigo(String codigo) {
        ClienteDaoImpl clienteDao = new ClienteDaoImpl();
        Cliente cliente = clienteDao.obtenerCliente(codigo);
        return cliente != null ? cliente.getId() : -1;
    }
}
