package repositorio.dao.vendedor;

import java.util.List;
import modelo.vendedor.Vendedor;

/**
 *
 * @author rodri
 */

public interface IDaoVendedor {
    public void insertarNuevoVendedor(Vendedor vendedor);

    public void eliminarVendedor(String codigo, String nombre, String apellido, String sucursal);

    public void modificarVendedor(String codigo, Vendedor vendedor);

    public List<Vendedor> getVendedores(String codigo, String nombre, String apellido, String sucursal);
    
    public Vendedor obtenerVendedor(String codigo);

    public String getProximoCodigoVendedor();
}
