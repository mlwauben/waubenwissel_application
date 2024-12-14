package com.waubenwissel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Desktop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import com.spire.xls.*;

//import com.spire.xls.Workbook;

public class Controller {

    @FXML
    private Label place1, place2, place3, place4, player1, player2, player3, player4;

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

        File file = new File("C:\\Users\\mikaw\\OneDrive\\Desktop\\wisselschema.xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        } catch (Exception e) {
            System.out.println("File not closed");
        }

        workbook.close();

        return file;
    }

    private void fillExcelFile(Sheet sheet) {
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(place1.getText());

        Cell cell2 = row.createCell(1);
        cell2.setCellValue(place2.getText());

        Cell cell3 = row.createCell(2);
        cell3.setCellValue(place3.getText());

        Cell cell4 = row.createCell(3);
        cell4.setCellValue(place4.getText());
    }

    @FXML
    private void makePngFile() throws IOException {
        com.spire.xls.Workbook workbook = new com.spire.xls.Workbook();

        workbook.loadFromFile(makeExcelFile().getAbsolutePath());
        
        Worksheet sheet = workbook.getWorksheets().get(0);

        sheet.saveToImage("C:\\Users\\mikaw\\OneDrive\\Desktop\\wisselschema.png");
        File file = new File("C:\\Users\\mikaw\\OneDrive\\Desktop\\wisselschema.png");
        try {
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            System.out.println("File not closed");
        }
    }
}
 