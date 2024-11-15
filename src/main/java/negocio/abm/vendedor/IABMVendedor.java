package negocio.abm.vendedor;

import modelo.vendedor.Vendedor;
import negocio.abm.vendedor.exception.VendedorException;

/**
 *
 * @author rodri
 */

public interface IABMVendedor {
    public void altaVendedor(Vendedor vendedor) throws VendedorException;

    public void bajaVendedor(String codigo) throws VendedorException;

    public void modificarDatosVendedor(String codigo, Vendedor vendedorModificado) throws VendedorException;

    public void listarVendedores(String codigo, String nombre, String apellido, int dni);

    public String asignarCodigoVendedor();
}