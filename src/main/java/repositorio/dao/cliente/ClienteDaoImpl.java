package repositorio.dao.cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import modelo.cliente.Cliente;
import repositorio.dao.ConexionDb;

/**
 *
 * @author rodri
 */

public class ClienteDaoImpl implements IDaoCliente{

    private ConexionDb conexionDb;

    @Override
    // Metodo para insertar
    public void insertarNuevoCliente(Cliente cliente) {
        
        String codigoCliente = getProximoCodigoCliente();

        String sqlPersona = "INSERT INTO personas (nombre, apellido, dni, email, telefono) VALUES (?, ?, ?, ?, ?)";
        HashMap<Integer, Object> paramPersona = new HashMap<>();
        paramPersona.put(1, cliente.getNombre());
        paramPersona.put(2, cliente.getApellido());
        paramPersona.put(3, cliente.getDni());
        paramPersona.put(4, cliente.getEmail());
        paramPersona.put(5, cliente.getTelefono());

        conexionDb = new ConexionDb();
        try {
            PreparedStatement stmtPersona = conexionDb.obtenerConexion().prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            stmtPersona.setString(1, cliente.getNombre());
            stmtPersona.setString(2, cliente.getApellido());
            stmtPersona.setString(3, cliente.getDni());
            stmtPersona.setString(4, cliente.getEmail());
            stmtPersona.setString(5, cliente.getTelefono());

            int affectedRowsPersona = stmtPersona.executeUpdate();

            if (affectedRowsPersona > 0) {
                ResultSet generatedKeysPersona = stmtPersona.getGeneratedKeys();
                if (generatedKeysPersona.next()) {
                    int personId = generatedKeysPersona.getInt(1); 

                    String sqlClient = "INSERT INTO clientes (codigo, id_persona, cuil) VALUES (?, ?, ?)";
                    PreparedStatement stmtClient = conexionDb.obtenerConexion().prepareStatement(sqlClient);
                    stmtClient.setString(1, codigoCliente); 
                    stmtClient.setInt(2, personId);
                    stmtClient.setString(3, cliente.getCuil()); 

                    int affectedRowsClient = stmtClient.executeUpdate();

                    if (affectedRowsClient > 0) {
                        System.out.println("Nuevo cliente insertado con éxito con código: " + codigoCliente);
                    } else {
                        System.out.println("Error al insertar el cliente.");
                    }
                }
            } else {
                System.out.println("Error al insertar la persona.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el cliente: " + e.getMessage());
        }
    }

    @Override
    // Metodo para Eliminar
    public void eliminarCliente(String codigo, String nombre, String apellido) {
        String sqlClientId = "SELECT id_persona FROM clientes c "
                + "INNER JOIN personas p ON p.id = c.id_persona "
                + "WHERE 1 = 1 ";  // Este WHERE 1 = 1 es para simplificar la adición de condiciones

        // Condiciones de búsqueda dinámica
        if (codigo != null && !codigo.isEmpty()) {
            sqlClientId += " AND c.codigo = ?";
        }
        if (nombre != null && !nombre.isEmpty()) {
            sqlClientId += " AND p.nombre = ?";
        }
        if (apellido != null && !apellido.isEmpty()) {
            sqlClientId += " AND p.apellido = ?";
        }

        String sqlDeletePerson = "DELETE FROM personas WHERE id = ?";  // Eliminar de personas
        String sqlDeleteClient = "DELETE FROM clientes WHERE codigo = ?";  // Eliminar de clientes

        HashMap<Integer, Object> param = new HashMap<>();
        int paramIndex = 0;  // Índice para los parámetros

        // Añadir los parámetros según el filtro disponible
        if (codigo != null && !codigo.isEmpty()) {
            param.put(paramIndex++, codigo);
        }
        if (nombre != null && !nombre.isEmpty()) {
            param.put(paramIndex++, nombre);
        }
        if (apellido != null && !apellido.isEmpty()) {
            param.put(paramIndex++, apellido);
        }

        Integer idPersona = 0;  // Variable para almacenar el id_persona

        conexionDb = new ConexionDb();

        try {
            // Ejecutar la consulta para obtener el id_persona correspondiente
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlClientId, param);
            if (rs.next()) {
                idPersona = rs.getInt("id_persona");  // Asignar el id_persona
            }

            if (idPersona != 0) {
                // Primero eliminar el cliente de la tabla 'clientes' usando el código
                param.clear();
                param.put(0, codigo);  // Pasamos el código para eliminar el cliente
                int rowsDeletedClient = conexionDb.ejecutarConsultaUpdate(sqlDeleteClient, param);

                if (rowsDeletedClient > 0) {
                    // Luego eliminar la persona de la tabla 'personas'
                    param.clear();
                    param.put(0, idPersona);  // El id_persona de la persona asociada
                    int rowsDeletedPerson = conexionDb.ejecutarConsultaUpdate(sqlDeletePerson, param);

                    if (rowsDeletedPerson > 0) {
                        System.out.println("El cliente se eliminó exitosamente.");
                    } else {
                        System.out.println("No se pudo eliminar la persona asociada al cliente.");
                    }
                } else {
                    System.out.println("No se encontró el cliente con los datos proporcionados.");
                }
            } else {
                System.out.println("Cliente no encontrado, no se pudo eliminar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el cliente: " + e.getMessage());
        }
    }
    
    @Override
    // Metodo para Modificar
    public void modificarCliente(String codigo, Cliente cliente) {
        conexionDb = new ConexionDb();

        // Actualizar el CUIL
        String sqlUpdateClient = "UPDATE clientes SET cuil = ? WHERE codigo = ?";
        HashMap<Integer, Object> param = new HashMap<>();
        param.put(0, cliente.getCuil());
        param.put(1, codigo);
        try {
            int rowsUpdated = conexionDb.ejecutarConsultaUpdate(sqlUpdateClient, param);

            if (rowsUpdated > 0) {
                // Actualizar los demás datos en la tabla personas
                String sqlUpdatePer = "UPDATE personas SET nombre = ?, apellido = ?, dni = ?, email = ?, telefono = ? "
                        + "WHERE id = (SELECT id_persona FROM clientes WHERE codigo = ?)";
                param.clear();
                param.put(0, cliente.getNombre());
                param.put(1, cliente.getApellido());
                param.put(2, cliente.getDni());
                param.put(3, cliente.getEmail());
                param.put(4, cliente.getTelefono());
                param.put(5, codigo);

                int rowsPersonUpdated = conexionDb.ejecutarConsultaUpdate(sqlUpdatePer, param);

                if (rowsPersonUpdated > 0) {
                    System.out.println("El cliente se actualizó con éxito");
                } else {
                    System.out.println("No se encontró la persona asociada al código proporcionado");
                }
            } else {
                System.out.println("No se encontró el cliente con el código proporcionado");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el cliente: " + e.getMessage());
        }
    }
    
    @Override
    // Sirve para la Baja y Modificacion - Lo usamos para buscar por codigo
    public Cliente obtenerCliente(String codigo) {
        String sqlClient = "SELECT * FROM clientes c "
                + "INNER JOIN personas p ON p.id = c.id_persona "
                + "WHERE c.codigo = ?";  

        Cliente cliente = null;
        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(0, codigo);  

            System.out.println("Parámetro 'codigo' añadido correctamente al HashMap: " + param.get(0));
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlClient, param);

            if (rs != null && rs.next()) {
                cliente = new Cliente(
                        rs.getString("cuil"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el cliente: " + e.getMessage());
        }

        return cliente;
    }

    @Override
    // Listado de Clientes
    public List<Cliente> getClientes(String codigo, String nombre, String apellido) {
        List<Cliente> clientes = new ArrayList<>();
        String sqlClients = "SELECT c.codigo, p.nombre, p.apellido, p.dni, p.telefono, p.email, c.cuil "
                + "FROM clientes c "
                + "INNER JOIN personas p ON p.id = c.id_persona "
                + "WHERE 1 = 1 ";

        // Se agregan estas condiciones si los parámetros no son nulos
        if (codigo != null && !codigo.isEmpty()) {
            sqlClients += " AND c.codigo = ?";
        }
        if (nombre != null && !nombre.isEmpty()) {
            sqlClients += " AND p.nombre LIKE ?";
        }
        if (apellido != null && !apellido.isEmpty()) {
            sqlClients += " AND p.apellido LIKE ?";
        }

        conexionDb = new ConexionDb();

        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sqlClients)) {
            int index = 1;

            // El código se pase primero, si esta presente
            if (codigo != null && !codigo.isEmpty()) {
                stmt.setString(index++, codigo);
            }

            // Luego el nombre con LIKE, si esta presente
            if (nombre != null && !nombre.isEmpty()) {
                stmt.setString(index++, "%" + nombre + "%");
            }

            // Finalmente, el apellido con LIKE, si esta presente
            if (apellido != null && !apellido.isEmpty()) {
                stmt.setString(index++, "%" + apellido + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Ejecutando consulta...");
                int clienteCount = 0;

                while (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getString("codigo"),
                            rs.getString("cuil"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("dni"),
                            rs.getString("telefono"),
                            rs.getString("email")
                    );
                    cliente.setCodigo(rs.getString("codigo"));
                    clientes.add(cliente);
                    clienteCount++;
                }
                System.out.println("Numero total de clientes encontrados: " + clienteCount);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de clientes: " + e.getMessage());
        }

        return clientes;
    }

    @Override
    // Sirve para el manejo de codigos
    public String getProximoCodigoCliente(){
        String sqlNextCode = "SELECT MAX(id) AS total FROM clientes";
        conexionDb = new ConexionDb();

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSql(sqlNextCode);

            if (rs.next()) {
                return "C-" + (rs.getInt("total") + 1);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el próximo código: " + e.getMessage());
        }

        return "C-1"; 
    }
    
    // Sirve para Pedidos
    public Cliente obtenerClientePorId(int idCliente) {
        Cliente cliente = null;
        // Consulta con JOIN entre clientes y personas
        String sql = "SELECT c.id, c.codigo, c.cuil, p.nombre, p.apellido, p.dni, p.telefono, p.email "
                + "FROM clientes c "
                + "INNER JOIN personas p ON c.id_persona = p.id "
                + // Asumiendo que id_persona es la clave foránea en clientes
                "WHERE c.id = ?"; // Consulta para obtener cliente por ID

        conexionDb = new ConexionDb();
        try {
            PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql);
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Creación del objeto Cliente utilizando los datos traídos de ambas tablas
                cliente = new Cliente(
                        rs.getString("codigo"),
                        rs.getString("cuil"),
                        rs.getString("nombre"), // Nombre tomado desde la tabla personas
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                cliente.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cliente: " + e.getMessage());
        }

        return cliente;
    }
}