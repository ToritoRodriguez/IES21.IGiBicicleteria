package negocio.abm.producto;

import java.util.List;
import modelo.producto.Categoria;
import negocio.abm.producto.exception.CategoriaException;
import repositorio.dao.categoria.CategoriaDaoImpl;
import repositorio.dao.categoria.IDaoCategoria;
import modelo.producto.TipoCategoria;

/**
 *
 * @author rodri
 */

public class ABMCategoria implements IABMCategoria {

    private IDaoCategoria iDaoCategoria = new CategoriaDaoImpl();

    private void validarDatosCategoria(Categoria categoria) throws CategoriaException {
        if (categoria == null) {
            throw new CategoriaException("La categoría no puede ser nula.");
        }
        if (categoria.getCategoria() == null || categoria.getCategoria().isEmpty()) {
            throw new CategoriaException("El nombre de la categoría no puede ser vacío.");
        }
        if (categoria.getTipo() == null) {
            throw new CategoriaException("El tipo de la categoría no puede ser nulo.");
        }
    }

    private void asignarTipoCategoria(Categoria categoria, int tipoOpcion) throws IllegalArgumentException {
        switch (tipoOpcion) {
            case 1:
                categoria.setTipo(TipoCategoria.BICICLETA);
                break;
            case 2:
                categoria.setTipo(TipoCategoria.ACCESORIO);
                break;
            case 3:
                categoria.setTipo(TipoCategoria.RESPUESTO);
                break;
            default:
                throw new IllegalArgumentException("Tipo de categoría inválido.");
        }
    }

    @Override
    public void altaCategoria(Categoria categoria, int tipoOpcion) throws CategoriaException {
        validarDatosCategoria(categoria);

        asignarTipoCategoria(categoria, tipoOpcion);

        iDaoCategoria.insertarNuevaCategoria(categoria);
        System.out.println("Categoría " + categoria.getCategoria() + " dada de alta con éxito.");
    }

    @Override
    public void bajaCategoriaPorId(int idCategoria) throws CategoriaException {
        if (idCategoria != 0) {
            Categoria categoria = iDaoCategoria.obtenerCategoria(idCategoria);
            if (categoria != null) {
                iDaoCategoria.eliminarCategoriaPorId(idCategoria);
                System.out.println("Categoría con ID " + idCategoria + " eliminada exitosamente.");
            } else {
                throw new CategoriaException("La categoría con ID " + idCategoria + " no existe.");
            }
        } else {
            throw new CategoriaException("Debe proporcionar un ID para eliminar la categoría.");
        }
    }

    @Override
    public void bajaCategoriaPorNombre(String nombreCategoria) throws CategoriaException {
        if (nombreCategoria != null && !nombreCategoria.isEmpty()) {
            if (iDaoCategoria.categoriaExistePorNombre(nombreCategoria)) {
                iDaoCategoria.eliminarCategoriaPorNombre(nombreCategoria);
                System.out.println("Categoría con nombre " + nombreCategoria + " eliminada exitosamente.");
            } else {
                throw new CategoriaException("La categoría con nombre " + nombreCategoria + " no existe.");
            }
        } else {
            throw new CategoriaException("Debe proporcionar un nombre para eliminar la categoría.");
        }
    }


    @Override
    public void modificarCategoria(int idCategoria, Categoria categoriaModificada) throws CategoriaException {
        validarDatosCategoria(categoriaModificada);

        asignarTipoCategoria(categoriaModificada, categoriaModificada.getTipo().ordinal() + 1); 

        Categoria categoriaExistente = iDaoCategoria.obtenerCategoria(idCategoria);
        if (categoriaExistente != null) {
            iDaoCategoria.modificarCategoria(idCategoria, categoriaModificada);
            System.out.println("Categoría con ID " + idCategoria + " modificada exitosamente.");
        } else {
            throw new CategoriaException("La categoría con ID " + idCategoria + " no existe.");
        }
    }

    @Override
    public List<Categoria> listarCategorias(int idCategoria, String nombreCategoria) {
        List<Categoria> categorias = iDaoCategoria.getCategorias(idCategoria, nombreCategoria);
        if (categorias.isEmpty()) {
            System.out.println("No se encontraron categorías.");
        } else {
            for (Categoria categoria : categorias) {
                System.out.println(categoria.toString());  
            }
        }
        return categorias;
    }

    public Categoria obtenerCategoriaPorCodigo(int idCategoria) {
        return iDaoCategoria.obtenerCategoria(idCategoria);
    }
    
    public Categoria obtenerCategoriaPorNombre(String nombreCategoria) throws CategoriaException {
        Categoria categoria = iDaoCategoria.buscarCategoriaPorNombre(nombreCategoria);

        if (categoria == null) {
            throw new CategoriaException("No se encontró una categoría con el nombre " + nombreCategoria);
        }

        return categoria;
    }
}