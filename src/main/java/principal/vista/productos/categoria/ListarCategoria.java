package principal.vista.productos.categoria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import modelo.producto.Categoria;
import repositorio.dao.categoria.CategoriaDaoImpl;
import principal.vista.productos.HomeMenuProductos;
import modelo.producto.TipoCategoria;
import java.util.stream.Collectors;


/**
 *
 * @author rodri
 */

public class ListarCategoria extends javax.swing.JFrame {

    private JTable categoriaTable;
    private JButton listarButton, backButton;
    private JComboBox<TipoCategoria> tipoCategoriaComboBox; 
    private JTextField nombreCategoriaField; 

    public ListarCategoria() {
        setTitle("Listar Categorías");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  
        setLayout(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Listar Categorías", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        filterPanel.add(new JLabel("Nombre de la Categoría (Opcional):"));
        nombreCategoriaField = new JTextField();
        filterPanel.add(nombreCategoriaField);

        filterPanel.add(new JLabel("Tipo de Categoría (Opcional):"));
        tipoCategoriaComboBox = new JComboBox<>();
        tipoCategoriaComboBox.addItem(null);  
        for (TipoCategoria tipo : TipoCategoria.values()) {
            tipoCategoriaComboBox.addItem(tipo);  
        }
        filterPanel.add(tipoCategoriaComboBox);

        listarButton = new JButton("Listar");
        listarButton.addActionListener(new ListarButtonListener()); 
        filterPanel.add(listarButton);

        add(filterPanel, BorderLayout.NORTH);

        
        String[] columnNames = {"ID", "Nombre", "Tipo"};
        categoriaTable = new JTable(new Object[0][3], columnNames);
        JScrollPane scrollPane = new JScrollPane(categoriaTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        backButton = new JButton("Volver");
        backButton.addActionListener(e -> {
            new HomeMenuProductos().setVisible(true);
            dispose();
        });
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class ListarButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String nombre = nombreCategoriaField.getText().trim();
            TipoCategoria tipoCategoria = (TipoCategoria) tipoCategoriaComboBox.getSelectedItem();

            actualizarTablaCategorias(nombre, tipoCategoria);
        }
    }

    private void actualizarTablaCategorias(String nombreCategoria, TipoCategoria tipoCategoria) {
        CategoriaDaoImpl categoriaDao = new CategoriaDaoImpl();
        List<Categoria> categorias = categoriaDao.getCategorias(null, nombreCategoria, tipoCategoria);

        Object[][] data = new Object[categorias.size()][3];
        for (int i = 0; i < categorias.size(); i++) {
            Categoria categoria = categorias.get(i);
            data[i][0] = categoria.getCodigo();
            data[i][1] = categoria.getNombre();
            data[i][2] = categoria.getTipo();
        }

        categoriaTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"ID", "Nombre", "Tipo"}
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
            java.util.logging.Logger.getLogger(ListarCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListarCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListarCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListarCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListarCategoria().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
