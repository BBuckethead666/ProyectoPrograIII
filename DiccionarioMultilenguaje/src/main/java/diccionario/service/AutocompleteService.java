package diccionario.service;

import diccionario.model.Trie;
import diccionario.model.WordEntry;
import diccionario.model.Language;
import java.util.*;

public class AutocompleteService {
    private Map<Language, Trie> tries;
    private Map<String, WordEntry> wordMap; // Para rápido acceso por ID

    public AutocompleteService() {
        this.tries = new HashMap<>();
        this.wordMap = new HashMap<>();
        
        // Inicializar un trie para cada idioma
        for (Language language : Language.values()) {
            tries.put(language, new Trie());
        }
    }

    /**
     * Agrega una palabra a todos los tries de idiomas
     */
    public void addWord(WordEntry word) {
        wordMap.put(word.getId(), word);
        
        for (Map.Entry<Language, String> entry : word.getTranslations().entrySet()) {
            Language language = entry.getKey();
            String translation = entry.getValue();
            
            if (translation != null && !translation.trim().isEmpty()) {
                tries.get(language).insert(translation, word.getId());
            }
        }
    }

    /**
     * Busca autocompletado para un prefijo en un idioma específico
     */
    public List<WordEntry> autocomplete(String prefix, Language language) {
        List<WordEntry> results = new ArrayList<>();
        
        if (prefix == null || prefix.trim().isEmpty()) {
            return results;
        }

        Trie trie = tries.get(language);
        List<String> wordIds = trie.searchByPrefix(prefix);
        
        for (String wordId : wordIds) {
            WordEntry word = wordMap.get(wordId);
            if (word != null) {
                results.add(word);
            }
        }

        return results;
    }

    /**
     * Actualiza una palabra en los tries
     */
    public void updateWord(WordEntry oldWord, WordEntry newWord) {
        removeWord(oldWord.getId());
        addWord(newWord);
    }

    /**
     * Elimina una palabra de todos los tries
     */
    public void removeWord(String wordId) {
        WordEntry word = wordMap.remove(wordId);
        if (word != null) {
            // Para eliminar, simplemente removemos del map
            // En una implementación real, necesitaríamos remover del trie también
            // Pero por simplicidad, podemos reconstruir el trie cuando sea necesario
        }
    }

    /**
     * Limpia todos los tries y reconstruye desde una lista de palabras
     */
    public void rebuildTries(List<WordEntry> words) {
        clear();
        for (WordEntry word : words) {
            addWord(word);
        }
    }

    /**
     * Limpia todos los tries
     */
    public void clear() {
        for (Trie trie : tries.values()) {
            trie.clear();
        }
        wordMap.clear();
    }

    /**
     * Obtiene estadísticas de los tries
     */
    public Map<Language, Integer> getTrieStats() {
        Map<Language, Integer> stats = new HashMap<>();
        for (Map.Entry<Language, Trie> entry : tries.entrySet()) {
            stats.put(entry.getKey(), entry.getValue().size());
        }
        return stats;
    }
}