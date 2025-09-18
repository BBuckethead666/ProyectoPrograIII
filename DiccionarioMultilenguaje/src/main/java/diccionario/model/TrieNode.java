package diccionario.model;

import java.util.*;

public class TrieNode {
    private Map<Character, TrieNode> children;
    private boolean isEndOfWord;
    private Set<String> wordIds; // Para almacenar IDs de palabras que terminan aquí

    public TrieNode() {
        this.children = new HashMap<>();
        this.isEndOfWord = false;
        this.wordIds = new HashSet<>();
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        isEndOfWord = endOfWord;
    }

    public Set<String> getWordIds() {
        return wordIds;
    }

    public void addWordId(String wordId) {
        wordIds.add(wordId);
    }

    public boolean hasChild(char c) {
        return children.containsKey(c);
    }

    public TrieNode getChild(char c) {
        return children.get(c);
    }

    public void addChild(char c, TrieNode node) {
        children.put(c, node);
    }
}