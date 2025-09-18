package diccionario.controller;

import diccionario.model.WordEntry;
import diccionario.model.Language;
import diccionario.service.DictionaryServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
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
        showInfo("Agregar palabra", "Funcionalidad de agregar palabra en desarrollo");
    }
    
    @FXML
    private void handleEditWord() {
        WordEntry selected = wordsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showInfo("Editar palabra", "Editando: " + selected.getId());
        } else {
            showWarning("Selección requerida", "Por favor selecciona una palabra para editar");
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
}