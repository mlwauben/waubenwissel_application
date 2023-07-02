package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public abstract class AccountController implements Initializable {

    public Scene oldBuilder;

    @FXML
    public TextField coachName;
    public TextField teamName;
    public VBox players;
    public TextField newPlayer;
    public Button addPlayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void addPlayer() {
        if (!(newPlayer.getText().length() == 0)) {
            Label l = new Label(newPlayer.getText());
            l.setOnMouseClicked(e -> players.getChildren().remove(l));
            l.setCursor(Cursor.HAND);
            players.getChildren().add(l);
        }
        newPlayer.setText("");
    }

    public Label addPlayer(String name) {
        Label l = new Label(name);
        l.setOnMouseClicked(e -> players.getChildren().remove(l));
        l.setCursor(Cursor.HAND);
        return l;
    }

    public void createNewAccount(ActionEvent actionEvent) {
        String name = coachName.getText();
        if (name.isBlank()) {
            System.out.println("name not filled in");
        }

        else  {
            try {
                String documentsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\WaubenWissel accounts";
                File file = new File(documentsPath + "/" + name + ".txt");
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                    //add new stuff
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(teamName.getText() + "\n");
                    List<Node> list = players.getChildren();
                    for (Node node : list) {
                        Label l = (Label) node;
                        fileWriter.write(l.getText() + "\n");
                    }
                    fileWriter.close();

                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/builder.fxml")));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    BuilderController con = loader.getController();
                    con.addPlayers(file);

                    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show();
                    System.out.println("display StartScene");
                } else {
                    System.out.println("user already exists.");
                    Alert b = new Alert(Alert.AlertType.WARNING, "Deze coach bestaat al!");
                    b.showAndWait();
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    public void returnToBuilder(ActionEvent actionEvent) {
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(oldBuilder);
        window.show();
        System.out.println("display builderScene");
    }

    public void setOldBuilder(Scene scene) {
        oldBuilder = scene;
    }
}
