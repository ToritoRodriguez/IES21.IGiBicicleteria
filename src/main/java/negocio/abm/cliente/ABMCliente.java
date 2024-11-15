package negocio.abm.cliente;

import java.util.List;
import modelo.cliente.Cliente;
import negocio.abm.cliente.Exception.ClienteException;
import repositorio.dao.cliente.ClienteDaoImpl;
import repositorio.dao.cliente.IDaoCliente;

/**
 *
 * @author rodri
*/

public class ABMCliente implements IABMCliente{

    private IDaoCliente iDaoCliente = new ClienteDaoImpl();

    private void validarDatosCliente(Cliente cliente) throws ClienteException {
        if (cliente == null) {
            throw new ClienteException("El cliente no puede ser nulo");
        }
        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
            throw new ClienteException("El nombre del cliente no puede ser vacío");
        }
        if (cliente.getApellido() == null || cliente.getApellido().isEmpty()) {
            throw new ClienteException("El apellido del cliente no puede ser vacío");
        }
        if (cliente.getDni() == null || cliente.getDni().isEmpty()) {
            throw new ClienteException("El DNI del cliente no puede ser vacío");
        }
            try {
                Long dniValue = Long.parseLong(cliente.getDni());

                // Validar que el DNI esté dentro de un rango razonable
                if (dniValue <= 0) {
                    throw new ClienteException("El valor del DNI no es correcto");
                }
            } catch (NumberFormatException e) {
                throw new ClienteException("El DNI debe ser un número válido");
            }
    }

    @Override
    public String asignarCodigoCliente() {
        return "C-" + iDaoCliente.getProximoCodigoCliente();
    }

    @Override
    public void altaCliente(Cliente cliente) throws ClienteException{
        validarDatosCliente(cliente);

        String codigo = asignarCodigoCliente();
        cliente.setCodigo(codigo);  

        if (iDaoCliente.obtenerCliente(cliente.getCodigo()) == null) {
            iDaoCliente.insertarNuevoCliente(cliente);
            System.out.println("El cliente se agregó de forma correcta");
        } else {
            System.out.println("El cliente ya existe");
        }
    }

    @Override
    public void listarClientes(String codigo, String nombre, String apellido) {
        List<Cliente> clientes = iDaoCliente.getClientes(codigo, nombre, apellido);

        for (Cliente cli : clientes) {
            System.out.println(cli.toString());
        }
    }
    
    public Cliente buscarClientePorCodigo(String codigo) {
        return iDaoCliente.obtenerCliente(codigo); 
    }
}