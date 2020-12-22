package com.example.battleship.Models;

import java.io.Serializable;

public class Statistic implements Serializable {
    User winner;
    User loser;
    String date;
    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Statistic(User winner, User loser, String date, String time){
        this.winner = winner;
        this.loser = loser;
        this.date = date;
        this.time = time;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public User getLoser() {
        return loser;
    }

    public void setLoser(User loser) {
        this.loser = loser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
