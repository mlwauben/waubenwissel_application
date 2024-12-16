package com.waubenwissel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Desktop;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.spire.xls.Worksheet;
import com.waubenwissel.GUIobjects.Place;
import com.waubenwissel.GUIobjects.Player;
import com.waubenwissel.GUIobjects.Setup;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FileManager {
    public static void readInPlayers(VBox playerList) {
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

    public static File makeExcelFile(HBox anchorPane) throws IOException {
        //Create excel file
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("First Sheet");

        //Fill the file
        fillExcelFile(sheet, anchorPane);

        File file = getFile(".xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        } catch (Exception e) {
            System.out.println("File not closed");
        }

        workbook.close();

        return file;
    }

    public static void fillExcelFile(Sheet sheet, HBox anchorPane) {
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

    public static void makePng(HBox anchorPane) throws IOException {
        com.spire.xls.Workbook workbook = new com.spire.xls.Workbook();
        workbook.loadFromFile(makeExcelFile(anchorPane).getAbsolutePath());
        Worksheet sheet = workbook.getWorksheets().get(0);
    
        File file = getFile(".png");
        sheet.saveToImage(file.getAbsolutePath());
    
        try {
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            System.out.println("File not closed");
        }
    }
    
    public static File getFile(String type) {
        File desktopDir = new File(System.getProperty("user.home"), "Desktop");
        String pathToDesktop = desktopDir.getPath();
        String s = "testWisselschema";
        File file = new File(pathToDesktop+System.getProperty("file.separator") + s + type);
        if (System.getProperty("user.name").equals("mikaw")) {
            file = new File("C:\\Users\\mikaw\\OneDrive\\Desktop\\wisselschema" + type);
        }
        return file;
    }
}
