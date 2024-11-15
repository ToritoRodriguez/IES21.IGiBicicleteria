package repositorio.dao.modelo;
import java.util.List;
import modelo.producto.marca.Modelo;
import modelo.producto.marca.Rodado;
import negocio.abm.producto.exception.ModeloException;

/**
 *
 * @author rodri
 */

public interface IDaoModelo {
    public void insertarNuevoModelo(Modelo modelo) throws ModeloException;

    public void eliminarModeloPorId(int idModelo) throws ModeloException;
    
    public void eliminarModeloPorNombre(String nombreModelo) throws ModeloException;
    
    public void modificarModelo(int codigoModelo, Modelo modeloModificado) throws ModeloException;

    public Modelo obtenerModelo(int codigoModelo);
    
    public Modelo buscarModeloPorNombre(String nombreModelo);
    
    public List<Modelo> getModelos(int idModelo, String nombreModelo);
    
    public List<Modelo> getModelosPorRodado(Rodado rodado);
    
    public List<Modelo> getModelosPorMarca(String marca);
    
    public List<Modelo> getModelosComboBox();
}