package com.waubenwissel.GUIobjects;

import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class Place extends Label {
    
    public Place(String name) {
        setText(name);
        setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        setOnDragDropped(e -> {
            Dragboard dragboard = e.getDragboard();
            boolean success = false;

            if (dragboard.hasString()) {
                this.setText(dragboard.getString());
                success = true;
            }

            e.setDropCompleted(success);
            e.consume();
        });
    }
}
