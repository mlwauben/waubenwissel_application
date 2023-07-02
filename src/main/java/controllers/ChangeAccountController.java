package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class ChangeAccountController extends AccountController {

    @FXML
    public TextField coachName;
    public TextField teamName;
    public VBox players;

    public void changeAccount(ActionEvent actionEvent) {
        String documentsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\WaubenWissel accounts";
        File file = new File(documentsPath + "\\" + coachName.getText() + ".txt");

        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Weet je zeker dat je de gegevens wilt aanpassen? " +
                "Het gemaakte wisselschema wordt gereset.", ButtonType.YES, ButtonType.CANCEL);
        a.showAndWait();
        if (a.getResult() == ButtonType.YES) {
            if (file.delete()) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
            createNewAccount(actionEvent);
        }
    }

    public void setCoachName(String coachName) {
        this.coachName.setText(coachName);
    }

    public void setTeamName(String teamName) {
        this.teamName.setText(teamName);
    }

    public void setPlayers(ObservableList<Node> playersList) {
        for (Node n : playersList) {
            Label l = (Label) n;
            players.getChildren().add(addPlayer(l.getText()));
        }
    }
}
