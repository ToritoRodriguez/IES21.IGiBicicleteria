package modelo.vendedor;

import modelo.persona.Persona;

/**
 *
 * @author rodri
 */

public class Vendedor extends Persona {

    private int id;
    private String codigo;
    private String cuit;
    private String sucursal;
    
    public Vendedor() {
        super();
    }

    // Se usa en obtenerVendedor, getVendedores y obtenerVendedorPorId
    public Vendedor(String cuit, String sucursal, String nombre, String apellido, String dni, String telefono, String email) {
        super(nombre, apellido, dni, telefono, email); 
        this.cuit = cuit;
        this.sucursal = sucursal;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }
}