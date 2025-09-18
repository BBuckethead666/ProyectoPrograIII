package diccionario.model;

import java.util.*;

public class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    /**
     * Inserta una palabra en el trie
     * @param word La palabra a insertar
     * @param wordId El ID de la palabra en el diccionario
     */
    public void insert(String word, String wordId) {
        TrieNode current = root;
        word = word.toLowerCase(); // Búsqueda case-insensitive

        for (char c : word.toCharArray()) {
            if (!current.hasChild(c)) {
                current.addChild(c, new TrieNode());
            }
            current = current.getChild(c);
        }

        current.setEndOfWord(true);
        current.addWordId(wordId);
    }

    /**
     * Busca palabras que comiencen con el prefijo dado
     * @param prefix El prefijo a buscar
     * @return Lista de IDs de palabras que coinciden
     */
    public List<String> searchByPrefix(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode current = root;
        prefix = prefix.toLowerCase();

        // Navegar hasta el final del prefijo
        for (char c : prefix.toCharArray()) {
            if (!current.hasChild(c)) {
                return results; // Prefijo no encontrado
            }
            current = current.getChild(c);
        }

        // Recoger todas las palabras desde este nodo
        collectWords(current, results);
        return results;
    }

    /**
     * Método recursivo para recolectar todas las palabras desde un nodo
     */
    private void collectWords(TrieNode node, List<String> results) {
        if (node.isEndOfWord()) {
            results.addAll(node.getWordIds());
        }

        for (TrieNode child : node.getChildren().values()) {
            collectWords(child, results);
        }
    }

    /**
     * Verifica si una palabra existe en el trie
     */
    public boolean containsWord(String word) {
        TrieNode current = root;
        word = word.toLowerCase();

        for (char c : word.toCharArray()) {
            if (!current.hasChild(c)) {
                return false;
            }
            current = current.getChild(c);
        }

        return current.isEndOfWord();
    }

    /**
     * Limpia el trie completamente
     */
    public void clear() {
        this.root = new TrieNode();
    }

    /**
     * Obtiene el número aproximado de palabras en el trie
     */
    public int size() {
        return countWords(root);
    }

    private int countWords(TrieNode node) {
        int count = node.isEndOfWord() ? node.getWordIds().size() : 0;

        for (TrieNode child : node.getChildren().values()) {
            count += countWords(child);
        }

        return count;
    }
}