package com.waubenwissel.GUIobjects;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.layout.FlowPane;

public class Setup extends FlowPane {

    private Set<String> team = new HashSet<>();
    private Set<String> mainNames = new HashSet<>();
    
    public Setup() {
        String[] places = {"LinksVoor", "Spits", "RechtsVoor", "LinksMidden", "MidMid", "RechtsMidden", "LinksAchter", "Voorstopper", 
            "RechtsAchter", "MidAchter", "Keeper"};
        for (String place : places) {
            Place p = new Place(place, place);
            team.add(p.getText());
            mainNames.add(p.getText());
            getChildren().add(p);
        }
        String[] benchPlaces = {"BANK1", "BANK2", "BANK3", "BANK4", "BANK5"};
        for (String benchPlace : benchPlaces) {
            Place p = new Place(benchPlace, benchPlace);
            team.add(p.getText());
            mainNames.add(p.getText());
            getChildren().add(p);
        }
        String[] subs = {"WISSEL1", "WISSEL2", "WISSEL3", "WISSEL4", "WISSEL5"};
        for (String sub : subs) {
            getChildren().add(new Place(sub, sub));
        }
        String[] penaltyCorner = {"aangeef", "kop", "afschuif"};
        for (String corner : penaltyCorner) {
            getChildren().add(new Place(corner, corner));
        }
    }

    public Set<String> getTeam() {
        return team;
    }

    public Set<String> getMainNames() {
        return mainNames;
    }

    public void replacePlace(String oldPlace, String newPlace) {
        if (!(team.contains(oldPlace) && team.contains(newPlace))) {
            team.remove(oldPlace);
            team.add(newPlace);
        }
    }
}
