package modelo.producto;

/**
 *
 * @author rodri
 */

public class Categoria {

    private String codigo;
    private String nombre;
    private int id;
    private String categoria;
    private TipoCategoria tipo;

    public Categoria(int id, String categoria, TipoCategoria tipo) {
        this.id = id;
        this.categoria = categoria;
        this.tipo = tipo;
    }

    public Categoria(String codigo, String nombre, TipoCategoria tipo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
    }
    
    public Categoria(String categoria, TipoCategoria tipo) {
        this.categoria = categoria;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public TipoCategoria getTipo() {
        return tipo;
    }

    public void setTipo(TipoCategoria tipo) {
        this.tipo = tipo;
    }

    public int getIdCategoria() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return this.getCategoria();  // Devolvemos solo el nombre de la categoría
    }
    
    // Getter para 'codigo'
    public String getCodigo() {
        return codigo;
    }

    // Setter para 'codigo'
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    // Getter para 'nombre'
    public String getNombre() {
        return nombre;
    }

    // Setter para 'nombre'
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}