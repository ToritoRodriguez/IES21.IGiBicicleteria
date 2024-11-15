package principal.vista.productos.categoria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import repositorio.dao.categoria.CategoriaDaoImpl;
import modelo.producto.Categoria;
import principal.vista.productos.HomeMenuProductos;

/**
 *
 * @author rodri
 */

public class EditarCategoria extends javax.swing.JFrame {

    private JTextField codigoField;
    private JTable categoriaTable;
    private JButton buscarButton, modificarButton;
        
    public EditarCategoria() {
        setTitle("Modificar Categoría");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrar la ventana en la pantalla
        setLayout(new BorderLayout(10, 10));

        // Panel superior para el título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Modificar Categoría", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel central para el formulario de búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.add(new JLabel("Código de la Categoría:"), BorderLayout.WEST);
        codigoField = new JTextField();
        inputPanel.add(codigoField, BorderLayout.CENTER);

        buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(new BuscarButtonListener());
        inputPanel.add(buscarButton, BorderLayout.EAST);

        searchPanel.add(inputPanel, BorderLayout.NORTH);

        // Panel central para mostrar la información de la categoría en una tabla
        String[] columnNames = {"Código", "Nombre", "Tipo"};
        Object[][] data = new Object[1][3]; // Inicialmente vacío
        categoriaTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(categoriaTable);
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
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un código de categoría.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                CategoriaDaoImpl categoriaDao = new CategoriaDaoImpl();
                // Usamos obtenerCategoria con código de tipo String
                Categoria categoria = categoriaDao.obtenerCategoria(input, null, null);  // No filtramos por nombre ni tipo

                if (categoria != null) {
                    // Actualizamos la tabla con la información de la categoría (Código, Nombre y Tipo)
                    Object[][] data = {
                        {categoria.getCodigo(), categoria.getNombre(), categoria.getTipo().toString()} // Aquí agregamos el Tipo
                    };
                    categoriaTable.setModel(new javax.swing.table.DefaultTableModel(
                            data,
                            new String[]{"Código", "Nombre", "Tipo"} // Actualizamos las columnas
                    ));
                    modificarButton.setEnabled(true);  // Habilitar el botón de modificar
                } else {
                    JOptionPane.showMessageDialog(null, "Categoría no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
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
                int selectedRow = categoriaTable.getSelectedRow();  // Obtener la fila seleccionada en la tabla
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione una categoría para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Obtener el código de la categoría como String
                String codigo = (String) categoriaTable.getValueAt(selectedRow, 0);  // Obtener el código de la categoría
               
                categoriaTable.getCellEditor().stopCellEditing();
                String nuevoNombre = (String) categoriaTable.getValueAt(selectedRow, 1);  // Obtener el nuevo nombre de la categoría

                if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El nombre de la categoría no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                CategoriaDaoImpl categoriaDao = new CategoriaDaoImpl();
                // Buscar la categoría por código (que ahora es de tipo String)
                Categoria categoriaExistente = categoriaDao.obtenerCategoria(codigo, null, null);  // Usamos solo el código

                if (categoriaExistente != null) {
                    categoriaExistente.setCategoria(nuevoNombre);  // Actualizar el nombre de la categoría
                    categoriaDao.modificarCategoria(codigo, categoriaExistente);  // Guardar los cambios en la base de datos
                    JOptionPane.showMessageDialog(null, "Categoría modificada con éxito.");

                    // Ahora, después de modificar, recargamos la información
                    Categoria categoriaModificada = categoriaDao.obtenerCategoria(codigo, null, null); // Obtener los datos actualizados
                    Object[][] data = {
                        {categoriaModificada.getCodigo(), categoriaModificada.getCategoria(), categoriaModificada.getTipo().toString()}
                    };
                    categoriaTable.setModel(new javax.swing.table.DefaultTableModel(
                            data,
                            new String[]{"Código", "Nombre", "Tipo"} // Actualizamos las columnas con la nueva información
                    ));
                } else {
                    JOptionPane.showMessageDialog(null, "Categoría no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(EditarCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditarCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditarCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditarCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditarCategoria().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
