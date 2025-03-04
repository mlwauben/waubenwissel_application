package com.waubenwissel.GUIobjects;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public abstract class Place extends Label {
    public String mainName;
    
    public Place(String mainName, String name) {
        this.mainName = mainName;
        setText(name);
        setPrefWidth(100);
        setAlignment(Pos.CENTER);
        //Checks if the dragged label has text
        setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        //prints the text of the dragged label to the Place
        setOnDragDropped(e -> {
            Dragboard dragboard = e.getDragboard();
            boolean success = false;
        
            if (dragboard.hasString()) {
                Setup setup = (Setup) getParent();
                String dragged = dragboard.getString();
                success = handlePlace(setup, dragged);
                setup.checkAndAdvanceSetup();
            }
        
            e.setDropCompleted(success);
            e.consume();
        });
    }
    
    abstract public void resetExistingPlace(Setup setup, String dragged);

    abstract public boolean handlePlace(Setup setup, String dragged);


    public String getMainName() {
        return mainName;
    }

    public boolean isFilled() {
        return !this.getText().equals(mainName);
    }
}
