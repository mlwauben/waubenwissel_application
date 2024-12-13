package com.waubenwissel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Desktop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label label;

    @FXML
    private void makeExcelFile() throws IOException {
        //Create excel file
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("First Sheet");

        //Fill the file
        String labelText = label.getText();

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(labelText);


        //Put file on the desktop and open it
        File file = new File("C:\\Users\\mikaw\\OneDrive\\Desktop\\wisselschema.xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            System.out.println("File not closed");
        }

        //Close workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void makePngFile() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void sendWhatsapp() throws IOException {
        App.setRoot("secondary");
    }
}
