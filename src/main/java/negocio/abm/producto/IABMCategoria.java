package negocio.abm.producto;

import java.sql.SQLException;
import java.util.List;
import modelo.producto.Categoria;
import negocio.abm.producto.exception.CategoriaException;

/**
 *
 * @author rodri
 */

public interface IABMCategoria {
    public void altaCategoria(Categoria categoria, int tipoOpcion) throws CategoriaException;

    public void bajaCategoriaPorNombre(String nombreCategoria) throws CategoriaException;
    
    public void bajaCategoriaPorId(int idCategoria) throws CategoriaException;

    public void modificarCategoria(int idCategoria, Categoria categoriaModificada) throws CategoriaException;
    
    public List<Categoria> listarCategorias(int idCategoria, String nombreCategoria);
}