package com.waubenwissel;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.waubenwissel.GUIobjects.Setup;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    private VBox playerList;
    @FXML
    private TabPane quarterTabs;

    @FXML
    public void initialize() {
        FileManager.readInPlayers(playerList);

        Setup[] setups = new Setup[8];

        for (int i = 0; i < setups.length; i++) {
            setups[i] = new Setup(null);
        }

        for (int i = 0; i < 7; i++) {
            setups[i].setNextSetup(setups[i+1]);
        }

        for (int i = 0; i < quarterTabs.getTabs().size(); i++) {
            Tab tab = quarterTabs.getTabs().get(i);
            HBox contentPane = (HBox) tab.getContent();

            contentPane.getChildren().add(setups[i * 2]);
            contentPane.getChildren().add(setups[i * 2 + 1]);
        }
    }

    /**
     * Creates and opens an excel file of the full setup
     * @throws IOException
     */
    @FXML
    private void openExcelFile() throws IOException {
        File file = FileManager.makeExcelFile(quarterTabs);
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
        FileManager.makePng(quarterTabs);
    }
}
 