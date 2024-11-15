package principal.vista.gente.cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;
import principal.vista.gente.HomeMenuGente;
import modelo.cliente.Cliente;
import repositorio.dao.cliente.ClienteDaoImpl;
import java.util.List;

/**
 *
 * @author rodri
 */

public class BajaCliente extends javax.swing.JFrame {

    private JTextField codigoField, nombreField, apellidoField;
    private JTable clienteTable;
    private JButton eliminarButton;

    public BajaCliente() {
        setTitle("Baja Cliente");
        setSize(600, 300);  // Se ajusta el tamaño para que quepan todas las columnas
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrar la ventana
        setLayout(new BorderLayout(10, 10));

        // Panel superior para el título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Baja Cliente", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel central para el formulario de búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));  // 4 filas y 2 columnas
        inputPanel.add(new JLabel("Código del Cliente:"));
        codigoField = new JTextField();
        inputPanel.add(codigoField);

        inputPanel.add(new JLabel("Nombre del Cliente:"));
        nombreField = new JTextField();
        inputPanel.add(nombreField);

        inputPanel.add(new JLabel("Apellido del Cliente:"));
        apellidoField = new JTextField();
        inputPanel.add(apellidoField);

        JButton buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(new BuscarButtonListener());
        inputPanel.add(buscarButton);  // El botón buscar debajo de la última label

        searchPanel.add(inputPanel, BorderLayout.NORTH);

        // Panel central para mostrar la información del cliente en una tabla
        String[] columnNames = {"Código", "CUIL", "Nombre", "Apellido", "DNI", "Teléfono", "Email"};
        Object[][] data = new Object[0][7]; // Inicialmente vacío
        clienteTable = new JTable(data, columnNames);
        clienteTable.setEnabled(false);  // Desactivar la edición de celdas
        clienteTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // Permitir solo una selección a la vez
        clienteTable.addMouseListener(new PedidosTableMouseListener());  // Agregar el MouseListener
        JScrollPane scrollPane = new JScrollPane(clienteTable);
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

        // Configurar la tabla
        configurarTabla();
    }

    private void configurarTabla() {
        // Configuración de la tabla
        clienteTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // Permite seleccionar solo una fila
        clienteTable.setEnabled(true);  // Habilitar la edición de las filas
        clienteTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // Permite solo una fila seleccionada

        // Añadir un listener para el cambio de selección
        clienteTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = clienteTable.getSelectedRow();
            if (selectedRow != -1) {
                // Al seleccionar una fila, habilitar el botón de eliminar
                eliminarButton.setEnabled(true);
            }
        });
    }

    private class PedidosTableMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int selectedRow = clienteTable.getSelectedRow();  // Obtener la fila seleccionada
            if (selectedRow != -1) {
                String codigo = clienteTable.getValueAt(selectedRow, 0).toString();  // Obtiene el Código del cliente seleccionado
                eliminarButton.setEnabled(true);  // Habilitar el botón eliminar
            }
        }
    }

    private class BuscarButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String codigo = codigoField.getText();
                String nombre = nombreField.getText();
                String apellido = apellidoField.getText();

                ClienteDaoImpl clienteDao = new ClienteDaoImpl();

                List<Cliente> clientes = clienteDao.getClientes(codigo, nombre, apellido);

                if (clientes != null && !clientes.isEmpty()) {
                    // Muestra la información de los clientes en la tabla
                    Object[][] data = new Object[clientes.size()][7];  // Actualizado para 7 columnas
                    for (int i = 0; i < clientes.size(); i++) {
                        Cliente cliente = clientes.get(i);
                        data[i][0] = cliente.getCodigo();  // Código del cliente
                        data[i][1] = cliente.getCuil();    // CUIL
                        data[i][2] = cliente.getNombre();  // Nombre
                        data[i][3] = cliente.getApellido(); // Apellido
                        data[i][4] = cliente.getDni();     // DNI
                        data[i][5] = cliente.getTelefono(); // Teléfono
                        data[i][6] = cliente.getEmail();   // Email
                    }

                    clienteTable.setModel(new DefaultTableModel(data, new String[]{"Código", "CUIL", "Nombre", "Apellido", "DNI", "Teléfono", "Email"}));
                    eliminarButton.setEnabled(false);  // Desactivar eliminar al hacer una nueva búsqueda
                } else {
                    Object[][] data = new Object[0][7]; // Tabla vacía
                    clienteTable.setModel(new DefaultTableModel(data, new String[]{"Código", "CUIL", "Nombre", "Apellido", "DNI", "Teléfono", "Email"}));
                    JOptionPane.showMessageDialog(null, "Cliente(s) no encontrado(s).", "Error", JOptionPane.ERROR_MESSAGE);
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
                int selectedRow = clienteTable.getSelectedRow();  // Obtener la fila seleccionada

                if (selectedRow != -1) {  // Verifica si hay una fila seleccionada
                    String codigo = clienteTable.getValueAt(selectedRow, 0).toString();  // Obtiene el Código del cliente seleccionado

                    ClienteDaoImpl clienteDao = new ClienteDaoImpl();

                    Cliente cliente = clienteDao.obtenerCliente(codigo);

                    if (cliente != null) {
                        int confirmacion = JOptionPane.showConfirmDialog(null,
                                "¿Está seguro de que desea eliminar este cliente?",
                                "Confirmar Eliminación",
                                JOptionPane.YES_NO_OPTION);

                        if (confirmacion == JOptionPane.YES_OPTION) {
                            clienteDao.eliminarCliente(codigo, null, null);
                            JOptionPane.showMessageDialog(null, "El cliente ha sido eliminado exitosamente.");

                            // Limpiar la tabla
                            clienteTable.setModel(new DefaultTableModel(
                                    new Object[0][7], new String[]{"Código", "CUIL", "Nombre", "Apellido", "DNI", "Teléfono", "Email"}
                            ));
                            codigoField.setText("");
                            nombreField.setText("");
                            apellidoField.setText("");
                            eliminarButton.setEnabled(false);  // Desactivar el botón eliminar
                        } else {
                            JOptionPane.showMessageDialog(null, "Eliminación cancelada.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un cliente de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(BajaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BajaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BajaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BajaCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BajaCliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
