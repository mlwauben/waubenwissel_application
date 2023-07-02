package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StartSceneController implements Initializable {

    @FXML
    public Label label;
    public AnchorPane startScene;
    public Button startButton;
    public Button newAccountButton;
    public TextField usernameField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");

        //Create accounts folder if not yet done
        File directory = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\WaubenWissel accounts");
        if (! directory.exists()) {
            directory.mkdir();
        }
    }

    public void displayBuilder(ActionEvent event) throws IOException {
        String documentsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\WaubenWissel accounts";
        File directory = new File(documentsPath);

        //If accounts exist and the account searched for is in it, open the builder with the account
        if (directory.exists()) {
            String[] accounts = directory.list();

            assert accounts != null;
            for (String s : accounts) {
                String accountStr = usernameField.getText() + ".txt";
                if (s.equals(accountStr)) {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/builder.fxml")));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    BuilderController con = loader.getController();
                    File file = new File(documentsPath + "/" + accountStr);
                    con.addPlayers(file);

                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show();
                    System.out.println("display builderScene");
                    return;
                }
            }
        }
        System.out.println("Account does not exist");
        Alert a = new Alert(Alert.AlertType.WARNING, "Dit account is niet bekend.");
        a.showAndWait();
    }

    public void displayCreateAccount(ActionEvent actionEvent) throws IOException {
        Parent dash = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/newAccountScene.fxml")));
        Scene scene = new Scene(dash);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        System.out.println("display newAccountScene");
    }
}