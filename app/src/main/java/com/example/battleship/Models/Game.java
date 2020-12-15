package com.example.battleship.Models;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private User hostUser;
    private User connectedUser;

    private Cell[][] hostMatrix;
    private Cell[][] connectedMatrix;
    private String gameId;

    public  Game(){

    }

    public Game(User hostUser, String gameId){
        this.hostUser = hostUser;
        this.gameId = gameId;
    }

    public User getHostUser(){
        return hostUser;
    }

    public User getConnectedUser(){
        return connectedUser;
    }

    public String getGameId(){
        return gameId;
    }

    public void setConnectedUser(User connectedUser){
        this.connectedUser = connectedUser;
    }

}


