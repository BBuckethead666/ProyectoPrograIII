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
    @FXML private Label titleLabel;
    @FXML private ComboBox<String> localeComboBox;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button statsButton;
    @FXML private Button saveButton;
    @FXML private Button loadButton;
    @FXML private Button searchButton;
    @FXML private Label searchLabel;
    @FXML private Label languageLabel;

    private DictionaryServiceImpl dictionaryService;
    private ResourceBundle bundle;

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

        // 4. Búsqueda en tiempo real
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                refreshWordList();
            } else {
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

        // 5. Inicializar ComboBox de selección de idioma de interfaz
        localeComboBox.getItems().addAll("Español", "English", "Français", "Deutsch");
        localeComboBox.setValue("Español"); // Valor inicial

        localeComboBox.setOnAction(e -> {
            String selected = localeComboBox.getValue();
            Locale locale;
            switch (selected) {
                case "English": locale = new Locale("en"); break;
                case "Français": locale = new Locale("fr"); break;
                case "Deutsch": locale = new Locale("de"); break;
                default: locale = new Locale("es");
            }
            bundle = ResourceBundle.getBundle("diccionario.i18n.messages", locale);
            updateTexts();
        });

        // 6. Cargar datos iniciales y textos
        bundle = ResourceBundle.getBundle("diccionario.i18n.messages", new Locale("es"));
        loadInitialData();
        updateTexts();
    }

  private void updateTexts() {
    titleLabel.setText(bundle.getString("app.title"));
    statusLabel.setText(bundle.getString("status.ready"));
    addButton.setText(bundle.getString("button.add"));
    editButton.setText(bundle.getString("button.edit"));
    deleteButton.setText(bundle.getString("button.delete"));
    statsButton.setText(bundle.getString("button.stats"));
    saveButton.setText(bundle.getString("button.save"));
    loadButton.setText(bundle.getString("button.load"));
    searchButton.setText(bundle.getString("button.search"));
    searchLabel.setText(bundle.getString("label.search"));
    languageLabel.setText(bundle.getString("label.language"));
    searchField.setPromptText(bundle.getString("prompt.search"));
    }

    private void loadInitialData() {
        try {
            dictionaryService.loadDictionary();
            refreshWordList();
            statusLabel.setText(bundle.getString("status.loaded") + ": " +
                dictionaryService.getTotalWordCount() + " " + bundle.getString("label.search"));
        } catch (Exception e) {
            statusLabel.setText(bundle.getString("status.error") + ": " + e.getMessage());
            showError(bundle.getString("status.error"), bundle.getString("status.error") + ": " + e.getMessage());
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

        statusLabel.setText("🔍 " + bundle.getString("label.search") + ": " + results.size());
    }

    @FXML
    private void handleAddWord() {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle(bundle.getString("button.add"));
            dialog.setHeaderText(bundle.getString("button.add"));

            FXMLLoader loader = new FXMLLoader();
            java.net.URL fxmlUrl = getClass().getResource("/diccionario/view/add-word-dialog.fxml");

            if (fxmlUrl == null) {
                showError(bundle.getString("status.error"), "No se encontró el archivo FXML del diálogo");
                return;
            }

            loader.setLocation(fxmlUrl);
            GridPane dialogPane = loader.load();

            AddWordController controller = loader.getController();

            if (controller == null) {
                showError(bundle.getString("status.error"), "No se pudo crear el controlador del diálogo");
                return;
            }

            dialog.getDialogPane().setContent(dialogPane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (!controller.isValid()) {
                    showError(bundle.getString("status.error"), "Complete ID y al menos una traducción");
                    return;
                }

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

                statusLabel.setText(bundle.getString("button.add") + ": " + newWord.getId());
            }

        } catch (Exception e) {
            showError(bundle.getString("status.error"), "Error al abrir diálogo: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteWord() {
        WordEntry selected = wordsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean deleted = dictionaryService.deleteWord(selected.getId());
            if (deleted) {
                refreshWordList();
                statusLabel.setText(bundle.getString("button.delete") + ": " + selected.getId());
            }
        } else {
            showWarning(bundle.getString("status.error"), "Por favor selecciona una palabra para eliminar");
        }
    }

    @FXML
    private void handleShowStats() {
        Map<Language, Integer> stats = dictionaryService.getWordCountByLanguage();
        int totalWords = dictionaryService.getTotalWordCount();

        StringBuilder message = new StringBuilder("📊 " + bundle.getString("button.stats") + "\n\n");
        message.append(bundle.getString("label.search")).append(" ").append(totalWords).append("\n\n");

        message.append("Palabras por idioma:\n");
        for (Map.Entry<Language, Integer> entry : stats.entrySet()) {
            double percentage = totalWords > 0 ? (entry.getValue() * 100.0 / totalWords) : 0;
            message.append("• ").append(entry.getKey().getDisplayName())
                  .append(": ").append(entry.getValue())
                  .append(" palabras (").append(String.format("%.1f", percentage)).append("%)\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("button.stats"));
        alert.setHeaderText(bundle.getString("button.stats"));
        alert.setContentText(message.toString());
        alert.setGraphic(null);
        alert.showAndWait();
    }

    @FXML
    private void handleSave() {
        try {
            dictionaryService.saveDictionary();
            statusLabel.setText(bundle.getString("status.saved"));
            showInfo(bundle.getString("button.save"), bundle.getString("status.saved"));
        } catch (Exception e) {
            statusLabel.setText(bundle.getString("status.error") + ": " + e.getMessage());
            showError(bundle.getString("status.error"), bundle.getString("status.error") + ": " + e.getMessage());
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
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle(bundle.getString("button.edit") + ": " + selected.getId());

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/diccionario/view/add-word-dialog.fxml"));
                GridPane dialogPane = loader.load();

                AddWordController controller = loader.getController();

                controller.idField.setText(selected.getId());
                controller.idField.setDisable(true);

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
                    WordEntry updatedWord = new WordEntry();
                    updatedWord.setId(selected.getId());

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

                    updatedWord.setDefinition(selected.getDefinition());
                    updatedWord.setExamples(selected.getExamples());
                    updatedWord.setTags(selected.getTags());

                    dictionaryService.updateWord(selected.getId(), updatedWord);
                    refreshWordList();
                    statusLabel.setText(bundle.getString("button.edit") + ": " + selected.getId());

                    autoSave();
                }

            } catch (Exception e) {
                showError(bundle.getString("status.error"), "No se pudo editar: " + e.getMessage());
            }
        } else {
            showWarning(bundle.getString("status.error"), "Por favor selecciona una palabra para editar");
        }
    }

    private void autoSave() {
        try {
            dictionaryService.saveDictionary();
        } catch (Exception e) {
            System.out.println("⚠️ Error en guardado automático: " + e.getMessage());
        }
    }
}