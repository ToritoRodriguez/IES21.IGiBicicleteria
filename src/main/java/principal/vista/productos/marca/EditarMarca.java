package principal.vista.productos.marca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import repositorio.dao.marca.MarcaDaoImpl;
import modelo.producto.marca.Marca;
import principal.vista.gente.HomeMenuGente;
import principal.vista.productos.HomeMenuProductos;

/**
 *
 * @author rodri
 */

public class EditarMarca extends javax.swing.JFrame {

    private JTextField codigoField;
    private JTable marcaTable;
    private JButton buscarButton, modificarButton;
    
    public EditarMarca() {
        setTitle("Modificar Marca");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrar la ventana en la pantalla
        setLayout(new BorderLayout(10, 10));

        // Panel superior para el título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Modificar Marca", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel central para el formulario de búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.add(new JLabel("Código de la Marca:"), BorderLayout.WEST);
        codigoField = new JTextField();
        inputPanel.add(codigoField, BorderLayout.CENTER);

        buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(new BuscarButtonListener());
        inputPanel.add(buscarButton, BorderLayout.EAST);

        searchPanel.add(inputPanel, BorderLayout.NORTH);

        // Panel central para mostrar la información de la marca en una tabla
        String[] columnNames = {"Código", "Nombre"};
        Object[][] data = new Object[1][2]; // Inicialmente vacío
        marcaTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(marcaTable);
        searchPanel.add(scrollPane, BorderLayout.CENTER);

        modificarButton = new JButton("Guardar Cambios");
        modificarButton.setEnabled(false);  // Desactivar inicialmente
        modificarButton.addActionListener(new ModificarButtonListener());
        searchPanel.add(modificarButton, BorderLayout.SOUTH);

        add(searchPanel, BorderLayout.CENTER);

        // Panel inferior para los botones de acción
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomeMenuProductos().setVisible(true);
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
                String input = codigoField.getText().trim();  // Obtenemos el valor ingresado

                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un código de marca.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verificamos si el código es un número
                if (!input.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "El código debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int codigo = Integer.parseInt(input);  // Convertimos el código a int

                MarcaDaoImpl marcaDao = new MarcaDaoImpl();
                Marca marca = marcaDao.obtenerMarcaPorId(codigo);  // Buscamos la marca por su ID (código)

                if (marca != null) {
                    // Actualizamos la tabla con la información de la marca
                    Object[][] data = {
                        {marca.getId(), marca.getMarca()}
                    };
                    marcaTable.setModel(new javax.swing.table.DefaultTableModel(
                            data,
                            new String[]{"Código", "Nombre"}
                    ));
                    modificarButton.setEnabled(true);  // Habilitar el botón de modificar
                } else {
                    JOptionPane.showMessageDialog(null, "Marca no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    modificarButton.setEnabled(false);  // Desactivar el botón modificar
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ModificarButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int selectedRow = marcaTable.getSelectedRow();  // Obtener la fila seleccionada en la tabla
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione una marca para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int codigo = (int) marcaTable.getValueAt(selectedRow, 0);  // Obtener el código de la marca
                marcaTable.getCellEditor().stopCellEditing();
                String nuevoNombre = (String) marcaTable.getValueAt(selectedRow, 1);  // Obtener el nuevo nombre de la marca

                if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El nombre de la marca no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                MarcaDaoImpl marcaDao = new MarcaDaoImpl();
                Marca marcaExistente = marcaDao.obtenerMarcaPorId(codigo);  // Buscar la marca en la base de datos

                if (marcaExistente != null) {
                    marcaExistente.setMarca(nuevoNombre);  // Actualizar el nombre de la marca
                    marcaDao.modificarMarca(codigo, marcaExistente);  // Guardar los cambios en la base de datos
                    JOptionPane.showMessageDialog(null, "Marca modificada con éxito.");
                } else {
                    JOptionPane.showMessageDialog(null, "Marca no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(EditarMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditarMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditarMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditarMarca.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditarMarca().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
