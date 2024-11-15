package negocio.abm.producto;

import java.util.List;
import negocio.abm.producto.exception.MarcaException;
import repositorio.dao.marca.MarcaDaoImpl;
import modelo.producto.marca.Marca;
import repositorio.dao.marca.IDaoMarca;


/**
 *
 * @author rodri
 */

public class ABMMarca implements IABMMarca {

    private IDaoMarca iDaoMarca = new MarcaDaoImpl();

    private void validarDatosMarca(Marca marca) throws MarcaException {
        if (marca == null) {
            throw new MarcaException("La marca no puede ser nula.");
        }
        if (marca.getMarca() == null || marca.getMarca().isEmpty()) {
            throw new MarcaException("El nombre de la marca no puede ser vacío.");
        }
    }

    @Override
    public void altaMarca(Marca marca) throws MarcaException {
        validarDatosMarca(marca);
        iDaoMarca.insertarNuevaMarca(marca);
        System.out.println("Marca " + marca.getMarca() + " dada de alta con éxito.");
    }

    @Override
    public void bajaMarcaPorId(int idMarca) throws MarcaException {
        // Intentamos obtener la marca por ID
        Marca marca = iDaoMarca.obtenerMarca(idMarca);

        // Verificamos si la marca existe
        if (marca != null) {
            // Obtenemos el nombre de la marca, por si lo necesitamos más tarde (para log o más detalles)
            String nombreMarca = marca.getMarca();

            // Procedemos a eliminar la marca por ID
            iDaoMarca.eliminarMarcaPorId(idMarca);

            // Informamos que la marca ha sido eliminada
            System.out.println("Marca con ID " + idMarca + " y nombre " + nombreMarca + " eliminada exitosamente.");
        } else {
            // Si la marca no existe, lanzamos una excepción personalizada
            throw new MarcaException("La marca con ID " + idMarca + " no existe.");
        }
    }

    @Override
    public void bajaMarcaPorNombre(String nombreMarca) throws MarcaException {
        // Intentamos obtener la marca por nombre
        Marca marca = iDaoMarca.buscarMarcaPorNombre(nombreMarca);

        // Verificamos si la marca existe
        if (marca != null) {
            // Procedemos a eliminar la marca por nombre
            iDaoMarca.eliminarMarcaPorNombre(nombreMarca);

            // Informamos que la marca ha sido eliminada
            System.out.println("Marca con nombre " + nombreMarca + " eliminada exitosamente.");
        } else {
            // Si la marca no existe, lanzamos una excepción personalizada
            throw new MarcaException("La marca con nombre " + nombreMarca + " no existe.");
        }
    } 

    @Override
    public void modificarMarca(int idMarca, Marca marcaModificada) throws MarcaException {
        validarDatosMarca(marcaModificada);
        Marca marcaExistente = iDaoMarca.obtenerMarca(idMarca);
        if (marcaExistente != null) {
            iDaoMarca.modificarMarca(idMarca, marcaModificada);
            System.out.println("Marca con ID " + idMarca + " modificada exitosamente.");
        } else {
            throw new MarcaException("La marca con ID " + idMarca + " no existe.");
        }
    }

    @Override
    public List<Marca> listarMarcas(int idMarca, String nombreMarca) {
        List<Marca> marcas = iDaoMarca.getMarcas(idMarca, nombreMarca);
        if (marcas.isEmpty()) {
            System.out.println("No se encontraron marcas.");
        } else {
            for (Marca marca : marcas) {
                System.out.println(marca.toString());  
            }
        }
        return marcas;
    }

    public Marca buscarMarcaPorCodigo(int idMarca) {
        return iDaoMarca.obtenerMarca(idMarca);
    }
    
    public Marca buscarMarcaPorNombre(String nombreMarca) {
        return iDaoMarca.buscarMarcaPorNombre(nombreMarca);
    }
}