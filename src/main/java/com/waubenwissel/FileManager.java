package com.waubenwissel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.awt.Desktop;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.spire.xls.Worksheet;
import com.waubenwissel.GUIobjects.Game;
import com.waubenwissel.GUIobjects.Place;
import com.waubenwissel.GUIobjects.Player;
import com.waubenwissel.GUIobjects.Setup;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FileManager {
    
    public void readInPlayers(VBox playerList) {
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

    public File makeExcelFile(TabPane quarterTabs, Game game) throws IOException {
        //Create excel file
        URL url = getClass().getResource("/com/waubenwissel/xlsx/template.xlsx");

        if (url == null) {
            throw new FileNotFoundException("Excel template not found in classpath.");
        }

        try (InputStream inputStream = url.openStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            
            //Fill the file
            String opponent = game.getOpponent();
            LocalDate date = game.getDate();
            
            fillExcelFile(sheet, quarterTabs);
            addSpecialInfo(sheet, date, opponent);

            File file = getFile(".xlsx", game);
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
            } catch (Exception e) {
                System.out.println("File not closed");
            }

            workbook.close();

            return file;
        }
    }

    public void fillExcelFile(Sheet sheet, TabPane quarterTabs) {
        int[] startingPoint = {5,1};
        int i = 0;
        int j = 0;
        for (Tab tab : quarterTabs.getTabs()) {
            HBox contentPane = (HBox) tab.getContent(); 
            for (Node setup : contentPane.getChildren()) {
                Setup s = (Setup) setup;
                fillSetup(sheet, s, startingPoint[0]+j, startingPoint[1]+i, i);

                i = (i == 0) ? 8 : 0;
            }
            j += 18;
        }
    }

    private void addSpecialInfo(Sheet firstSheet, LocalDate date, String opponent) {
        //Add opposition
        firstSheet.getRow(1).getCell(0).setCellValue("WISSELSCHEMA " + opponent);

        //Add date with dd-MM-yyyy
        firstSheet.getRow(1).getCell(6).setCellValue("Datum: " + date);

        // TODO Add absentees
        // Row absenteesRow = firstSheet.getRow(1);
        // Cell absenteesCell = absenteesRow.getCell(9);
        // //TO DO:: Look could be nicer
        // // absenteesCell.setCellValue("Afwezig: " + absentees.toString());
    }

    public void fillSetup(Sheet sheet, Setup s, int row, int column, int side) {
        int[][] coords = {  {0,0},{0,2},{0,4},
                            {3,0},{3,2},{3,4},
                                  {6,0},
                            {5,2},      {6,4},
                                  {7,2},
                                  {9,2},
                            {12,0},{13,0},{14,0},{12,3},
                            {13,3},{12,2},{13,2},{14,2},
                            {12,5},{13,5}};
        for (int i = 0; i < coords.length; i++) {
            Place p = (Place) s.getChildren().get(i);
            sheet.getRow(row+coords[i][0]).getCell(column+coords[i][1]).setCellValue(p.getText());

        }
        int[][] cornerCoords = {{2,6},{3,6},{4,6}};
        if (side != 0) {
            int[][] rightCoords = {{6,-2},{7,-2},{8,-2}};
            cornerCoords = rightCoords;
        } 
        for (int i = 0; i < cornerCoords.length; i++) {
            Place p = (Place) s.getChildren().get(i+21);
            sheet.getRow(row+cornerCoords[i][0]).getCell(column+cornerCoords[i][1]).setCellValue(p.getText());
        }
    }

    public void makePng(TabPane quarterTabs, Game game) throws IOException {
        com.spire.xls.Workbook workbook = new com.spire.xls.Workbook();
        workbook.loadFromFile(makeExcelFile(quarterTabs, game).getAbsolutePath());
        Worksheet sheet = workbook.getWorksheets().get(0);
    
        File file = getFile(".png", game);
        sheet.saveToImage(file.getAbsolutePath());
    
        try {
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            System.out.println("File not closed");
        }
    }
    
    public File getFile(String type, Game game) {
        File desktopDir = new File(System.getProperty("user.home"), "Desktop");
        String pathToDesktop = desktopDir.getPath();
        String s = game.getDate().toString() + " wisselschema " + game.getOpponent(); 
        File file = new File(pathToDesktop+System.getProperty("file.separator") + s + type);
        if (System.getProperty("user.name").equals("mikaw")) {
            file = new File("C:\\Users\\mikaw\\OneDrive\\Desktop\\wisselschema" + type);
        }
        return file;
    }
}
