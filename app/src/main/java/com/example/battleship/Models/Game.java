package com.example.battleship.Models;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public User hostUser;
    public User connectedUser;

    public List<ArrayList<Integer>> secondPlayerField;
    public String gameId;

    public  Game(){

    }

    public Game(User hostUser, String gameId){
        this.hostUser = hostUser;
        this.gameId = gameId;
        //this.firstPlayerField = new int[10][10];
        //this.secondPlayerField = new int[10][10];
    }
}


