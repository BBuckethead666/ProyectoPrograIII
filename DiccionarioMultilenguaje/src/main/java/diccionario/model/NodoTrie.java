package diccionario.model;

import java.util.Map;

public class NodoTrie {
    char Caracter;
    Map <Character, NodoTrie> Hijos;
    boolean EsFinDePalabra;
    Palabra palabraAsociada;

    public NodoTrie(char caracter, Map<Character, NodoTrie> hijos, boolean esFinDePalabra, Palabra palabraAsociada) {
        Caracter = caracter;
        Hijos = hijos;
        EsFinDePalabra = esFinDePalabra;
        this.palabraAsociada = palabraAsociada;
    }
    public void agregarHijo(NodoTrie hijo) {
        Hijos.put(hijo.Caracter, hijo);
    }

    public NodoTrie obtenerHijo(char caracter) {
        return Hijos.get(caracter);
    }

}