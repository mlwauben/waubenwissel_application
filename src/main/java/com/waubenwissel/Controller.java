package com.waubenwissel;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.waubenwissel.GUIobjects.Game;
import com.waubenwissel.GUIobjects.Setup;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
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
    @FXML
    private ScrollPane scrollPane;

    FileManager manager = new FileManager();
    Game game = new Game();

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

        // game.setSetups(setups);
        game.setOpponent(opponentSign.getText());
        if (!(dateSign.getValue() == null)) {
            game.setDate(dateSign.getValue());
        }

        // Mouse wheel scroll multiplier
        scrollPane.getContent().setOnScroll(event -> {
            double deltaY = event.getDeltaY() * 5; 
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getHeight());
            event.consume();
        });

        // Optional: increase unit increment for dragging scrollbar
        ScrollBar vBar = (ScrollBar) scrollPane.lookup(".scroll-bar:vertical");
        if (vBar != null) {
            vBar.setUnitIncrement(20);
        }
    }

    /**
     * Creates and opens an excel file of the full setup
     * @throws IOException
     */
    @FXML
    private void openExcelFile() throws IOException {
        File file = manager.makeExcelFile(quarterTabs, game);
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
        manager.makePng(quarterTabs, game);
    }
}
 