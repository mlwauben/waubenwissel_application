package controllers;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

//ctrl shift minus to close all methods
public class BuilderController implements Initializable {

    public List<Pair<Integer, Integer>> positionCells;
    public List<Pair<Integer, Integer>> substitutesCells;
    public List<Pair<Integer, Integer>> oldSubstitutesCells;
    public List<Integer> startLabels;
    public List<String> startSubstitutesLabels;
    public List<String> startOldSubstitutesLabels;

    public List<String> green;
    public List<String> blue;
    public List<String> red;

    @FXML
    public TextField newPlayer;
    public VBox players;
    public int playersSize;

    public DatePicker dateSign;
    public TextField opponentTag;
    public ToggleButton awayGame;
    public List<String> absentees;

    public TabPane quarters;

    public Label coachText;
    public String teamName;

    public ImageView explosion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        positionCells = new ArrayList<>(Arrays.asList(new Pair<>(5, 1), new Pair<>(5, 3), new Pair<>(5, 5), new Pair<>(8, 1),
                new Pair<>(8, 3), new Pair<>(8, 5), new Pair<>(10, 3), new Pair<>(11, 1), new Pair<>(11, 5), new Pair<>(12, 3), new Pair<>(14, 3)));

        //substitutesCells = new ArrayList<>(Arrays.asList(new Pair<>(16, 5), new Pair<>(17, 5), new Pair<>(18, 5)));
        substitutesCells = new ArrayList<>(Arrays.asList(new Pair<>(17, 3), new Pair<>(18, 3), new Pair<>(19, 3), new Pair<>(17, 6), new Pair<>(18, 6)));

        //oldSubstitutesCells = new ArrayList<>(Arrays.asList(new Pair<>(16, 3), new Pair<>(17, 3), new Pair<>(18, 3)));
        oldSubstitutesCells = new ArrayList<>(Arrays.asList(new Pair<>(17, 1), new Pair<>(18, 1), new Pair<>(19, 1), new Pair<>(17, 4), new Pair<>(18, 4)));

        startLabels = new ArrayList<>(Arrays.asList(11, 10, 9, 5, 6, 8, 7, 4, 2, 3, 1));
        startSubstitutesLabels = new ArrayList<>(Arrays.asList("wissel van 1", "wissel van 2", "wissel van 3", "wissel van 4", "wissel van 5"));
        startOldSubstitutesLabels = new ArrayList<>(Arrays.asList("wissel 1", "wissel 2", "wissel 3", "wissel 4", "wissel 5"));

        absentees = new ArrayList<>();

        green = new ArrayList<>();
        blue = new ArrayList<>();
        red = new ArrayList<>();
    }

    public void addPlayers(File account) {
            try {
                InputStream stream = new FileInputStream(account);
                coachText.setText("Hallo, " +  FilenameUtils.removeExtension(account.getName()));

                Scanner myReader = new Scanner(stream);
                //Expand the team part
                teamName = myReader.nextLine();
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    //Add to players vbox list
                    addOnePlayer(data);
                }
                playersSize = players.getChildren().size();

                myReader.close();
            } catch (Exception e) {
                System.out.println("Player file not found.");
                Alert a = new Alert(Alert.AlertType.WARNING, "spelerslijst is niet aanwezig!");
                a.showAndWait();
            }
    }

    private void addOnePlayer(String data) {
        for (int i : new int[] {0, 1, 2, 3}) {
            Label l = new Label(data);
            l.setCursor(Cursor.OPEN_HAND);
            l.setPadding(new Insets(2, 0, 2, 4));
            l.setId("0");
            l.setOnDragDetected(e -> {
                Dragboard db = l.startDragAndDrop(TransferMode.ANY);
                ClipboardContent cb = new ClipboardContent();
                cb.putString(l.getText());
                db.setContent(cb);

                //players chosen test
                l.setStyle("-fx-font-style: italic");
            });
            if (i == 0) {
                l.setOnMouseClicked(e -> {
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Wil je " + l.getText() +
                            " op afwezig zetten? ", ButtonType.YES, ButtonType.CANCEL);
                    a.showAndWait();

                    if (a.getResult() == ButtonType.YES) {
                        l.setDisable(true);
                        playersSize--;
                        setVisibilitySubstitutes();
                        absentees.add(l.getText());
                    }
                });
            }

            AnchorPane quarter = (AnchorPane) quarters.getTabs().get(i).getContent();
            VBox players;
            ScrollPane sc = (ScrollPane) quarter.getChildren().get(2);
            players = (VBox) sc.getContent();
            players.getChildren().add(l);
        }
    }

    public void addPlayer() {
        if (!(newPlayer.getText().length() == 0)) {
            addOnePlayer(newPlayer.getText());
        }
        newPlayer.setText("");

        //Adapt substitute places correctly
        playersSize++;
        setVisibilitySubstitutes();
    }

    public void resetAbsentees() {
        for (int i = 0; i < players.getChildren().size(); i++) {
            if (players.getChildren().get(i).isDisable()) {
                players.getChildren().get(i).setDisable(false);
                playersSize++;
            }
        }
        setVisibilitySubstitutes();
    }

    private void setVisibilitySubstitutes() {
        int size = playersSize;
        for (int i : new int[]{0,1,2,3}) {
            //get substitutes panes and old substitutes panes and check the size to adapt correctly for
            //every lineup and every tab
            AnchorPane quarter = (AnchorPane) quarters.getTabs().get(i).getContent();
            for (int c = 0; c < 2; c++) {
                Pane lineup = (Pane) quarter.getChildren().get(c);
                Pane places = (Pane) lineup.getChildren().get(0);
                Pane substitutes = (Pane) lineup.getChildren().get(1);
                Pane oldSubstitutes = (Pane) places.getChildren().get(places.getChildren().size() - 1);

                substitutes.getChildren().get(0).setVisible(false);
                oldSubstitutes.getChildren().get(0).setVisible(false);
                substitutes.getChildren().get(1).setVisible(false);
                oldSubstitutes.getChildren().get(1).setVisible(false);
                substitutes.getChildren().get(2).setVisible(false);
                oldSubstitutes.getChildren().get(2).setVisible(false);
                substitutes.getChildren().get(3).setVisible(false);
                oldSubstitutes.getChildren().get(3).setVisible(false);
                substitutes.getChildren().get(4).setVisible(false);
                oldSubstitutes.getChildren().get(4).setVisible(false);

                if (size == 12) {
                    substitutes.getChildren().get(0).setVisible(true);
                    oldSubstitutes.getChildren().get(0).setVisible(true);
                    ((Label) substitutes.getChildren().get(1)).setText(startSubstitutesLabels.get(1));
                    ((Label) oldSubstitutes.getChildren().get(1)).setText(startOldSubstitutesLabels.get(1));
                    ((Label) substitutes.getChildren().get(2)).setText(startSubstitutesLabels.get(2));
                    ((Label) oldSubstitutes.getChildren().get(2)).setText(startOldSubstitutesLabels.get(2));
                    ((Label) substitutes.getChildren().get(3)).setText(startSubstitutesLabels.get(3));
                    ((Label) oldSubstitutes.getChildren().get(3)).setText(startOldSubstitutesLabels.get(3));
                    ((Label) substitutes.getChildren().get(4)).setText(startSubstitutesLabels.get(4));
                    ((Label) oldSubstitutes.getChildren().get(4)).setText(startOldSubstitutesLabels.get(4));
                }
                if (size == 13) {
                    substitutes.getChildren().get(0).setVisible(true);
                    oldSubstitutes.getChildren().get(0).setVisible(true);
                    substitutes.getChildren().get(1).setVisible(true);
                    oldSubstitutes.getChildren().get(1).setVisible(true);
                    ((Label) substitutes.getChildren().get(2)).setText(startSubstitutesLabels.get(2));
                    ((Label) oldSubstitutes.getChildren().get(2)).setText(startOldSubstitutesLabels.get(2));
                    ((Label) substitutes.getChildren().get(3)).setText(startSubstitutesLabels.get(3));
                    ((Label) oldSubstitutes.getChildren().get(3)).setText(startOldSubstitutesLabels.get(3));
                    ((Label) substitutes.getChildren().get(4)).setText(startSubstitutesLabels.get(4));
                    ((Label) oldSubstitutes.getChildren().get(4)).setText(startOldSubstitutesLabels.get(4));
                }
                if (size == 14) {
                    substitutes.getChildren().get(0).setVisible(true);
                    oldSubstitutes.getChildren().get(0).setVisible(true);
                    substitutes.getChildren().get(1).setVisible(true);
                    oldSubstitutes.getChildren().get(1).setVisible(true);
                    substitutes.getChildren().get(2).setVisible(true);
                    oldSubstitutes.getChildren().get(2).setVisible(true);
                    ((Label) substitutes.getChildren().get(3)).setText(startSubstitutesLabels.get(3));
                    ((Label) oldSubstitutes.getChildren().get(3)).setText(startOldSubstitutesLabels.get(3));
                    ((Label) substitutes.getChildren().get(4)).setText(startSubstitutesLabels.get(4));
                    ((Label) oldSubstitutes.getChildren().get(4)).setText(startOldSubstitutesLabels.get(4));
                }
                if (size == 15) {
                    substitutes.getChildren().get(0).setVisible(true);
                    oldSubstitutes.getChildren().get(0).setVisible(true);
                    substitutes.getChildren().get(1).setVisible(true);
                    oldSubstitutes.getChildren().get(1).setVisible(true);
                    substitutes.getChildren().get(2).setVisible(true);
                    oldSubstitutes.getChildren().get(2).setVisible(true);
                    substitutes.getChildren().get(3).setVisible(true);
                    oldSubstitutes.getChildren().get(3).setVisible(true);
                    ((Label) substitutes.getChildren().get(4)).setText(startSubstitutesLabels.get(4));
                    ((Label) oldSubstitutes.getChildren().get(4)).setText(startOldSubstitutesLabels.get(4));
                }
                if (size >= 16) {
                    substitutes.getChildren().get(0).setVisible(true);
                    oldSubstitutes.getChildren().get(0).setVisible(true);
                    substitutes.getChildren().get(1).setVisible(true);
                    oldSubstitutes.getChildren().get(1).setVisible(true);
                    substitutes.getChildren().get(2).setVisible(true);
                    oldSubstitutes.getChildren().get(2).setVisible(true);
                    substitutes.getChildren().get(3).setVisible(true);
                    oldSubstitutes.getChildren().get(3).setVisible(true);
                    substitutes.getChildren().get(4).setVisible(true);
                    oldSubstitutes.getChildren().get(4).setVisible(true);
                }
            }
        }
    }

    public void resetLineup(MouseEvent mouseEvent) {
        explosion.setVisible(true);

        for (int i : new int[] {0, 1, 2, 3}) {
            AnchorPane quarter = (AnchorPane) quarters.getTabs().get(i).getContent();

            //Loop over the 2 lineups in the tab (quarter)
            for (int c = 0; c < 2; c++) {
                Pane lineup = (Pane) quarter.getChildren().get(c);
                Pane places = (Pane) lineup.getChildren().get(0);
                Pane substitutes = (Pane) lineup.getChildren().get(1);
                Pane oldSubstitutes = (Pane) places.getChildren().get(places.getChildren().size()-1);

                //Loop over all positions in the lineup
                for (int j = 0; j < places.getChildren().size()-1; j++) {
                    Label l = (Label) places.getChildren().get(j);
                    l.setText(String.valueOf(startLabels.get(j)));
                    l.setStyle("-fx-border-color: black");
                }

                //Loop over the old and new substitutes
                for (int j = 0; j < oldSubstitutes.getChildren().size(); j++) {
                    Label l = (Label) oldSubstitutes.getChildren().get(j);
                    l.setText(startOldSubstitutesLabels.get(j));
                    l.setStyle("-fx-border-color: black");

                    Label l2 = (Label) substitutes.getChildren().get(j);
                    l2.setText(startSubstitutesLabels.get(j));
                    l2.setStyle("-fx-border-color: black");
                }
            }
        }

        for (int i = 0; i < players.getChildren().size(); i++) {
            Label l = (Label) players.getChildren().get(i);
            l.setStyle("");
        }

        green = new ArrayList<>();
        blue = new ArrayList<>();
        red = new ArrayList<>();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                explosion.setVisible(false);
            }
        };
        Timer timer = new Timer("Timer");

        long delay = 2000L;
        timer.schedule(task, delay);
    }


    public void handleTextDragOver(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasString()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void handleDragEntered(DragEvent dragEvent) {
        Label l = (Label) dragEvent.getSource();
        String s = "-fx-border-color: green";

        l.setStyle(l.getStyle() + ";" + s);
    }

    public void handleDragExited(DragEvent dragEvent) {
        Label l = (Label) dragEvent.getSource();
        String s = "-fx-border-color: black";

        l.setStyle(l.getStyle() + ";" + s);
    }

    public void handleTextDropped(DragEvent dragEvent) {
        Label l = (Label) dragEvent.getSource();
        Pane parent = (Pane) l.getParent();
        String s = dragEvent.getDragboard().getString();

        for (int i = 0; i < parent.getChildren().size()-1; i++) {
            Label l2 = (Label) parent.getChildren().get(i);
            if (l2.getText().equals(dragEvent.getDragboard().getString())) {
                //only change text if it is not a number
                if (l2.getText().length() > 2) {
                    l2.setText(l.getText());
                }
                l2.setStyle(getStyleColourList(l.getText()));
                //If a number of a substitute text get switched, return to start label
                if (l2.getText().length() < 3) {
                    l2.setText(String.valueOf(startLabels.get(i)));
                }
                else if (startOldSubstitutesLabels.contains(l2.getText())) {
                    l2.setText(startOldSubstitutesLabels.get(i));
                }
                else if (startSubstitutesLabels.contains(l2.getText())) {
                    l2.setText(startSubstitutesLabels.get(i));
                }
            }
        }
        //If NAN gets dragged, paste it in the new label
        if (s.length() > 2) {
            l.setText(s);
            l.setStyle(getStyleColourList(s));
        }
    }

    public void handlePositionDragDetection(MouseEvent mouseEvent) {
        Label l = (Label) mouseEvent.getSource();
        Dragboard db = l.startDragAndDrop(TransferMode.ANY);

        ClipboardContent cb = new ClipboardContent();
        cb.putString(l.getText());

        db.setContent(cb);

        mouseEvent.consume();
    }

    public void resetPosition(MouseEvent mouseEvent) {
        Label l = (Label) mouseEvent.getSource();

        //Reset italic players vbox
        for (int i = 0; i < playersSize; i++) {
            Label l2 = (Label) players.getChildren().get(i);
            if (l2.getText().equals(l.getText())) {
                l2.setStyle("");
            }
        }

        //return to start label
        int index = ((Label) mouseEvent.getSource()).getParent().getChildrenUnmodifiable().indexOf(l);
        l.setText(String.valueOf(startLabels.get(index)));
        l.setStyle("-fx-border-color: black");


    }

    public void resetOldSubstitute(MouseEvent mouseEvent) {
        Label l = (Label) mouseEvent.getSource();
        int i = ((Label) mouseEvent.getSource()).getParent().getChildrenUnmodifiable().indexOf(l);
        l.setText(String.valueOf(startOldSubstitutesLabels.get(i)));
        l.setStyle("-fx-border-color: black");
    }

    public void resetSubstitute(MouseEvent mouseEvent) {
        Label l = (Label) mouseEvent.getSource();
        int i = ((Label) mouseEvent.getSource()).getParent().getChildrenUnmodifiable().indexOf(l);
        l.setText(String.valueOf(startSubstitutesLabels.get(i)));
        l.setStyle("-fx-border-color: black");

    }


    public void writeExcelFile() throws IOException {
        Workbook workbook;
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(
                "excel/template.xlsx");

        assert inputStream != null;
        workbook = new XSSFWorkbook(inputStream);

        Sheet firstSheet = workbook.getSheetAt(0);

        addSpecialInfo(firstSheet);

        copyToExcel(firstSheet);

        String s = createFileTitle();
        if (System.getProperty("user.name").equals("mikaw")) {
            File file = new File("C:\\Users\\mikaw\\OneDrive\\Desktop\\wisselschema.xlsx");
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
                Desktop.getDesktop().open(file);
            } catch (Exception e) {
                System.out.println("File not closed");
            }
        }
        else {
            File desktopDir = new File(System.getProperty("user.home"), "Desktop");
            String pathToDesktop = desktopDir.getPath();

            File file = new File(pathToDesktop+System.getProperty("file.separator") + s + ".xlsx");
            try (FileOutputStream out =  new FileOutputStream(file)) {
                workbook.write(out);
                Desktop.getDesktop().open(file);
            } catch (Exception e) {
                System.out.println("File not closed");
            }
        }

        workbook.close();
        System.out.println("Excel file complete");
    }

    private String createFileTitle() {
        String date = "no date";
        if (!(dateSign.getValue() == null)) {
            LocalDate d = dateSign.getValue();
            date = d.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        String opponent;
        if (awayGame.isSelected()) {
            opponent = " wisselschema " + opponentTag.getText() + " - " + teamName;
        }
        else {
            opponent = " wisselschema " + teamName + " - " + opponentTag.getText();
        }

        return date + opponent;
    }

    private void addSpecialInfo(Sheet firstSheet) {
        //Add opposition
        Row opRow = firstSheet.getRow(1);
        Cell opCell = opRow.getCell(0);
        if (awayGame.isSelected()) {
            opCell.setCellValue("WISSELSCHEMA " + opponentTag.getText() + " - " + teamName);
        }
        else {
            opCell.setCellValue("WISSELSCHEMA " + teamName + " - " + opponentTag.getText());
        }

        //Add date with dd-MM-yyyy
        Row dateRow = firstSheet.getRow(1);
        Cell dateCell = dateRow.getCell(6);
        String date = "01-01-22";
        if (!(dateSign.getValue() == null)) {
            LocalDate d = dateSign.getValue();
            date = d.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
        dateCell.setCellValue("Datum: " + date);

        //Add absentees
        Row absenteesRow = firstSheet.getRow(1);
        Cell absenteesCell = absenteesRow.getCell(9);
        //TO DO:: Look could be nicer
        absenteesCell.setCellValue("Afwezig: " + absentees.toString());
    }

    private void copyToExcel(Sheet firstSheet) {
        //Loop over all 4 tabs (the 4 quarters)
        for (int i : new int[] {0, 1, 2, 3}) {
            AnchorPane quarter = (AnchorPane) quarters.getTabs().get(i).getContent();

            //Loop over the 2 lineups in the tab (quarter)
            for (int c = 0; c < 2; c++) {
                Pane lineup = (Pane) quarter.getChildren().get(c);
                Pane places = (Pane) lineup.getChildren().get(0);
                Pane substitutes = (Pane) lineup.getChildren().get(1);
                Pane oldSubstitutes = (Pane) places.getChildren().get(places.getChildren().size()-1);

                //Loop over all positions in the lineup
                for (int j = 0; j < places.getChildren().size()-1; j++) {
                    setCellValues(firstSheet, i, c, places, j, positionCells);
                }

                //Loop over the old and new substitutes
                for (int j = 0; j < oldSubstitutes.getChildren().size(); j++) {
                    setCellValues(firstSheet, i, c, oldSubstitutes, j, oldSubstitutesCells);

                    setCellValues(firstSheet, i, c, substitutes, j, substitutesCells);
                }
            }
        }
    }

    private void setCellValues(Sheet firstSheet, int i, int c, Pane places, int j, List<Pair<Integer, Integer>> cells) {
        System.out.println(j);
        Label l = (Label) places.getChildren().get(j);

        Row row = firstSheet.getRow(cells.get(j).getKey() + i * 18);
        Cell cell = row.getCell(cells.get(j).getValue() + c * 8);
        if (cell == null) {
            cell = row.createCell(cells.get(j).getValue() + c * 8);
        }

        System.out.println(row.cellIterator().toString());
        System.out.println(cells.get(j).getValue() + c * 8);
        if (cells.equals(positionCells)) {
            cell.setCellValue(startLabels.get(j) + ". " + l.getText());
        }
        else if (!(cells.equals(substitutesCells) && i == 3 && c == 1) &&
                places.getChildren().get(j).isVisible()){
            cell.setCellValue(l.getText());
            System.out.println(l.getText());
            if (cells.equals(oldSubstitutesCells) &&
                    !(cells.equals(oldSubstitutesCells) && i == 3 && c == 1)) {
                Cell metCell = row.getCell(cell.getColumnIndex()+1);
                if (metCell == null) {
                    metCell = row.createCell(cell.getColumnIndex()+1);
                }

                metCell.setCellValue("met");
            }
        }
    }


    public void nextLineup(ActionEvent actionEvent) {
        addNamesToColourLists(actionEvent);

        Button b = (Button) actionEvent.getSource();
        Pane lineup = (Pane) b.getParent();
        Pane places = (Pane) lineup.getChildren().get(0);
        Pane substitutes = (Pane) lineup.getChildren().get(1);
        Pane oldSubstitutes = (Pane) places.getChildren().get(places.getChildren().size()-1);

        if (checkWarnings(places, substitutes, oldSubstitutes)) {
            if (b.getText().equals("Volgende")) {
                sameTab(lineup, places, substitutes, oldSubstitutes);
            }
            else {
                nextTab(places, substitutes, oldSubstitutes);
            }
        }
    }

    private void sameTab(Pane lineup, Pane places, Pane substitutes, Pane oldSubstitutes) {
        AnchorPane quarter = (AnchorPane) lineup.getParent();
        Pane lineup2 = (Pane) quarter.getChildren().get(1);
        Pane places2 = (Pane) lineup2.getChildren().get(0);

        replacePlaces(places, substitutes, oldSubstitutes, places2);
        lineup2.setDisable(false);
    }

    private void nextTab(Pane places, Pane substitutes, Pane oldSubstitutes) {
        //Get old places
        AnchorPane quarter = (AnchorPane) quarters.getTabs().get(quarters.getSelectionModel().getSelectedIndex()+1).getContent();
        Pane lineup2 = (Pane) quarter.getChildren().get(0);
        Pane places2 = (Pane) lineup2.getChildren().get(0);

        replacePlaces(places, substitutes, oldSubstitutes, places2);
        lineup2.setDisable(false);

        //Go to next tab
        quarters.getSelectionModel().select(quarters.getSelectionModel().getSelectedIndex()+1);
    }

    private void replacePlaces(Pane places, Pane substitutes, Pane oldSubstitutes, Pane places2) {
        //Replace old places with the new places
        for (int i = 0; i < places.getChildren().size() - 1; i++) {
            Label l = (Label) places.getChildren().get(i);
            Label l2 = (Label) places2.getChildren().get(i);
            l2.setText(l.getText());
            l2.setStyle(l.getStyle());
        }

        //Replace the substitutes, i = new substitutes
        for (int i = 0; i < substitutes.getChildren().size(); i++) {
            Label l = (Label) substitutes.getChildren().get(i);
            //j = possible substitute in the field
            for (int j = 0; j < places2.getChildren().size() - 1; j++) {
                Label l2 = (Label) places2.getChildren().get(j);
                // if l2 == l than a substitute in the field is found, l3 = old substitute
                if (l2.getText().equals(l.getText())) {
                    Label l3 = (Label) oldSubstitutes.getChildren().get(i);
                    //addToColourList(l3.getText());
                    l2.setText(l3.getText());
                    l2.setStyle(getStyleColourList(l3.getText()));
                }
            }
            //Add the new substitutes to the side
            Pane p = (Pane) places2.getChildren().get(places.getChildren().size() - 1);
            Label l4 = (Label) p.getChildren().get(i);
            if (!startSubstitutesLabels.contains(l.getText()) && !startOldSubstitutesLabels.contains(l.getText())) {
                l4.setText(l.getText());
            }
        }
    }

    private void addNamesToColourLists(ActionEvent actionEvent) {
        green = new ArrayList<>();
        blue = new ArrayList<>();
        red = new ArrayList<>();
        Button b = (Button) actionEvent.getSource();
        Pane lineupPane = (Pane) b.getParent();

        //Loop over all 4 tabs (the 4 quarters)
        for (int i : new int[] {0, 1, 2, 3}) {
            AnchorPane quarter = (AnchorPane) quarters.getTabs().get(i).getContent();

            //Loop over the 2 lineups in the tab (quarter)
            for (int c = 0; c < 2; c++) {
                Pane lineup = (Pane) quarter.getChildren().get(c);
                Pane places = (Pane) lineup.getChildren().get(0);
                Pane oldSubstitutes = (Pane) places.getChildren().get(places.getChildren().size() - 1);

                for (int j = 0; j < oldSubstitutes.getChildren().size(); j++) {
                    Label l = (Label) oldSubstitutes.getChildren().get(j);
                    if (!startOldSubstitutesLabels.contains(l.getText()) &&
                            !startSubstitutesLabels.contains(l.getText())) {
                        addToColourList(l.getText());
                    }
                }
                if (lineup.equals(lineupPane)) {
                    return;
                }
            }
        }

    }

    private void addToColourList(String name) {
        if (green.contains(name)) {
            green.remove(name);
            blue.add(name);
        } else if (blue.contains(name)) {
            blue.remove(name);
            red.add(name);
        } else {
            green.add(name);
        }
    }

    private String getStyleColourList(String name) {
        String styleString = "-fx-font-weight: bold;" + "-fx-border-color: black;";

        if (green.contains(name)) {
            return styleString + "-fx-text-fill: green";
        } else if (blue.contains(name)) {
            return styleString + "-fx-text-fill: blue";
        } else if (red.contains(name)){
            return styleString + "-fx-text-fill: red";
        } else {
            return "-fx-border-color: black;";
        }
    }

    private boolean checkWarnings(Pane places, Pane substitutes, Pane oldSubstitutes) {
        boolean bool = true;
        List<String> players = new ArrayList<>();

        for (int i = 0; i < places.getChildren().size() - 1; i++) {
            Label label = (Label) places.getChildren().get(i);
            String s = label.getText();
            players.add(s);
        }

        for (int i : new int[]{0, 1, 2}) {
            Label l = (Label) substitutes.getChildren().get(i);
            if (!players.contains(l.getText()) && !startSubstitutesLabels.contains(l.getText())) {
                Alert a = new Alert(Alert.AlertType.WARNING, l.getText() + " staat niet in het veld!");
                a.showAndWait();
                bool = false;
            }
        }

        for (int i : new int[]{0, 1, 2}) {
            Label sub = (Label) substitutes.getChildren().get(i);
            Label oldSub = (Label) oldSubstitutes.getChildren().get(i);
            if (!startSubstitutesLabels.contains(sub.getText()) && startOldSubstitutesLabels.contains(oldSub.getText())) {
                Alert a = new Alert(Alert.AlertType.WARNING, sub.getText() + " heeft geen wissel!");
                a.showAndWait();
                bool = false;
            }

            if (startSubstitutesLabels.contains(sub.getText()) && !startOldSubstitutesLabels.contains(oldSub.getText())) {
                Alert a = new Alert(Alert.AlertType.WARNING, oldSub.getText() + " heeft geen wissel!");
                a.showAndWait();
                bool = false;
            }
        }
        return bool;
    }

    public void returnToStart(ActionEvent actionEvent) throws IOException {
        Parent dash = FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader().getResource("fxml/startScene.fxml")));
        Scene scene = new Scene(dash);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        System.out.println("display startScene");
    }

    public void changeAccount(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/changeAccountScene.fxml")));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        ChangeAccountController con = loader.getController();
        Scene oldBuilder = ((Node) actionEvent.getSource()).getScene();
        con.setOldBuilder(oldBuilder);
        con.setCoachName(coachText.getText().replaceAll("Hallo, ", ""));
        con.setTeamName(teamName);
        con.setPlayers(players.getChildren());

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        System.out.println("display changeAccountScene");
    }
}
