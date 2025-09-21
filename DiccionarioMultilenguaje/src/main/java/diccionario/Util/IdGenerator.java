package diccionario.util;
import java.util.UUID;

public class IdGenerator {
    public static String generateWordId(String baseWord) {
        // Generar ID basado en la palabra + timestamp
        String cleanWord = baseWord.toLowerCase()
                .replace(" ", "-")
                .replaceAll("[^a-z0-9-]", "");
        
        return cleanWord + "-" + System.currentTimeMillis();
    }
    
    public static String generateUniqueId() {
        // Generar UUID único
        return UUID.randomUUID().toString().substring(0, 8);
    }
    
    public static String generateIdFromWord(String word, int sequence) {
        // Generar ID con secuencia: palabra-001, palabra-002, etc.
        String cleanWord = word.toLowerCase()
                .replace(" ", "-")
                .replaceAll("[^a-z0-9-]", "");
        
        return cleanWord + "-" + String.format("%03d", sequence);
    }
}