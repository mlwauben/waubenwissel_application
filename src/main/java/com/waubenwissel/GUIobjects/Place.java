package com.waubenwissel.GUIobjects;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class Place extends Label {
    private String mainName;
    
    public Place(String mainName, String name) {
        this.mainName = mainName;
        setText(name);
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
        
                if (isMainTeamPlace(setup)) {
                    success = handleMainTeamPlace(setup, dragged);
                } else {
                    success = handleSubsOrCornerPlace(dragged);
                }
            }
        
            e.setDropCompleted(success);
            e.consume();
        });
    }
        
    private boolean isMainTeamPlace(Setup setup) {
        return setup.getMainNames().contains(this.mainName);
    }
    
    private boolean handleMainTeamPlace(Setup setup, String dragged) {
        System.out.println(setup.getTeam().toString());
        if (setup.getTeam().contains(dragged)) {
            resetExistingPlace(setup, dragged);
        }
        setup.replacePlace(this.getText(), dragged);
        this.setText(dragged);
        return true;
    }
    
    private void resetExistingPlace(Setup setup, String dragged) {
        for (Node node : setup.getChildren()) {
            Place place = (Place) node;
            if (dragged.equals(place.getText()) && setup.getMainNames().contains(place.getMainName())) {
                //If the new place has no name, put the main name in the old place
                if (this.mainName.equals(this.getText())) {
                    place.setText(place.getMainName());
                    setup.replacePlace(dragged, place.getMainName());
                //Else if the new place has a name, switch the names around
                } else {
                    place.setText(this.getText());
                }
                break;
            }
        }
    }
    
    private boolean handleSubsOrCornerPlace(String dragged) {
        // TODO: Add validation logic for subs/corner if necessary
        this.setText(dragged);
        return true;
    }

    public String getMainName() {
        return mainName;
    }
}
