package com.example.battleship.Models;

import com.example.battleship.Utils.Constants;

public class Cell {
//  int x; //X coordinate of ship start point
//  int y; //Y coordinate of ship start point
    int rotation; // horizontal or vertical
    public int type;
    public boolean isHead;
    public Cell(){
        this.type = Constants.EMPTY_CELL;
    }
}
