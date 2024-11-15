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

/**
 *
 * @author rodri
 */

public class CategoriaDaoImpl implements IDaoCategoria {

    private ConexionDb conexionDb;

    public CategoriaDaoImpl() {
        this.conexionDb = new ConexionDb();
    }
    
    @Override
    // Método para insertar
    public void insertarNuevaCategoria(Categoria categoria) {
        // Obtener el próximo código de categoría
        String codigoCategoria = getProximoCodigoCategoria();

        // SQL para insertar en la tabla 'categorias'
        String sqlCategoria = "INSERT INTO categorias (codigo, categoria, tipo) VALUES (?, ?, ?)";
        HashMap<Integer, Object> paramCategoria = new HashMap<>();
        paramCategoria.put(1, codigoCategoria);  // Código generado
        paramCategoria.put(2, categoria.getCategoria());  // Nombre de la categoría
        paramCategoria.put(3, categoria.getTipo().toString());  // Tipo de categoría (convertido a String)

        conexionDb = new ConexionDb();
        try {
            // Preparar la sentencia para insertar la categoría
            PreparedStatement stmtCategoria = conexionDb.obtenerConexion().prepareStatement(sqlCategoria);
            stmtCategoria.setString(1, codigoCategoria);
            stmtCategoria.setString(2, categoria.getCategoria());
            stmtCategoria.setString(3, categoria.getTipo().toString());

            // Ejecutar la inserción en la base de datos
            int affectedRowsCategoria = stmtCategoria.executeUpdate();

            if (affectedRowsCategoria > 0) {
                System.out.println("Categoría " + categoria.getCategoria() + " insertada con éxito con código: " + codigoCategoria);
            } else {
                System.out.println("Error al insertar la categoría.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar la categoría: " + e.getMessage());
        }
    }

    @Override
    public void eliminarCategoria(String codigoCategoria, String nombreCategoria, TipoCategoria tipoCategoria) {
        StringBuilder sqlQuery = new StringBuilder("DELETE FROM categorias WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 0;

        try {
            // Filtramos por código si se proporciona
            if (codigoCategoria != null && !codigoCategoria.isEmpty()) {
                sqlQuery.append(" AND codigo = ?");
                param.put(index++, codigoCategoria);
            }

            // Filtramos por nombre si se proporciona
            if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
                sqlQuery.append(" AND categoria = ?");
                param.put(index++, nombreCategoria);
            }

            // Filtramos por tipo si se proporciona
            if (tipoCategoria != null) {
                sqlQuery.append(" AND tipo = ?");
                param.put(index++, tipoCategoria.name());
            }

            // Mostrar la consulta generada
            System.out.println("Consulta SQL: " + sqlQuery.toString());

            // Ejecutar la consulta directamente
            PreparedStatement stmtDelete = conexionDb.obtenerConexion().prepareStatement(sqlQuery.toString());

            // Parametrizar la consulta DELETE
            for (int i = 0; i < param.size(); i++) {
                stmtDelete.setObject(i + 1, param.get(i));
            }

            // Ejecutar el update y verificar si se eliminaron filas
            int affectedRows = stmtDelete.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Categoría eliminada exitosamente.");
            } else {
                System.out.println("No se pudo eliminar la categoría.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la categoría: " + e.getMessage());
        }
    }

    @Override
    public void modificarCategoria(String codigoCategoria, Categoria categoriaModificada) {
        String sqlUpdateCategoria = "UPDATE categorias SET categoria = ?, tipo = ? WHERE codigo = ?";

        try {
            // Preparar la consulta para modificar la categoría
            System.out.println("Ejecutando consulta para modificar categoría...");
            try (PreparedStatement stmtUpdate = conexionDb.obtenerConexion().prepareStatement(sqlUpdateCategoria)) {

                // Establecer los parámetros de la consulta
                String nombreCategoria = categoriaModificada.getCategoria();
                String tipoCategoria = categoriaModificada.getTipo() != null ? categoriaModificada.getTipo().toString() : "";

                System.out.println("Parámetros de la consulta:");
                System.out.println("Nombre de categoría: " + nombreCategoria);
                System.out.println("Tipo de categoría: " + tipoCategoria);
                System.out.println("Código de categoría: " + codigoCategoria);

                stmtUpdate.setString(1, nombreCategoria);
                stmtUpdate.setString(2, tipoCategoria);
                stmtUpdate.setString(3, codigoCategoria);  // Usamos el código en lugar del id

                // Ejecutar la actualización
                int affectedRows = stmtUpdate.executeUpdate();
                System.out.println("Número de filas afectadas: " + affectedRows);

                if (affectedRows > 0) {
                    System.out.println("Categoría con código " + codigoCategoria + " actualizada exitosamente.");
                } else {
                    System.out.println("No se pudo actualizar la categoría con código " + codigoCategoria);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar la categoría: " + e.getMessage());
        }
    }

    @Override
    public List<Categoria> getCategorias(String codigoCategoria, String nombreCategoria, TipoCategoria tipoCategoria) {
        List<Categoria> categorias = new ArrayList<>();
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM categorias WHERE 1=1");

        HashMap<Integer, Object> param = new HashMap<>();
        int index = 0;

        try {
            // Mostrar la consulta SQL antes de agregar los filtros
            System.out.println("Consulta SQL inicial: " + sqlQuery.toString());

            // Filtrar por código si se proporciona
            if (codigoCategoria != null && !codigoCategoria.isEmpty()) {
                sqlQuery.append(" AND codigo = ?");
                param.put(index++, codigoCategoria);
            }

            // Filtrar por nombre si se proporciona
            if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
                sqlQuery.append(" AND categoria LIKE ?");
                param.put(index++, "%" + nombreCategoria + "%");
            }

            // Filtrar por tipo si se proporciona
            if (tipoCategoria != null) {
                sqlQuery.append(" AND tipo = ?");
                param.put(index++, tipoCategoria.name());  // Usamos el nombre del enum
            }

            // Mostrar la consulta SQL con los filtros aplicados
            System.out.println("Consulta SQL con filtros: " + sqlQuery.toString());

            // Mostrar los parámetros que se están pasando
            System.out.println("Parámetros de búsqueda:");
            for (Integer key : param.keySet()) {
                System.out.println("Parametro " + key + ": " + param.get(key));
            }

            // Ejecutar la consulta
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param);

            // Recuperar los resultados
            while (rs.next()) {
                String codigo = rs.getString("codigo");  // Cambiar id por codigo
                String nombre = rs.getString("categoria");
                String tipo = rs.getString("tipo");

                // Mostrar los resultados obtenidos
                System.out.println("Código: " + codigo);
                System.out.println("Nombre de la categoría: " + nombre);
                System.out.println("Tipo de categoría: " + tipo);

                // Convertimos el tipo de String a TipoCategoria
                TipoCategoria tipoCat = TipoCategoria.valueOf(tipo);

                // Crear el objeto Categoria usando 'codigo' en lugar de 'id'
                Categoria categoria = new Categoria(codigo, nombre, tipoCat);
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las categorías: " + e.getMessage());
        }

        // Devolver las categorías encontradas
        return categorias;
    }
    
    @Override
    public Categoria obtenerCategoria(String codigoCategoria, String nombreCategoria, TipoCategoria tipoCategoria) {
        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM categorias WHERE 1=1");
        HashMap<Integer, Object> param = new HashMap<>();
        int index = 0;
        Categoria categoria = null;

        try {
            // Filtrar por código si se proporciona
            if (codigoCategoria != null && !codigoCategoria.isEmpty()) {
                sqlQuery.append(" AND codigo = ?");
                param.put(index++, codigoCategoria);
            }

            // Filtrar por nombre si se proporciona
            if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
                sqlQuery.append(" AND categoria = ?");
                param.put(index++, nombreCategoria);
            }

            // Filtrar por tipo si se proporciona
            if (tipoCategoria != null) {
                sqlQuery.append(" AND tipo = ?");
                param.put(index++, tipoCategoria.name());  // Usamos el nombre del enum
            }

            // Mostrar la consulta SQL generada para depuración
            System.out.println("Consulta SQL: " + sqlQuery.toString());

            // Mostrar parámetros que se están pasando
            System.out.println("Parámetros de búsqueda:");
            for (Integer key : param.keySet()) {
                System.out.println("Parametro " + key + ": " + param.get(key));
            }

            // Ejecutar la consulta
            ResultSet rs = conexionDb.ejecutarConsultaSqlConParametros(sqlQuery.toString(), param);

            if (rs.next()) {
                // Mostrar los valores obtenidos del ResultSet
                String codigo = rs.getString("codigo");  // Aseguramos que 'codigo' es tratado como String
                String categoriaNombre = rs.getString("categoria");
                String tipoString = rs.getString("tipo");

                // Verificar los datos obtenidos
                System.out.println("Código: " + codigo);
                System.out.println("Nombre de la categoría: " + categoriaNombre);
                System.out.println("Tipo de categoría: " + tipoString);

                // Convertir el tipo al valor correspondiente
                TipoCategoria tipo = TipoCategoria.valueOf(tipoString);

                categoria = new Categoria(codigo, categoriaNombre, tipo);  // Asumimos que el constructor acepta 'codigo' como String
            } else {
                System.out.println("No se encontró una categoría con los parámetros proporcionados.");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la categoría: " + e.getMessage());
        }

        return categoria;
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

    public String getProximoCodigoCategoria() {
        String sqlNextCode = "SELECT MAX(id) AS total FROM categorias";
        conexionDb = new ConexionDb();

        try {
            ResultSet rs = conexionDb.ejecutarConsultaSql(sqlNextCode);
            if (rs.next()) {
                return "C-" + (rs.getInt("total") + 1); // "C-" indica que es un código de categoría
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el próximo código de categoría: " + e.getMessage());
        }

        return "C-1"; // En caso de no haber categorías, el primer código será C-1
    }
}