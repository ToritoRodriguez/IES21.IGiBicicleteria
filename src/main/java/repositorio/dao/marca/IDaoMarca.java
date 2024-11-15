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

    public void eliminarMarcaPorNombre(String nombreMarca) throws MarcaException;
    
    public void eliminarMarcaPorId(int idMarca) throws MarcaException;

    public void modificarMarca(int idMarca, Marca marcaModificada) throws MarcaException;

    public Marca obtenerMarca(int idMarca);
    
    public Marca buscarMarcaPorNombre(String nombreMarca);
    
    public List<Marca> getMarcas(int idMarca, String nombreMarca);
    
    public List<Marca> getMarcasComboBox();
}
