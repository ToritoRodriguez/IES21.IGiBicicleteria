package repositorio.dao.proveedor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import modelo.proveedor.Proveedor;
import repositorio.dao.ConexionDb;

/**
 *
 * @author rodri
 */

public class ProveedorDaoImpl implements IDaoProveedor{

    private ConexionDb conexionDb;

    public ProveedorDaoImpl() {
        this.conexionDb = new ConexionDb();
    }

    @Override
    // Metodo para insertar
    public void insertarNuevoProveedor(Proveedor proveedor) {
        // Obtener el próximo código de proveedor
        String codigoProveedor = getProximoCodigoProveedor();

        // Consultas SQL para insertar en las tablas correspondientes
        String sqlPersona = "INSERT INTO personas (nombre, apellido, dni, email, telefono) VALUES (?, ?, ?, ?, ?)";
        String sqlProveedor = "INSERT INTO proveedores (id_persona, codigo, nombre_fantasia, cuit) VALUES (?, ?, ?, ?)";

        conexionDb = new ConexionDb();
        try {
            // Preparar la consulta para insertar la persona
            PreparedStatement stmtPersona = conexionDb.obtenerConexion().prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            stmtPersona.setString(1, proveedor.getNombre());
            stmtPersona.setString(2, proveedor.getApellido());
            stmtPersona.setString(3, proveedor.getDni());
            stmtPersona.setString(4, proveedor.getEmail());
            stmtPersona.setString(5, proveedor.getTelefono());

            // Ejecutar la inserción de la persona
            int affectedRowsPersona = stmtPersona.executeUpdate();

            if (affectedRowsPersona > 0) {
                // Obtener el ID de la persona insertada
                ResultSet generatedKeysPersona = stmtPersona.getGeneratedKeys();
                if (generatedKeysPersona.next()) {
                    int personId = generatedKeysPersona.getInt(1);

                    // Preparar la consulta para insertar el proveedor
                    PreparedStatement stmtProveedor = conexionDb.obtenerConexion().prepareStatement(sqlProveedor);
                    stmtProveedor.setInt(1, personId);
                    stmtProveedor.setString(2, codigoProveedor);
                    stmtProveedor.setString(3, proveedor.getNombreFantasia());
                    stmtProveedor.setString(4, proveedor.getCuit());

                    // Ejecutar la inserción del proveedor
                    int affectedRowsProveedor = stmtProveedor.executeUpdate();

                    if (affectedRowsProveedor > 0) {
                        System.out.println("Nuevo proveedor insertado con éxito con código: " + codigoProveedor);
                    } else {
                        System.out.println("Error al insertar el proveedor.");
                    }
                }
            } else {
                System.out.println("Error al insertar la persona.");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el proveedor: " + e.getMessage());
        }
    }

    @Override
    // Metodo para Eliminar
    public void eliminarProveedor(String codigo, String nombre, String apellido, String nombreFantasia) {
        // Consulta para obtener el id_persona asociado al proveedor, con filtros opcionales
        String sqlProveedorId = "SELECT id_persona FROM proveedores p "
                + "INNER JOIN personas pe ON pe.id = p.id_persona "
                + "WHERE 1 = 1 ";

        // Agregar condiciones dinámicamente según los parámetros no nulos
        if (codigo != null && !codigo.isEmpty()) {
            sqlProveedorId += " AND p.codigo = ?";
        }
        if (nombre != null && !nombre.isEmpty()) {
            sqlProveedorId += " AND pe.nombre = ?";
        }
        if (apellido != null && !apellido.isEmpty()) {
            sqlProveedorId += " AND pe.apellido = ?";
        }
        if (nombreFantasia != null && !nombreFantasia.isEmpty()) {
            sqlProveedorId += " AND p.nombre_fantasia = ?";
        }

        // Consultas para eliminar el proveedor y la persona asociada
        String sqlDeletePerson = "DELETE FROM personas WHERE id = ?";
        String sqlDeleteProveedor = "DELETE FROM proveedores WHERE codigo = ?";

        HashMap<Integer, Object> param = new HashMap<>();
        int paramIndex = 0;

        // Agregar parámetros según los valores no nulos
        if (codigo != null && !codigo.isEmpty()) {
            param.put(paramIndex++, codigo);
        }
        if (nombre != null && !nombre.isEmpty()) {
            param.put(paramIndex++, nombre);
        }
        if (apellido != null && !apellido.isEmpty()) {
            param.put(paramIndex++, apellido);
        }
        if (nombreFantasia != null && !nombreFantasia.isEmpty()) {
            param.put(paramIndex++, nombreFantasia);
        }

        Integer idPersona = null;  // Variable para almacenar el ID de la persona

        conexionDb = new ConexionDb();

        try {
            // Ejecutar la consulta para obtener el id_persona
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlProveedorId, param);
            if (rs.next()) {
                idPersona = rs.getInt("id_persona");

                // Eliminar el proveedor si existe
                if (idPersona != null) {
                    // Primero eliminar el proveedor
                    param.clear();
                    param.put(0, codigo);
                    int rowsDeletedProveedor = conexionDb.ejecutarConsultaUpdate(sqlDeleteProveedor, param);

                    // Si el proveedor fue eliminado, eliminar también la persona asociada
                    if (rowsDeletedProveedor > 0) {
                        param.clear();
                        param.put(0, idPersona);
                        int rowsDeletedPerson = conexionDb.ejecutarConsultaUpdate(sqlDeletePerson, param);

                        if (rowsDeletedPerson > 0) {
                            System.out.println("El proveedor y la persona asociada se eliminaron exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar la persona asociada al proveedor.");
                        }
                    } else {
                        System.out.println("No se encontró el proveedor con los datos proporcionados.");
                    }
                } else {
                    System.out.println("Proveedor no encontrado, no se pudo eliminar.");
                }
            } else {
                System.out.println("No se encontró el proveedor con los datos proporcionados.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el proveedor: " + e.getMessage());
        }
    }
    
    @Override
    // Metodo para Modificar
    public void modificarProveedor(String codigo, Proveedor proveedor) {
        conexionDb = new ConexionDb();

        String sqlUpdateProveedor = "UPDATE proveedores SET nombre_fantasia = ?, cuit = ? WHERE codigo = ?";
        HashMap<Integer, Object> paramProveedor = new HashMap<>();
        paramProveedor.put(0, proveedor.getNombreFantasia());
        paramProveedor.put(1, proveedor.getCuit());
        paramProveedor.put(2, codigo);  

        try {
            conexionDb.ejecutarConsultaUpdate(sqlUpdateProveedor, paramProveedor);

            String sqlUpdatePersona = "UPDATE personas SET nombre = ?, apellido = ?, dni = ?, email = ?, telefono = ? "
                    + "WHERE id = (SELECT id_persona FROM proveedores WHERE codigo = ?)";

            HashMap<Integer, Object> paramPersona = new HashMap<>();
            paramPersona.put(0, proveedor.getNombre());
            paramPersona.put(1, proveedor.getApellido());
            paramPersona.put(2, proveedor.getDni());
            paramPersona.put(3, proveedor.getEmail());
            paramPersona.put(4, proveedor.getTelefono());
            paramPersona.put(5, codigo);  
            conexionDb.ejecutarConsultaUpdate(sqlUpdatePersona, paramPersona);

            System.out.println("El proveedor se actualizó con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al modificar el proveedor: " + e.getMessage());
        }
    }

    @Override
    // Listado de Proveedores
    public List<Proveedor> getProveedores(String codigo, String nombre, String apellido, String nombreFantasia) {
        List<Proveedor> proveedores = new ArrayList<>();

        String sqlProveedores = "SELECT pr.codigo, p.nombre, p.apellido, p.dni, p.telefono, p.email, pr.nombre_fantasia, pr.cuit "
                + "FROM proveedores pr "
                + "INNER JOIN personas p ON p.id = pr.id_persona "
                + "WHERE 1 = 1";

        if (codigo != null && !codigo.isEmpty()) {
            sqlProveedores += " AND pr.codigo = ?";
        }
        if (nombre != null && !nombre.isEmpty()) {
            sqlProveedores += " AND p.nombre = ?";
        }
        if (apellido != null && !apellido.isEmpty()) {
            sqlProveedores += " AND p.apellido = ?";
        }
        if (nombreFantasia != null && !nombreFantasia.isEmpty()) {
            sqlProveedores += " AND pr.nombre_fantasia = ?";
        }

        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            int index = 0;

            if (codigo != null && !codigo.isEmpty()) {
                param.put(index++, codigo);
            }
            if (nombre != null && !nombre.isEmpty()) {
                param.put(index++, nombre);
            }
            if (apellido != null && !apellido.isEmpty()) {
                param.put(index++, apellido);
            }
            if (nombreFantasia != null && !nombreFantasia.isEmpty()) {
                param.put(index++, nombreFantasia);
            }

            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlProveedores, param);

            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                        rs.getString("codigo"),
                        rs.getString("cuit"),
                        rs.getString("nombre_fantasia"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                proveedores.add(proveedor);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de proveedores: " + e.getMessage());
        }

        return proveedores;
    }
    
    @Override
    // Sirve para la Baja y Modificacion - Lo usamos para buscar
    public Proveedor obtenerProveedor(String codigo) {
        String sqlProveedor = "SELECT * FROM proveedores pr "
                + "INNER JOIN personas p ON p.id = pr.id_persona "
                + "WHERE pr.codigo = ?";

        Proveedor proveedor = null;
        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(0, codigo);

            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlProveedor, param);

            if (rs.next()) {
                proveedor = new Proveedor(
                        rs.getString("cuit"),
                        rs.getString("nombre_fantasia"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el proveedor: " + e.getMessage());
        }

        return proveedor;
    }
    
    @Override
    // Listado de Proveedores para Lista desplegable
    public List<Proveedor> getProveedoresComboBox() {
        List<Proveedor> proveedores = new ArrayList<>();

        String sqlQuery = "SELECT pr.codigo, pr.nombre_fantasia FROM proveedores pr";

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSql(sqlQuery);

            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String nombreFantasia = rs.getString("nombre_fantasia");

                Proveedor proveedor = new Proveedor(codigo, nombreFantasia);
                proveedores.add(proveedor);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de proveedores: " + e.getMessage());
        }

        return proveedores;
    }
    
    // Sirve para el manejo de codigos
    public String getProximoCodigoProveedor() {
        String sqlNextCode = "SELECT MAX(id) AS total FROM proveedores";
        conexionDb = new ConexionDb();

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSql(sqlNextCode);

            if (rs.next()) {
                int nextId = rs.getInt("total") + 1;
                return "P-" + Year.now().getValue() + "-" + nextId;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el próximo código: " + e.getMessage());
        }

        return "P-" + Year.now().getValue() + "-1";
    }
}
