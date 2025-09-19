package diccionario.controller;

import diccionario.model.WordEntry;
import diccionario.model.Language;
import diccionario.service.DictionaryServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import java.util.*;



public class MainController {
    @FXML private ComboBox<Language> languageComboBox;
    @FXML private TextField searchField;
    @FXML private ListView<WordEntry> wordsListView;
    @FXML private Label statusLabel;
    
    private DictionaryServiceImpl dictionaryService;
    
    @FXML
    public void initialize() {
        // Inicializar servicio
        dictionaryService = new DictionaryServiceImpl();
        
        // Configurar comboBox de idiomas
        languageComboBox.setItems(FXCollections.observableArrayList(Language.values()));
        languageComboBox.setValue(Language.SPANISH);
        
        // Configurar la lista
        wordsListView.setCellFactory(param -> new ListCell<WordEntry>() {
            @Override
            protected void updateItem(WordEntry word, boolean empty) {
                super.updateItem(word, empty);
                if (empty || word == null) {
                    setText(null);
                } else {
                    setText(word.getId() + " - " + 
                           word.getTranslation(Language.SPANISH) + " / " +
                           word.getTranslation(Language.ENGLISH));
                }
            }
        });
        
        // Cargar datos iniciales
        loadInitialData();
    }
    
    private void loadInitialData() {
        try {
            dictionaryService.loadDictionary();
            refreshWordList();
            statusLabel.setText("✅ Diccionario cargado: " + 
                dictionaryService.getTotalWordCount() + " palabras");
        } catch (Exception e) {
            statusLabel.setText("❌ Error cargando: " + e.getMessage());
            showError("Error al cargar", "No se pudo cargar el diccionario: " + e.getMessage());
        }
    }
    
    private void refreshWordList() {
        wordsListView.getItems().clear();
        wordsListView.getItems().addAll(dictionaryService.getAllWords());
    }
    
    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        Language language = languageComboBox.getValue();
        
        List<WordEntry> results = dictionaryService.searchWords(query, language);
        wordsListView.getItems().clear();
        wordsListView.getItems().addAll(results);
        
        statusLabel.setText("🔍 Encontradas: " + results.size() + " palabras");
    }
    
@FXML
private void handleAddWord() {
    try {
        System.out.println("🔄 Intentando cargar diálogo...");
        
        // Crear el diálogo
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Agregar Nueva Palabra");
        dialog.setHeaderText("Complete los datos de la nueva palabra");
        
        // Cargar el FXML del diálogo
        FXMLLoader loader = new FXMLLoader();
        java.net.URL fxmlUrl = getClass().getResource("/diccionario/view/add-word-dialog.fxml");
        
        if (fxmlUrl == null) {
            showError("Error", "No se encontró el archivo FXML del diálogo");
            System.out.println("❌ No se encontró el FXML en: /diccionario/view/add-word-dialog.fxml");
            return;
        }
        
        System.out.println("✅ FXML encontrado: " + fxmlUrl);
        
        loader.setLocation(fxmlUrl);
        GridPane dialogPane = loader.load();
        System.out.println("✅ FXML cargado correctamente");
        
        // Obtener el controlador
        AddWordController controller = loader.getController();
        
        if (controller == null) {
            showError("Error", "No se pudo crear el controlador del diálogo");
            System.out.println("❌ Controller es null");
            return;
        }
        
        System.out.println("✅ Controller obtenido: " + controller);
        
        // Configurar el diálogo
        dialog.getDialogPane().setContent(dialogPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Mostrar el diálogo
        Optional<ButtonType> result = dialog.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (!controller.isValid()) {
                showError("Error", "Complete ID y al menos una traducción");
                return;
            }
            
            // Crear palabra simple por ahora
            WordEntry newWord = new WordEntry();
            newWord.setId(controller.getId());
            
            if (!controller.getSpanish().isEmpty()) 
                newWord.addTranslation(Language.SPANISH, controller.getSpanish());
            if (!controller.getEnglish().isEmpty()) 
                newWord.addTranslation(Language.ENGLISH, controller.getEnglish());
            if (!controller.getFrench().isEmpty()) 
                newWord.addTranslation(Language.FRENCH, controller.getFrench());
            if (!controller.getGerman().isEmpty()) 
                newWord.addTranslation(Language.GERMAN, controller.getGerman());
            
            dictionaryService.addWord(newWord);
            refreshWordList();
            
            statusLabel.setText("✅ Palabra agregada: " + newWord.getId());
        }
        
    } catch (Exception e) {
        System.out.println("❌ Error: " + e.getMessage());
        e.printStackTrace();
        showError("Error", "Error al abrir diálogo: " + e.getMessage());
    }
} 
    @FXML
    private void handleDeleteWord() {
        WordEntry selected = wordsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean deleted = dictionaryService.deleteWord(selected.getId());
            if (deleted) {
                refreshWordList();
                statusLabel.setText("✅ Palabra eliminada: " + selected.getId());
            }
        } else {
            showWarning("Selección requerida", "Por favor selecciona una palabra para eliminar");
        }
    }
    
    @FXML
    private void handleShowStats() {
        Map<Language, Integer> stats = dictionaryService.getWordCountByLanguage();
        
        StringBuilder message = new StringBuilder("📊 Estadísticas por idioma:\n\n");
        for (Map.Entry<Language, Integer> entry : stats.entrySet()) {
            message.append(entry.getKey().getDisplayName())
                  .append(": ")
                  .append(entry.getValue())
                  .append(" palabras\n");
        }
        
        showInfo("Estadísticas", message.toString());
    }
    
    @FXML
    private void handleSave() {
        try {
            dictionaryService.saveDictionary();
            statusLabel.setText("💾 Diccionario guardado correctamente");
            showInfo("Guardado", "Diccionario guardado exitosamente");
        } catch (Exception e) {
            statusLabel.setText("❌ Error guardando: " + e.getMessage());
            showError("Error al guardar", "No se pudo guardar el diccionario: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLoad() {
        loadInitialData();
    }
    
    // Métodos utilitarios para mostrar diálogos
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
private void handleEditWord() {
    WordEntry selected = wordsListView.getSelectionModel().getSelectedItem();
    if (selected != null) {
        showInfo("Editar palabra", "Funcionalidad de edición en desarrollo. Palabra seleccionada: " + selected.getId());
        // TODO: Implementar la edición completa
    } else {
        showWarning("Selección requerida", "Por favor selecciona una palabra para editar");
    }
}
}