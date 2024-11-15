package negocio.abm.pedido;

import java.util.List;
import modelo.cliente.Cliente;
import modelo.pedido.DetallePedido;
import modelo.pedido.Pedido;
import modelo.producto.Producto;
import modelo.vendedor.Vendedor;

/**
 *
 * @author rodri
 */

public interface IABMPedido {
    
    public void crearPedido(Vendedor vendedor,Cliente cliente,List<DetallePedido>  detalles);    
    
    public DetallePedido crearDetalle(Producto producto,int cantidad,int descueto);   
    
    public List<Pedido> traerTodosLosPedidos();   
    
    public List<Pedido> pedidosPorVendedor(String codigo);
    
    public List<Pedido> pedidosPorCliente(String codigo);
    
}