package com.example.battleship.Models;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {
    private User hostUser;
    private User connectedUser;
    private Matrix hostMatrix;
    private Matrix connectedMatrix;
    private String gameId;

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

    private boolean hostReady;
    private boolean guestReady;

    public  Game(){

    }


    public Game(User hostUser, String gameId){
        this.hostUser = hostUser;
        this.gameId = gameId;
        this.hostReady = false;
        this.guestReady = false;
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


