package negocio.abm.vendedor;

import modelo.vendedor.Vendedor;
import negocio.abm.vendedor.exception.VendedorException;

/**
 *
 * @author rodri
 */

public interface IABMVendedor {
    public void altaVendedor(Vendedor vendedor) throws VendedorException;

    public void modificarDatosVendedor(String codigo, Vendedor vendedorModificado) throws VendedorException;

    public String asignarCodigoVendedor();
}