package diccionario.persistence;

import diccionario.model.WordEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonStorage {
    private static final String DEFAULT_FILE_PATH = "data/dictionary.json";
    private final Gson gson;
    
    public JsonStorage() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();
        
        // Crear directorio data si no existe
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            System.err.println("Error creando directorio data: " + e.getMessage());
        }
    }
    
    public void saveWords(List<WordEntry> words) throws IOException {
        saveWords(words, DEFAULT_FILE_PATH);
    }
    
    public void saveWords(List<WordEntry> words, String filePath) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("version", "1.0");
        data.put("lastModified", new Date().toString());
        data.put("words", words);
        
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
        }
    }
    
    public List<WordEntry> loadWords(String filePath) throws IOException {
        // Verificar si el archivo existe
        if (!Files.exists(Paths.get(filePath))) {
            System.out.println("Archivo no encontrado, creando nuevo diccionario");
            return new ArrayList<>();
        }
        
        try (Reader reader = new FileReader(filePath)) {
            // Leer el JSON completo
            Map<String, Object> data = gson.fromJson(reader, new TypeToken<Map<String, Object>>(){}.getType());
            
            if (data == null || !data.containsKey("words")) {
                return new ArrayList<>();
            }
            
            // Convertir la lista de maps a lista de WordEntry
            List<Map<String, Object>> wordsData = (List<Map<String, Object>>) data.get("words");
            List<WordEntry> words = new ArrayList<>();
            
            for (Map<String, Object> wordData : wordsData) {
                // Convertir el map a JSON string y luego a WordEntry
                String jsonWord = gson.toJson(wordData);
                WordEntry word = gson.fromJson(jsonWord, WordEntry.class);
                words.add(word);
            }
            
            return words;
        }
    }
    
    public List<WordEntry> loadWords() throws IOException {
        return loadWords(DEFAULT_FILE_PATH);
    }
}