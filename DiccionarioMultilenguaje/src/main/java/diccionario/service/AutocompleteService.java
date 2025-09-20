package diccionario.service;

import diccionario.model.Trie;
import diccionario.model.WordEntry;
import diccionario.model.Language;
import java.util.*;

public class AutocompleteService {
    private Map<Language, Trie> tries = new HashMap<>();

    public AutocompleteService() {
        for (Language lang : Language.values()) {
            tries.put(lang, new Trie());
        }
    }

    public void addWord(WordEntry word) {
        for (Language lang : Language.values()) {
            String translation = word.getTranslation(lang);
            if (translation != null && !translation.isEmpty()) {
                tries.get(lang).insert(translation);
            }
        }
    }

    public void updateWord(WordEntry oldWord, WordEntry newWord) {
        removeWord(oldWord);
        addWord(newWord);
    }

    public void removeWord(WordEntry word) {
        for (Language lang : Language.values()) {
            String translation = word.getTranslation(lang);
            if (translation != null && !translation.isEmpty()) {
                tries.get(lang).delete(translation);
            }
        }
    }

    public List<String> autocomplete(String prefix, Language language) {
        return tries.get(language).startsWith(prefix);
    }

    public void rebuildTries(List<WordEntry> words) {
        for (Language lang : Language.values()) {
            tries.get(lang).clear();
        }
        for (WordEntry word : words) {
            addWord(word);
        }
    }

    public void clear() {
        for (Trie trie : tries.values()) {
            trie.clear();
        }
    }
}