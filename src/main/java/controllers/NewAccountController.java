package controllers;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NewAccountController extends AccountController {

    @FXML
    public TextField coachName;
    public TextField teamName;
    public VBox players;
    public TextField newPlayer;
    public Button addPlayer;

    public void returnToStart(ActionEvent actionEvent) throws IOException {
        Parent dash = FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader().getResource("fxml/startScene.fxml")));
        Scene scene = new Scene(dash);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        System.out.println("display startScene");
    }
}
