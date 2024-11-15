package repositorio.dao.modelo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.producto.marca.Marca;
import repositorio.dao.ConexionDb;
import modelo.producto.marca.Modelo;
import modelo.producto.marca.Rodado;
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

    @Override
    public void insertarNuevoModelo(Modelo modelo) {
        String codigoModelo = getProximoCodigoModelo();
        String sqlInsertModelo = "INSERT INTO modelos (codigo, modelo, codigo_marca, rodado, descripcion) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmtModelo = conexionDb.obtenerConexion().prepareStatement(sqlInsertModelo);

            // Asignar valores a la consulta
            stmtModelo.setString(1, codigoModelo); 

            stmtModelo.setString(2, modelo.getModelo()); 

            stmtModelo.setString(3, modelo.getMarca().getCodigo()); 

            stmtModelo.setString(4, modelo.getRodado().toString());

            String descripcion = modelo.getDescripcion();
            if (descripcion == null || descripcion.isEmpty()) {
                descripcion = ""; 
            }
            stmtModelo.setString(5, descripcion);

            int affectedRows = stmtModelo.executeUpdate();
            System.out.println("Filas afectadas: " + affectedRows);

            if (affectedRows > 0) {
                System.out.println("Modelo insertado con éxito con código: " + codigoModelo);
            } else {
                System.out.println("Error al insertar el modelo.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el modelo: " + e.getMessage());
        }
    }

    @Override
    public void eliminarModelo(String codigoModelo, String nombreModelo) {
        // Construir la consulta dinámica
        StringBuilder sqlDelete = new StringBuilder("DELETE FROM modelos WHERE 1=1");
        HashMap<Integer, Object> param = new HashMap<>();
        int index = 1;

        try {
            // Agregar condiciones según los parámetros proporcionados
            if (codigoModelo != null && !codigoModelo.isEmpty()) {
                sqlDelete.append(" AND codigo = ?");
                param.put(index++, codigoModelo);
            }
            if (nombreModelo != null && !nombreModelo.isEmpty()) {
                sqlDelete.append(" AND modelo = ?");
                param.put(index++, nombreModelo);
            }

            // Validar que al menos un filtro esté presente
            if (param.isEmpty()) {
                System.out.println("Debe proporcionar un código o un nombre para eliminar un modelo.");
                return;
            }

            // Ejecutar la consulta
            try (PreparedStatement stmtDelete = conexionDb.obtenerConexion().prepareStatement(sqlDelete.toString())) {
                for (Map.Entry<Integer, Object> entry : param.entrySet()) {
                    stmtDelete.setObject(entry.getKey(), entry.getValue());
                }

                int affectedRows = stmtDelete.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Modelo eliminado exitosamente.");
                } else {
                    System.out.println("No se encontró un modelo con los criterios especificados.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el modelo: " + e.getMessage());
        }
    }

    @Override
    public void modificarModelo(String codigoModelo, Modelo modeloModificado) {
        // Actualizamos la consulta para utilizar el campo 'codigoMarca' en lugar de 'id_marca'
        String sqlUpdateModelo = "UPDATE modelos SET modelo = ?, codigo_marca = ?, descripcion = ?, rodado = ? WHERE codigo = ?";

        try {
            // Ejecutar la consulta de actualización
            try (PreparedStatement stmtUpdate = conexionDb.obtenerConexion().prepareStatement(sqlUpdateModelo)) {
                stmtUpdate.setString(1, modeloModificado.getModelo());

                // Usamos el código de la marca, no el ID
                stmtUpdate.setString(2, modeloModificado.getMarca().getCodigo());  // Aquí cambiamos a 'codigoMarca'

                stmtUpdate.setString(3, modeloModificado.getDescripcion());
                stmtUpdate.setString(4, modeloModificado.getRodado().toString());
                stmtUpdate.setString(5, codigoModelo);  // Usamos 'codigoModelo' como identificador

                int affectedRows = stmtUpdate.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Modelo con código " + codigoModelo + " actualizado exitosamente.");
                } else {
                    System.out.println("No se pudo actualizar el modelo con código " + codigoModelo + ". Puede que no exista o no haya cambios.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el modelo: " + e.getMessage());
        }
    }

    @Override
    public Modelo obtenerModelo(String codigoModelo, String nombreModelo, String codigoMarca, Rodado rodado) {
        Modelo modelo = null;

        // Construir consulta SQL dinámica
        StringBuilder sqlQuery = new StringBuilder("SELECT m.codigo, m.modelo, m.descripcion, m.rodado, m.codigo_marca, mar.marca AS nombre_marca ");
        sqlQuery.append("FROM modelos m ");
        sqlQuery.append("JOIN marcas mar ON m.codigo_marca = mar.codigo ");  // Usamos mar.codigo en lugar de mar.id
        sqlQuery.append("WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 0;

        // Agregar filtros dinámicos
        try {
            if (codigoModelo != null && !codigoModelo.isEmpty()) {
                sqlQuery.append(" AND m.codigo = ?");  // Usamos el código del modelo
                param.put(index++, codigoModelo); // Asumimos que el código del modelo es un String
            }

            if (nombreModelo != null && !nombreModelo.isEmpty()) {
                sqlQuery.append(" AND m.modelo LIKE ?");
                param.put(index++, "%" + nombreModelo + "%");
            }

            if (codigoMarca != null && !codigoMarca.isEmpty()) {
                sqlQuery.append(" AND mar.codigo = ?");  // Usamos el código de la marca
                param.put(index++, codigoMarca); // Asumimos que el código de marca es un String
            }

            if (rodado != null) {
                sqlQuery.append(" AND m.rodado = ?");
                param.put(index++, rodado.name());
            }

            // Ejecutar consulta
            try (ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param)) {
                if (rs.next()) {
                    // Construcción del objeto Modelo usando el código del modelo
                    String codigo = rs.getString("codigo");  // Ahora obtenemos el código del modelo
                    String nombre = rs.getString("modelo");
                    String descripcion = rs.getString("descripcion");
                    Marca marca = new Marca(rs.getString("nombre_marca")); // Obtenemos la marca con su código
                    Rodado rodadoObj = Rodado.valueOf(rs.getString("rodado"));

                    modelo = new Modelo(codigo, nombre, marca, descripcion, rodadoObj);  // Usamos el código del modelo
                } else {
                    System.out.println("No se encontró un modelo que coincida con los criterios especificados.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el modelo: " + e.getMessage());
        }

        return modelo;
    }

    @Override
    public List<Modelo> getModelos(String codigoModelo, String nombreModelo, String codigoMarca, Rodado rodado) {
        List<Modelo> modelos = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT m.codigo AS codigo_modelo, m.modelo, mar.codigo AS codigo_marca, ");
        sqlQuery.append("mar.marca AS nombre_marca, m.descripcion, m.rodado ");
        sqlQuery.append("FROM modelos m ");
        sqlQuery.append("JOIN marcas mar ON m.codigo_marca = mar.codigo ");
        sqlQuery.append("WHERE 1=1"); // Condición base para facilitar adiciones dinámicas

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 0;

        try {
            // Filtro por código del modelo
            if (codigoModelo != null && !codigoModelo.isEmpty()) {
                sqlQuery.append(" AND m.codigo = ?");
                param.put(index++, codigoModelo); // Ahora lo tratamos como String
            }

            // Filtro por nombre del modelo
            if (nombreModelo != null && !nombreModelo.isEmpty()) {
                sqlQuery.append(" AND m.modelo LIKE ?");
                param.put(index++, "%" + nombreModelo + "%");
            }

            // Filtro por código de marca
            if (codigoMarca != null && !codigoMarca.isEmpty()) {
                sqlQuery.append(" AND mar.codigo = ?");
                param.put(index++, codigoMarca); // Usar el código de la marca como String
            }

            // Filtro por rodado
            if (rodado != null) {
                sqlQuery.append(" AND m.rodado = ?");
                param.put(index++, rodado.name());
            }

            // Ejecutar la consulta con parámetros
            try (ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param)) {
                // Procesar los resultados
                while (rs.next()) {
                    // Recuperar valores de la consulta
                    String codigo = rs.getString("codigo_modelo");  // Cambiado a 'codigo' en lugar de 'codigo_modelo'
                    String nombre = rs.getString("modelo");
                    String codigoMarcaRecuperado = rs.getString("codigo_marca");
                    String nombreMarca = rs.getString("nombre_marca");
                    String descripcion = rs.getString("descripcion");
                    Rodado rodadoObjeto = Rodado.valueOf(rs.getString("rodado"));

                    // Crear la instancia de Marca
                    Marca marca = new Marca(codigoMarcaRecuperado, nombreMarca);

                    // Crear instancia de Modelo
                    Modelo modelo = new Modelo(codigo, nombre, marca, descripcion, rodadoObjeto);

                    // Agregar a la lista de modelos
                    modelos.add(modelo);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de modelos: " + e.getMessage());
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
    
    public String getProximoCodigoModelo() {
        String sqlNextCode = "SELECT MAX(id) AS total FROM modelos";
        conexionDb = new ConexionDb();

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSql(sqlNextCode);

            if (rs.next()) {
                int nextId = rs.getInt("total") + 1; // Obtiene el siguiente ID incremental
                return "M-" + Year.now().getValue() + "-" + nextId; // Genera el código en formato "M-YYYY-ID"
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el próximo código para el modelo: " + e.getMessage());
        }

        return "M-" + Year.now().getValue() + "-1"; // Código inicial en caso de error o tabla vacía
    }
}