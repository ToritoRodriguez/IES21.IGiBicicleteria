package repositorio.dao.marca;

import java.util.List;
import modelo.producto.marca.Marca;
import negocio.abm.producto.exception.MarcaException;

/**
 *
 * @author rodri
 */

public interface IDaoMarca{
    public void insertarNuevaMarca(Marca marca) throws MarcaException;
    
    public void eliminarMarca(String codigo, String nombre);

    public void modificarMarca(String codigoMarca, Marca marcaModificada) throws MarcaException;

    public Marca obtenerMarca(String codigoMarca, String nombreMarca);
    
    public List<Marca> getMarcas(String codigoMarca, String nombreMarca);
}
