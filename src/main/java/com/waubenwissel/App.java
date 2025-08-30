package com.waubenwissel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("startScene"));
        stage.setScene(scene);
        stage.setTitle("Waubenwissel");
        stage.setMaximized(true);
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
        double scale = newVal.doubleValue() / 1280.0;  // baseline: 1280px width
            scene.getRoot().setStyle("-fx-font-size: " + (16 * scale) + "px;");
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double scale = newVal.doubleValue() / 720.0;   // baseline: 720px height
            scene.getRoot().setStyle("-fx-font-size: " + (16 * scale) + "px;");
        });
        scene.getRoot().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}