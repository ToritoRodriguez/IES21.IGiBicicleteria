package repositorio.dao.modelo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import modelo.producto.marca.Marca;
import repositorio.dao.ConexionDb;
import modelo.producto.marca.Modelo;
import modelo.producto.marca.Rodado;
import negocio.abm.producto.exception.ModeloException;
import repositorio.dao.marca.MarcaDaoImpl;


/**
 *
 * @author rodri
 */

public class ModeloDaoImpl implements IDaoModelo {

    private ConexionDb conexionDb;

    public ModeloDaoImpl() {
        this.conexionDb = new ConexionDb();
    }
    
    private static final String SQL_GET_MODELO_BY_ID = "SELECT * FROM modelos WHERE id = ?";
    private static final String SQL_GET_MODELO_BY_NOMBRE = "SELECT * FROM modelos WHERE modelo = ?";

    @Override
    public void insertarNuevoModelo(Modelo modelo) throws ModeloException {
        String nombreModelo = modelo.getModelo();  
        Marca marca = modelo.getMarca();  
        Rodado rodado = modelo.getRodado(); 

        String sqlInsertModelo = "INSERT INTO modelos (modelo, id_marca, rodado, descripcion) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmtModelo = conexionDb.obtenerConexion().prepareStatement(sqlInsertModelo, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtModelo.setString(1, nombreModelo);  
            stmtModelo.setInt(2, marca.getId());
            stmtModelo.setString(3, rodado.toString());  
            String descripcion = modelo.getDescripcion();
            
            if (descripcion == null || descripcion.isEmpty()) {
                descripcion = "";  
            }
            stmtModelo.setString(4, descripcion);  

            int affectedRows = stmtModelo.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmtModelo.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    modelo.setId(idGenerado);
                }
                System.out.println("Modelo " + nombreModelo + " insertado con éxito.");
            } else {
                System.out.println("Error al insertar el modelo.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el modelo: " + e.getMessage());
        }
    }

    @Override
    public void eliminarModeloPorId(int idModelo) throws ModeloException {
        String sqlDeleteId = "DELETE FROM modelos WHERE id = ?";

        try {
            if (idModelo != 0) {
                if (modeloExistePorId(idModelo)) {
                    try (PreparedStatement stmtDelete = conexionDb.obtenerConexion().prepareStatement(sqlDeleteId)) {
                        stmtDelete.setInt(1, idModelo);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Modelo con ID " + idModelo + " eliminado exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar el modelo con ID " + idModelo);
                        }
                    }
                } else {
                    System.out.println("El modelo con ID " + idModelo + " no existe.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el modelo: " + e.getMessage());
        }
    }

    @Override
    public void eliminarModeloPorNombre(String nombreModelo) throws ModeloException {
        String sqlDeleteName = "DELETE FROM modelos WHERE modelo = ?";

        try {
            if (nombreModelo != null && !nombreModelo.isEmpty()) {
                if (modeloExistePorNombre(nombreModelo)) {
                    try (PreparedStatement stmtDelete = conexionDb.obtenerConexion().prepareStatement(sqlDeleteName)) {
                        stmtDelete.setString(1, nombreModelo);
                        int affectedRows = stmtDelete.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Modelo con nombre " + nombreModelo + " eliminado exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar el modelo con nombre " + nombreModelo);
                        }
                    }
                } else {
                    System.out.println("El modelo con nombre " + nombreModelo + " no existe.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el modelo: " + e.getMessage());
        }
    }

    @Override
    public void modificarModelo(int idModelo, Modelo modeloModificado) throws ModeloException {
        String sqlUpdateModelo = "UPDATE modelos SET modelo = ?, id_marca = ?, descripcion = ?, rodado = ? WHERE id = ?";

        try {
            if (modeloExistePorId(idModelo)) {
                try (PreparedStatement stmtUpdate = conexionDb.obtenerConexion().prepareStatement(sqlUpdateModelo)) {
                    stmtUpdate.setString(1, modeloModificado.getModelo());

                    // Usar el ID de la marca, no el nombre
                    stmtUpdate.setInt(2, modeloModificado.getMarca().getId());  // Cambiado aquí

                    stmtUpdate.setString(3, modeloModificado.getDescripcion());
                    stmtUpdate.setString(4, modeloModificado.getRodado().toString());
                    stmtUpdate.setInt(5, idModelo);

                    int affectedRows = stmtUpdate.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Modelo con ID " + idModelo + " actualizado exitosamente.");
                    } else {
                        System.out.println("No se pudo actualizar el modelo con ID " + idModelo);
                    }
                }
            } else {
                System.out.println("El modelo con ID " + idModelo + " no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el modelo: " + e.getMessage());
        }
    }

    @Override
    public Modelo obtenerModelo(int idModelo) {
        Modelo modelo = null;
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(SQL_GET_MODELO_BY_ID)) {
            stmt.setInt(1, idModelo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombreModelo = rs.getString("modelo"); 
                String descripcion = rs.getString("descripcion"); 
                String marcaId = rs.getString("id_marca"); 
                Marca marca = new Marca(marcaId); 

                Rodado rodado = Rodado.valueOf(rs.getString("rodado")); 

                int id = rs.getInt("id"); 
                modelo = new Modelo(id, nombreModelo, marca, descripcion, rodado); 
            } else {
                System.out.println("No se encontró un modelo con el ID " + idModelo);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el modelo por ID: " + e.getMessage());
        }
        return modelo;
    }

    @Override
    public List<Modelo> getModelos(int idModelo, String nombreModelo) {
        List<Modelo> modelos = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT m.id, m.modelo, m.id_marca, m.descripcion, m.rodado, mar.marca AS nombre_marca "); 
        sqlQuery.append("FROM modelos m ");
        sqlQuery.append("JOIN marcas mar ON m.id_marca = mar.id ");  
        sqlQuery.append("WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 1;

        try {
            if (idModelo != 0) {
                sqlQuery.append(" AND m.id = ?");
                param.put(index++, idModelo);
            }

            if (nombreModelo != null && !nombreModelo.isEmpty()) {
                sqlQuery.append(" AND m.modelo LIKE ?");
                param.put(index++, "%" + nombreModelo + "%");
            }

            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("modelo");
                Marca marca = new Marca(rs.getString("nombre_marca")); 
                Rodado rodado = Rodado.valueOf(rs.getString("rodado"));
                String descripcion = rs.getString("descripcion");

                Modelo modelo = new Modelo(id, nombre, marca, descripcion, rodado);
                modelos.add(modelo);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de modelos: " + e.getMessage());
        }

        return modelos;
    }
    
    @Override
    public List<Modelo> getModelosPorMarca(String marca) {
        List<Modelo> modelos = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT m.id, m.modelo, m.id_marca, m.descripcion, m.rodado, mar.marca AS nombre_marca ");
        sqlQuery.append("FROM modelos m ");
        sqlQuery.append("JOIN marcas mar ON m.id_marca = mar.id ");
        sqlQuery.append("WHERE mar.marca LIKE ?"); // Filtro por nombre de marca

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 0;

        try {
            param.put(index++, "%" + marca + "%"); // Agregar la marca a los parámetros de la consulta

            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("modelo");
                Marca marcaObjeto = new Marca(rs.getString("nombre_marca"));
                Rodado rodado = Rodado.valueOf(rs.getString("rodado"));
                String descripcion = rs.getString("descripcion");

                Modelo modelo = new Modelo(id, nombre, marcaObjeto, descripcion, rodado);
                modelos.add(modelo);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de modelos por marca: " + e.getMessage());
        }

        return modelos;
    }

    @Override
    public List<Modelo> getModelosPorRodado(Rodado rodado) {
        List<Modelo> modelos = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT m.id, m.modelo, m.id_marca, m.descripcion, m.rodado, mar.marca AS nombre_marca ");
        sqlQuery.append("FROM modelos m ");
        sqlQuery.append("JOIN marcas mar ON m.id_marca = mar.id ");
        sqlQuery.append("WHERE m.rodado = ?"); // Filtro por rodado

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 0;

        try {
            param.put(index++, rodado.name()); // Agregar el rodado a los parámetros de la consulta

            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("modelo");
                Marca marca = new Marca(rs.getString("nombre_marca"));
                Rodado rodadoObjeto = Rodado.valueOf(rs.getString("rodado"));
                String descripcion = rs.getString("descripcion");

                Modelo modelo = new Modelo(id, nombre, marca, descripcion, rodadoObjeto);
                modelos.add(modelo);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de modelos por rodado: " + e.getMessage());
        }

        return modelos;
    }
    
    @Override
    public List<Modelo> getModelosComboBox() {
        List<Modelo> modelos = new ArrayList<>();
        String sqlQuery = "SELECT id, modelo FROM modelos";  // Traemos id y modelo

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSql(sqlQuery);  // Ejecutamos la consulta

            while (rs.next()) {
                int id = rs.getInt("id");
                String modelo = rs.getString("modelo");

                Modelo mod = new Modelo(id, modelo);  // Creamos el objeto Modelo
                modelos.add(mod);  // Agregamos el objeto Modelo completo a la lista
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de modelos: " + e.getMessage());
        }

        return modelos;  // Devolvemos la lista de objetos Modelo
    }


    
    public boolean modeloExistePorId(int idModelo) throws SQLException {
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(SQL_GET_MODELO_BY_ID)) {
            stmt.setInt(1, idModelo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean modeloExistePorNombre(String nombreModelo) throws SQLException {
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(SQL_GET_MODELO_BY_NOMBRE)) {
            stmt.setString(1, nombreModelo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    
    public Modelo obtenerModeloPorId(int idModelo) throws SQLException {
        String sql = "SELECT * FROM modelos WHERE id = ?";
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setInt(1, idModelo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Obtener la marca
                int idMarca = rs.getInt("id_marca");
                MarcaDaoImpl marcaDao = new MarcaDaoImpl();
                Marca marca = marcaDao.obtenerMarcaPorId(idMarca);

                String modeloNombre = rs.getString("modelo");
                String descripcion = rs.getString("descripcion");
                String rodadoStr = rs.getString("rodado");
                Rodado rodado = Rodado.valueOf(rodadoStr);  

                return new Modelo(idModelo, modeloNombre, marca, descripcion, rodado);
            } else {
                return null; 
            }
        }
    }
    
    @Override
    public Modelo buscarModeloPorNombre(String nombreModelo) {
        String sql = "SELECT * FROM modelos WHERE modelo = ?";
        Modelo modelo = null;

        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setString(1, nombreModelo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String modeloNombre = rs.getString("modelo");
                String descripcion = rs.getString("descripcion");
                Rodado rodado = Rodado.valueOf(rs.getString("rodado"));  
                int idMarca = rs.getInt("id_marca");
                MarcaDaoImpl marcaDao = new MarcaDaoImpl();
                Marca marca = marcaDao.obtenerMarcaPorId(idMarca);

                modelo = new Modelo(id, modeloNombre, marca, descripcion, rodado);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar modelo: " + e.getMessage());
        }

        return modelo;
    }
}