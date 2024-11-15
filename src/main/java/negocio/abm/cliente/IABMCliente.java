package negocio.abm.cliente;

import negocio.abm.cliente.Exception.ClienteException;
import modelo.cliente.Cliente;

/**
 *
 * @author rodri
 */

public interface IABMCliente {
    public void altaCliente(Cliente cliente) throws ClienteException;  

    public void listarClientes(String codigo, String nombre, String apellido);  

    public String asignarCodigoCliente();
}