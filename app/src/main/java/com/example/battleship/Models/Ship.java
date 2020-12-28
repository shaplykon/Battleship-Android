package com.example.battleship.Models;

import java.io.Serializable;
import java.util.List;

public class Ship implements Serializable {
    List<Cell> shipCells;
    List<Cell> surroundCells;
    int cellsRemain;

    public List<Cell> getShipCells() {
        return shipCells;
    }

    public void setShipCells(List<Cell> shipCells) {
        this.shipCells = shipCells;
    }

    public List<Cell> getSurroundCells() {
        return surroundCells;
    }

    public void setSurroundCells(List<Cell> surroundCells) {
        this.surroundCells = surroundCells;
    }

    public int getCellsRemain() {
        return cellsRemain;
    }

    public void setCellsRemain(int cellsRemain) {
        this.cellsRemain = cellsRemain;
    }

    public Ship(){

    }

    public Ship(List<Cell> shipCells, List<Cell> surroundCells){
        this.shipCells = shipCells;
        this.surroundCells = surroundCells;
        this.cellsRemain = shipCells.size();
    }

}
