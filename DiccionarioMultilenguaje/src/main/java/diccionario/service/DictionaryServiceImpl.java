package diccionario.service;

import diccionario.model.WordEntry;
import diccionario.model.Language;
import diccionario.persistence.JsonStorage;
import java.util.*;
import java.util.stream.Collectors;

public class DictionaryServiceImpl implements DictionaryService {
    private List<WordEntry> words;
    private AutocompleteService autocompleteService;
    private JsonStorage jsonStorage;

    public DictionaryServiceImpl() {
        this.words = new ArrayList<>();
        this.autocompleteService = new AutocompleteService();
        this.jsonStorage = new JsonStorage();
    }

    @Override
    public void addWord(WordEntry word) {
        if (getWord(word.getId()).isPresent()) {
            throw new IllegalArgumentException("La palabra con ID " + word.getId() + " ya existe");
        }
        if (word.getTranslations().isEmpty()) {
            throw new IllegalArgumentException("La palabra debe tener al menos una traducción");
        }
        if (word.getId() == null || word.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la palabra no puede estar vacío");
        }
        words.add(word);
        autocompleteService.addWord(word);
    }

    @Override
    public void updateWord(String id, WordEntry updatedWord) {
        Optional<WordEntry> existingWord = getWord(id);
        if (existingWord.isPresent()) {
            WordEntry oldWord = existingWord.get();
            words.remove(oldWord);
            words.add(updatedWord);
            autocompleteService.updateWord(oldWord, updatedWord);
        } else {
            throw new IllegalArgumentException("Palabra con ID " + id + " no encontrada");
        }
    }

    @Override
    public boolean deleteWord(String id) {
        Optional<WordEntry> word = getWord(id);
        if (word.isPresent()) {
            autocompleteService.removeWord(word.get());
            words.remove(word.get());
            return true;
        }
        return false;
    }

    @Override
    public Optional<WordEntry> getWord(String id) {
        return words.stream()
                .filter(word -> word.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<WordEntry> getAllWords() {
        return new ArrayList<>(words);
    }

    @Override
    public List<WordEntry> searchWords(String query, Language language) {
        if (query == null || query.trim().isEmpty()) {
            return getAllWords();
        }
        String searchTerm = query.toLowerCase();
        return words.stream()
                .filter(word -> {
                    String translation = word.getTranslation(language);
                    return translation != null &&
                           translation.toLowerCase().contains(searchTerm);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<WordEntry> searchByTranslation(String text, Language language) {
        return words.stream()
                .filter(word -> {
                    String translation = word.getTranslation(language);
                    return translation != null &&
                           translation.equalsIgnoreCase(text);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<WordEntry> getWordsByLanguage(Language language) {
        return words.stream()
                .filter(word -> word.getTranslation(language) != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<WordEntry> getWordsByTag(String tag) {
        return words.stream()
                .filter(word -> word.getTags() != null &&
                               word.getTags().stream()
                                   .anyMatch(t -> t.equalsIgnoreCase(tag)))
                .collect(Collectors.toList());
    }

    @Override
    public List<WordEntry> autocomplete(String prefix, Language language) {
        // Devuelve WordEntry según las sugerencias del Trie
        List<String> suggestions = autocompleteService.autocomplete(prefix, language);
        return words.stream()
                .filter(word -> {
                    String translation = word.getTranslation(language);
                    return translation != null && suggestions.contains(translation);
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean importWords(List<WordEntry> newWords) {
        try {
            for (WordEntry word : newWords) {
                addWord(word);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error importando palabras: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<WordEntry> exportWords() {
        return new ArrayList<>(words);
    }

    @Override
    public int getTotalWordCount() {
        return words.size();
    }

    @Override
    public Map<Language, Integer> getWordCountByLanguage() {
        Map<Language, Integer> counts = new HashMap<>();
        for (Language language : Language.values()) {
            long count = words.stream()
                    .filter(word -> word.getTranslation(language) != null)
                    .count();
            counts.put(language, (int) count);
        }
        return counts;
    }

    @Override
    public void saveDictionary() throws Exception {
        jsonStorage.saveWords(words);
    }

    @Override
    public void loadDictionary() throws Exception {
        List<WordEntry> loadedWords = jsonStorage.loadWords();
        words.clear();
        words.addAll(loadedWords);
        autocompleteService.rebuildTries(words);
    }

    public void clearDictionary() {
        words.clear();
        autocompleteService.clear();
    }

    public boolean isWordIdAvailable(String id) {
        return getWord(id).isEmpty();
    }
}