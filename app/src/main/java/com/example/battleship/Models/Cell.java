package com.example.battleship.Models;

import com.example.battleship.Utils.Constants;

import java.io.Serializable;

public class Cell implements Serializable {
    private int rotation; // horizontal or vertical
    private int type;

    private int xCoordinate;
    private int yCoordinate;

    private boolean isHead;

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean head) {
        isHead = head;
    }

    public Cell(){
        this.type = Constants.EMPTY_CELL;
    }
    public Cell(boolean isHead, int type){
        this.type = type;
        this.isHead = isHead;
    }


}
