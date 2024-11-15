package principal.vista.productos.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import repositorio.dao.marca.MarcaDaoImpl;
import modelo.producto.marca.Modelo;
import modelo.producto.marca.Rodado;
import modelo.producto.marca.Marca;
import principal.vista.productos.HomeMenuProductos;
import repositorio.dao.modelo.ModeloDaoImpl;
import java.util.List;

/**
 *
 * @author rodri
 */

public class AltaModelo extends javax.swing.JFrame {

    private JTextField nombreModeloField;
    private JTextField descripcionField;
    private JComboBox<Marca> marcaComboBox;  // JComboBox de Marca
    private JComboBox<String> rodadoComboBox;

    public AltaModelo() {
        setTitle("Alta Modelo");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));

        // Agregamos los componentes del formulario
        add(new JLabel("Nombre del Modelo:"));
        nombreModeloField = new JTextField();
        add(nombreModeloField);

        add(new JLabel("Descripción del Modelo:"));
        descripcionField = new JTextField();  // Usamos JTextField para la descripción
        add(descripcionField);

        add(new JLabel("Marca del Modelo:"));
        marcaComboBox = new JComboBox<>();
        cargarMarcas();  // Cargar marcas en el JComboBox
        add(marcaComboBox);

        add(new JLabel("Rodado del Modelo:"));
        rodadoComboBox = new JComboBox<>(new String[]{"RODADO24", "RODADO27", "RODADO29", "RODADO30"});
        add(rodadoComboBox);

        JButton submitButton = new JButton("Dar de alta");
        submitButton.addActionListener(new SubmitButtonListener());
        add(submitButton);

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomeMenuProductos().setVisible(true);
                dispose();
            }
        });
        add(backButton);
    }

    // Método para cargar las marcas en el JComboBox
    private void cargarMarcas() {
        MarcaDaoImpl marcaDao = new MarcaDaoImpl();
        List<Marca> marcas = marcaDao.getMarcasComboBox();  // Obtener marcas desde la base de datos
        for (Marca marca : marcas) {
            marcaComboBox.addItem(marca);  // Agregar cada marca al ComboBox
        }
    }

    private class SubmitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Obtener los datos del formulario
                String nombreModelo = nombreModeloField.getText();
                String descripcion = descripcionField.getText();
                Marca marcaSeleccionada = (Marca) marcaComboBox.getSelectedItem();  // Obtener la marca seleccionada
                String rodadoString = (String) rodadoComboBox.getSelectedItem();
                Rodado rodado = Rodado.valueOf(rodadoString);  // Convertir a enum Rodado

                if (nombreModelo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El nombre del modelo no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "La descripción del modelo no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (marcaSeleccionada == null) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una marca.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Obtener el código de la marca seleccionada
                String codigoMarca = marcaSeleccionada.getCodigo();  // Captura el código de la marca

                // Crear un objeto Modelo
                Modelo modelo = new Modelo(codigoMarca, nombreModelo, marcaSeleccionada, descripcion, rodado);

                // Crear instancia de ModeloDaoImpl y registrar el modelo
                ModeloDaoImpl modeloDao = new ModeloDaoImpl();
                modeloDao.insertarNuevoModelo(modelo);

                // Mostrar mensaje de éxito
                JOptionPane.showMessageDialog(null, "Modelo registrado exitosamente.");
            } catch (IllegalArgumentException ex) {
                // Manejo de excepción en caso de un rodado inválido
                JOptionPane.showMessageDialog(null, "Rodado no válido. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                // Manejo de excepciones generales
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
            java.util.logging.Logger.getLogger(AltaModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AltaModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AltaModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AltaModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AltaModelo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
