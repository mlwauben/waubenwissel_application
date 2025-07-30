package com.waubenwissel.GUIobjects;

import java.time.LocalDate;
import java.util.ArrayList;

public class Game {
    Setup[] setups;
    LocalDate date = LocalDate.of(2025, 1, 1);
    ArrayList<Player> players;
    String opponent;
    String coach;

    public Game() {
    }

    public Setup[] getSetups() {
        return setups;
    }

    public void setSetups(Setup[] setups) {
        this.setups = setups;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    

    
    
}