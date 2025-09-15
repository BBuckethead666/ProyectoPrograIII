package diccionario.model;
import java.util.ArrayList;
import java.util.List;

/**
 * Estructura de datos Trie (árbol de prefijos) para almacenar palabras.
 * Permite inserción y búsqueda eficiente de traducciones.
 */
public class Trie {

    /** Nodo raíz del Trie */
    private Nodo raiz;

    /** Constructor: inicializa el Trie vacío */
    public Trie() {
        this.raiz = new Nodo();
    }

    /**
     * Inserta una palabra en el Trie.
     *
     * @param palabra objeto Palabra a insertar
     */
    public void insertar(Palabra palabra) {
        Nodo nodoActual = raiz;
        String texto = palabra.getTexto().toLowerCase();

        for (char c : texto.toCharArray()) {
            nodoActual.getHijos().putIfAbsent(c, new Nodo());
            nodoActual = nodoActual.getHijos().get(c);
        }

        nodoActual.setEsFinDePalabra(true);
        nodoActual.setPalabra(palabra);
    }

    /**
     * Busca una palabra en el Trie.
     *
     * @param texto texto de la palabra a buscar
     * @return la Palabra encontrada o null si no existe
     */
    public Palabra buscar(String texto) {
        Nodo nodoActual = raiz;
        String palabra = texto.toLowerCase();

        for (char c : palabra.toCharArray()) {
            if (!nodoActual.getHijos().containsKey(c)) {
                return null;
            }
            nodoActual = nodoActual.getHijos().get(c);
        }

        return nodoActual.isEsFinDePalabra() ? nodoActual.getPalabra() : null;
    }

    /**
     * Devuelve todas las palabras almacenadas en el Trie.
     *
     * @return lista de Palabra
     */
    public List<Palabra> obtenerTodasLasPalabras() {
        List<Palabra> lista = new ArrayList<>();
        recorrer(raiz, lista);
        return lista;
    }

    /** Método auxiliar para recorrer el Trie */
    private void recorrer(Nodo nodo, List<Palabra> lista) {
        if (nodo.isEsFinDePalabra() && nodo.getPalabra() != null) {
            lista.add(nodo.getPalabra());
        }
        for (Nodo hijo : nodo.getHijos().values()) {
            recorrer(hijo, lista);
        }
    }
}
