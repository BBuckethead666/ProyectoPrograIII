/**
 * The Trie class implements a trie data structure for storing and manipulating words efficiently.
 */
package diccionario.model;

import java.util.*;

public class Trie {
    private TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode node = root;
        for (char ch : word.toLowerCase().toCharArray()) {
            node = node.getChildren().computeIfAbsent(ch, c -> new TrieNode());
        }
        node.setEndOfWord(true);
        node.getWords().add(word);
    }

    public void delete(String word) {
        delete(root, word.toLowerCase(), 0);
    }

    private boolean delete(TrieNode node, String word, int index) {
        if (index == word.length()) {
            if (!node.isEndOfWord()) return false;
            node.setEndOfWord(false);
            node.getWords().remove(word);
            return node.getChildren().isEmpty();
        }
        char ch = word.charAt(index);
        TrieNode child = node.getChildren().get(ch);
        if (child == null) return false;
        boolean shouldDeleteChild = delete(child, word, index + 1);
        if (shouldDeleteChild) {
            node.getChildren().remove(ch);
            return node.getChildren().isEmpty() && !node.isEndOfWord();
        }
        return false;
    }

    public List<String> startsWith(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toLowerCase().toCharArray()) {
            node = node.getChildren().get(ch);
            if (node == null) return Collections.emptyList();
        }
        List<String> results = new ArrayList<>();
        collectWords(node, results);
        return results;
    }

    private void collectWords(TrieNode node, List<String> results) {
        if (node.isEndOfWord()) {
            results.addAll(node.getWords());
        }
        for (TrieNode child : node.getChildren().values()) {
            collectWords(child, results);
        }
    }

    public void clear() {
        root = new TrieNode();
    }
}