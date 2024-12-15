package com.waubenwissel.GUIobjects;

import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;

public class Player extends Label {
    
    public Player(String name) {
        setText(name);
        setOnDragDetected(e -> {
            javafx.scene.input.Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);

            ClipboardContent content = new ClipboardContent();
            content.putString(this.getText());
            dragboard.setContent(content);

            e.consume();
        });
    }
}
