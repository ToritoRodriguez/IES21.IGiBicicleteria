package negocio.abm.vendedor;

import java.util.List;
import modelo.vendedor.Vendedor;
import negocio.abm.vendedor.exception.VendedorException;
import repositorio.dao.vendedor.IDaoVendedor;
import repositorio.dao.vendedor.VendedorDaoImpl;

/**
 *
 * @author rodri
 */

public class ABMVendedor implements IABMVendedor {

    private IDaoVendedor iDaoVendedor = new VendedorDaoImpl();

    private void validarDatosVendedor(Vendedor vendedor) throws VendedorException {
        if (vendedor == null) {
            throw new VendedorException("El vendedor no puede ser nulo");
        }
        if (vendedor.getNombre() == null || vendedor.getNombre().isEmpty()) {
            throw new VendedorException("El nombre del vendedor no puede ser vacío");
        }
        if (vendedor.getApellido() == null || vendedor.getApellido().isEmpty()) {
            throw new VendedorException("El apellido del vendedor no puede ser vacío");
        }
        if (vendedor.getDni() == null || vendedor.getDni().isEmpty()) {
            throw new VendedorException("El DNI del vendedor no puede ser vacío");
        }

            try {
                // Verificar si el DNI es un número válido
                Long dniValue = Long.parseLong(vendedor.getDni());

                // Validar que el DNI esté dentro de un rango razonable
                if (dniValue <= 0) {
                    throw new VendedorException("El valor del DNI no es correcto");
                }
            } catch (NumberFormatException e) {
                throw new VendedorException("El DNI debe ser un número válido");
            }

        if (vendedor.getSucursal() == null || vendedor.getSucursal().isEmpty()) {
            throw new VendedorException("La sucursal del vendedor no puede ser vacía");
        }
    }

    @Override
    public String asignarCodigoVendedor() {
        return "V-" + iDaoVendedor.getProximoCodigoVendedor();
    }

    @Override
    public void altaVendedor(Vendedor vendedor) throws VendedorException {
        validarDatosVendedor(vendedor);

        String codigo = asignarCodigoVendedor();
        vendedor.setCodigo(codigo);  

        if (iDaoVendedor.obtenerVendedor(vendedor.getCodigo()) == null) {
            iDaoVendedor.insertarNuevoVendedor(vendedor); 
            System.out.println("El vendedor se agregó de forma correcta");
        } else {
            System.out.println("El vendedor ya existe");
        }
    }

    @Override
    public void bajaVendedor(String codigo) throws VendedorException {
        if (codigo != null && !codigo.isEmpty()) {
            iDaoVendedor.eliminarVendedor(codigo);
            System.out.println("El Vendedor con código: " + codigo + " fue eliminado con éxito");
        } else {
            System.out.println("El código del vendedor a eliminar no puede ser nulo o vacío");
        }
    }

    @Override
    public void modificarDatosVendedor(String codigo, Vendedor vendedorModificado) throws VendedorException {
        validarDatosVendedor(vendedorModificado);

        Vendedor vendedorExistente = iDaoVendedor.obtenerVendedor(codigo);
        if (vendedorExistente != null) {
            iDaoVendedor.modificarVendedor(codigo, vendedorModificado);
            System.out.println("Los datos del vendedor han sido modificados correctamente");
        } else {
            System.out.println("El vendedor no existe");
        }
    }

    @Override
    public void listarVendedores(String codigo, String nombre, String apellido, int dni) {
        List<Vendedor> vendedores = iDaoVendedor.getVendedores(codigo, nombre, apellido);

        for (Vendedor vendedor : vendedores) {
            System.out.println(vendedor.toString());
        }
    }
    
    public Vendedor buscarVendedorPorCodigo(String codigo) {
        return iDaoVendedor.obtenerVendedor(codigo);  
    }
}