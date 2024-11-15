package principal.vista.productos.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import modelo.producto.marca.Modelo;
import repositorio.dao.modelo.ModeloDaoImpl;
import modelo.producto.marca.Rodado;
import java.util.stream.Collectors;
import principal.vista.productos.HomeMenuProductos;
import modelo.producto.marca.Marca;
import repositorio.dao.marca.MarcaDaoImpl;


/**
 *
 * @author rodri
 */

public class ListarModelo extends javax.swing.JFrame {

    private JTable modeloTable;
    private JButton listarButton, backButton;
    private JComboBox<Rodado> rodadoComboBox;  // ComboBox para seleccionar el rodado
    private JTextField marcaField; // Campo para ingresar el código del modelo o la marca
    private JComboBox<String> marcaComboBox; // ComboBox para seleccionar la marca

    public ListarModelo() {
        setTitle("Listar Modelos");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrar la ventana en la pantalla
        setLayout(new BorderLayout(10, 10));

        // Panel superior para el título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Listar Modelos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel de filtro para el código del modelo (opcional), la marca y el rodado
        JPanel filterPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        filterPanel.add(new JLabel("Marca (Opcional):"));
        marcaComboBox = new JComboBox<>();
        marcaComboBox.addItem("");  // Agregar un item vacío como opción predeterminada
        marcaComboBox.addItem("Seleccione una marca");  // Opción de texto para mostrar un valor "por defecto"
        MarcaDaoImpl MarcaDao = new MarcaDaoImpl();
        List<Marca> marcas = MarcaDao.getMarcasComboBox();  // Método que obtiene todas las marcas desde la base de datos
        for (Marca marca : marcas) {
            marcaComboBox.addItem(marca.getMarca());  // Agregar nombre de marca al ComboBox
        }
        filterPanel.add(marcaComboBox);

        // ComboBox de rodado
        filterPanel.add(new JLabel("Rodado (Opcional):"));
        rodadoComboBox = new JComboBox<>();
        rodadoComboBox.addItem(null);  // Opción de "Todos" (deseleccionado)
        for (Rodado rodado : Rodado.values()) {
            rodadoComboBox.addItem(rodado);  // Añadir los valores de rodado
        }
        filterPanel.add(rodadoComboBox);

        listarButton = new JButton("Listar");
        listarButton.addActionListener(new ListarButtonListener());  // Pasamos el campo de código al listener
        filterPanel.add(listarButton);

        add(filterPanel, BorderLayout.NORTH);

        // Tabla para listar los modelos con columnas id, marca, nombre, descripcion, rodado
        String[] columnNames = {"ID", "Marca", "Nombre", "Descripción", "Rodado"};
        modeloTable = new JTable(new Object[0][5], columnNames);
        JScrollPane scrollPane = new JScrollPane(modeloTable);
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
            String marca = (String) marcaComboBox.getSelectedItem();  // Obtener marca seleccionada
            Rodado rodado = (Rodado) rodadoComboBox.getSelectedItem();

            if (marca != null && !marca.isEmpty() && rodado != null) {
                actualizarTablaModelos(marca, rodado);  // Buscar por marca y rodado
            } else if (marca != null && !marca.isEmpty()) {
                actualizarTablaModelosPorMarca(marca);  // Buscar solo por marca
            } else if (rodado != null) {
                actualizarTablaModelosPorRodado(rodado);  // Buscar solo por rodado
            } else {
                actualizarTablaModelos(null, null);  // Si todos los campos están vacíos, traer todos los modelos
            }
        }
    }

    private void actualizarTablaModelos(String marca, Rodado rodado) {
        ModeloDaoImpl modeloDao = new ModeloDaoImpl();
        List<Modelo> modelos = modeloDao.getModelos(0, null); // Inicialmente obtiene todos los modelos.

        // Filtrar los modelos por marca y rodado si se proporcionan
        if (marca != null && !marca.isEmpty()) {
            modelos = modelos.stream()
                    .filter(modelo -> modelo.getMarca().getMarca().equalsIgnoreCase(marca))
                    .collect(Collectors.toList());
        }
        if (rodado != null) {
            modelos = modelos.stream()
                    .filter(modelo -> modelo.getRodado() == rodado)
                    .collect(Collectors.toList());
        }

        // Convertir la lista de modelos a una estructura de datos que la JTable pueda usar
        Object[][] data = new Object[modelos.size()][5];
        for (int i = 0; i < modelos.size(); i++) {
            Modelo modelo = modelos.get(i);
            data[i][0] = modelo.getId();
            data[i][1] = modelo.getMarca().getMarca();
            data[i][2] = modelo.getModelo();
            data[i][3] = modelo.getDescripcion();
            data[i][4] = modelo.getRodado();
        }

        // Establecer los datos en la tabla
        modeloTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"ID", "Marca", "Nombre", "Descripción", "Rodado"}
        ));
    }

    private void actualizarTablaModelosPorMarca(String marca) {
        ModeloDaoImpl modeloDao = new ModeloDaoImpl();
        List<Modelo> modelos = modeloDao.getModelosPorMarca(marca);

        Object[][] data = new Object[modelos.size()][5];
        for (int i = 0; i < modelos.size(); i++) {
            Modelo modelo = modelos.get(i);
            data[i][0] = modelo.getId();
            data[i][1] = modelo.getMarca().getMarca();
            data[i][2] = modelo.getModelo();
            data[i][3] = modelo.getDescripcion();
            data[i][4] = modelo.getRodado();
        }

        modeloTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"ID", "Marca", "Nombre", "Descripción", "Rodado"}
        ));
    }

    private void actualizarTablaModelosPorRodado(Rodado rodado) {
        ModeloDaoImpl modeloDao = new ModeloDaoImpl();
        List<Modelo> modelos = modeloDao.getModelosPorRodado(rodado);

        Object[][] data = new Object[modelos.size()][5];
        for (int i = 0; i < modelos.size(); i++) {
            Modelo modelo = modelos.get(i);
            data[i][0] = modelo.getId();
            data[i][1] = modelo.getMarca().getMarca();
            data[i][2] = modelo.getModelo();
            data[i][3] = modelo.getDescripcion();
            data[i][4] = modelo.getRodado();
        }

        modeloTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"ID", "Marca", "Nombre", "Descripción", "Rodado"}
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
            java.util.logging.Logger.getLogger(ListarModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListarModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListarModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListarModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListarModelo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
