package com.waubenwissel;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.spire.xls.Worksheet;
import com.waubenwissel.GUIobjects.Place;
import com.waubenwissel.GUIobjects.Player;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    private VBox playerList, placesList;

    @FXML
    public void initialize() {
        String[] names = {"Mika", "Zea", "Pascal", "Brendy"};
        for (String name : names) {
            playerList.getChildren().add(new Player(name));
        }

        String[] places = {"Voor", "Midden", "Achter", "Bank"};
        for (String place : places) {
            placesList.getChildren().add(new Place(place));
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
        Row row = sheet.createRow(2);
        int i = 0;
        for (Node place : placesList.getChildren()) {
            Cell cell = row.createCell(i);
            cell.setCellValue(((Place) place).getText());
            i++;
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
 