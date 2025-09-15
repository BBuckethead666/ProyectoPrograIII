package diccionario.model;

import java.util.Map;

import java.util.HashMap;


/**
 * Clase que representa un nodo en la estructura Trie.
 * Cada nodo puede almacenar un carácter, sus hijos y,
 * opcionalmente, una palabra completa (traducción).
 */
public class Nodo {

    /** Hijos del nodo, indexados por el carácter que los representa */
    private Map<Character, Nodo> hijos;

    /** Indica si este nodo marca el final de una palabra */
    private boolean esFinDePalabra;

    /** Palabra asociada (si el nodo corresponde al final de una palabra) */
    private Palabra palabra;

    /** Constructor por defecto */
    public Nodo() {
        this.hijos = new HashMap<>();
        this.esFinDePalabra = false;
        this.palabra = null;
    }

    // Getters y Setters
    public Map<Character, Nodo> getHijos() {
        return hijos;
    }

    public boolean isEsFinDePalabra() {
        return esFinDePalabra;
    }

    public void setEsFinDePalabra(boolean esFinDePalabra) {
        this.esFinDePalabra = esFinDePalabra;
    }

    public Palabra getPalabra() {
        return palabra;
    }

    public void setPalabra(Palabra palabra) {
        this.palabra = palabra;
    }
}
