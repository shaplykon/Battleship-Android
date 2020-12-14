package com.example.battleship.Models;

import com.example.battleship.Utils.Constants;

public class Cell {
    int rotation; // horizontal or vertical
    public int type;
    public boolean isHead;
    public Cell(){
        this.type = Constants.EMPTY_CELL;
    }

}
