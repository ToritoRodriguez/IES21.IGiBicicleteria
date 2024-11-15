package principal.vista.gente.vendedor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import modelo.vendedor.Vendedor;
import repositorio.dao.vendedor.VendedorDaoImpl;
import principal.vista.gente.HomeMenuGente;

public class ListarVendedor extends javax.swing.JFrame {

    private JTable vendedoresTable;
    private JTextField nombreField, apellidoField, sucursalField;
    private JButton listarButton, backButton;
    
    public ListarVendedor() {
        setTitle("Listar Vendedores");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrar la ventana en la pantalla
        setLayout(new BorderLayout(10, 10));

        // Panel superior para el título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Listar Vendedores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel de filtro para nombre, apellido y sucursal
        JPanel filterPanel = new JPanel(new GridLayout(1, 7, 10, 10));
        filterPanel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        filterPanel.add(nombreField);
        filterPanel.add(new JLabel("Apellido:"));
        apellidoField = new JTextField();
        filterPanel.add(apellidoField);
        filterPanel.add(new JLabel("Sucursal:"));
        sucursalField = new JTextField();
        filterPanel.add(sucursalField);

        listarButton = new JButton("Listar");
        listarButton.addActionListener(new ListarButtonListener());
        filterPanel.add(listarButton);

        add(filterPanel, BorderLayout.NORTH);

        // Tabla para listar los vendedores
        String[] columnNames = {"Código", "CUIT", "Nombre", "Apellido", "DNI", "Teléfono", "Email", "Sucursal"};
        vendedoresTable = new JTable(new Object[0][8], columnNames);
        JScrollPane scrollPane = new JScrollPane(vendedoresTable);
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
            String sucursal = sucursalField.getText().trim(); // Agregar el campo para sucursal

            // Si todos los campos están vacíos, se usa el método general `getVendedores`
            if (!nombre.isEmpty() && !apellido.isEmpty() && !sucursal.isEmpty()) {
                actualizarTablaVendedores(nombre, apellido, sucursal);  // Buscar por nombre, apellido y sucursal
            } else if (!nombre.isEmpty()) {
                actualizarTablaVendedoresPorNombre(nombre);  // Buscar solo por nombre
            } else if (!apellido.isEmpty()) {
                actualizarTablaVendedoresPorApellido(apellido);  // Buscar solo por apellido
            } else if (!sucursal.isEmpty()) {
                actualizarTablaVendedoresPorSucursal(sucursal);  // Buscar solo por sucursal
            } else {
                actualizarTablaVendedores(null, null, null);  // Si todos los campos están vacíos, traer todos los vendedores
            }
        }
    }

    private void actualizarTablaVendedores(String nombre, String apellido, String sucursal) {
        VendedorDaoImpl vendedorDao = new VendedorDaoImpl();
        List<Vendedor> vendedores = vendedorDao.getVendedores(nombre, apellido, sucursal); // Llamada al método con todos los filtros

        // Convertir la lista de vendedores a una estructura de datos que la JTable pueda usar
        Object[][] data = new Object[vendedores.size()][8];
        for (int i = 0; i < vendedores.size(); i++) {
            Vendedor vendedor = vendedores.get(i);
            data[i][0] = vendedor.getCodigo();
            data[i][1] = vendedor.getCuit();
            data[i][2] = vendedor.getNombre();
            data[i][3] = vendedor.getApellido();
            data[i][4] = vendedor.getDni();
            data[i][5] = vendedor.getTelefono();
            data[i][6] = vendedor.getEmail();
            data[i][7] = vendedor.getSucursal();
        }

        // Establecer los datos en la tabla
        vendedoresTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"Código", "CUIT", "Nombre", "Apellido", "DNI", "Teléfono", "Email", "Sucursal"}
        ));
    }

    private void actualizarTablaVendedoresPorNombre(String nombre) {
        VendedorDaoImpl vendedorDao = new VendedorDaoImpl();
        List<Vendedor> vendedores = vendedorDao.getVendedoresPorNombre(nombre); // Llamada al método específico por nombre

        // Convertir la lista de vendedores a una estructura de datos que la JTable pueda usar
        Object[][] data = new Object[vendedores.size()][8];
        for (int i = 0; i < vendedores.size(); i++) {
            Vendedor vendedor = vendedores.get(i);
            data[i][0] = vendedor.getCodigo();
            data[i][1] = vendedor.getCuit();
            data[i][2] = vendedor.getNombre();
            data[i][3] = vendedor.getApellido();
            data[i][4] = vendedor.getDni();
            data[i][5] = vendedor.getTelefono();
            data[i][6] = vendedor.getEmail();
            data[i][7] = vendedor.getSucursal();
        }

        // Establecer los datos en la tabla
        vendedoresTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"Código", "CUIT", "Nombre", "Apellido", "DNI", "Teléfono", "Email", "Sucursal"}
        ));
    }

    private void actualizarTablaVendedoresPorApellido(String apellido) {
        VendedorDaoImpl vendedorDao = new VendedorDaoImpl();
        List<Vendedor> vendedores = vendedorDao.getVendedoresPorApellido(apellido); // Llamada al método específico por apellido

        // Convertir la lista de vendedores a una estructura de datos que la JTable pueda usar
        Object[][] data = new Object[vendedores.size()][8];
        for (int i = 0; i < vendedores.size(); i++) {
            Vendedor vendedor = vendedores.get(i);
            data[i][0] = vendedor.getCodigo();
            data[i][1] = vendedor.getCuit();
            data[i][2] = vendedor.getNombre();
            data[i][3] = vendedor.getApellido();
            data[i][4] = vendedor.getDni();
            data[i][5] = vendedor.getTelefono();
            data[i][6] = vendedor.getEmail();
            data[i][7] = vendedor.getSucursal();
        }

        // Establecer los datos en la tabla
        vendedoresTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"Código", "CUIT", "Nombre", "Apellido", "DNI", "Teléfono", "Email", "Sucursal"}
        ));
    }

    private void actualizarTablaVendedoresPorSucursal(String sucursal) {
        VendedorDaoImpl vendedorDao = new VendedorDaoImpl();
        List<Vendedor> vendedores = vendedorDao.getVendedoresPorSucursal(sucursal); // Llamada al método específico por sucursal

        // Convertir la lista de vendedores a una estructura de datos que la JTable pueda usar
        Object[][] data = new Object[vendedores.size()][8];
        for (int i = 0; i < vendedores.size(); i++) {
            Vendedor vendedor = vendedores.get(i);
            data[i][0] = vendedor.getCodigo();
            data[i][1] = vendedor.getCuit();
            data[i][2] = vendedor.getNombre();
            data[i][3] = vendedor.getApellido();
            data[i][4] = vendedor.getDni();
            data[i][5] = vendedor.getTelefono();
            data[i][6] = vendedor.getEmail();
            data[i][7] = vendedor.getSucursal();
        }

        // Establecer los datos en la tabla
        vendedoresTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"Código", "CUIT", "Nombre", "Apellido", "DNI", "Teléfono", "Email", "Sucursal"}
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
            java.util.logging.Logger.getLogger(ListarVendedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListarVendedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListarVendedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListarVendedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListarVendedor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
