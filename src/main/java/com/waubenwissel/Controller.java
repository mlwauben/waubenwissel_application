package com.waubenwissel;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.spire.xls.Worksheet;
import com.waubenwissel.GUIobjects.Place;
import com.waubenwissel.GUIobjects.Player;
import com.waubenwissel.GUIobjects.Setup;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    private VBox playerList;
    @FXML
    private HBox anchorPane;

    @FXML
    public void initialize() {
        readInPlayers();

        anchorPane.getChildren().add(new Setup());
        anchorPane.getChildren().add(new Setup());
    }

    private void readInPlayers() {
        String filePath = "src\\main\\resources\\com\\waubenwissel\\txt\\team.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Label label = new Player(line);
                playerList.getChildren().add(label);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openExcelFile() throws IOException {
        //Put file on the desktop and open it
        File file = makeExcelFile();
        try {
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            System.out.println("File not closed");
        }
    }

    private File makeExcelFile() throws IOException {
        //Create excel file
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("First Sheet");

        //Fill the file
        fillExcelFile(sheet);

        File file = getFile(".xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        } catch (Exception e) {
            System.out.println("File not closed");
        }

        workbook.close();

        return file;
    }

    private File getFile(String type) {
        File desktopDir = new File(System.getProperty("user.home"), "Desktop");
        String pathToDesktop = desktopDir.getPath();
        String s = "testWisselschema";
        File file = new File(pathToDesktop+System.getProperty("file.separator") + s + type);
        if (System.getProperty("user.name").equals("mikaw")) {
            file = new File("C:\\Users\\mikaw\\OneDrive\\Desktop\\wisselschema" + type);
        }
        return file;
    }

    private void fillExcelFile(Sheet sheet) {
        int i = 0;
        int j = 2;
        for (Node setup : anchorPane.getChildren()) {
            Row row = sheet.createRow(j);
            Setup s = (Setup) setup;
            for (Node place : s.getChildren()) {
                Place p = (Place) place;

                Cell cell = row.createCell(i);
                cell.setCellValue(p.getText());
                i++;
            }
            j++;
            i = 0;
        }
    }

    @FXML
    private void makePngFile() throws IOException {
        com.spire.xls.Workbook workbook = new com.spire.xls.Workbook();
        workbook.loadFromFile(makeExcelFile().getAbsolutePath());
        Worksheet sheet = workbook.getWorksheets().get(0);

        File file = getFile(".png");
        sheet.saveToImage(file.getAbsolutePath());

        try {
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            System.out.println("File not closed");
        }
    }
}
 