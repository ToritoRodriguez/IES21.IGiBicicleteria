package repositorio.dao.categoria;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import repositorio.dao.ConexionDb;
import modelo.producto.Categoria;
import modelo.producto.TipoCategoria;
import negocio.abm.producto.exception.CategoriaException;

/**
 *
 * @author rodri
 */

public class CategoriaDaoImpl implements IDaoCategoria {

    private ConexionDb conexionDb;

    public CategoriaDaoImpl() {
        this.conexionDb = new ConexionDb();
    }
    
    private static final String SQL_INSERT_CATEGORIA = "INSERT INTO categorias (categoria, tipo) VALUES (?, ?)";
    private static final String SQL_GET_CATEGORIA_BY_ID = "SELECT * FROM categorias WHERE id = ?";

    @Override
    public void insertarNuevaCategoria(Categoria categoria) {
        String nombreCategoria = categoria.getCategoria();  
        TipoCategoria tipoCategoria = categoria.getTipo();  
        if (nombreCategoria == null || nombreCategoria.isEmpty()) {
            System.out.println("El nombre de la categoría no puede ser vacío.");
            return;
        }
        if (tipoCategoria == null) {
            System.out.println("El tipo de categoría no puede ser nulo.");
            return;
        }

        try (PreparedStatement stmtCategoria = conexionDb.obtenerConexion().prepareStatement(SQL_INSERT_CATEGORIA)) {
            stmtCategoria.setString(1, nombreCategoria);
            stmtCategoria.setString(2, tipoCategoria.toString());  
            int affectedRows = stmtCategoria.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Categoría " + nombreCategoria + " insertada con éxito.");
            } else {
                System.out.println("Error al insertar la categoría.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar la categoría: " + e.getMessage());
        }
    }

    @Override
    public void eliminarCategoriaPorId(int idCategoria) throws CategoriaException {
        String sqlDeleteId = "DELETE FROM categorias WHERE id = ?";

        try {
            if (idCategoria != 0) {
                if (categoriaExistePorId(idCategoria)) {
                    try (PreparedStatement stmtDelete = conexionDb.obtenerConexion().prepareStatement(sqlDeleteId)) {
                        stmtDelete.setInt(1, idCategoria);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Categoría con id " + idCategoria + " eliminada exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar la categoría con id " + idCategoria);
                        }
                    }
                } else {
                    System.out.println("La categoría con id " + idCategoria + " no existe.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la categoría por id: " + e.getMessage());
        }
    }

    @Override
    public void eliminarCategoriaPorNombre(String nombreCategoria) throws CategoriaException{
        String sqlDeleteName = "DELETE FROM categorias WHERE categoria = ?";

        try {
            if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
                if (categoriaExistePorNombre(nombreCategoria)) {
                    try (PreparedStatement stmtDelete = conexionDb.obtenerConexion().prepareStatement(sqlDeleteName)) {
                        stmtDelete.setString(1, nombreCategoria);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Categoría con nombre " + nombreCategoria + " eliminada exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar la categoría con nombre " + nombreCategoria);
                        }
                    }
                } else {
                    System.out.println("La categoría con nombre " + nombreCategoria + " no existe.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la categoría por nombre: " + e.getMessage());
        }
    }
    
    @Override
    public void modificarCategoria(int idCategoria, Categoria categoriaModificada) {
        String sqlUpdateCategoria = "UPDATE categorias SET categoria = ?, tipo = ? WHERE id = ?";

        try {
            if (categoriaExistePorId(idCategoria)) {
                try (PreparedStatement stmtUpdate = conexionDb.obtenerConexion().prepareStatement(sqlUpdateCategoria)) {
                    stmtUpdate.setString(1, categoriaModificada.getCategoria()); 
                    stmtUpdate.setString(2, categoriaModificada.getTipo() != null ? categoriaModificada.getTipo().toString() : ""); 
                    stmtUpdate.setInt(3, idCategoria); 

                    int affectedRows = stmtUpdate.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Categoría con id " + idCategoria + " actualizada exitosamente.");
                    } else {
                        System.out.println("No se pudo actualizar la categoría con id " + idCategoria);
                    }
                }
            } else {
                System.out.println("La categoría con id " + idCategoria + " no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar la categoría: " + e.getMessage());
        }
    }

    @Override
    public Categoria obtenerCategoria(int idCategoria) {
        Categoria categoria = null;
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(SQL_GET_CATEGORIA_BY_ID)) {
            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombreCategoria = rs.getString("categoria");
                String tipo = rs.getString("tipo");
                TipoCategoria tipoCategoria = TipoCategoria.valueOf(tipo);

                int id = rs.getInt("id");  
                categoria = new Categoria(id, nombreCategoria, tipoCategoria);  
            } else {
                System.out.println("No se encontró una categoría con el ID " + idCategoria);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la categoría por ID: " + e.getMessage());
        }
        return categoria;
    }
    
    @Override
    public List<Categoria> getCategorias(int idCategoria, String nombreCategoria) {
        List<Categoria> categorias = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM categorias WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 1;

        try {
            if (idCategoria != 0) {
                sqlQuery.append(" AND id = ?");
                param.put(index++, idCategoria);
            }

            if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
                sqlQuery.append(" AND categoria LIKE ?");
                param.put(index++, "%" + nombreCategoria + "%");
            }

            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("categoria");
                String tipo = rs.getString("tipo");

                TipoCategoria tipoCategoria = TipoCategoria.valueOf(tipo);
                Categoria categoria = new Categoria(id, nombre, tipoCategoria);  
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de categorías: " + e.getMessage());
        }

        return categorias;
    }
    
    @Override
    public List<Categoria> getCategoriaPorTipo(TipoCategoria tipoCategoria) {
        List<Categoria> categorias = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM categorias WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 0;

        try {
            if (tipoCategoria != null) {
                sqlQuery.append(" AND tipo = ?");
                param.put(index++, tipoCategoria.name());  // Usamos el nombre del enum
            }

            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("categoria");
                String tipo = rs.getString("tipo");

                TipoCategoria tipoCat = TipoCategoria.valueOf(tipo);  // Convertimos el String a TipoCategoria
                Categoria categoria = new Categoria(id, nombre, tipoCat);
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las categorías por tipo: " + e.getMessage());
        }

        return categorias;
    }

    @Override
    public List<Categoria> getCategoriaPorNombre(String nombreCategoria) {
        List<Categoria> categorias = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM categorias WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 0;

        try {
            if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
                sqlQuery.append(" AND categoria LIKE ?");
                param.put(index++, "%" + nombreCategoria + "%");  // Usamos LIKE para permitir búsqueda parcial
            }

            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("categoria");
                String tipo = rs.getString("tipo");

                TipoCategoria tipoCat = TipoCategoria.valueOf(tipo);  // Convertimos el String a TipoCategoria
                Categoria categoria = new Categoria(id, nombre, tipoCat);
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las categorías por nombre: " + e.getMessage());
        }

        return categorias;
    }
    
    @Override
    public List<Categoria> getCategoriasComboBox() {
        List<Categoria> categorias = new ArrayList<>();
        String sqlQuery = "SELECT id, categoria, tipo FROM categorias";  // Traemos todo (id, nombre y tipo)

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSql(sqlQuery);  // Ejecutamos la consulta

            while (rs.next()) {
                int id = rs.getInt("id");
                String categoria = rs.getString("categoria");
                String tipo = rs.getString("tipo");

                TipoCategoria tipoCategoria = TipoCategoria.valueOf(tipo);  // Asumiendo que tienes un enum TipoCategoria
                Categoria cat = new Categoria(id, categoria, tipoCategoria);  // Creamos la categoría
                categorias.add(cat);  // Agregamos la categoría completa a la lista
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de categorías: " + e.getMessage());
        }

        return categorias;  // Devolvemos la lista de objetos Categoria
    }

    public boolean categoriaExistePorId(int idCategoria) {
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(SQL_GET_CATEGORIA_BY_ID)) {
            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la categoría por ID: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean categoriaExistePorNombre(String nombreCategoria) {
        String sql = "SELECT * FROM categorias WHERE categoria = ?";  
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setString(1, nombreCategoria);  
            ResultSet rs = stmt.executeQuery();  

            return rs.next();  
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la categoría por nombre: " + e.getMessage());
            return false; 
        }
    }
    
    @Override
    public Categoria buscarCategoriaPorNombre(String nombreCategoria){
        String sql = "SELECT * FROM categorias WHERE categoria = ?";
        Categoria categoria = null;

        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setString(1, nombreCategoria);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String categoriaNombre = rs.getString("categoria");
                TipoCategoria tipo = TipoCategoria.valueOf(rs.getString("tipo"));
                categoria = new Categoria(id, categoriaNombre, tipo);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar categoría: " + e.getMessage());
        }

        return categoria;
    }
    
    public Categoria buscarCategoriaPorId(int idCategoria) throws SQLException {
        String sql = "SELECT * FROM categorias WHERE id = ?";
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String categoriaNombre = rs.getString("categoria");
                String tipoStr = rs.getString("tipo");  // Asumiendo que 'tipo' es un string en la base de datos
                TipoCategoria tipo = TipoCategoria.valueOf(tipoStr);  // Asumiendo que 'TipoCategoria' es un enum

                return new Categoria(idCategoria, categoriaNombre, tipo);
            } else {
                return null;  // Si no se encuentra la categoría
            }
        }
    }
}