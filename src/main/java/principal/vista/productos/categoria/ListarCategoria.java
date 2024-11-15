package principal.vista.productos.categoria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import modelo.producto.Categoria;
import repositorio.dao.categoria.CategoriaDaoImpl;
import principal.vista.gente.HomeMenuGente;
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
    private JComboBox<TipoCategoria> tipoCategoriaComboBox;  // ComboBox para seleccionar el tipo de categoría
    private JTextField nombreCategoriaField; // Campo para ingresar el nombre de la categoría

    public ListarCategoria() {
        setTitle("Listar Categorías");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrar la ventana en la pantalla
        setLayout(new BorderLayout(10, 10));

        // Panel superior para el título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Listar Categorías", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel de filtro para el nombre de la categoría (opcional) y el tipo
        JPanel filterPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        filterPanel.add(new JLabel("Nombre de la Categoría (Opcional):"));
        nombreCategoriaField = new JTextField();
        filterPanel.add(nombreCategoriaField);

        // ComboBox de tipo de categoría
        filterPanel.add(new JLabel("Tipo de Categoría (Opcional):"));
        tipoCategoriaComboBox = new JComboBox<>();
        tipoCategoriaComboBox.addItem(null);  // Opción de "Todos" (deseleccionado)
        for (TipoCategoria tipo : TipoCategoria.values()) {
            tipoCategoriaComboBox.addItem(tipo);  // Añadir los valores de tipo de categoría
        }
        filterPanel.add(tipoCategoriaComboBox);

        listarButton = new JButton("Listar");
        listarButton.addActionListener(new ListarButtonListener());  // Pasamos el campo de nombre al listener
        filterPanel.add(listarButton);

        add(filterPanel, BorderLayout.NORTH);

        // Tabla para listar las categorías con columnas id, nombre, tipo
        String[] columnNames = {"ID", "Nombre", "Tipo"};
        categoriaTable = new JTable(new Object[0][3], columnNames);
        JScrollPane scrollPane = new JScrollPane(categoriaTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior para el botón de volver
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

            if (!nombre.isEmpty() && tipoCategoria != null) {
                actualizarTablaCategorias(nombre, tipoCategoria);  // Buscar por nombre y tipo
            } else if (!nombre.isEmpty()) {
                actualizarTablaCategoriasPorNombre(nombre);  // Buscar solo por nombre
            } else if (tipoCategoria != null) {
                actualizarTablaCategoriasPorTipo(tipoCategoria);  // Buscar solo por tipo
            } else {
                actualizarTablaCategorias(null, null);  // Si todos los campos están vacíos, traer todas las categorías
            }
        }
    }

    private void actualizarTablaCategorias(String nombreCategoria, TipoCategoria tipoCategoria) {
        CategoriaDaoImpl categoriaDao = new CategoriaDaoImpl();
        List<Categoria> categorias = categoriaDao.getCategorias(0, null); // Inicialmente obtiene todas las categorías.

        // Filtrar las categorías por nombre y tipo si se proporcionan
        if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
            categorias = categorias.stream()
                    .filter(categoria -> categoria.getCategoria().equalsIgnoreCase(nombreCategoria))
                    .collect(Collectors.toList());
        }
        if (tipoCategoria != null) {
            categorias = categorias.stream()
                    .filter(categoria -> categoria.getTipo() == tipoCategoria)
                    .collect(Collectors.toList());
        }

        // Convertir la lista de categorías a una estructura de datos que la JTable pueda usar
        Object[][] data = new Object[categorias.size()][3];
        for (int i = 0; i < categorias.size(); i++) {
            Categoria categoria = categorias.get(i);
            data[i][0] = categoria.getId();
            data[i][1] = categoria.getCategoria();
            data[i][2] = categoria.getTipo();
        }

        // Establecer los datos en la tabla
        categoriaTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"ID", "Nombre", "Tipo"}
        ));
    }

    private void actualizarTablaCategoriasPorNombre(String nombreCategoria) {
        CategoriaDaoImpl categoriaDao = new CategoriaDaoImpl();
        List<Categoria> categorias = categoriaDao.getCategoriaPorNombre(nombreCategoria);

        Object[][] data = new Object[categorias.size()][3];
        for (int i = 0; i < categorias.size(); i++) {
            Categoria categoria = categorias.get(i);
            data[i][0] = categoria.getId();
            data[i][1] = categoria.getCategoria();
            data[i][2] = categoria.getTipo();
        }

        categoriaTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"ID", "Nombre", "Tipo"}
        ));
    }

    private void actualizarTablaCategoriasPorTipo(TipoCategoria tipoCategoria) {
        CategoriaDaoImpl categoriaDao = new CategoriaDaoImpl();
        List<Categoria> categorias = categoriaDao.getCategoriaPorTipo(tipoCategoria);

        Object[][] data = new Object[categorias.size()][3];
        for (int i = 0; i < categorias.size(); i++) {
            Categoria categoria = categorias.get(i);
            data[i][0] = categoria.getId();
            data[i][1] = categoria.getCategoria();
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
