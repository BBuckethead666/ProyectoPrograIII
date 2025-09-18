package diccionario.service;

import diccionario.model.WordEntry;
import diccionario.model.Language;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DictionaryService {
    // Operaciones CRUD básicas
    void addWord(WordEntry word);
    void updateWord(String id, WordEntry updatedWord);
    boolean deleteWord(String id);
    Optional<WordEntry> getWord(String id);
    List<WordEntry> getAllWords();
    
    // Búsquedas y consultas
    List<WordEntry> searchWords(String query, Language language);
    List<WordEntry> searchByTranslation(String text, Language language);
    List<WordEntry> getWordsByLanguage(Language language);
    List<WordEntry> getWordsByTag(String tag);
    
    // Autocompletado
    List<WordEntry> autocomplete(String prefix, Language language);
    
    // Import/Export
    boolean importWords(List<WordEntry> words);
    List<WordEntry> exportWords();
    
    // Estadísticas
    int getTotalWordCount();
    Map<Language, Integer> getWordCountByLanguage();
    
    // Persistencia
    void saveDictionary() throws Exception;
    void loadDictionary() throws Exception;
}