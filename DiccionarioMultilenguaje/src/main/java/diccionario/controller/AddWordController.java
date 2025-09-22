/**
 * The `AddWordController` class in Java is a controller for adding words to a dictionary application
 * with methods to retrieve word details and validate input.
 */

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
// The `initialize()` method in the `AddWordController` class is a method that is automatically called
// when the FXML file is loaded and the controller is initialized.
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
    /** 
     * @return String
     */
    public String getId() { 
        return idField != null ? idField.getText() : ""; 
    }
    
    /** 
     * @return String
     */
    public String getSpanish() { 
        return spanishField != null ? spanishField.getText() : ""; 
    }
    
    /** 
     * @return String
     */
    public String getEnglish() { 
        return englishField != null ? englishField.getText() : ""; 
    }
    
    /** 
     * @return String
     */
    public String getFrench() { 
        return frenchField != null ? frenchField.getText() : ""; 
    }
    
    /** 
     * @return String
     */
    public String getGerman() { 
        return germanField != null ? germanField.getText() : ""; 
    }
    
    /** 
     * @return boolean
     */
    public boolean isValid() {
    boolean hasId = !getId().isEmpty();
    boolean hasTranslation = !getSpanish().isEmpty() || !getEnglish().isEmpty() || 
                            !getFrench().isEmpty() || !getGerman().isEmpty();
    return hasId && hasTranslation;
}
    /** 
     * @return String
     */
    public String getDefinition() { 
    return definitionField != null ? definitionField.getText().trim() : ""; 
}

/** 
 * @return List<String>
 */
public List<String> getExamples() {
    if (examplesField == null || examplesField.getText().trim().isEmpty()) {
        return new ArrayList<>();
    }
    return Arrays.asList(examplesField.getText().trim().split("\\n"));
}

/** 
 * @return List<String>
 */
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