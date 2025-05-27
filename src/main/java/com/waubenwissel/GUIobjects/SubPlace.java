package com.waubenwissel.GUIobjects;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SubPlace extends Place {

    public SubPlace(String mainName, String name) {
        super(mainName, name);
    }

    public boolean handlePlace(Setup setup, String dragged) {
        // Ensure only field players can be placed in sub spots
        if (!setup.getFieldPlayers().contains(dragged)) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Speler niet op het veld");
            alert.setHeaderText(null);
            alert.setContentText("Een speler moet eerst op het veld staan voordat die gewisseld kan worden.");
            alert.showAndWait();

            return false;
        }

        if (setup.getSubs().contains(dragged)) {
            resetExistingPlace(setup, dragged);
        }
        setup.replaceSubPlace(this.getText(), dragged);
        this.setText(dragged);
        setup.updatePlaceMap(this);
        return true;
    }

    public void resetExistingPlace(Setup setup, String dragged) {
        for (Node node : setup.getChildren()) {
            Place place = (Place) node;
            if (dragged.equals(place.getText()) && setup.getSubNames().contains(place.getMainName())) {
                //If the new place has no name, put the main name in the old place
                if (this.mainName.equals(this.getText())) {
                    place.setText(place.getMainName());
                    setup.replaceSubPlace(dragged, place.getMainName());
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
