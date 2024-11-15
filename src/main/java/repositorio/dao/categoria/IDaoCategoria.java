package repositorio.dao.categoria;

import java.util.List;
import modelo.producto.Categoria;
import modelo.producto.TipoCategoria;
import negocio.abm.producto.exception.CategoriaException;

/**
 *
 * @author rodri
 */

public interface IDaoCategoria {
    public void insertarNuevaCategoria(Categoria categoria) throws CategoriaException;

    public void eliminarCategoriaPorId(int idCategoria) throws CategoriaException;
    
    public void eliminarCategoriaPorNombre(String nombreCategoria) throws CategoriaException;

    public void modificarCategoria(int codigoCategoria, Categoria categoriaModificada) throws CategoriaException;

    public Categoria obtenerCategoria(int idCategoria);
    
    public boolean categoriaExistePorNombre(String nombreCategoria);
    
    public Categoria buscarCategoriaPorNombre(String nombreCategoria);
    
    public List<Categoria> getCategorias(int idCategoria, String nombreCategoria);
    
    public List<Categoria> getCategoriaPorTipo(TipoCategoria tipoCategoria);
    
    public List<Categoria> getCategoriaPorNombre(String nombreCategoria);
    
    public List<Categoria> getCategoriasComboBox();
}