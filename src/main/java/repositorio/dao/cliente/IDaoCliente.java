package repositorio.dao.cliente;

import java.util.List;
import modelo.cliente.Cliente;

/**
 *
 * @author rodri
 */

public interface IDaoCliente{
    public void insertarNuevoCliente(Cliente cliente);
    
    public void eliminarCliente(String codigo, String nombre, String apellido);
    
    public void modificarCliente(String codigo, Cliente cliente);

    public Cliente obtenerCliente(String codigo);

    public List<Cliente> getClientes(String codigo, String nombre, String apellido);
    
    public String getProximoCodigoCliente();
}