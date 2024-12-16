package com.waubenwissel.GUIobjects;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;

public class Player extends Label {
    
    public Player(String name) {
        setText(name);
        setCursor(Cursor.OPEN_HAND);
        setOnDragDetected(e -> {
            javafx.scene.input.Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(this.getText());
            dragboard.setContent(content);

            e.consume();
        });
        setOnMousePressed(e -> {
            setCursor(Cursor.CLOSED_HAND);
        });
        setOnMouseMoved(e -> {
            setCursor(Cursor.OPEN_HAND);
        });
    }
}
