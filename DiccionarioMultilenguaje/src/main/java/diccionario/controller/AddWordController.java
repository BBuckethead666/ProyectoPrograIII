package diccionario.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import diccionario.util.IdGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddWordController {
    @FXML public TextField idField;
    @FXML public TextField spanishField;
    @FXML public TextField englishField;
    @FXML public TextField frenchField;
    @FXML public TextField germanField;
    @FXML public TextArea definitionField;
    @FXML public TextArea examplesField;
    @FXML public TextField tagsField;
    
   @FXML
public void initialize() {
    System.out.println("✅ AddWordController inicializado");
    if (idField != null && (idField.getText() == null || idField.getText().trim().isEmpty())) {
        // Usa la traducción principal como base para el ID, o un valor por defecto si está vacío
        String base = (spanishField != null && !spanishField.getText().trim().isEmpty())
                        ? spanishField.getText().trim()
                        : "palabra";
        idField.setText(IdGenerator.generateWordId(base));
    }
}
    public String getId() { 
        return idField != null ? idField.getText() : ""; 
    }
    
    public String getSpanish() { 
        return spanishField != null ? spanishField.getText() : ""; 
    }
    
    public String getEnglish() { 
        return englishField != null ? englishField.getText() : ""; 
    }
    
    public String getFrench() { 
        return frenchField != null ? frenchField.getText() : ""; 
    }
    
    public String getGerman() { 
        return germanField != null ? germanField.getText() : ""; 
    }
    
    public boolean isValid() {
    boolean hasId = !getId().isEmpty();
    boolean hasTranslation = !getSpanish().isEmpty() || !getEnglish().isEmpty() || 
                            !getFrench().isEmpty() || !getGerman().isEmpty();
    return hasId && hasTranslation;
}
    public String getDefinition() { 
    return definitionField != null ? definitionField.getText().trim() : ""; 
}

public List<String> getExamples() {
    if (examplesField == null || examplesField.getText().trim().isEmpty()) {
        return new ArrayList<>();
    }
    return Arrays.asList(examplesField.getText().trim().split("\\n"));
}

public List<String> getTags() {
    if (tagsField == null || tagsField.getText().trim().isEmpty()) {
        return new ArrayList<>();
    }
    return Arrays.stream(tagsField.getText().trim().split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toList());
}

}