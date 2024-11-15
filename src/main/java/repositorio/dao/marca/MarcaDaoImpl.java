package repositorio.dao.marca;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import repositorio.dao.ConexionDb;
import modelo.producto.marca.Marca;
import negocio.abm.producto.exception.MarcaException;

/**
 *
 * @author rodri
 */

public class MarcaDaoImpl implements IDaoMarca {

    private ConexionDb conexionDb;

    public MarcaDaoImpl() {
        this.conexionDb = new ConexionDb();
    }
    
    @Override
    public void insertarNuevaMarca(Marca marca) throws MarcaException {
        String nombreMarca = marca.getMarca(); 

        String sqlInsertMarca = "INSERT INTO marcas (marca) VALUES (?)";

        try (PreparedStatement stmtMarca = conexionDb.obtenerConexion().prepareStatement(sqlInsertMarca)) {
            stmtMarca.setString(1, nombreMarca); 

            int affectedRows = stmtMarca.executeUpdate(); 
            if (affectedRows > 0) {
                System.out.println("Marca " + nombreMarca + " insertada con éxito.");
            } else {
                System.out.println("Error al insertar la marca.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar la marca: " + e.getMessage());
        }
    }

    @Override
    public void eliminarMarcaPorId(int idMarca) throws MarcaException {
        String sqlDeleteId = "DELETE FROM marcas WHERE id = ?";

        try {
            if (idMarca != 0) {
                if (marcaExistePorId(idMarca)) {
                    try (PreparedStatement stmtDelete = conexionDb.obtenerConexion().prepareStatement(sqlDeleteId)) {
                        stmtDelete.setInt(1, idMarca);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Marca con id " + idMarca + " eliminada exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar la marca con id " + idMarca);
                        }
                    }
                } else {
                    System.out.println("La marca con id " + idMarca + " no existe.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la marca por ID: " + e.getMessage());
        }
    }

    @Override
    public void eliminarMarcaPorNombre(String nombreMarca) throws MarcaException{
        String sqlDeleteName = "DELETE FROM marcas WHERE marca = ?";

        try {
            if (nombreMarca != null && !nombreMarca.isEmpty()) {
                if (marcaExistePorNombre(nombreMarca)) {
                    try (PreparedStatement stmtDelete = conexionDb.obtenerConexion().prepareStatement(sqlDeleteName)) {
                        stmtDelete.setString(1, nombreMarca);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Marca con nombre " + nombreMarca + " eliminada exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar la marca con nombre " + nombreMarca);
                        }
                    }
                } else {
                    System.out.println("La marca con nombre " + nombreMarca + " no existe.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la marca por nombre: " + e.getMessage());
        }
    }

    @Override
    public void modificarMarca(int idMarca, Marca marcaModificada) throws MarcaException {
        String sqlUpdateMarca = "UPDATE marcas SET marca = ? WHERE id = ?"; 

        try {
            if (marcaExistePorId(idMarca)) {
                try (PreparedStatement stmtUpdate = conexionDb.obtenerConexion().prepareStatement(sqlUpdateMarca)) {
                    stmtUpdate.setString(1, marcaModificada.getMarca());
                    stmtUpdate.setInt(2, idMarca); 

                    int affectedRows = stmtUpdate.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Marca con id " + idMarca + " actualizada exitosamente.");
                    } else {
                        System.out.println("No se pudo actualizar la marca con id " + idMarca);
                    }
                }
            } else {
                System.out.println("La marca con id " + idMarca + " no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar la marca: " + e.getMessage());
        }
    }

    @Override
    public Marca obtenerMarca(int idMarca) {
        Marca marca = null;
        String sql = "SELECT * FROM marcas WHERE id = ?";

        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setInt(1, idMarca);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("marca");

                marca = new Marca(nombre);
                marca.setModelos(new ArrayList<>());
            } else {
                System.out.println("La marca con id " + idMarca + " no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la marca por id: " + e.getMessage());
        }

        return marca;
    }

    @Override
    public List<Marca> getMarcas(int idMarca, String nombreMarca) {
        List<Marca> marcas = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM marcas WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 1;

        if (idMarca != 0) {
            sqlQuery.append(" AND id = ?");
            param.put(index++, idMarca);
        }

        if (nombreMarca != null && !nombreMarca.isEmpty()) {
            sqlQuery.append(" AND marca LIKE ?");
            param.put(index++, "%" + nombreMarca + "%");
        }

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param);

            while (rs.next()) {
                // Obtener el id de la marca
                int id = rs.getInt("id");

                // Obtener el nombre de la marca
                String nombre = rs.getString("marca");

                // Crear una nueva instancia de Marca
                Marca marca = new Marca(nombre);
                marca.setId(id);  // Asignar el id de la marca al objeto Marca

                // Inicializar la lista de modelos (si es necesario en tu lógica)
                marca.setModelos(new ArrayList<>());

                // Agregar la marca a la lista
                marcas.add(marca);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de marcas: " + e.getMessage());
        }

        return marcas;
    }

    @Override
    public List<Marca> getMarcasComboBox() {
        List<Marca> marcas = new ArrayList<>();
        String sqlQuery = "SELECT * FROM marcas"; // Consulta para obtener todas las marcas

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSql(sqlQuery); // Ejecutar la consulta

            while (rs.next()) {
                // Obtener el id de la marca
                int id = rs.getInt("id");

                // Obtener el nombre de la marca
                String nombre = rs.getString("marca");

                // Crear una nueva instancia de Marca
                Marca marca = new Marca(nombre);
                marca.setId(id);  // Asignar el id de la marca al objeto Marca

                // Inicializar la lista de modelos (si es necesario en tu lógica)
                marca.setModelos(new ArrayList<>()); // Si necesitas los modelos asociados, lo puedes agregar aquí.

                // Agregar la marca a la lista
                marcas.add(marca);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de marcas: " + e.getMessage());
        }

        return marcas;
    }
    
    public boolean marcaExistePorId(int idMarca) throws SQLException {
        String sql = "SELECT * FROM marcas WHERE id = ?";  
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setInt(1, idMarca);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean marcaExistePorNombre(String nombreMarca) throws SQLException {
        String sql = "SELECT * FROM marcas WHERE marca = ?"; 
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setString(1, nombreMarca);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    
    @Override
    public Marca buscarMarcaPorNombre(String nombreMarca) {
        String sql = "SELECT * FROM marcas WHERE marca = ?";
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setString(1, nombreMarca);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("marca");
                return new Marca(id, nombre);  // Devuelve la marca si se encuentra
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Puedes manejar la excepción aquí
        }
        return null;  // Retorna null si no se encuentra la marca o hubo un error
    }
    
    public Marca obtenerMarcaPorId(int idMarca) throws SQLException {
        String sql = "SELECT * FROM marcas WHERE id = ?";
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setInt(1, idMarca);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombreMarca = rs.getString("marca");
                return new Marca(idMarca, nombreMarca);
            } else {
                return null;  // No se encontró la marca
            }
        }
    }
}