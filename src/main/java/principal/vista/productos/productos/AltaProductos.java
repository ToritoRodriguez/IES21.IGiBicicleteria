package principal.vista.productos.productos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.producto.Producto;
import negocio.abm.producto.ABMProducto;
import negocio.abm.producto.ABMModelo;
import negocio.abm.producto.ABMCategoria;
import negocio.abm.proveedor.ABMProveedor;
import principal.vista.productos.HomeMenuProductos;
import modelo.producto.Categoria;
import modelo.producto.marca.Modelo;
import modelo.proveedor.Proveedor;
import repositorio.dao.categoria.CategoriaDaoImpl;
import repositorio.dao.modelo.ModeloDaoImpl;
import repositorio.dao.proveedor.ProveedorDaoImpl;

/**
 *
 * @author rodri
 */

public class AltaProductos extends javax.swing.JFrame {

    private JTextField nombreProductoField, descripcionProductoField, precioField, cantidadField, imagenField;
    private JComboBox<Categoria> categoriaComboBox;
    private JComboBox<Modelo> modeloComboBox;
    private JComboBox<Proveedor> proveedorComboBox;

    public AltaProductos() {
        setTitle("Alta Producto");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 2, 10, 10));

        // Agregamos los componentes del formulario
        add(new JLabel("Nombre del Producto:"));
        nombreProductoField = new JTextField();
        add(nombreProductoField);

        add(new JLabel("Descripción del Producto:"));
        descripcionProductoField = new JTextField();
        add(descripcionProductoField);

        add(new JLabel("Categoría:"));
        categoriaComboBox = new JComboBox<>();
        cargarCategorias();  // Cargar categorías en el ComboBox
        add(categoriaComboBox);

        add(new JLabel("Modelo:"));
        modeloComboBox = new JComboBox<>();
        cargarModelos();  // Cargar modelos en el ComboBox
        add(modeloComboBox);

        add(new JLabel("Proveedor:"));
        proveedorComboBox = new JComboBox<>();
        cargarProveedores();  // Cargar proveedores en el ComboBox
        add(proveedorComboBox);

        add(new JLabel("Precio:"));
        precioField = new JTextField();
        add(precioField);

        add(new JLabel("Cantidad:"));
        cantidadField = new JTextField();
        add(cantidadField);

        add(new JLabel("Imagen (ruta):"));
        imagenField = new JTextField();
        add(imagenField);

        // Botón para registrar el producto
        JButton submitButton = new JButton("Dar de alta");
        submitButton.addActionListener(new SubmitButtonListener());
        add(submitButton);

        // Botón para volver
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

    private void cargarCategorias() {
        CategoriaDaoImpl CrudCategoria = new CategoriaDaoImpl();
        java.util.List<Categoria> categorias = CrudCategoria.getCategoriasComboBox();  // Obtener objetos Categoria completos
        for (Categoria categoria : categorias) {
            categoriaComboBox.addItem(categoria);  // Agregar cada objeto Categoria al ComboBox
        }
    }

    private void cargarModelos() {
        ModeloDaoImpl CrudModelo = new ModeloDaoImpl();
        java.util.List<Modelo> modelos = CrudModelo.getModelosComboBox();  // Obtener objetos Modelo completos
        for (Modelo modelo : modelos) {
            modeloComboBox.addItem(modelo);  // Agregar cada objeto Modelo al ComboBox
        }
    }

    private void cargarProveedores() {
        ProveedorDaoImpl CrudProveedor = new ProveedorDaoImpl();
        java.util.List<Proveedor> proveedores = CrudProveedor.getProveedoresComboBox();  // Obtener objetos Proveedor completos
        for (Proveedor proveedor : proveedores) {
            proveedorComboBox.addItem(proveedor);  // Agregar cada objeto Proveedor al ComboBox
        }
    }

    private class SubmitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Obtener los datos del formulario
                String nombreProducto = nombreProductoField.getText();
                String descripcionProducto = descripcionProductoField.getText();
                Categoria categoria = (Categoria) categoriaComboBox.getSelectedItem();  // Obtener categoría seleccionada
                Modelo modelo = (Modelo) modeloComboBox.getSelectedItem();  // Obtener modelo seleccionado
                Proveedor proveedor = (Proveedor) proveedorComboBox.getSelectedItem();  // Obtener proveedor seleccionado
                float precio = Float.parseFloat(precioField.getText());
                int cantidad = Integer.parseInt(cantidadField.getText());
                String pathImagen = imagenField.getText();

                // Validaciones básicas
                if (nombreProducto.isEmpty() || descripcionProducto.isEmpty() || categoria == null || modelo == null || proveedor == null || precio <= 0 || cantidad <= 0 || pathImagen.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos deben ser completados correctamente.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear el objeto Producto
                Producto producto = new Producto(0, nombreProducto, descripcionProducto, categoria, modelo, proveedor, precio, pathImagen, cantidad);

                // Alta producto
                ABMProducto abmProducto = new ABMProducto();
                abmProducto.altaProducto(producto);

                // Mostrar mensaje de éxito
                JOptionPane.showMessageDialog(null, "Producto registrado exitosamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El precio o cantidad no son válidos.", "Error", JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(AltaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AltaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AltaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AltaProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AltaProductos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
