package diccionario.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddWordController {
    @FXML public TextField idField;
    @FXML public TextField spanishField;
    @FXML public TextField englishField;
    @FXML public TextField frenchField;
    @FXML public TextField germanField;
    
    @FXML
    public void initialize() {
        System.out.println("✅ AddWordController inicializado");
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
        return !getId().isEmpty() && 
               (!getSpanish().isEmpty() || !getEnglish().isEmpty() || 
                !getFrench().isEmpty() || !getGerman().isEmpty());
    }
}