package repositorio.dao.pedido;

import java.util.List;
import modelo.pedido.Pedido;

/**
 *
 * @author rodri
 */

public interface IDaoPedido {
    public void insertarNuevoPedido(Pedido pedido);

    public void eliminarPedido(int idPedido);

    public void modificarPedido(int idPedido, Pedido pedido);

    public List<Pedido> obtenerTodosLosPedidos();
    
    public Pedido obtenerPedido(int idPedido);

    public List<Pedido> obtenerPedidosPorVendedor(int idVendedor);

    public List<Pedido> obtenerPedidosPorCliente(int idCliente);

}
