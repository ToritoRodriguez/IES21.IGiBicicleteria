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
    
    private static final String SQL_INSERT_PERSONA = "INSERT INTO personas (nombre, apellido, dni, email, telefono) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_PROVEEDOR = "INSERT INTO proveedores (id_persona, codigo, nombre_fantasia, cuit) VALUES (?, ?, ?, ?)";

    @Override
    public void insertarNuevoProveedor(Proveedor proveedor) {
        String codigoProveedor = getProximoCodigoProveedor();

        conexionDb = new ConexionDb();
        try {
            PreparedStatement stmtPersona = conexionDb.obtenerConexion().prepareStatement(SQL_INSERT_PERSONA, Statement.RETURN_GENERATED_KEYS);
            stmtPersona.setString(1, proveedor.getNombre());
            stmtPersona.setString(2, proveedor.getApellido());
            stmtPersona.setString(3, proveedor.getDni());
            stmtPersona.setString(4, proveedor.getTelefono());
            stmtPersona.setString(5, proveedor.getEmail());

            int affectedRowsPersona = stmtPersona.executeUpdate();

            if (affectedRowsPersona > 0) {
                ResultSet generatedKeysPersona = stmtPersona.getGeneratedKeys();
                if (generatedKeysPersona.next()) {
                    int personId = generatedKeysPersona.getInt(1);

                    PreparedStatement stmtProveedor = conexionDb.obtenerConexion().prepareStatement(SQL_INSERT_PROVEEDOR);
                    stmtProveedor.setInt(1, personId);
                    stmtProveedor.setString(2, codigoProveedor);
                    stmtProveedor.setString(3, proveedor.getNombreFantasia());
                    stmtProveedor.setString(4, proveedor.getCuit());

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
    public void eliminarProveedor(String codigo) {
        String sqlProveedorId = "SELECT id_persona FROM proveedores WHERE codigo = ?";
        String sqlDeletePerson = "DELETE FROM personas WHERE id = ?";
        String sqlDeleteProveedor = "DELETE FROM proveedores WHERE codigo = ?";
        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(0, codigo);  

            System.out.println("Ejecutando consulta para obtener el ID de la persona con el código: " + codigo);

            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlProveedorId, param);
            if (rs.next()) {
                int personId = rs.getInt("id_persona");
                System.out.println("ID de la persona asociada al proveedor encontrado: " + personId);

                param.clear();
                param.put(0, codigo);
                System.out.println("Eliminando proveedor con código: " + codigo);
                conexionDb.ejecutarConsultaUpdate(sqlDeleteProveedor, param);

                param.clear();
                param.put(0, personId);
                System.out.println("Eliminando persona con ID: " + personId);
                conexionDb.ejecutarConsultaUpdate(sqlDeletePerson, param);

                System.out.println("El proveedor y la persona asociada se eliminaron exitosamente.");
            } else {
                System.out.println("Proveedor no encontrado para el código: " + codigo);
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el proveedor: " + e.getMessage());
        }
    }
    
    @Override
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
    // Listado de Proveedores
    public List<Proveedor> getProveedor(String codigo, String nombre, String apellido) {
        List<Proveedor> proveedores = new ArrayList<>();

        // Consulta SQL básica
        String sqlProveedores = "SELECT pr.codigo, pr.nombre_fantasia, p.nombre, p.apellido, p.dni, p.telefono, p.email, pr.cuit "
                + "FROM proveedores pr "
                + "INNER JOIN personas p ON p.id = pr.id_persona "
                + "WHERE 1 = 1";

        // Construcción dinámica de la consulta SQL según los parámetros de búsqueda.
        if (codigo != null && !codigo.isEmpty()) {
            sqlProveedores += " AND pr.codigo = ?";
        }
        if (nombre != null && !nombre.isEmpty()) {
            sqlProveedores += " AND p.nombre = ?";
        }
        if (apellido != null && !apellido.isEmpty()) {
            sqlProveedores += " AND p.apellido = ?";
        }

        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            int index = 1;

            // Agregar los parámetros de la consulta según los filtros proporcionados.
            if (codigo != null && !codigo.isEmpty()) {
                param.put(index++, codigo);
            }
            if (nombre != null && !nombre.isEmpty()) {
                param.put(index++, nombre);
            }
            if (apellido != null && !apellido.isEmpty()) {
                param.put(index++, apellido);
            }

            // Ejecutar la consulta con los parámetros especificados.
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlProveedores, param);

            // Procesar los resultados de la consulta.
            while (rs.next()) {
                // Crear el objeto Proveedor con los datos recuperados
                Proveedor proveedor = new Proveedor(
                        rs.getString("codigo"), // Código del proveedor
                        rs.getString("cuit"), // CUIT del proveedor
                        rs.getString("nombre_fantasia"), // Nombre de fantasía
                        rs.getString("nombre"), // Nombre de la persona
                        rs.getString("apellido"), // Apellido de la persona
                        rs.getString("dni"), // DNI
                        rs.getString("telefono"), // Teléfono
                        rs.getString("email") // Email
                );
                proveedores.add(proveedor);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de proveedores: " + e.getMessage());
        }

        return proveedores;
    }

    @Override
    public List<Proveedor> getProveedoresComboBox() {
        List<Proveedor> proveedores = new ArrayList<>();

        String sqlQuery = "SELECT pr.codigo, pr.nombre_fantasia FROM proveedores pr";  // Traemos código y nombre de fantasía

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSql(sqlQuery);  // Ejecutamos la consulta

            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String nombreFantasia = rs.getString("nombre_fantasia");

                Proveedor proveedor = new Proveedor(codigo, nombreFantasia);  // Creamos el objeto Proveedor
                proveedores.add(proveedor);  // Agregamos el objeto Proveedor completo a la lista
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de proveedores: " + e.getMessage());
        }

        return proveedores;  // Devolvemos la lista de objetos Proveedor
    }


    
    @Override
    public List<Proveedor> getProveedoresPorNombre(String nombre) {
        List<Proveedor> proveedores = new ArrayList<>();

        // Consulta SQL filtrada por nombre
        String sqlProveedores = "SELECT pr.codigo, pr.nombre_fantasia, p.nombre, p.apellido, p.dni, p.telefono, p.email, pr.cuit "
                + "FROM proveedores pr "
                + "INNER JOIN personas p ON p.id = pr.id_persona "
                + "WHERE p.nombre LIKE ?";  // Utilizamos LIKE para permitir búsqueda parcial de nombre

        // Crear una nueva instancia de conexión
        conexionDb = new ConexionDb();

        try {
            // Parametrizar la búsqueda con un comodín
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(0, "%" + nombre + "%");  // Utilizamos "%" para realizar una búsqueda parcial por nombre

            // Ejecutar la consulta con el parámetro correspondiente
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlProveedores, param);

            // Procesar los resultados de la consulta
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
            System.out.println("Error al obtener la lista de proveedores por nombre: " + e.getMessage());
        }

        return proveedores;
    }


    @Override
    public List<Proveedor> getProveedoresPorApellido(String apellido) {
        List<Proveedor> proveedores = new ArrayList<>();

        // Consulta SQL filtrada por apellido
        String sqlProveedores = "SELECT pr.codigo, pr.nombre_fantasia, p.nombre, p.apellido, p.dni, p.telefono, p.email, pr.cuit "
                + "FROM proveedores pr "
                + "INNER JOIN personas p ON p.id = pr.id_persona "
                + "WHERE p.apellido = ?";  // Filtramos solo por apellido

        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(0, apellido);  // Asignamos el parámetro de apellido

            // Ejecutar la consulta con el parámetro correspondiente
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlProveedores, param);

            // Procesar los resultados de la consulta
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
            System.out.println("Error al obtener la lista de proveedores por apellido: " + e.getMessage());
        }

        return proveedores;
    }

    @Override
    public List<Proveedor> getProveedoresPorNombreFantasia(String nombreFantasia) {
        List<Proveedor> proveedores = new ArrayList<>();

        // Consulta SQL filtrada por nombre de fantasía
        String sqlProveedores = "SELECT pr.codigo, pr.nombre_fantasia, p.nombre, p.apellido, p.dni, p.telefono, p.email, pr.cuit "
                + "FROM proveedores pr "
                + "INNER JOIN personas p ON p.id = pr.id_persona "
                + "WHERE pr.nombre_fantasia LIKE ?";  // Usamos LIKE para permitir búsqueda parcial

        conexionDb = new ConexionDb();

        try {
            // Parametrizar la búsqueda con un comodín
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(0, "%" + nombreFantasia + "%");  // Comodín % para permitir búsqueda parcial

            // Ejecutar la consulta con el parámetro correspondiente
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlProveedores, param);

            // Procesar los resultados de la consulta
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
            System.out.println("Error al obtener la lista de proveedores por nombre de fantasía: " + e.getMessage());
        }

        return proveedores;
    }


    @Override
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
    
    public Proveedor buscarProveedorPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM proveedores WHERE codigo = ?";
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idProveedor = rs.getInt("id");
                String nombreFantasia = rs.getString("nombre_fantasia");
                String cuit = rs.getString("cuit");

                Proveedor proveedor = new Proveedor(idProveedor, codigo, nombreFantasia, cuit);
                return proveedor;
            } else {
                return null;  // Si no se encuentra el proveedor
            }
        }
    }
    
    public Proveedor buscarProveedorPorId(int idProveedor) throws SQLException {
        String sql = "SELECT * FROM proveedores WHERE id = ?";
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setInt(1, idProveedor);  // Establecer el ID del proveedor
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Obtener los datos del proveedor
                String codigo = rs.getString("codigo");
                String nombreFantasia = rs.getString("nombre_fantasia");
                String cuit = rs.getString("cuit");

                // Crear y retornar el objeto Proveedor con los valores obtenidos
                Proveedor proveedor = new Proveedor(idProveedor, codigo, nombreFantasia, cuit);
                return proveedor;
            } else {
                return null;  // Si no se encuentra el proveedor
            }
        }
    }
}
