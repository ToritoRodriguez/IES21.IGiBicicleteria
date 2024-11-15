package negocio.abm.producto;

import java.util.List;
import modelo.producto.Producto;
import modelo.proveedor.Proveedor;
import negocio.abm.producto.exception.ProductoException;
import repositorio.dao.producto.IDaoProducto;
import repositorio.dao.producto.ProductoDaoImpl;

/**
 *
 * @author rodri
 */

public class ABMProducto implements IABMProducto {
    private IDaoProducto iDaoProducto = new ProductoDaoImpl();

    private void validarDatosProducto(Producto producto) throws ProductoException {
        if (producto == null) {
            throw new ProductoException("El producto no puede ser nulo.");
        }
        if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
            throw new ProductoException("El nombre del producto no puede ser vacío.");
        }
        if (producto.getDescripcion() == null || producto.getDescripcion().isEmpty()) {
            throw new ProductoException("La descripción del producto no puede ser vacía.");
        }
        if (producto.getPrecio() <= 0) {
            throw new ProductoException("El precio del producto debe ser mayor que cero.");
        }
        if (producto.getCantidad() < 0) {
            throw new ProductoException("La cantidad del producto no puede ser negativa.");
        }
    }

    @Override
    public void altaProducto(Producto producto) throws ProductoException {
        // Validar los datos del producto
        validarDatosProducto(producto);

        // Validar que el proveedor existe antes de insertar el producto
        Proveedor proveedor = producto.getProveedor();
        if (proveedor == null || proveedor.getId() == 0) {
            throw new ProductoException("Proveedor no válido.");
        }

        // Insertar el nuevo producto en la base de datos
        iDaoProducto.insertarNuevoProducto(producto);

        // Confirmar éxito
        System.out.println("Producto " + producto.getNombre() + " dado de alta con éxito.");
    }

    @Override
    public void bajaProducto(int idProducto, String nombreProducto) throws ProductoException {
        Producto producto = null;

        // Verificar si se busca eliminar por ID o por nombre
        if (idProducto > 0) {
            producto = iDaoProducto.obtenerProducto(idProducto);
        } else if (nombreProducto != null && !nombreProducto.isEmpty()) {
            producto = iDaoProducto.obtenerProductoPorNombre(nombreProducto);
        }

        // Si el producto existe, proceder a eliminar
        if (producto != null) {
            iDaoProducto.eliminarProducto(idProducto, nombreProducto);
            String mensaje = (idProducto > 0) ? "ID " + idProducto : "nombre '" + nombreProducto + "'";
            System.out.println("Producto con " + mensaje + " eliminado exitosamente.");
        } else {
            String mensajeError = (idProducto > 0) ? "ID " + idProducto : "nombre '" + nombreProducto + "'";
            throw new ProductoException("El producto con " + mensajeError + " no existe.");
        }
    }

    @Override
    public void modificarProducto(int idProducto, Producto productoModificado) throws ProductoException {
        validarDatosProducto(productoModificado);
        Producto productoExistente = iDaoProducto.obtenerProducto(idProducto);
        if (productoExistente != null) {
            iDaoProducto.modificarProducto(idProducto, productoModificado);
            System.out.println("Producto con ID " + idProducto + " modificado exitosamente.");
        } else {
            throw new ProductoException("El producto con ID " + idProducto + " no existe.");
        }
    }

    @Override
    public List<Producto> listarProductos(int idProducto, String nombreProducto) {
        List<Producto> productos = iDaoProducto.getProductos(idProducto, nombreProducto);

        if (productos.isEmpty()) {
            System.out.println("No se encontraron productos.");
        } else {
            // Si se encuentran productos, imprimir cada uno de ellos
            for (Producto producto : productos) {
                System.out.println(producto.toString());  // Asegúrate de que el método toString() de Producto esté implementado correctamente
            }
        }

        return productos;
    }
}