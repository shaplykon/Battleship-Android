package com.example.battleship.Models;

import com.example.battleship.Utils.Constants;

import java.io.Serializable;

public class Game implements Serializable {
    private User hostUser;
    private User connectedUser;
    private Matrix hostMatrix;
    private Matrix connectedMatrix;
    private String gameState;
    private String gameId;
    private boolean hostReady;
    private boolean guestReady;

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public boolean isHostReady() {
        return hostReady;
    }

    public void setHostReady(boolean hostReady) {
        this.hostReady = hostReady;
    }

    public boolean isGuestReady() {
        return guestReady;
    }

    public void setGuestReady(boolean guestReady) {
        this.guestReady = guestReady;
    }

    public  Game(){

    }

    public Game(User hostUser, String gameId){
        this.hostUser = hostUser;
        this.gameId = gameId;
        this.hostReady = false;
        this.guestReady = false;
        this.gameState = Constants.WAITING_STATE;
    }

    public Matrix getHostMatrix(){
        return hostMatrix;
    }

    public Matrix getConnectedMatrix()
    {
        return  connectedMatrix;
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


