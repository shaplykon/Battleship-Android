package com.example.battleship.Models;

import com.example.battleship.Utils.Constants;

import java.io.Serializable;

public class Cell implements Serializable {
    int rotation; // horizontal or vertical
    public int type;
    public boolean isHead;
    public Cell(){
        this.type = Constants.EMPTY_CELL;
    }
    public Cell(boolean isHead, int type){
        this.type = type;
        this.isHead = isHead;
    }


}
