package principal.vista.productos.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import repositorio.dao.modelo.ModeloDaoImpl;
import principal.vista.productos.HomeMenuProductos;
import principal.vista.gente.HomeMenuGente;
import modelo.producto.marca.Modelo;

/**
 *
 * @author rodri
 */

public class BajaModelo extends javax.swing.JFrame {

    private JTextField codigoField;
    private JTable modeloTable;
    private JButton eliminarButton;
    
    public BajaModelo() {
        setTitle("Baja Modelo");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrar la ventana en la pantalla
        setLayout(new BorderLayout(10, 10));

        // Panel superior para el título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Baja Modelo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel central para el formulario de búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.add(new JLabel("ID o Nombre del Modelo:"), BorderLayout.WEST);
        codigoField = new JTextField();
        inputPanel.add(codigoField, BorderLayout.CENTER);

        JButton buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(new BuscarButtonListener());
        inputPanel.add(buscarButton, BorderLayout.EAST);

        searchPanel.add(inputPanel, BorderLayout.NORTH);

        // Panel central para mostrar la información del modelo en una tabla
        String[] columnNames = {"ID", "Modelo", "Marca", "Rodado"};
        Object[][] data = new Object[1][4]; // Inicialmente vacío
        modeloTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(modeloTable);
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
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID o nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ModeloDaoImpl modeloDao = new ModeloDaoImpl();
                Modelo modelo = null;

                // Verificamos si el input es un número (ID) o texto (nombre)
                if (input.matches("\\d+")) {  // Si es numérico, buscamos por ID
                    int id = Integer.parseInt(input);  // Convertimos a int
                    modelo = modeloDao.obtenerModelo(id);
                } else {  // Si es texto, buscamos por nombre
                    modelo = modeloDao.buscarModeloPorNombre(input);
                }

                if (modelo != null) {
                    // Muestra la información del modelo en una tabla
                    Object[][] data = {
                        {modelo.getId(), modelo.getModelo(), modelo.getMarca().getMarca(), modelo.getRodado()}
                    };
                    modeloTable.setModel(new javax.swing.table.DefaultTableModel(
                            data,
                            new String[]{"ID", "Modelo", "Marca", "Rodado"}
                    ));
                    eliminarButton.setEnabled(true);  // Habilita el botón eliminar
                } else {
                    Object[][] data = new Object[1][4]; // Tabla vacía
                    modeloTable.setModel(new javax.swing.table.DefaultTableModel(
                            data,
                            new String[]{"ID", "Modelo", "Marca", "Rodado"}
                    ));
                    JOptionPane.showMessageDialog(null, "Modelo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
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
                String input = codigoField.getText().trim();  // Obtenemos el valor ingresado

                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID o nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ModeloDaoImpl modeloDao = new ModeloDaoImpl();
                Modelo modelo = null;

                // Verificamos si el input es un número (ID) o texto (nombre)
                if (input.matches("\\d+")) {  // Si es numérico, buscamos por ID
                    int id = Integer.parseInt(input);  // Convertimos a int
                    modelo = modeloDao.obtenerModelo(id);
                } else {  // Si es texto, buscamos por nombre
                    modelo = modeloDao.buscarModeloPorNombre(input);
                }

                if (modelo != null) {
                    // Si el modelo existe, pedimos confirmación para eliminarlo
                    int confirmacion = JOptionPane.showConfirmDialog(null,
                            "¿Está seguro de que desea eliminar este modelo?",
                            "Confirmar Eliminación",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        if (input.matches("\\d+")) {  // Si es por ID
                            modeloDao.eliminarModeloPorId(Integer.parseInt(input));
                        } else {  // Si es por nombre
                            modeloDao.eliminarModeloPorNombre(input);
                        }
                        JOptionPane.showMessageDialog(null, "El modelo ha sido eliminado exitosamente.");

                        // Actualizamos la tabla y desactivamos el botón de eliminar
                        modeloTable.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[1][4], new String[]{"ID", "Modelo", "Marca", "Rodado"}
                        ));
                        codigoField.setText("");  // Limpiamos el campo de texto
                        eliminarButton.setEnabled(false);  // Desactivamos el botón eliminar
                    } else {
                        JOptionPane.showMessageDialog(null, "Eliminación cancelada.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Modelo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    eliminarButton.setEnabled(false);  // Desactivamos el botón eliminar si no se encuentra el modelo
                }
            } catch (Exception ex) {
                // En caso de error, mostramos el mensaje
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
            java.util.logging.Logger.getLogger(BajaModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BajaModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BajaModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BajaModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BajaModelo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
