package com.waubenwissel.GUIobjects;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CornerPlace extends Place {

    public CornerPlace(String mainName, String name) {
        super(mainName, name);
    }

    public boolean handlePlace(Setup setup, String dragged) {
        // Ensure only field players can be placed in corner spots
        if (!setup.getFieldPlayers().contains(dragged)) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Speler niet op het veld");
            alert.setHeaderText(null);
            alert.setContentText("Een speler moet eerst op het veld staan voordat die deel kan zijn van de strafcorner.");
            alert.showAndWait();

            return false;
        }

        if (setup.getCorners().contains(dragged)) {
            resetExistingPlace(setup, dragged);
        }
        setup.replaceCornerPlace(this.getText(), dragged);
        this.setText(dragged);
        setup.updatePlaceMap(this);
        return true;
    }
    
    @Override
    public void resetExistingPlace(Setup setup, String dragged) {
        for (Node node : setup.getChildren()) {
            Place place = (Place) node;
            if (dragged.equals(place.getText()) && setup.getCornerNames().contains(place.getMainName())) {
                //If the new place has no name, put the main name in the old place
                if (this.mainName.equals(this.getText())) {
                    place.setText(place.getMainName());
                    setup.replaceCornerPlace(dragged, place.getMainName());
                //Else if the new place has a name, switch the names around
                } else {
                    place.setText(this.getText());
                }
                setup.updatePlaceMap(place);
                break;
            }
        }
    }
    
}
