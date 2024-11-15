package repositorio.dao.producto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import repositorio.dao.ConexionDb;
import modelo.producto.Producto;
import negocio.abm.producto.exception.ProductoException;

import modelo.producto.Categoria;
import modelo.producto.TipoCategoria;
import modelo.producto.marca.Modelo;
import modelo.proveedor.Proveedor;
import repositorio.dao.categoria.CategoriaDaoImpl;
import repositorio.dao.modelo.ModeloDaoImpl;
import repositorio.dao.proveedor.ProveedorDaoImpl;

/**
 *
 * @author rodri
 */

public class ProductoDaoImpl implements IDaoProducto {

    private ConexionDb conexionDb;

    public ProductoDaoImpl() {
        this.conexionDb = new ConexionDb();
    }
    
    private static final String SQL_GET_PRODUCTO_BY_ID = "SELECT * FROM productos WHERE id = ?";
    private static final String SQL_GET_PRODUCTO_BY_NOMBRE = "SELECT * FROM productos WHERE nombre = ?";

    @Override
    public void insertarNuevoProducto(Producto producto) throws ProductoException {
        String nombreProducto = producto.getNombre();
        double precio = producto.getPrecio();
        String descripcion = producto.getDescripcion();
        int cantidad = producto.getCantidad();
        String pathImagen = producto.getPathImagen();

        int idCategoria = producto.getCategoria().getId();
        int idModelo = producto.getModelo().getId();
        int idProveedor = producto.getProveedor().getId(); // ID del proveedor

        String sqlInsertProducto = "INSERT INTO productos (nombre, precio, descripcion, cantidad, path_imagen, id_categoria, id_modelo, id_proveedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmtProducto = conexionDb.obtenerConexion().prepareStatement(sqlInsertProducto)) {
            stmtProducto.setString(1, nombreProducto);
            stmtProducto.setDouble(2, precio);
            stmtProducto.setString(3, descripcion);
            stmtProducto.setInt(4, cantidad);
            stmtProducto.setString(5, pathImagen);
            stmtProducto.setInt(6, idCategoria);
            stmtProducto.setInt(7, idModelo);
            stmtProducto.setInt(8, idProveedor);  // Asegúrate de que el ID del proveedor es válido

            int affectedRows = stmtProducto.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Producto " + nombreProducto + " insertado con éxito.");
            } else {
                System.out.println("Error al insertar el producto.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el producto: " + e.getMessage());
            throw new ProductoException("Error al insertar el producto: " + e.getMessage());
        }
    }

    @Override
    public void eliminarProducto(int idProducto, String nombreProducto) throws ProductoException {
        String sqlDeleteId = "DELETE FROM productos WHERE id = ?";
        String sqlDeleteName = "DELETE FROM productos WHERE nombre = ?";

        try {
            if (idProducto != 0) {
                if (productoExistePorId(idProducto)) {
                    try (PreparedStatement stmtDelete = conexionDb.obtenerConexion().prepareStatement(sqlDeleteId)) {
                        stmtDelete.setInt(1, idProducto);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Producto con ID " + idProducto + " eliminado exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar el producto con ID " + idProducto);
                        }
                    }
                } else {
                    System.out.println("El producto con ID " + idProducto + " no existe.");
                }
            } else if (nombreProducto != null && !nombreProducto.isEmpty()) {
                if (productoExistePorNombre(nombreProducto)) {
                    try (PreparedStatement stmtDelete = conexionDb.obtenerConexion().prepareStatement(sqlDeleteName)) {
                        stmtDelete.setString(1, nombreProducto);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Producto con nombre " + nombreProducto + " eliminado exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar el producto con nombre " + nombreProducto);
                        }
                    }
                } else {
                    System.out.println("El producto con nombre " + nombreProducto + " no existe.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
        }
    }

    @Override
    public void modificarProducto(int idProducto, Producto productoModificado) throws ProductoException {
        String sqlUpdateProducto = "UPDATE productos SET nombre = ?, precio = ?, descripcion = ?, cantidad = ?, "
                + "id_categoria = ?, id_modelo = ?, id_proveedor = ?, path_imagen = ? WHERE id = ?";

        try {
            if (productoExistePorId(idProducto)) {
                try (PreparedStatement stmtUpdate = conexionDb.obtenerConexion().prepareStatement(sqlUpdateProducto)) {
                    // Actualizar los campos del producto
                    stmtUpdate.setString(1, productoModificado.getNombre());
                    stmtUpdate.setDouble(2, productoModificado.getPrecio());
                    stmtUpdate.setString(3, productoModificado.getDescripcion());
                    stmtUpdate.setInt(4, productoModificado.getCantidad());

                    // Asignar las relaciones: categoría, modelo, proveedor y ruta de imagen
                    stmtUpdate.setInt(5, productoModificado.getCategoria().getId()); // ID de la categoría
                    stmtUpdate.setInt(6, productoModificado.getModelo().getId());    // ID del modelo
                    stmtUpdate.setInt(7, productoModificado.getProveedor().getId()); // ID del proveedor
                    stmtUpdate.setString(8, productoModificado.getPathImagen());     // Ruta de la imagen
                    stmtUpdate.setInt(9, idProducto); // ID del producto que se está modificando

                    // Ejecutar la actualización
                    int affectedRows = stmtUpdate.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Producto con ID " + idProducto + " actualizado exitosamente.");
                    } else {
                        System.out.println("No se pudo actualizar el producto con ID " + idProducto);
                    }
                }
            } else {
                System.out.println("El producto con ID " + idProducto + " no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el producto: " + e.getMessage());
        }
    }

    @Override
    public Producto obtenerProducto(int idProducto) {
        Producto producto = null;

        try (PreparedStatement stmtProducto = conexionDb.obtenerConexion().prepareStatement(SQL_GET_PRODUCTO_BY_ID)) {
            stmtProducto.setInt(1, idProducto);
            ResultSet rsProducto = stmtProducto.executeQuery();

            if (rsProducto.next()) {
                // Recuperar los datos básicos del producto
                String nombre = rsProducto.getString("nombre");  // Verifica que "nombre" exista en la tabla "productos"
                double precio = rsProducto.getDouble("precio");  // Verifica que "precio" exista en la tabla "productos"
                String descripcion = rsProducto.getString("descripcion");  // Verifica que "descripcion" exista en la tabla "productos"
                int cantidad = rsProducto.getInt("cantidad");  // Verifica que "cantidad" exista en la tabla "productos"
                int categoriaId = rsProducto.getInt("id_categoria");  // Verifica que "id_categoria" exista en la tabla "productos"
                int modeloId = rsProducto.getInt("id_modelo");  // Verifica que "id_modelo" exista en la tabla "productos"
                int proveedorId = rsProducto.getInt("id_proveedor");  // Verifica que "id_proveedor" exista en la tabla "productos"
                String pathImagen = rsProducto.getString("path_imagen");  // Verifica que "path_imagen" exista en la tabla "productos"

                // Obtener datos de la tabla categorias
                Categoria categoria = null;
                try (PreparedStatement stmtCategoria = conexionDb.obtenerConexion().prepareStatement("SELECT * FROM categorias WHERE id = ?")) {
                    stmtCategoria.setInt(1, categoriaId);
                    ResultSet rsCategoria = stmtCategoria.executeQuery();
                    if (rsCategoria.next()) {
                        String nombreCategoria = rsCategoria.getString("categoria");  // Verifica que "categoria" exista en la tabla "categorias"
                        String tipoCategoria = rsCategoria.getString("tipo");  // Verifica que "tipo" exista en la tabla "categorias"
                        TipoCategoria tipo = TipoCategoria.valueOf(tipoCategoria);  // Asegúrate que el valor sea un valor válido de TipoCategoria
                        categoria = new Categoria(categoriaId, nombreCategoria, tipo);
                    }
                }

                // Obtener datos de la tabla modelos
                Modelo modelo = null;
                try (PreparedStatement stmtModelo = conexionDb.obtenerConexion().prepareStatement("SELECT * FROM modelos WHERE id = ?")) {
                    stmtModelo.setInt(1, modeloId);
                    ResultSet rsModelo = stmtModelo.executeQuery();
                    if (rsModelo.next()) {
                        String nombreModelo = rsModelo.getString("modelo");  // Verifica que "nombre" exista en la tabla "modelos"
                        modelo = new Modelo(modeloId, nombreModelo, null, null, null);  // Verifica el uso de otros parámetros
                    }
                }

                // Obtener datos de la tabla proveedores
                Proveedor proveedor = null;
                try (PreparedStatement stmtProveedor = conexionDb.obtenerConexion().prepareStatement("SELECT * FROM proveedores WHERE id = ?")) {
                    stmtProveedor.setInt(1, proveedorId);
                    ResultSet rsProveedor = stmtProveedor.executeQuery();
                    if (rsProveedor.next()) {
                        String codigoProveedor = rsProveedor.getString("codigo");  // Verifica que "codigo" exista en la tabla "proveedores"
                        String nombreFantasia = rsProveedor.getString("nombre_fantasia");  // Verifica que "nombre_fantasia" exista en la tabla "proveedores"
                        String cuit = rsProveedor.getString("cuit");  // Verifica que "cuit" exista en la tabla "proveedores"
                        proveedor = new Proveedor(codigoProveedor, nombreFantasia, cuit);
                    }
                }

                // Crear el objeto Producto con las relaciones obtenidas
                producto = new Producto(nombre, precio, descripcion, cantidad, categoria, modelo, proveedor, pathImagen);
                producto.setId(idProducto); // Establece el ID en el objeto producto

            } else {
                System.out.println("No se encontró un producto con el ID " + idProducto);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el producto por ID: " + e.getMessage());
        }

        return producto;
    }

    @Override
    public List<Producto> getProductos(int idProducto, String nombreProducto) {
        List<Producto> productos = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM productos WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 1;

        try {
            if (idProducto != 0) {
                sqlQuery.append(" AND id = ?");
                param.put(index++, idProducto);
            }

            if (nombreProducto != null && !nombreProducto.isEmpty()) {
                sqlQuery.append(" AND nombre LIKE ?");
                param.put(index++, "%" + nombreProducto + "%");
            }

            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param);

            while (rs.next()) {
                int id = rs.getInt("id"); 
                String nombre = rs.getString("nombre"); 
                double precio = rs.getDouble("precio"); 
                String descripcion = rs.getString("descripcion"); 
                int cantidad = rs.getInt("cantidad"); 
                int categoriaId = rs.getInt("id_categoria"); 
                int modeloId = rs.getInt("id_modelo"); 
                int proveedorId = rs.getInt("id_proveedor"); 
                String pathImagen = rs.getString("path_imagen"); 

                Categoria categoria = null;
                try (PreparedStatement stmtCategoria = conexionDb.obtenerConexion().prepareStatement("SELECT * FROM categorias WHERE id = ?")) {
                    stmtCategoria.setInt(1, categoriaId);
                    ResultSet rsCategoria = stmtCategoria.executeQuery();
                    if (rsCategoria.next()) {
                        String nombreCategoria = rsCategoria.getString("categoria");
                        String tipoCategoria = rsCategoria.getString("tipo");
                        TipoCategoria tipo = TipoCategoria.valueOf(tipoCategoria); 
                        categoria = new Categoria(categoriaId, nombreCategoria, tipo);
                    }
                }

                Modelo modelo = null;
                try (PreparedStatement stmtModelo = conexionDb.obtenerConexion().prepareStatement("SELECT * FROM modelos WHERE id = ?")) {
                    stmtModelo.setInt(1, modeloId);
                    ResultSet rsModelo = stmtModelo.executeQuery();
                    if (rsModelo.next()) {
                        String nombreModelo = rsModelo.getString("modelo");
                        modelo = new Modelo(modeloId, nombreModelo, null, null, null); 
                    }
                }

                Proveedor proveedor = null;
                try (PreparedStatement stmtProveedor = conexionDb.obtenerConexion().prepareStatement("SELECT * FROM proveedores WHERE id = ?")) {
                    stmtProveedor.setInt(1, proveedorId);
                    ResultSet rsProveedor = stmtProveedor.executeQuery();
                    if (rsProveedor.next()) {
                        String codigoProveedor = rsProveedor.getString("codigo");
                        String nombreFantasia = rsProveedor.getString("nombre_fantasia");
                        String cuit = rsProveedor.getString("cuit");
                        proveedor = new Proveedor(codigoProveedor, nombreFantasia, cuit); 
                    }
                }

                Producto producto = new Producto(nombre, precio, descripcion, cantidad, categoria, modelo, proveedor, pathImagen);
                producto.setId(id); 

                productos.add(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de productos: " + e.getMessage());
        }

        return productos;
    }

    public boolean productoExistePorId(int idProducto) throws SQLException {
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(SQL_GET_PRODUCTO_BY_ID)) {
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    private boolean productoExistePorNombre(String nombreProducto) throws SQLException {
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(SQL_GET_PRODUCTO_BY_NOMBRE)) {
            stmt.setString(1, nombreProducto);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    
    @Override
    public Producto obtenerProductoPorNombre(String nombreProducto) {
        String sql = "SELECT * FROM productos WHERE nombre = ?";
        Producto producto = null;

        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setString(1, nombreProducto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                String descripcion = rs.getString("descripcion");
                int cantidad = rs.getInt("cantidad");
                String pathImagen = rs.getString("path_imagen");

                // Obtener las relaciones
                int idCategoria = rs.getInt("id_categoria");
                CategoriaDaoImpl categoriaDao = new CategoriaDaoImpl();
                Categoria categoria = categoriaDao.buscarCategoriaPorId(idCategoria);

                int idModelo = rs.getInt("id_modelo");
                ModeloDaoImpl modeloDao = new ModeloDaoImpl();
                Modelo modelo = modeloDao.obtenerModeloPorId(idModelo);

                int idProveedor = rs.getInt("id_proveedor");
                ProveedorDaoImpl proveedorDao = new ProveedorDaoImpl();
                Proveedor proveedor = proveedorDao.buscarProveedorPorId(idProveedor);

                // Crear el producto con las relaciones
                producto = new Producto(nombre, precio, descripcion, cantidad, categoria, modelo, proveedor, pathImagen);
                producto.setId(id);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener producto: " + e.getMessage());
        }

        return producto;
    }
    
    @Override
    public Producto obtenerProductoPorId(int idProducto) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        Producto producto = null;

        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {

            stmt.setInt(1, idProducto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id"); // Aquí capturas el ID
                    String nombre = rs.getString("nombre");
                    double precio = rs.getDouble("precio");
                    String descripcion = rs.getString("descripcion");
                    int cantidad = rs.getInt("cantidad");
                    String pathImagen = rs.getString("path_imagen");

                    // Obtener las relaciones (verificar que no sean cero o nulos)
                    int idCategoria = rs.getInt("id_categoria");
                    Categoria categoria = null;
                    if (idCategoria > 0) {
                        CategoriaDaoImpl categoriaDao = new CategoriaDaoImpl();
                        categoria = categoriaDao.buscarCategoriaPorId(idCategoria);
                    }

                    int idModelo = rs.getInt("id_modelo");
                    Modelo modelo = null;
                    if (idModelo > 0) {
                        ModeloDaoImpl modeloDao = new ModeloDaoImpl();
                        modelo = modeloDao.obtenerModeloPorId(idModelo);
                    }

                    int idProveedor = rs.getInt("id_proveedor");
                    Proveedor proveedor = null;
                    if (idProveedor > 0) {
                        ProveedorDaoImpl proveedorDao = new ProveedorDaoImpl();
                        proveedor = proveedorDao.buscarProveedorPorId(idProveedor);
                    }

                    // Crear el producto con las relaciones, incluyendo el id
                    producto = new Producto(id, nombre, precio, descripcion, cantidad, categoria, modelo, proveedor, pathImagen);
                } else {
                    System.out.println("Producto con ID " + idProducto + " no encontrado.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener producto: " + e.getMessage());
        }

        return producto;
    }
}