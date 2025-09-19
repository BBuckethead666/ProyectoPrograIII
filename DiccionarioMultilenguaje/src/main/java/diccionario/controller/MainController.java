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
    // 1. Inicializar servicio
    dictionaryService = new DictionaryServiceImpl();
    
    // 2. Configurar comboBox de idiomas
    languageComboBox.setItems(FXCollections.observableArrayList(Language.values()));
    languageComboBox.setValue(Language.SPANISH);
    
    // 3. Configurar la lista de palabras
    wordsListView.setCellFactory(param -> new ListCell<WordEntry>() {
        @Override
        protected void updateItem(WordEntry word, boolean empty) {
            super.updateItem(word, empty);
            if (empty || word == null) {
                setText(null);
                setGraphic(null);
            } else {
                // 🆕 MEJORAR ESTA PARTE con la visualización mejorada
                StringBuilder sb = new StringBuilder();
                sb.append("📝 ").append(word.getId()).append("\n");
                
                if (word.getTranslation(Language.SPANISH) != null) {
                    sb.append("🇪🇸 ").append(word.getTranslation(Language.SPANISH)).append("  ");
                }
                if (word.getTranslation(Language.ENGLISH) != null) {
                    sb.append("🇺🇸 ").append(word.getTranslation(Language.ENGLISH)).append("  ");
                }
                if (word.getTranslation(Language.FRENCH) != null) {
                    sb.append("🇫🇷 ").append(word.getTranslation(Language.FRENCH)).append("  ");
                }
                if (word.getTranslation(Language.GERMAN) != null) {
                    sb.append("🇩🇪 ").append(word.getTranslation(Language.GERMAN));
                }
                
                setText(sb.toString());
                setStyle("-fx-padding: 5px; -fx-font-size: 12px;");
            }
        }
    });
    
    // 4. 🆕 Búsqueda en tiempo real
    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue == null || newValue.trim().isEmpty()) {
            refreshWordList(); // Mostrar todas si está vacío
        } else {
            // Búsqueda automática después de 300ms de inactividad
            java.util.Timer timer = new java.util.Timer();
            timer.schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> {
                        handleSearch();
                        timer.cancel();
                    });
                }
            }, 300);
        }
    });
    
    // 5. Cargar datos iniciales
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
    int totalWords = dictionaryService.getTotalWordCount();
    
    StringBuilder message = new StringBuilder("📊 **Estadísticas del Diccionario**\n\n");
    message.append("**Total de palabras:** ").append(totalWords).append("\n\n");
    
    message.append("**Palabras por idioma:**\n");
    for (Map.Entry<Language, Integer> entry : stats.entrySet()) {
        double percentage = totalWords > 0 ? (entry.getValue() * 100.0 / totalWords) : 0;
        message.append("• ").append(entry.getKey().getDisplayName())
              .append(": ").append(entry.getValue())
              .append(" palabras (").append(String.format("%.1f", percentage)).append("%)\n");
    }
    
    // Información adicional
    message.append("\n**Información adicional:**\n");
    message.append("• Puedes agregar más palabras usando el botón 'Agregar'\n");
    message.append("• Usa la búsqueda para encontrar palabras rápidamente\n");
    message.append("• Recuerda guardar cambios regularmente");
    
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Estadísticas del Diccionario");
    alert.setHeaderText("Resumen de contenido");
    alert.setContentText(message.toString());
    alert.setGraphic(null);
    alert.showAndWait();
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
        try {
            // Abrir diálogo de edición
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Editar Palabra: " + selected.getId());
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/diccionario/view/add-word-dialog.fxml"));
            GridPane dialogPane = loader.load();
            
            AddWordController controller = loader.getController();
            
            // Precargar datos existentes
            controller.idField.setText(selected.getId());
            controller.idField.setDisable(true); // ID no editable
            
            if (selected.getTranslation(Language.SPANISH) != null) {
                controller.spanishField.setText(selected.getTranslation(Language.SPANISH));
            }
            if (selected.getTranslation(Language.ENGLISH) != null) {
                controller.englishField.setText(selected.getTranslation(Language.ENGLISH));
            }
            if (selected.getTranslation(Language.FRENCH) != null) {
                controller.frenchField.setText(selected.getTranslation(Language.FRENCH));
            }
            if (selected.getTranslation(Language.GERMAN) != null) {
                controller.germanField.setText(selected.getTranslation(Language.GERMAN));
            }
            
            dialog.getDialogPane().setContent(dialogPane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            
            Optional<ButtonType> result = dialog.showAndWait();
            
            if (result.isPresent() && result.get() == ButtonType.OK && controller.isValid()) {
                // Crear palabra actualizada
                WordEntry updatedWord = new WordEntry();
                updatedWord.setId(selected.getId()); // Mantener mismo ID
                
                // Actualizar traducciones
                if (!controller.getSpanish().isEmpty()) {
                    updatedWord.addTranslation(Language.SPANISH, controller.getSpanish());
                }
                if (!controller.getEnglish().isEmpty()) {
                    updatedWord.addTranslation(Language.ENGLISH, controller.getEnglish());
                }
                if (!controller.getFrench().isEmpty()) {
                    updatedWord.addTranslation(Language.FRENCH, controller.getFrench());
                }
                if (!controller.getGerman().isEmpty()) {
                    updatedWord.addTranslation(Language.GERMAN, controller.getGerman());
                }
                
                // Mantener datos existentes
                updatedWord.setDefinition(selected.getDefinition());
                updatedWord.setExamples(selected.getExamples());
                updatedWord.setTags(selected.getTags());
                
                dictionaryService.updateWord(selected.getId(), updatedWord);
                refreshWordList();
                statusLabel.setText("✅ Palabra actualizada: " + selected.getId());
                
                // Guardado automático
                autoSave();
            }
            
        } catch (Exception e) {
            showError("Error", "No se pudo editar: " + e.getMessage());
        }
    } else {
        showWarning("Selección requerida", "Por favor selecciona una palabra para editar");
    }
}
private void autoSave() {
    try {
        dictionaryService.saveDictionary();
        System.out.println("💾 Guardado automático realizado");
    } catch (Exception e) {
        System.out.println("⚠️ Error en guardado automático: " + e.getMessage());
    }
}
}