package principal.vista.gente.vendedor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import principal.vista.gente.HomeMenuGente;
import repositorio.dao.vendedor.VendedorDaoImpl;
import modelo.vendedor.Vendedor;

/**
 *
 * @author rodri
 */

public class BajaVendedor extends javax.swing.JFrame {

    private JTextField codigoField;
    private JTable vendedorTable;
    private JButton eliminarButton;
    
    public BajaVendedor() {
        setTitle("Baja Vendedor");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrar la ventana en la pantalla
        setLayout(new BorderLayout(10, 10));

        // Panel superior para el título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Baja Vendedor", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel central para el formulario de búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.add(new JLabel("Código del Vendedor:"), BorderLayout.WEST);
        codigoField = new JTextField();
        inputPanel.add(codigoField, BorderLayout.CENTER);

        JButton buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(new BuscarButtonListener());
        inputPanel.add(buscarButton, BorderLayout.EAST);

        searchPanel.add(inputPanel, BorderLayout.NORTH);

        // Panel central para mostrar la información del vendedor en una tabla
        String[] columnNames = {"CUIT", "Sucursal", "Nombre", "Apellido", "DNI", "Teléfono", "Email"};
        Object[][] data = new Object[1][7]; // Inicialmente vacío
        vendedorTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(vendedorTable);
        searchPanel.add(scrollPane, BorderLayout.CENTER);

        eliminarButton = new JButton("Eliminar");
        eliminarButton.setForeground(Color.WHITE);
        eliminarButton.setBackground(Color.RED);
        eliminarButton.setEnabled(false);  // Desactivar inicialmente
        eliminarButton.addActionListener(new EliminarButtonListener());
        searchPanel.add(eliminarButton, BorderLayout.SOUTH);

        add(searchPanel, BorderLayout.CENTER);

        // Panel inferior para los botones de acción
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomeMenuGente().setVisible(true);
                dispose();
            }
        });
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class BuscarButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String codigo = codigoField.getText();

                VendedorDaoImpl vendedorDao = new VendedorDaoImpl();
                Vendedor vendedor = vendedorDao.obtenerVendedor(codigo);

                if (vendedor != null) {
                    Object[][] data = {
                        {vendedor.getCuit(), vendedor.getSucursal(), vendedor.getNombre(), vendedor.getApellido(), vendedor.getDni(), vendedor.getTelefono(), vendedor.getEmail()}
                    };
                    vendedorTable.setModel(new javax.swing.table.DefaultTableModel(
                            data,
                            new String[]{"CUIT", "Sucursal", "Nombre", "Apellido", "DNI", "Teléfono", "Email"}
                    ));
                    eliminarButton.setEnabled(true);  // Habilita el botón eliminar
                } else {
                    Object[][] data = new Object[1][7]; // Tabla vacía
                    vendedorTable.setModel(new javax.swing.table.DefaultTableModel(
                            data,
                            new String[]{"CUIT", "Sucursal", "Nombre", "Apellido", "DNI", "Teléfono", "Email"}
                    ));
                    JOptionPane.showMessageDialog(null, "Vendedor no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    eliminarButton.setEnabled(false);  // Desactivar el botón eliminar
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class EliminarButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String codigo = codigoField.getText();

                VendedorDaoImpl vendedorDao = new VendedorDaoImpl();
                Vendedor vendedor = vendedorDao.obtenerVendedor(codigo);

                if (vendedor != null) {
                    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este vendedor?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        vendedorDao.eliminarVendedor(codigo);
                        JOptionPane.showMessageDialog(null, "El vendedor ha sido eliminado exitosamente.");

                        vendedorTable.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[1][7], new String[]{"CUIT", "Sucursal", "Nombre", "Apellido", "DNI", "Teléfono", "Email"}
                        ));
                        codigoField.setText("");
                        eliminarButton.setEnabled(false);  // Desactivar el botón eliminar
                    } else {
                        JOptionPane.showMessageDialog(null, "Eliminación cancelada.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Vendedor no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    eliminarButton.setEnabled(false);  // Desactivar el botón eliminar
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
            java.util.logging.Logger.getLogger(BajaVendedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BajaVendedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BajaVendedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BajaVendedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BajaVendedor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
