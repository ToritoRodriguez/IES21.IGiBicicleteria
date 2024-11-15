package modelo.producto.marca;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rodri
 */

public class Marca {

    private int id; 
    private String codigo;
    private String marca;
    private List<Modelo> modelos;

    public Marca(int id, String marca) {
        this.id = id;
        this.marca = marca;
    }
    
    public Marca(String marca) {
        this.marca = marca;
        this.modelos = new ArrayList<>();
    }

    // Setter para el código
    public void setCodigo(String codigo) {
        this.codigo = codigo;  // Asegúrate de que esta línea esté presente
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCodigo() {
        return codigo;
    }
    
    public List<Modelo> getModelos() {
        return modelos;
    }

    public void setModelos(List<Modelo> modelos) {
        this.modelos = modelos;
    }

    @Override
    public String toString() {
        return "ID: " + this.getId() + ", Marca: " + this.getMarca();
    }
}