/**
 * The Main class in this Java application sets up the user interface with localization support using
 * resource bundles.
 */
package diccionario;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
public void start(Stage primaryStage) throws Exception {
    Locale locale = Locale.getDefault(); // O el idioma que elija el usuario
    ResourceBundle bundle = ResourceBundle.getBundle("diccionario.i18n.messages", locale);
    Parent root = FXMLLoader.load(getClass().getResource("/diccionario/view/main.fxml"), bundle);
    primaryStage.setTitle(bundle.getString("app.title"));
    primaryStage.setScene(new Scene(root, 800, 600));
    primaryStage.show();
}
    


    public static void main(String[] args) {
        launch(args);
    }
}