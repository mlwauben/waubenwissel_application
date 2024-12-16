package com.waubenwissel.GUIobjects;

import javafx.scene.layout.FlowPane;

public class Setup extends FlowPane {
    
    public Setup() {
        String[] places = {"LinksVoor", "Spits", "RechtsVoor", "LinksMidden", "MidMid", "RechtsMidden", "LinksAchter", "Voorstopper", 
            "RechtsAchter", "MidAchter", "Keeper"};
        for (String place : places) {
            getChildren().add(new Place(place));
        }
        String[] benchPlaces = {"BANK1", "BANK2", "BANK3", "BANK4", "BANK5"};
        for (String benchPlace : benchPlaces) {
            getChildren().add(new Place(benchPlace));
        }
        String[] subs = {"WISSEL1", "WISSEL2", "WISSEL3", "WISSEL4", "WISSEL5"};
        for (String sub : subs) {
            getChildren().add(new Place(sub));
        }
    }
}
