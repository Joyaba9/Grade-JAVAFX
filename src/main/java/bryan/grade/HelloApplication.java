package bryan.grade;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Entry point for the JavaFX application.
 *
 * This class extends {@link javafx.application.Application} and sets up the primary stage
 * for the application using an FXML layout.
 */
public class HelloApplication extends Application {

    /**
     * Starts the primary stage of the application.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     *              The primary stage is provided by the platform.
     * @throws IOException If the FXML file "hello-view.fxml" cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 700);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args Command-line arguments passed to the application. Not used in this application.
     */
    public static void main(String[] args) {
        launch();
    }
}