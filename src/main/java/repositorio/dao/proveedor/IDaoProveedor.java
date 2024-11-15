package repositorio.dao.proveedor;

import java.util.List;
import modelo.proveedor.Proveedor;

/**
 *
 * @author rodri
 */

public interface IDaoProveedor{
    public void insertarNuevoProveedor(Proveedor proveedor);

    public void modificarProveedor(String codigo, Proveedor proveedor);

    public void eliminarProveedor(String codigo, String nombre, String apellido, String nombreFantasia);

    public Proveedor obtenerProveedor(String codigo);

    public List<Proveedor> getProveedores(String codigo, String nombre, String apellido, String nombreFantasia);
    
    public List<Proveedor> getProveedoresComboBox();

    public String getProximoCodigoProveedor();
}
