package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class
 *
 * FUTURE ENHANCEMENT: One of the ways to enhance this application would be to have the parts and products be stored in
 * a database instead of in memory as an object.
 *
 * @author Scott Alesci
 */
public class Main extends Application {
    /**
     * Where the FirstScreen gets initialized for the first time
     *
     * @param stage sets the stage
     * @throws Exception general exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/FirstScreen.fxml"));
        stage.setTitle("First Screen");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * main of the program
     *
     * @param args command line arguments
     */
    public static void main(String[] args){

        launch(args);

    }
}
