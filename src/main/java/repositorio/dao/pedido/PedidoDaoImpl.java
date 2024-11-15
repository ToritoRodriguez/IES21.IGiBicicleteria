package repositorio.dao.pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.pedido.DetallePedido;
import modelo.pedido.EstadoPedido;
import repositorio.dao.ConexionDb;
import modelo.pedido.Pedido;
import repositorio.dao.cliente.ClienteDaoImpl;
import repositorio.dao.vendedor.VendedorDaoImpl;
import modelo.cliente.Cliente;
import modelo.vendedor.Vendedor;
import repositorio.dao.producto.ProductoDaoImpl;
import modelo.producto.Producto;


/**
 *
 * @author rodri
 */

public class PedidoDaoImpl implements IDaoPedido {

    private static final String SQL_INSERT_PEDIDO = "INSERT INTO pedido (fecha, id_vendedor, id_cliente, estado, total_pedido) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_DETALLE_PEDIDO = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio, descuento, total) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_DETALLES_BY_PEDIDO = "SELECT * FROM detalle_pedido dp INNER JOIN productos p ON dp.id_producto = p.id WHERE dp.id_pedido = ?";
    private static final String SQL_SELECT_PEDIDO = "SELECT * FROM pedido p INNER JOIN vendedores v ON v.id = p.id_vendedor INNER JOIN clientes c ON c.id = p.id_cliente WHERE p.id = ?";
    private static final String SQL_SELECT_PEDIDOS_BY_VENDEDOR = "SELECT * FROM pedido p WHERE p.id_vendedor = ?";
    private static final String SQL_SELECT_PEDIDOS_BY_CLIENTE = "SELECT * FROM pedido p WHERE p.id_cliente = ?";
    private static final String SQL_SELECT_ALL_PEDIDOS = "SELECT * FROM pedido";

    private ConexionDb conexionDb;

    public PedidoDaoImpl() {
        this.conexionDb = new ConexionDb();
    }

    ProductoDaoImpl productoDao = new ProductoDaoImpl();
    
    @Override
    public void insertarNuevoPedido(Pedido pedido) {
        conexionDb = new ConexionDb();
        try (PreparedStatement stmtPedido = conexionDb.obtenerConexion().prepareStatement(SQL_INSERT_PEDIDO, Statement.RETURN_GENERATED_KEYS)) {
            LocalDate localDate = pedido.getFecha();
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);

            stmtPedido.setDate(1, sqlDate);
            stmtPedido.setInt(2, pedido.getVendedor().getId());  
            stmtPedido.setInt(3, pedido.getCliente().getId());   
            stmtPedido.setString(4, pedido.getEstado().name());
            stmtPedido.setDouble(5, pedido.getTotalPedido());

            int affectedRowsPedido = stmtPedido.executeUpdate();
            if (affectedRowsPedido > 0) {
                try (ResultSet generatedKeysPedido = stmtPedido.getGeneratedKeys()) {
                    if (generatedKeysPedido.next()) {
                        int idPedido = generatedKeysPedido.getInt(1);  // Obtener el ID generado
                        pedido.setId(idPedido);  // Establecer el ID en el objeto Pedido
                        insertarDetallesPedido(idPedido, pedido.getDetalles());
                        System.out.println("Pedido insertado con éxito con ID: " + idPedido);
                    }
                }
            } else {
                System.out.println("Error al insertar el pedido.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar el pedido: " + e.getMessage());
        }
    }

    private void insertarDetallesPedido(int idPedido, List<DetallePedido> detalles) throws SQLException {
        try (PreparedStatement stmtDetalle = conexionDb.obtenerConexion().prepareStatement(SQL_INSERT_DETALLE_PEDIDO)) {
            for (DetallePedido detalle : detalles) {
                stmtDetalle.setInt(1, idPedido);
                stmtDetalle.setInt(2, detalle.getProducto().getId());
                stmtDetalle.setInt(3, detalle.getCantidad());
                stmtDetalle.setDouble(4, detalle.getPrecio());
                stmtDetalle.setInt(5, detalle.getDescuento());
                stmtDetalle.setDouble(6, detalle.getTotal());
                stmtDetalle.executeUpdate();
            }
        }
    }

    @Override
    public void eliminarPedido(int idPedido) {
        conexionDb = new ConexionDb();
        try {
            try (PreparedStatement stmtDetalle = conexionDb.obtenerConexion().prepareStatement("DELETE FROM detalle_pedido WHERE id_pedido = ?")) {
                stmtDetalle.setInt(1, idPedido);
                stmtDetalle.executeUpdate();
            }

            try (PreparedStatement stmtPedido = conexionDb.obtenerConexion().prepareStatement("DELETE FROM pedido WHERE id = ?")) {
                stmtPedido.setInt(1, idPedido);
                stmtPedido.executeUpdate();
            }
            System.out.println("Pedido eliminado exitosamente con ID: " + idPedido);
        } catch (SQLException e) {
            System.out.println("Error al eliminar el pedido: " + e.getMessage());
        }
    }

    @Override
    public void modificarPedido(int idPedido, Pedido pedido) {
        conexionDb = new ConexionDb();

        // Verificar que el vendedor no sea nulo y que el ID del vendedor sea válido
        if (pedido.getVendedor() == null || pedido.getVendedor().getId() <= 0) {
            System.out.println("Vendedor no encontrado para el pedido.");
            return;
        }

        try (Connection conn = conexionDb.obtenerConexion()) {
            // Desactivar el auto-commit para realizar un control de transacciones manual
            conn.setAutoCommit(false);

            // Calcular el total del pedido sumando los totales de cada detalle
            double totalPedido = 0.0;
            for (DetallePedido detalle : pedido.getDetalles()) {
                totalPedido += detalle.getTotal();  // Sumar el total de cada detalle
            }

            // Actualizar la información del pedido
            String sqlUpdatePedido = "UPDATE pedido SET fecha = ?, id_vendedor = ?, id_cliente = ?, estado = ?, total_pedido = ? WHERE id = ?";
            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlUpdatePedido)) {
                stmtPedido.setDate(1, java.sql.Date.valueOf(pedido.getFecha()));  // Convertir LocalDate a java.sql.Date
                stmtPedido.setInt(2, pedido.getVendedor().getId());  // Obtener el ID del vendedor
                stmtPedido.setInt(3, pedido.getCliente().getId());  // Obtener el ID del cliente
                stmtPedido.setString(4, pedido.getEstado().name());  // Obtener el nombre del estado
                stmtPedido.setDouble(5, totalPedido);  // Total calculado del pedido
                stmtPedido.setInt(6, idPedido);  // ID del pedido que se está modificando

                // Ejecutar la actualización del pedido
                int rowsUpdated = stmtPedido.executeUpdate();
                if (rowsUpdated <= 0) {
                    System.out.println("No se encontró el pedido con ID: " + idPedido);
                    return;
                }

                // Eliminar los detalles del pedido anterior (solo si se modifican)
                String deleteDetalles = "DELETE FROM detalle_pedido WHERE id_pedido = ?";
                try (PreparedStatement stmtDeleteDetalles = conn.prepareStatement(deleteDetalles)) {
                    stmtDeleteDetalles.setInt(1, idPedido);
                    stmtDeleteDetalles.executeUpdate();
                }

                // Insertar o actualizar los detalles del pedido
                String insertDetallePedido = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio, descuento, total) VALUES (?, ?, ?, ?, ?, ?)";
                for (DetallePedido detalle : pedido.getDetalles()) {
                    // Verificar si el producto existe antes de insertarlo
                    if (!productoDao.productoExistePorId(detalle.getProducto().getId())) {
                        System.out.println("Error: El Producto con ID " + detalle.getProducto().getId() + " no existe. No se insertará este detalle.");
                        continue;  // Omite la inserción de este detalle si el producto no existe
                    }

                    // Verificar si es una modificación (en este caso, asumimos que siempre hay una actualización de detalles)
                    try (PreparedStatement stmtDetalle = conn.prepareStatement(insertDetallePedido)) {
                        stmtDetalle.setInt(1, idPedido);
                        stmtDetalle.setInt(2, detalle.getProducto().getId());  // ID del producto
                        stmtDetalle.setInt(3, detalle.getCantidad());  // Cantidad de producto
                        stmtDetalle.setDouble(4, detalle.getPrecio());  // Precio del producto
                        stmtDetalle.setInt(5, detalle.getDescuento());  // Descuento aplicado
                        stmtDetalle.setDouble(6, detalle.getTotal());  // Total del detalle

                        stmtDetalle.executeUpdate();  // Ejecuta la inserción de cada detalle
                    }
                }

                // Confirmar los cambios en la base de datos
                conn.commit();
                System.out.println("Pedido actualizado con éxito.");

            } catch (SQLException e) {
                // Si ocurre un error, hacer rollback
                conn.rollback();
                System.out.println("Error al modificar el pedido: " + e.getMessage());
            }
        } catch (SQLException e) {
            // Manejar errores de la conexión y de transacciones
            System.out.println("Error al conectar con la base de datos o al realizar la transacción: " + e.getMessage());
        }
    }

    @Override
    public List<Pedido> obtenerTodosLosPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(SQL_SELECT_ALL_PEDIDOS)) {
            ResultSet rs = stmt.executeQuery();  // Ejecutar la consulta
            while (rs.next()) {
                Pedido pedido = mapearPedido(rs);
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    public Pedido obtenerPedido(int idPedido) {
        Pedido pedido = null;
        conexionDb = new ConexionDb();
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(SQL_SELECT_PEDIDO)) {
            stmt.setInt(1, idPedido);  // Establecer el ID del pedido en la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pedido = mapearPedido(rs);  // Mapear el pedido recuperado
                } else {
                    System.out.println("No se encontró el pedido con ID: " + idPedido);  // Agregar log de depuración
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el pedido: " + e.getMessage());
        }
        return pedido;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        int idPedido = rs.getInt("id");  // Asegurarse de que se obtiene el ID correctamente
        int idVendedor = rs.getInt("id_vendedor");
        Vendedor vendedor = new VendedorDaoImpl().obtenerVendedorPorId(idVendedor);

        // Verificar si el vendedor se ha recuperado correctamente
        if (vendedor == null) {
            System.out.println("Advertencia: El vendedor con ID " + idVendedor + " no se encontró para el pedido " + idPedido);
        }

        Cliente cliente = new ClienteDaoImpl().obtenerClientePorId(rs.getInt("id_cliente"));
        EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado"));

        // Verificar que se asigna correctamente el ID
        System.out.println("ID Pedido recuperado: " + idPedido);  // Log para depuración

        return new Pedido(
                rs.getDate("fecha").toLocalDate(),
                vendedor,
                cliente,
                estado,
                rs.getDouble("total_pedido"),
                obtenerDetallesPorPedido(idPedido), // Obtener detalles del pedido
                idPedido // Pasar el ID al objeto Pedido
        );
    }

    private List<DetallePedido> obtenerDetallesPorPedido(int idPedido) {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = SQL_SELECT_DETALLES_BY_PEDIDO;  // Asegúrate de que esta constante esté definida correctamente
        conexionDb = new ConexionDb();

        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idProducto = rs.getInt("id_producto");

                    // Verificar si el producto existe
                    Producto producto = new ProductoDaoImpl().obtenerProductoPorId(idProducto);
                    if (producto == null) {
                        System.out.println("No se encontró el producto con ID: " + idProducto);
                        continue;  // Si no se encuentra el producto, saltamos a la siguiente fila
                    }

                    // Crear el DetallePedido y agregarlo a la lista
                    DetallePedido detalle = new DetallePedido(
                            producto, // Producto validado
                            rs.getInt("cantidad"),
                            rs.getDouble("precio"),
                            rs.getInt("descuento"),
                            rs.getDouble("total")
                    );
                    detalles.add(detalle);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los detalles del pedido: " + e.getMessage());
        }
        return detalles;
    }
    
    @Override
    public List<Pedido> obtenerPedidosPorVendedor(int idVendedor) {
        List<Pedido> pedidos = new ArrayList<>();
        conexionDb = new ConexionDb();

        // Consulta SQL para obtener los pedidos por vendedor
        String sql = "SELECT * FROM pedido WHERE id_vendedor = ?";

        try (Connection conn = conexionDb.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Establecer el ID del vendedor en la consulta
            stmt.setInt(1, idVendedor);

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Obtener los datos del pedido
                    int idPedido = rs.getInt("id");  // ID del pedido
                    Vendedor vendedor = new VendedorDaoImpl().obtenerVendedorPorId(rs.getInt("id_vendedor"));
                    Cliente cliente = new ClienteDaoImpl().obtenerClientePorId(rs.getInt("id_cliente"));
                    EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado"));
                    double totalPedido = rs.getDouble("total_pedido");
                    LocalDate fecha = rs.getDate("fecha").toLocalDate();

                    // Obtener los detalles del pedido (asumiendo que tienes un método para esto)
                    List<DetallePedido> detalles = obtenerDetallesPorPedido(idPedido);

                    // Crear el objeto Pedido
                    Pedido pedido = new Pedido(idPedido, fecha, vendedor, cliente, estado, totalPedido, detalles);
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los pedidos del vendedor: " + e.getMessage());
        }

        return pedidos;  // Retornar la lista de pedidos
    }


    @Override
    public List<Pedido> obtenerPedidosPorCliente(int idCliente) {
        List<Pedido> pedidos = new ArrayList<>();
        conexionDb = new ConexionDb();

        // Consulta SQL para obtener los pedidos por cliente
        String sql = "SELECT * FROM pedido WHERE id_cliente = ?";

        try (Connection conn = conexionDb.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Establecer el ID del cliente en la consulta
            stmt.setInt(1, idCliente);

            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Obtener los datos del pedido
                    int idPedido = rs.getInt("id");  // ID del pedido
                    Vendedor vendedor = new VendedorDaoImpl().obtenerVendedorPorId(rs.getInt("id_vendedor"));
                    Cliente cliente = new ClienteDaoImpl().obtenerClientePorId(rs.getInt("id_cliente"));
                    EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado"));
                    double totalPedido = rs.getDouble("total_pedido");
                    LocalDate fecha = rs.getDate("fecha").toLocalDate();

                    // Obtener los detalles del pedido (asumiendo que tienes un método para esto)
                    List<DetallePedido> detalles = obtenerDetallesPorPedido(idPedido);

                    // Crear el objeto Pedido
                    Pedido pedido = new Pedido(idPedido, fecha, vendedor, cliente, estado, totalPedido, detalles);
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los pedidos del cliente: " + e.getMessage());
        }

        return pedidos;  // Retornar la lista de pedidos
    }
    
    // Método que verifica si el pedido existe
    public boolean pedidoExistePorId(int idPedido) throws SQLException {
        String sql = "SELECT 1 FROM pedido WHERE id = ?";  // Consulta para verificar si el pedido existe
        try (PreparedStatement stmt = conexionDb.obtenerConexion().prepareStatement(sql)) {
            stmt.setInt(1, idPedido);  // Establecer el ID del pedido como parámetro
            ResultSet rs = stmt.executeQuery();  // Ejecutar la consulta
            return rs.next();  // Si hay un resultado, el pedido existe
        }
    }
}