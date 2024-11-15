package principal.vista.productos.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import repositorio.dao.modelo.ModeloDaoImpl;
import principal.vista.gente.HomeMenuGente;
import modelo.producto.marca.Modelo;
import modelo.producto.marca.Rodado;
import principal.vista.productos.HomeMenuProductos;

/**
 *
 * @author rodri
 */

public class EditarModelo extends javax.swing.JFrame {
/*
    private JTextField idField;
    private JTable modeloTable;
    private JButton buscarButton, modificarButton;
    
    public EditarModelo() {
        setTitle("Modificar Modelo");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrar la ventana en la pantalla
        setLayout(new BorderLayout(10, 10));

        // Panel superior para el título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Modificar Modelo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel central para el formulario de búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.add(new JLabel("ID del Modelo:"), BorderLayout.WEST);
        idField = new JTextField();
        inputPanel.add(idField, BorderLayout.CENTER);

        buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(new BuscarButtonListener());
        inputPanel.add(buscarButton, BorderLayout.EAST);

        searchPanel.add(inputPanel, BorderLayout.NORTH);

        // Panel central para mostrar la información del modelo en una tabla
        String[] columnNames = {"ID", "Marca", "Nombre", "Descripción", "Rodado"};
        Object[][] data = new Object[1][5]; // Inicialmente vacío
        modeloTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(modeloTable);
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
                String input = idField.getText().trim();  // Obtenemos el valor ingresado

                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de modelo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verificamos si el ID es un número
                if (!input.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int idModelo = Integer.parseInt(input);  // Convertimos el ID a int

                ModeloDaoImpl modeloDao = new ModeloDaoImpl();
                Modelo modelo = modeloDao.obtenerModeloPorId(idModelo);  // Buscamos el modelo por su ID

                if (modelo != null) {
                    // Actualizamos la tabla con la información del modelo
                    Object[][] data = {
                        {modelo.getId(), modelo.getMarca().getMarca(), modelo.getModelo(), modelo.getDescripcion(), modelo.getRodado().toString()}
                    };
                    modeloTable.setModel(new javax.swing.table.DefaultTableModel(
                            data,
                            new String[]{"ID", "Marca", "Nombre", "Descripción", "Rodado"}
                    ));
                    modificarButton.setEnabled(true);  // Habilitar el botón de modificar
                } else {
                    JOptionPane.showMessageDialog(null, "Modelo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
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
                int selectedRow = modeloTable.getSelectedRow();  // Obtener la fila seleccionada en la tabla
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un modelo para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int idModelo = (int) modeloTable.getValueAt(selectedRow, 0);  // Obtener el ID del modelo
                String nuevaMarca = (String) modeloTable.getValueAt(selectedRow, 1);  // Obtener el nuevo nombre de la marca
                String nuevoNombre = (String) modeloTable.getValueAt(selectedRow, 2);  // Obtener el nuevo nombre del modelo
                String nuevaDescripcion = (String) modeloTable.getValueAt(selectedRow, 3);  // Obtener la nueva descripción
                String nuevoRodado = (String) modeloTable.getValueAt(selectedRow, 4);  // Obtener el nuevo rodado

                if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El nombre del modelo no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ModeloDaoImpl modeloDao = new ModeloDaoImpl();
                Modelo modeloExistente = modeloDao.obtenerModeloPorId(idModelo);  // Buscar el modelo en la base de datos

                if (modeloExistente != null) {
                    modeloExistente.getMarca().setMarca(nuevaMarca);  // Actualizar la marca
                    modeloExistente.setModelo(nuevoNombre);  // Actualizar el nombre del modelo
                    modeloExistente.setDescripcion(nuevaDescripcion);  // Actualizar la descripción
                    modeloExistente.setRodado(Rodado.valueOf(nuevoRodado));  // Actualizar el rodado

                    modeloDao.modificarModelo(idModelo, modeloExistente);  // Guardar los cambios en la base de datos
                    JOptionPane.showMessageDialog(null, "Modelo modificado con éxito.");
                } else {
                    JOptionPane.showMessageDialog(null, "Modelo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
*/

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
            java.util.logging.Logger.getLogger(EditarModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditarModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditarModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditarModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditarModelo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
