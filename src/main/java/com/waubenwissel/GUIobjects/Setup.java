package com.waubenwissel.GUIobjects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javafx.scene.layout.FlowPane;

public class Setup extends FlowPane {

    //Actual text of the places
    private Set<String> fieldPlayers = new HashSet<>();
    private Set<String> team = new HashSet<>();
    private Set<String> subs = new HashSet<>();
    private Set<String> corners = new HashSet<>();

    //Placeholder text of the places
    private Set<String> mainNames = new HashSet<>();
    private Set<String> subNames = new HashSet<>();
    private Set<String> cornerNames = new HashSet<>();

    //Connection between main names and their places
    private Map<String, Place> placeMap = new HashMap<>();

    private Setup nextSetup;
    
    public Setup(Setup nextSetup) {
        this.nextSetup = nextSetup;
        String[] places = {"LinksVoor", "Spits", "RechtsVoor", "LinksMidden", "MidMid", "RechtsMidden", "LinksAchter", "Voorstopper", 
            "RechtsAchter", "MidAchter", "Keeper"};
        for (String place : places) {
            MainPlace p = new MainPlace(place, place);
            team.add(p.getText());
            mainNames.add(p.getText());
            fieldPlayers.add(p.getText());
            getChildren().add(p);
            placeMap.put(place, p);
        }
        String[] benchPlaces = {"BANK1", "BANK2", "BANK3", "BANK4", "BANK5"};
        for (String benchPlace : benchPlaces) {
            MainPlace p = new MainPlace(benchPlace, benchPlace);
            team.add(p.getText());
            mainNames.add(p.getText());
            getChildren().add(p);
            placeMap.put(benchPlace, p);
        }
        String[] subsStrings = {"WISSEL1", "WISSEL2", "WISSEL3", "WISSEL4", "WISSEL5"};
        for (String sub : subsStrings) {
            SubPlace p = new SubPlace(sub, sub);
            subs.add(p.getText());
            subNames.add(p.getText());
            getChildren().add(p);
            placeMap.put(sub, p);
        }
        String[] penaltyCorner = {"aangeef", "kop", "afschuif"};
        for (String corner : penaltyCorner) {
            CornerPlace p = new CornerPlace(corner, corner);
            corners.add(p.getText());
            cornerNames.add(p.getText());
            getChildren().add(p);
        }
    }

    public Set<String> getTeam() {
        return team;
    }

    public Set<String> getSubs() {
        return subs;
    }

    public Set<String> getCorners() {
        return corners;
    }

    public Set<String> getMainNames() {
        return mainNames;
    }

    public Set<String> getFieldPlayers() {
        return fieldPlayers;
    }

    public Set<String> getSubNames() {
        return subNames;
    }
    
    public Set<String> getCornerNames() {
        return cornerNames;
    }

    public void replaceMainPlace(String oldPlace, String newPlace) {
        if (!(team.contains(oldPlace) && team.contains(newPlace))) {
            team.remove(oldPlace);
            team.add(newPlace);
        }
    }

    public void replaceFieldPlayer(String oldPlace, String newPlace) {
        if (!(fieldPlayers.contains(oldPlace) && fieldPlayers.contains(newPlace))) {
            fieldPlayers.remove(oldPlace);
            fieldPlayers.add(newPlace);
        }
    }

    public void replaceSubPlace(String oldPlace, String newPlace) {
        if (!(subs.contains(oldPlace) && subs.contains(newPlace))) {
            subs.remove(oldPlace);
            subs.add(newPlace);
        }
    }

    public void replaceCornerPlace(String oldPlace, String newPlace) {
        if (!(corners.contains(oldPlace) && corners.contains(newPlace))) {
            corners.remove(oldPlace);
            corners.add(newPlace);
        }
    }

    public void setNextSetup(Setup nextSetup) {
        this.nextSetup = nextSetup;
    }

    public void updatePlaceMap(Place p) {
        placeMap.put(p.getMainName(), p);
    }

    public Place getPlaceByName(String name) {
        return placeMap.get(name);
    }

    public void checkAndAdvanceSetup() {
        boolean allFilled = getChildren().stream()
            .filter(node -> node instanceof Place)
            .map(node -> (Place) node)
            .allMatch(Place::isFilled);

        if (allFilled) {
            updateNextSetup();
        }
    }

    public void updateNextSetup() {
        System.out.println("next setup filled in");
        if (nextSetup == null) {
            return;
        }

        // Step 1: Create a mapping for the next setup
        Map<String, String> newAssignments = new HashMap<>();

        // Step 2: Copy current field players to the next setup
        for (String fieldPlace : mainNames) {
            newAssignments.put(fieldPlace, placeMap.get(fieldPlace).getText());
        }

        // Step 3: Handle substitutions
        String[] subs = {"WISSEL1", "WISSEL2", "WISSEL3", "WISSEL4", "WISSEL5"};
        String[] benchPlaces = {"BANK1", "BANK2", "BANK3", "BANK4", "BANK5"};

        for (int i = 0; i < subs.length; i++) {
            String subSpot = subs[i];
            String benchSpot = benchPlaces[i];

            Place subPlace = placeMap.get(subSpot);
            Place benchPlace = placeMap.get(benchSpot);
            Place subInField = null;
            for (Entry<String, Place> e : placeMap.entrySet()) {
                if (getMainNames().contains(e.getKey())) {
                    if (e.getValue().getText() == subPlace.getText()) {
                        subInField = e.getValue();
                        break;
                    }
                }
            }

            if (subPlace != null && benchPlace != null) {
                String subPlayer = subPlace.getText();
                String benchPlayer = benchPlace.getText();

                // Only swap if a sub has been chosen
                if (!subPlayer.equals(subSpot)) {  
                    newAssignments.put(benchSpot, subPlayer); // Sub moves to bench
                    newAssignments.put(subSpot, subSpot);     // Reset sub spot
                    newAssignments.put(subInField.getMainName(), benchPlayer); // Bench moves to field
                }
            }
        }

        // Step 4: Apply assignments to the next setup
        for (Map.Entry<String, String> entry : newAssignments.entrySet()) {
            Place nextPlace = nextSetup.getPlaceByName(entry.getKey());
            if (nextPlace != null) {
                nextPlace.setText(entry.getValue());
            }
        }
    }
}
