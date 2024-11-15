package repositorio.dao.marca;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import repositorio.dao.ConexionDb;
import modelo.producto.marca.Marca;

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
    // Metodo para insertar
    public void insertarNuevaMarca(Marca marca) {
        String nombreMarca = marca.getMarca();
        String codigoMarca = getProximoCodigoMarca();  // Obtener el próximo código para la marca

        String sqlInsertMarca = "INSERT INTO marcas (codigo, marca) VALUES (?, ?)";

        try (PreparedStatement stmtMarca = conexionDb.obtenerConexion().prepareStatement(sqlInsertMarca)) {
            stmtMarca.setString(1, codigoMarca);  // Asignar el código a la marca
            stmtMarca.setString(2, nombreMarca);  // Asignar el nombre a la marca

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
    // Método para eliminar una marca
    public void eliminarMarca(String codigo, String nombre) {
        // Consulta para obtener el id de la marca con filtros opcionales
        String sqlMarcaId = "SELECT id FROM marcas WHERE 1 = 1";

        // Agregar condiciones según los valores no nulos
        if (codigo != null && !codigo.isEmpty()) {
            sqlMarcaId += " AND codigo = ?";
        }
        if (nombre != null && !nombre.isEmpty()) {
            sqlMarcaId += " AND marca = ?";
        }

        // Consulta para eliminar la marca por id
        String sqlDeleteMarca = "DELETE FROM marcas WHERE id = ?";

        HashMap<Integer, Object> param = new HashMap<>();
        int paramIndex = 0;

        // Agregar parámetros según los valores no nulos
        if (codigo != null && !codigo.isEmpty()) {
            param.put(paramIndex++, codigo);
        }
        if (nombre != null && !nombre.isEmpty()) {
            param.put(paramIndex++, nombre);
        }

        Integer idMarca = null;  // Variable para almacenar el ID de la marca

        conexionDb = new ConexionDb();

        try {
            // Ejecutar la consulta para obtener el id de la marca
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlMarcaId, param);
            if (rs.next()) {
                idMarca = rs.getInt("id");

                // Eliminar la marca si existe
                if (idMarca != null) {
                    param.clear();
                    param.put(0, idMarca);
                    int rowsDeletedMarca = conexionDb.ejecutarConsultaUpdate(sqlDeleteMarca, param);

                    // Si la marca fue eliminada
                    if (rowsDeletedMarca > 0) {
                        System.out.println("Marca eliminada exitosamente.");
                    } else {
                        System.out.println("No se pudo eliminar la marca con el id proporcionado.");
                    }
                } else {
                    System.out.println("Marca no encontrada, no se pudo eliminar.");
                }
            } else {
                System.out.println("No se encontró la marca con los datos proporcionados.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la marca: " + e.getMessage());
        }
    }

    @Override
    // Metodo para Modificar
    public void modificarMarca(String codigoMarca, Marca marcaModificada) {
        String sqlUpdateMarca = "UPDATE marcas SET marca = ? WHERE codigo = ?";

        try (PreparedStatement stmtUpdate = conexionDb.obtenerConexion().prepareStatement(sqlUpdateMarca)) {
            stmtUpdate.setString(1, marcaModificada.getMarca());
            stmtUpdate.setString(2, codigoMarca);  // Cambiar a String para usar el código

            int affectedRows = stmtUpdate.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Marca con código " + codigoMarca + " actualizada exitosamente.");
            } else {
                System.out.println("No se pudo actualizar la marca con código " + codigoMarca + " porque no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar la marca: " + e.getMessage());
        }
    }

    @Override
    // Listado de Marcas
    public List<Marca> getMarcas(String codigoMarca, String nombreMarca) {
        List<Marca> marcas = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM marcas WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 0;

        // Filtrar por código de la marca si se proporciona
        if (codigoMarca != null && !codigoMarca.isEmpty()) {
            sqlQuery.append(" AND codigo = ?");
            param.put(index++, codigoMarca);
        }

        // Filtrar por nombre de la marca si se proporciona
        if (nombreMarca != null && !nombreMarca.isEmpty()) {
            sqlQuery.append(" AND marca LIKE ?");
            param.put(index++, "%" + nombreMarca + "%");
        }

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param);

            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String nombre = rs.getString("marca");
                Marca marca = new Marca(nombre);
                marca.setCodigo(codigo);
                marca.setModelos(new ArrayList<>());
                marcas.add(marca);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de marcas: " + e.getMessage());
        }

        return marcas;
    }
    
    @Override
    // Sirve para la Baja y Modificacion - Lo usamos para buscar
    public Marca obtenerMarca(String codigoMarca, String nombreMarca) {
        Marca marca = null;
        StringBuilder sql = new StringBuilder("SELECT * FROM marcas WHERE 1=1");

        // Agregar filtro por código de la marca si se proporciona
        if (codigoMarca != null && !codigoMarca.isEmpty()) {
            sql.append(" AND codigo = ?");
        }

        // Agregar filtro por nombre de la marca si se proporciona
        if (nombreMarca != null && !nombreMarca.isEmpty()) {
            sql.append(" AND marca = ?");
        }

        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Establecer los parámetros
            if (codigoMarca != null && !codigoMarca.isEmpty()) {
                stmt.setString(paramIndex++, codigoMarca);
            }
            if (nombreMarca != null && !nombreMarca.isEmpty()) {
                stmt.setString(paramIndex++, nombreMarca);
            }

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Obtener el nombre de la marca
                String nombre = rs.getString("marca");

                // Crear la marca
                marca = new Marca(nombre);
                marca.setCodigo(rs.getString("codigo"));  // Asignar el código de la marca

                // Inicializar la lista de modelos
                marca.setModelos(new ArrayList<>());
            } else {
                System.out.println("No se encontró una marca con los parámetros proporcionados.");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la marca: " + e.getMessage());
        }

        return marca;
    }
    
    // Sirve para el manejo de codigos
    public String getProximoCodigoMarca() {
        String sqlNextCode = "SELECT MAX(id) AS total FROM marcas";  // Consulta para obtener el máximo id
        conexionDb = new ConexionDb();

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSql(sqlNextCode);  // Ejecutar la consulta
            if (rs.next()) {
                return "M-" + (rs.getInt("total") + 1);  // Retorna el próximo código de marca
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el próximo código de marca: " + e.getMessage());
        }

        return "M-1";  // Si no se encuentra ningún código, asigna "M-1" como código inicial
    }
}