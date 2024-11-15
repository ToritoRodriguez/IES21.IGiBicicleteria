package repositorio.dao.vendedor;

import java.util.List;
import modelo.vendedor.Vendedor;

/**
 *
 * @author rodri
 */

public interface IDaoVendedor {
    public void insertarNuevoVendedor(Vendedor vendedor);

    public void eliminarVendedor(String codigo);

    public void modificarVendedor(String codigo, Vendedor vendedor);

    public Vendedor obtenerVendedor(String codigo);

    public List<Vendedor> getVendedores(String codigo, String nombre, String apellido);
    
    public List<Vendedor> getVendedoresPorNombre(String nombre);
    
    public List<Vendedor> getVendedoresPorApellido(String apellido);
    
    public List<Vendedor> getVendedoresPorSucursal(String sucursal);

    public String getProximoCodigoVendedor();
}
