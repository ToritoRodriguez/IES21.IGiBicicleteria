package repositorio.dao.vendedor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import repositorio.dao.ConexionDb;
import modelo.vendedor.Vendedor;

/**
 *
 * @author rodri
 */

public class VendedorDaoImpl implements IDaoVendedor {

    private ConexionDb conexionDb;

    @Override
    // Metodo para insertar
    public void insertarNuevoVendedor(Vendedor vendedor) {
        String codigoVendedor = getProximoCodigoVendedor();

        String sqlPersona = "INSERT INTO personas (nombre, apellido, dni, email, telefono) VALUES (?, ?, ?, ?, ?)";
        HashMap<Integer, Object> paramPersona = new HashMap<>();
        paramPersona.put(1, vendedor.getNombre());
        paramPersona.put(2, vendedor.getApellido());
        paramPersona.put(3, vendedor.getDni());
        paramPersona.put(4, vendedor.getEmail());
        paramPersona.put(5, vendedor.getTelefono());
        
        conexionDb = new ConexionDb();
        try {
            PreparedStatement stmtPersona = conexionDb.obtenerConexion().prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);
            stmtPersona.setString(1, vendedor.getNombre());
            stmtPersona.setString(2, vendedor.getApellido());
            stmtPersona.setString(3, vendedor.getDni());
            stmtPersona.setString(4, vendedor.getEmail());
            stmtPersona.setString(5, vendedor.getTelefono());

            int affectedRowsPersona = stmtPersona.executeUpdate();

            if (affectedRowsPersona > 0) {
                ResultSet generatedKeysPersona = stmtPersona.getGeneratedKeys();
                if (generatedKeysPersona.next()) {
                    int personId = generatedKeysPersona.getInt(1);

                    String sqlVendedor = "INSERT INTO vendedores (id_persona, codigo, sucursal, cuit) VALUES (?, ?, ?, ?)";
                    
                    PreparedStatement stmtVendedor = conexionDb.obtenerConexion().prepareStatement(sqlVendedor);
                    stmtVendedor.setInt(1, personId);
                    stmtVendedor.setString(2, codigoVendedor);
                    stmtVendedor.setString(3, vendedor.getSucursal());
                    stmtVendedor.setString(4, vendedor.getCuit());

                    int affectedRowsVendedor = stmtVendedor.executeUpdate();

                    if (affectedRowsVendedor > 0) {
                        System.out.println("Nuevo vendedor insertado con éxito con código: " + codigoVendedor);
                    } else {
                        System.out.println("Error al insertar el vendedor.");
                    }
                }
            } else {
                System.out.println("Error al insertar la persona.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el vendedor: " + e.getMessage());
        }
    }

    @Override
    // Método para eliminar vendedor
    public void eliminarVendedor(String codigo, String nombre, String apellido, String sucursal) {
        // Consulta para obtener el id_persona asociado al vendedor, con filtros opcionales
        String sqlVendedorId = "SELECT id_persona FROM vendedores v "
                + "INNER JOIN personas p ON p.id = v.id_persona "
                + "WHERE 1 = 1 ";

        if (codigo != null && !codigo.isEmpty()) {
            sqlVendedorId += " AND v.codigo = ?";
        }
        if (nombre != null && !nombre.isEmpty()) {
            sqlVendedorId += " AND p.nombre = ?";
        }
        if (apellido != null && !apellido.isEmpty()) {
            sqlVendedorId += " AND p.apellido = ?";
        }
        if (sucursal != null && !sucursal.isEmpty()) {
            sqlVendedorId += " AND s.nombre = ?";  // Suponiendo que 'nombre' es el campo de la sucursal
        }

        // Consultas para eliminar el vendedor y la persona asociada
        String sqlDeletePerson = "DELETE FROM personas WHERE id = ?";
        String sqlDeleteVendedor = "DELETE FROM vendedores WHERE codigo = ?";

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
        if (sucursal != null && !sucursal.isEmpty()) {
            param.put(paramIndex++, sucursal);
        }

        Integer idPersona = null;  // Variable para almacenar el ID de la persona

        conexionDb = new ConexionDb();

        try {
            // Ejecutar la consulta para obtener el id_persona
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlVendedorId, param);
            if (rs.next()) {
                idPersona = rs.getInt("id_persona");

                // Eliminar el vendedor si existe
                if (idPersona != null) {
                    param.clear();
                    param.put(0, codigo);
                    int rowsDeletedVendedor = conexionDb.ejecutarConsultaUpdate(sqlDeleteVendedor, param);

                    // Si el vendedor fue eliminado, eliminar también la persona asociada
                    if (rowsDeletedVendedor > 0) {
                        param.clear();
                        param.put(0, idPersona);
                        int rowsDeletedPerson = conexionDb.ejecutarConsultaUpdate(sqlDeletePerson, param);

                        if (rowsDeletedPerson > 0) {
                            System.out.println("El vendedor se eliminó exitosamente.");
                        } else {
                            System.out.println("No se pudo eliminar la persona asociada al vendedor.");
                        }
                    } else {
                        System.out.println("No se encontró el vendedor con los datos proporcionados.");
                    }
                } else {
                    System.out.println("Vendedor no encontrado, no se pudo eliminar.");
                }
            } else {
                System.out.println("No se encontró el vendedor con los datos proporcionados.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el vendedor: " + e.getMessage());
        }
    }

    @Override
    // Metodo para Modificar
    public void modificarVendedor(String codigo, Vendedor vendedor) {
        String sqlUpdateVendedor = "UPDATE vendedores SET cuit = ?, sucursal = ? WHERE codigo = ?";
        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(0, vendedor.getCuit());    
            param.put(1, vendedor.getSucursal()); 
            param.put(2, codigo);                

            System.out.println("Ejecutando actualización del vendedor con código: " + codigo);
            int rowsUpdated = conexionDb.ejecutarConsultaUpdate(sqlUpdateVendedor, param);

            if (rowsUpdated > 0) {
                System.out.println("Vendedor actualizado con éxito.");

                String sqlUpdatePersona = "UPDATE personas SET nombre = ?, apellido = ?, dni = ?, email = ?, telefono = ? WHERE id = (SELECT id_persona FROM vendedores WHERE codigo = ?)";
                param.clear(); 
                param.put(0, vendedor.getNombre()); 
                param.put(1, vendedor.getApellido()); 
                param.put(2, vendedor.getDni());     
                param.put(3, vendedor.getEmail());   
                param.put(4, vendedor.getTelefono()); 
                param.put(5, codigo);                 

                System.out.println("Ejecutando actualización de la persona asociada al vendedor con código: " + codigo);
                int rowsPersonaUpdated = conexionDb.ejecutarConsultaUpdate(sqlUpdatePersona, param);

                if (rowsPersonaUpdated > 0) {
                    System.out.println("La persona asociada al vendedor se actualizó con éxito.");
                } else {
                    System.out.println("No se encontró la persona asociada.");
                }
            } else {
                System.out.println("No se encontró el vendedor con código: " + codigo);
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el vendedor: " + e.getMessage());
        }
    }

    @Override
    // Listado de Vendedores
    public List<Vendedor> getVendedores(String codigo, String nombre, String apellido, String sucursal) {
        List<Vendedor> vendedores = new ArrayList<>();

        String sqlVendedores = "SELECT v.codigo, p.nombre, p.apellido, p.dni, p.telefono, p.email, v.cuit, v.sucursal "
                + "FROM vendedores v "
                + "INNER JOIN personas p ON p.id = v.id_persona "
                + "WHERE 1 = 1"; 

        if (codigo != null && !codigo.isEmpty()) {
            sqlVendedores += " AND v.codigo = ?";
        }
        if (nombre != null && !nombre.isEmpty()) {
            sqlVendedores += " AND p.nombre = ?";
        }
        if (apellido != null && !apellido.isEmpty()) {
            sqlVendedores += " AND p.apellido = ?";
        }
        if (sucursal != null && !sucursal.isEmpty()) {
            sqlVendedores += " AND v.sucursal = ?";  
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
            if (sucursal != null && !sucursal.isEmpty()) {
                param.put(index++, sucursal); 
            }

            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlVendedores, param);

            while (rs.next()) {
                Vendedor vendedor = new Vendedor(
                        rs.getString("cuit"),
                        rs.getString("sucursal"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                vendedor.setCodigo(rs.getString("codigo"));
                vendedores.add(vendedor);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la lista de vendedores: " + e.getMessage());
        }

        return vendedores;
    }

    @Override
    // Sirve para la Baja y Modificacion - Lo usamos para buscar
    public Vendedor obtenerVendedor(String codigo) {
        String sqlVendedor = "SELECT * FROM vendedores v "
                + "INNER JOIN personas p ON p.id = v.id_persona "
                + "WHERE v.codigo = ?";

        Vendedor vendedor = null;
        conexionDb = new ConexionDb();

        try {
            HashMap<Integer, Object> param = new HashMap<>();
            param.put(0, codigo);

            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlVendedor, param);

            if (rs != null && rs.next()) {
                vendedor = new Vendedor(
                        rs.getString("cuit"),
                        rs.getString("sucursal"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el vendedor: " + e.getMessage());
        }

        return vendedor;
    }
    
    // Sirve para el manejo de codigos
    public String getProximoCodigoVendedor() {
        String sqlNextCode = "SELECT MAX(id) AS total FROM vendedores";
        conexionDb = new ConexionDb();

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSql(sqlNextCode);
            if (rs.next()) {
                return "V-" + (rs.getInt("total") + 1);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el próximo código: " + e.getMessage());
        }

        return "V-1"; 
    }
}