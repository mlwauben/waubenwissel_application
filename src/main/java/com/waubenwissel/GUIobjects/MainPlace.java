package com.waubenwissel.GUIobjects;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;

public class MainPlace extends Place {

    public MainPlace(String mainName, String name) {
        super(mainName, name);
        setTooltip(new Tooltip("Rondes in het veld: 1"));
    }

    public boolean handlePlace(Setup setup, String dragged) {
        if (setup.getTeam().contains(dragged)) {
            resetExistingPlace(setup, dragged);
        }
        setup.replaceMainPlace(this.getText(), dragged);
        if (setup.getFieldPlayers().contains(this.mainName)) {
            setup.replaceFieldPlayer(this.getText(), dragged);
        }
        this.setText(dragged);
        setup.updatePlaceMap(this);
        return true;
    }

    public void resetExistingPlace(Setup setup, String dragged) {
        for (Node node : setup.getChildren()) {
            Place place = (Place) node;
            if (dragged.equals(place.getText()) && setup.getMainNames().contains(place.getMainName())) {
                //If the new place has no name, put the main name in the old place
                if (this.mainName.equals(this.getText())) {
                    place.setText(place.getMainName());
                    setup.replaceMainPlace(dragged, place.getMainName());
                //Else if the new place has a name, switch the names around
                } else {
                    place.setText(this.getText());
                }
                setup.updatePlaceMap(place);
                break;
            }
        }
    }

    public void setHoverValue(int hoverValue) {
        getTooltip().setText("Rondes in het veld: " + hoverValue);
    }

    public int getHoverValue() {
        String tooltext = getTooltip().getText();
        return Integer.parseInt(tooltext.substring(tooltext.length() - 1));
    } 
}
