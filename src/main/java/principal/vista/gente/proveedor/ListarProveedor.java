package principal.vista.gente.proveedor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import principal.vista.gente.HomeMenuGente;
import repositorio.dao.proveedor.ProveedorDaoImpl;
import modelo.proveedor.Proveedor;


/**
 *
 * @author rodri
 */

public class ListarProveedor extends javax.swing.JFrame {

    private JTable proveedoresTable;
    private JTextField nombreField, apellidoField, nombreFantasiaField;
    private JButton listarButton, backButton;

    public ListarProveedor() {
        setTitle("Listar Proveedores");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrar la ventana en la pantalla
        setLayout(new BorderLayout(10, 10));

        // Panel superior para el título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Listar Proveedores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel de filtro para nombre, apellido y nombre de fantasía
        JPanel filterPanel = new JPanel(new GridLayout(1, 7, 10, 10));
        filterPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        filterPanel.add(nombreField);
        filterPanel.add(new JLabel("Apellido:"));
        apellidoField = new JTextField();
        filterPanel.add(apellidoField);
        filterPanel.add(new JLabel("Nombre Fantasía:"));
        nombreFantasiaField = new JTextField();
        filterPanel.add(nombreFantasiaField);

        listarButton = new JButton("Listar");
        listarButton.addActionListener(new ListarButtonListener());
        filterPanel.add(listarButton);

        add(filterPanel, BorderLayout.NORTH);

        // Tabla para listar los proveedores
        String[] columnNames = {"Código", "CUIT", "Nombre", "Apellido", "DNI", "Teléfono", "Email", "Nombre Fantasía"};
        proveedoresTable = new JTable(new Object[0][8], columnNames);
        JScrollPane scrollPane = new JScrollPane(proveedoresTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior para el botón de volver
        JPanel buttonPanel = new JPanel(new FlowLayout());

        backButton = new JButton("Volver");
        backButton.addActionListener(e -> {
            new HomeMenuGente().setVisible(true);
            dispose();
        });
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class ListarButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String nombre = nombreField.getText().trim();
            String apellido = apellidoField.getText().trim();
            String nombreFantasia = nombreFantasiaField.getText().trim();

            // Si todos los campos están vacíos, se usa el método general `getProveedor`
            if (!nombre.isEmpty() && !apellido.isEmpty() && !nombreFantasia.isEmpty()) {
                actualizarTablaProveedores(nombre, apellido, nombreFantasia);  // Buscar por nombre, apellido y nombre de fantasía
            } else if (!nombre.isEmpty()) {
                actualizarTablaProveedoresPorNombre(nombre);  // Buscar solo por nombre
            } else if (!apellido.isEmpty()) {
                actualizarTablaProveedoresPorApellido(apellido);  // Buscar solo por apellido
            } else if (!nombreFantasia.isEmpty()) {
                actualizarTablaProveedoresPorNombreFantasia(nombreFantasia);  // Buscar solo por nombre de fantasía
            } else {
                actualizarTablaProveedores(null, null, null);  // Si todos los campos están vacíos, traer todos los proveedores
            }
        }
    }

    private void actualizarTablaProveedores(String nombre, String apellido, String nombreFantasia) {
        ProveedorDaoImpl proveedorDao = new ProveedorDaoImpl();
        List<Proveedor> proveedores = proveedorDao.getProveedor(nombre, apellido, nombreFantasia); // Llamada al método con todos los filtros

        // Convertir la lista de proveedores a una estructura de datos que la JTable pueda usar
        Object[][] data = new Object[proveedores.size()][8];
        for (int i = 0; i < proveedores.size(); i++) {
            Proveedor proveedor = proveedores.get(i);
            data[i][0] = proveedor.getCodigo();
            data[i][1] = proveedor.getCuit();
            data[i][2] = proveedor.getNombre();
            data[i][3] = proveedor.getApellido();
            data[i][4] = proveedor.getDni();
            data[i][5] = proveedor.getTelefono();
            data[i][6] = proveedor.getEmail();
            data[i][7] = proveedor.getNombreFantasia();
        }

        // Establecer los datos en la tabla
        proveedoresTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"Código", "CUIT", "Nombre", "Apellido", "DNI", "Teléfono", "Email", "Nombre Fantasía"}
        ));
    }

    private void actualizarTablaProveedoresPorNombre(String nombre) {
        ProveedorDaoImpl proveedorDao = new ProveedorDaoImpl();
        List<Proveedor> proveedores = proveedorDao.getProveedoresPorNombre(nombre); // Llamada al método específico por nombre

        // Convertir la lista de proveedores a una estructura de datos que la JTable pueda usar
        Object[][] data = new Object[proveedores.size()][8];
        for (int i = 0; i < proveedores.size(); i++) {
            Proveedor proveedor = proveedores.get(i);
            data[i][0] = proveedor.getCodigo();
            data[i][1] = proveedor.getCuit();
            data[i][2] = proveedor.getNombre();
            data[i][3] = proveedor.getApellido();
            data[i][4] = proveedor.getDni();
            data[i][5] = proveedor.getTelefono();
            data[i][6] = proveedor.getEmail();
            data[i][7] = proveedor.getNombreFantasia();
        }

        // Establecer los datos en la tabla
        proveedoresTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"Código", "CUIT", "Nombre", "Apellido", "DNI", "Teléfono", "Email", "Nombre Fantasía"}
        ));
    }

    private void actualizarTablaProveedoresPorApellido(String apellido) {
        ProveedorDaoImpl proveedorDao = new ProveedorDaoImpl();
        List<Proveedor> proveedores = proveedorDao.getProveedoresPorApellido(apellido); // Llamada al método específico por apellido

        // Convertir la lista de proveedores a una estructura de datos que la JTable pueda usar
        Object[][] data = new Object[proveedores.size()][8];
        for (int i = 0; i < proveedores.size(); i++) {
            Proveedor proveedor = proveedores.get(i);
            data[i][0] = proveedor.getCodigo();
            data[i][1] = proveedor.getCuit();
            data[i][2] = proveedor.getNombre();
            data[i][3] = proveedor.getApellido();
            data[i][4] = proveedor.getDni();
            data[i][5] = proveedor.getTelefono();
            data[i][6] = proveedor.getEmail();
            data[i][7] = proveedor.getNombreFantasia();
        }

        // Establecer los datos en la tabla
        proveedoresTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"Código", "CUIT", "Nombre", "Apellido", "DNI", "Teléfono", "Email", "Nombre Fantasía"}
        ));
    }

    private void actualizarTablaProveedoresPorNombreFantasia(String nombreFantasia) {
        ProveedorDaoImpl proveedorDao = new ProveedorDaoImpl();
        List<Proveedor> proveedores = proveedorDao.getProveedoresPorNombreFantasia(nombreFantasia); // Llamada al método específico por nombre de fantasía

        // Convertir la lista de proveedores a una estructura de datos que la JTable pueda usar
        Object[][] data = new Object[proveedores.size()][8];
        for (int i = 0; i < proveedores.size(); i++) {
            Proveedor proveedor = proveedores.get(i);
            data[i][0] = proveedor.getCodigo();
            data[i][1] = proveedor.getCuit();
            data[i][2] = proveedor.getNombre();
            data[i][3] = proveedor.getApellido();
            data[i][4] = proveedor.getDni();
            data[i][5] = proveedor.getTelefono();
            data[i][6] = proveedor.getEmail();
            data[i][7] = proveedor.getNombreFantasia();
        }

        // Establecer los datos en la tabla
        proveedoresTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"Código", "CUIT", "Nombre", "Apellido", "DNI", "Teléfono", "Email", "Nombre Fantasía"}
        ));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ListarProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListarProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListarProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListarProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListarProveedor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
