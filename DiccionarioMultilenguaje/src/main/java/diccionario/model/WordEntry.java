package diccionario.model;

import java.util.*;

public class WordEntry {
    private String id;
    private Map<Language, String> translations;
    private String definition;
    private List<String> examples;
    private List<String> tags;
    
    public WordEntry() {
        this.translations = new HashMap<>();
        this.examples = new ArrayList<>();
        this.tags = new ArrayList<>();
    }
    
    public WordEntry(String id, Map<Language, String> translations, 
                    String definition, List<String> examples, List<String> tags) {
        this.id = id;
        this.translations = translations;
        this.definition = definition;
        this.examples = examples;
        this.tags = tags;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Map<Language, String> getTranslations() { return translations; }
    public void setTranslations(Map<Language, String> translations) { 
        this.translations = translations; 
    }
    
    public String getDefinition() { return definition; }
    public void setDefinition(String definition) { this.definition = definition; }
    
    public List<String> getExamples() { return examples; }
    public void setExamples(List<String> examples) { this.examples = examples; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public void addTranslation(Language language, String translation) {
        translations.put(language, translation);
    }
    
    public String getTranslation(Language language) {
        return translations.get(language);
    }
    
    @Override
    public String toString() {
        return "WordEntry{" +
               "id='" + id + '\'' +
               ", translations=" + translations +
               '}';
    }
}