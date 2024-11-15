package negocio.abm.producto;

import java.util.List;
import modelo.producto.marca.Marca;
import negocio.abm.producto.exception.MarcaException;

/**
 *
 * @author rodri
 */

public interface IABMMarca {
    public void altaMarca(Marca marca) throws MarcaException;

    public void bajaMarcaPorId(int idMarca) throws MarcaException;
    
    public void bajaMarcaPorNombre(String nombreMarca) throws MarcaException;

    public void modificarMarca(int idMarca, Marca marcaModificada) throws MarcaException;

    public List<Marca> listarMarcas(int idMarca, String nombreMarca);
}
