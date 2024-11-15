package repositorio.dao.producto;

import java.util.List;
import modelo.producto.Producto;
import negocio.abm.producto.exception.ProductoException;

/**
 *
 * @author rodri
 */

public interface IDaoProducto {
    public void insertarNuevoProducto(Producto producto) throws ProductoException;

    public void eliminarProducto(int idProducto, String nombreProducto) throws ProductoException;

    public void modificarProducto(int idProducto, Producto productoModificado) throws ProductoException;

    public Producto obtenerProducto(int idProducto) throws ProductoException;
    
    public Producto obtenerProductoPorNombre(String nombreProducto);
    
    public Producto obtenerProductoPorId(int idProducto);
    
    public List<Producto> getProductos(int idProducto, String nombreProducto);
}