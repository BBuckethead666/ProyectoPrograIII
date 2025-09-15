package diccionario.model;

import java.util.List;

/**
 * Clase Diccionario que actúa como fachada para manejar
 * la estructura Trie y las palabras en distintos idiomas.
 */
public class Diccionario {

    /** Estructura interna de almacenamiento */
    private Trie trie;

    /** Idioma principal del diccionario */
    private Idioma idiomaBase;

    /** Constructor con idioma base */
    public Diccionario(Idioma idiomaBase) {
        this.trie = new Trie();
        this.idiomaBase = idiomaBase;
    }

    /**
     * Agrega una nueva palabra al diccionario.
     *
     * @param palabra objeto Palabra a agregar
     */
    public void agregarPalabra(Palabra palabra) {
        trie.insertar(palabra);
    }

    /**
     * Busca una palabra por texto.
     *
     * @param texto texto a buscar
     * @return Palabra encontrada o null si no existe
     */
    public Palabra buscarPalabra(String texto) {
        return trie.buscar(texto);
    }

    /**
     * Obtiene todas las palabras del diccionario.
     *
     * @return lista de Palabra
     */
    public List<Palabra> obtenerPalabras() {
        return trie.obtenerTodasLasPalabras();
    }

    // Getters
    public Idioma getIdiomaBase() {
        return idiomaBase;
    }
}
