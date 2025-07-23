package com.waubenwissel;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.waubenwissel.GUIobjects.Setup;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    private VBox playerList;
    @FXML
    private TabPane quarterTabs;
    @FXML
    private DatePicker dateSign;
    @FXML
    private TextField opponentSign;

    FileManager manager = new FileManager();

    @FXML
    public void initialize() {
        manager.readInPlayers(playerList);

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
        File file = manager.makeExcelFile(quarterTabs, getDate(), opponentSign.getText());
        try {
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            System.out.println("File not closed");
        }
    }

    private String getDate() {
        String date = "01-01-22";
        if (!(dateSign.getValue() == null)) {
            LocalDate d = dateSign.getValue();
            date = d.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
        return date;
    }

    /**
     * Creates and opens a png file of the full setup
     * @throws IOException
     */
    @FXML
    private void makePngFile() throws IOException {
        manager.makePng(quarterTabs, getDate(), opponentSign.getText());
    }
}
 