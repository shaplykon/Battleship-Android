package com.example.battleship.Models;

import com.example.battleship.Utils.Constants;

import java.io.Serializable;

public class Game implements Serializable {
    private User hostUser;
    private User connectedUser;
    private Matrix playerMatrix;
    private Matrix opponentMatrix;
    private String gameState;
    private String gameId;
    private boolean hostReady;
    private boolean guestReady;

    public Game(User hostUser, User connectedUser, Matrix hostMatrix, Matrix connectedMatrix,
                String gameState, String gameId, boolean hostReady, boolean guestReady) {
        this.hostUser = hostUser;
        this.connectedUser = connectedUser;
        this.playerMatrix = hostMatrix;
        this.opponentMatrix = connectedMatrix;
        this.gameState = gameState;
        this.gameId = gameId;
        this.hostReady = hostReady;
        this.guestReady = guestReady;
    }


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

    public Matrix getPlayerMatrix(){
        return playerMatrix;
    }

    public Matrix getOpponentMatrix()
    {
        return  opponentMatrix;
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


