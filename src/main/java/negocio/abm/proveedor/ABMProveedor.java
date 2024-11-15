package negocio.abm.proveedor;

import java.sql.SQLException;
import java.util.List;
import modelo.proveedor.Proveedor;
import negocio.abm.proveedor.exception.ProveedorException;
import repositorio.dao.proveedor.IDaoProveedor;
import repositorio.dao.proveedor.ProveedorDaoImpl;

/**
 *
 * @author rodri
 */

public class ABMProveedor implements IAMBProveedor{

    private IDaoProveedor iDaoProveedor = new ProveedorDaoImpl();

    private void validarDatosProveedor(Proveedor proveedor) throws ProveedorException {
        if (proveedor == null) {
            throw new ProveedorException("El proveedor no puede ser nulo");
        }
        if (proveedor.getNombre() == null || proveedor.getNombre().isEmpty()) {
            throw new ProveedorException("El nombre del proveedor no puede ser vacío");
        }
        if (proveedor.getApellido() == null || proveedor.getApellido().isEmpty()) {
            throw new ProveedorException("El apellido del proveedor no puede ser vacío");
        }
        if (proveedor.getDni() == null || proveedor.getDni().isEmpty()) {
            throw new ProveedorException("El DNI del proveedor no puede ser vacío");
        }

            try {
                // Verificar si el DNI es un número válido
                Long dniValue = Long.parseLong(proveedor.getDni());

                // Validar que el DNI esté dentro de un rango razonable
                if (dniValue <= 0) {
                    throw new ProveedorException("El valor del DNI no es correcto");
                }
            } catch (NumberFormatException e) {
                throw new ProveedorException("El DNI debe ser un número válido");
            }

        if (proveedor.getCuit() == null || proveedor.getCuit().isEmpty()) {
            throw new ProveedorException("El CUIT no puede ser vacío");
        }
        if (proveedor.getNombreFantasia() == null || proveedor.getNombreFantasia().isEmpty()) {
            throw new ProveedorException("El nombre de fantasía no puede ser vacío");
        }
    }


    @Override
    public String asignarCodigoProveedor() {
        return "P-" + iDaoProveedor.getProximoCodigoProveedor();
    }

    @Override
    public void altaProveedor(Proveedor proveedor) throws ProveedorException {
        validarDatosProveedor(proveedor);
        proveedor.setCodigo(asignarCodigoProveedor());

        if (iDaoProveedor.obtenerProveedor(proveedor.getCodigo()) == null) {
            iDaoProveedor.insertarNuevoProveedor(proveedor);
            System.out.println("El proveedor se agregó de forma correcta");
        } else {
            System.out.println("El proveedor ya existe");
        }
    }

    @Override
    public void bajaProveedor(String codigo) {
        if (codigo != null && !codigo.isEmpty()) {
            iDaoProveedor.eliminarProveedor(codigo);
            System.out.println("El Proveedor con código: " + codigo + " fue eliminado con éxito.");
        } else {
            System.out.println("El proveedor o su código a eliminar no puede ser nulo o vacío.");
        }
    }

    @Override
    public void modificarDatosProveedor(String codigo, Proveedor proveedorModificado) throws ProveedorException {
        validarDatosProveedor(proveedorModificado);

        Proveedor proveedorExistente = iDaoProveedor.obtenerProveedor(codigo);
        if (proveedorExistente != null) {
            iDaoProveedor.modificarProveedor(codigo, proveedorModificado);
            System.out.println("Los datos del proveedor han sido modificados correctamente");
        } else {
            System.out.println("El proveedor no existe");
        }
    }

    @Override
    public void listarProveedor(String codigo, String nombre, String apellido) {
        List<Proveedor> proveedores = iDaoProveedor.getProveedor(codigo, nombre, apellido);
        
        for (Proveedor proveedor : proveedores) {
            System.out.println(proveedor.toString());
        }
    }

    public Proveedor buscarProveedorPorCodigo(String codigo) {
        return iDaoProveedor.obtenerProveedor(codigo);
    }
}