package negocio.abm.producto;

import java.util.List;
import negocio.abm.producto.exception.ModeloException;
import repositorio.dao.modelo.IDaoModelo;
import repositorio.dao.modelo.ModeloDaoImpl;
import modelo.producto.marca.Modelo;

/**
 *
 * @author rodri
 */

public class ABMModelo implements IABMModelo {

    private IDaoModelo iDaoModelo = new ModeloDaoImpl();

    private void validarDatosModelo(Modelo modelo) throws ModeloException {
        if (modelo == null) {
            throw new ModeloException("El modelo no puede ser nulo.");
        }
        if (modelo.getModelo() == null || modelo.getModelo().isEmpty()) {
            throw new ModeloException("El nombre del modelo no puede ser vacío.");
        }
        if (modelo.getDescripcion() == null || modelo.getDescripcion().isEmpty()) {
            throw new ModeloException("La descripción del modelo no puede ser vacía.");
        }
        if (modelo.getMarca() == null) {
            throw new ModeloException("La marca del modelo no puede ser nula.");
        }
        if (modelo.getRodado() == null) {
            throw new ModeloException("El rodado del modelo no puede ser nulo.");
        }
    }

    @Override
    public void altaModelo(Modelo modelo) throws ModeloException {
        validarDatosModelo(modelo);
        iDaoModelo.insertarNuevoModelo(modelo);
        System.out.println("Modelo " + modelo.getModelo() + " dado de alta con éxito.");
    }

    @Override
    public void bajaModeloPorId(int idModelo) throws ModeloException {
        // Verificamos si el modelo existe por ID
        Modelo modelo = iDaoModelo.obtenerModelo(idModelo);

        // Si el modelo existe, lo eliminamos
        if (modelo != null) {
            iDaoModelo.eliminarModeloPorId(idModelo);
            System.out.println("Modelo con ID " + idModelo + " eliminado exitosamente.");
        } else {
            throw new ModeloException("El modelo con ID " + idModelo + " no existe.");
        }
    }

    @Override
    public void bajaModeloPorNombre(String nombreModelo) throws ModeloException {
        // Verificamos si el modelo existe por nombre
        Modelo modelo = iDaoModelo.buscarModeloPorNombre(nombreModelo);

        // Si el modelo existe, lo eliminamos
        if (modelo != null) {
            iDaoModelo.eliminarModeloPorNombre(nombreModelo);
            System.out.println("Modelo con nombre '" + nombreModelo + "' eliminado exitosamente.");
        } else {
            throw new ModeloException("El modelo con nombre '" + nombreModelo + "' no existe.");
        }
    }

    @Override
    public void modificarModelo(int idModelo, Modelo modeloModificado) throws ModeloException {
        validarDatosModelo(modeloModificado);
        Modelo modeloExistente = iDaoModelo.obtenerModelo(idModelo);
        if (modeloExistente != null) {
            iDaoModelo.modificarModelo(idModelo, modeloModificado);
            System.out.println("Modelo con ID " + idModelo + " modificado exitosamente.");
        } else {
            throw new ModeloException("El modelo con ID " + idModelo + " no existe.");
        }
    }

    @Override
    public List<Modelo> listarModelos(int idModelo, String nombreModelo) {
        List<Modelo> modelos = iDaoModelo.getModelos(idModelo, nombreModelo);

        if (modelos.isEmpty()) {
            System.out.println("No se encontraron modelos.");
        } else {
            for (Modelo modelo : modelos) {
                System.out.println(modelo.toString());  
            }
        }

        return modelos;
    }
    
    public Modelo obtenerModeloPorId(int idModelo) {
        // Llamamos al DAO para obtener el modelo por ID desde la base de datos
        return iDaoModelo.obtenerModelo(idModelo);
    }

    public Modelo obtenerModeloPorNombre(String nombreModelo) {
        // Llamamos al DAO para obtener el modelo por nombre desde la base de datos
        return iDaoModelo.buscarModeloPorNombre(nombreModelo);
    }
}