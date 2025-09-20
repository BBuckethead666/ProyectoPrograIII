package diccionario.model;

import java.util.*;

public class TrieNode {
    private Map<Character, TrieNode> children = new HashMap<>();
    private boolean isEndOfWord = false;
    private Set<String> words = new HashSet<>(); // Palabras completas que terminan aquí

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        isEndOfWord = endOfWord;
    }

    public Set<String> getWords() {
        return words;
    }
}