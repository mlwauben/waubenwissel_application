package com.waubenwissel;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.waubenwissel.GUIobjects.Setup;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    private VBox playerList;
    @FXML
    private HBox anchorPane;

    @FXML
    public void initialize() {
        FileManager.readInPlayers(playerList);

        anchorPane.getChildren().add(new Setup());
        anchorPane.getChildren().add(new Setup());
    }

    /**
     * Creates and opens an excel file of the full setup
     * @throws IOException
     */
    @FXML
    private void openExcelFile() throws IOException {
        //Put file on the desktop and open it
        File file = FileManager.makeExcelFile(anchorPane);
        try {
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            System.out.println("File not closed");
        }
    }

    /**
     * Creates and opens a png file of the full setup
     * @throws IOException
     */
    @FXML
    private void makePngFile() throws IOException {
        FileManager.makePng(anchorPane);
    }
}
 