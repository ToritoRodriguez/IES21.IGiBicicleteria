package modelo.cliente;

import modelo.persona.Persona;

/**
 *
 * @author rodri
 */

public class Cliente extends Persona {

    private int id;
    private String codigo;
    private String cuil;

    public Cliente() {
        super();
    }

    public Cliente(String cuil) {
        this.cuil = cuil;
    }
    
    public Cliente(String cuil, String nombre, String apellido, String dni, String telefono, String email) {
        super(nombre, apellido, dni, telefono, email);
        this.cuil = cuil;
    }
    
    public Cliente(String codigo, String cuil, String nombre, String apellido, String dni, String telefono, String email) {
        super(nombre, apellido, dni, telefono, email);
        this.codigo = codigo; 
        this.cuil = cuil;
    }

    public Cliente(int id, String codigo, String cuil, String nombre, String apellido, String dni, String telefono, String email) {
        super(nombre, apellido, dni, telefono, email);
        this.id = id;
        this.codigo = codigo;
        this.cuil = cuil;
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

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    @Override
    public String toString() {
        return super.toString() + " CODIGO: " + this.getCodigo() + " CUIL: " + this.cuil;
    }
}