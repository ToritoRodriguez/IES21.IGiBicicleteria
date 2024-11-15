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

    public void eliminarProveedor(String codigo);

    public Proveedor obtenerProveedor(String codigo);

    public List<Proveedor> getProveedor(String codigo, String nombre, String apellido);
    
    public List<Proveedor> getProveedoresPorNombre(String nombre);
    
    public List<Proveedor> getProveedoresPorApellido(String apellido);
    
    public List<Proveedor> getProveedoresPorNombreFantasia(String nombreFantasia);
    
    public List<Proveedor> getProveedoresComboBox();

    public String getProximoCodigoProveedor();
}
