package com.example.battleship.Models;

import com.example.battleship.Utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Matrix implements Serializable {
    private final int[] array = {4, 3, 3, 2, 2, 2, 1, 1, 1,1};
    public Cell[][] matrix;
    public List<Ship> ships;
    public Matrix() {
        this.matrix = new Cell[10][10];
        this.ships = new ArrayList<>();
        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 10; column++) {
                this.matrix[row][column] = new Cell();
            }
        }
    }
    public List<List<Cell>> GetList(){
        List<List<Cell>> matrixList = new ArrayList<>();
        for(int row = 0; row < 10; row++){
            ArrayList<Cell> listRow = new ArrayList<>(Arrays.asList(matrix[row]).subList(0, 10));
            matrixList.add(listRow);
        }
        return  matrixList;
    }
    public void GenerateMatrix() {
        for (int i = 0; i < array.length; ) {
            Random random = new Random();
            int pointX = random.nextInt(10);
            int pointY = random.nextInt(10);
            int rotation = random.nextInt(Constants.VERTICAL - Constants.HORIZONTAL +1) + Constants.HORIZONTAL;

            if(CheckCell(pointX, pointY, rotation, array[i])){
                i++;
            }
        }
    }
    private boolean CheckCell(int pointX, int pointY, int rotation, int length) {
        List<Cell> shipCells = new ArrayList<>();
        List<Cell> surroundCells = new ArrayList<>();
        if (rotation == Constants.HORIZONTAL) {
            for (int k = 0; k < length; k++) {
                try {
                    if (matrix[pointY][pointX + k].getType() != Constants.EMPTY_CELL) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }

            }

            for (int k = 0; k < length; k++) {
                matrix[pointY][pointX + k].setType( Constants.SHIP_CELL);
                matrix[pointY][pointX + k].setXCoordinate(pointX + k);
                matrix[pointY][pointX + k].setYCoordinate(pointY);
                shipCells.add(matrix[pointY][pointX + k]);
                try {
                    if (k == 0) {
                        try {
                            matrix[pointY][pointX + k].setType(Constants.SHIP_CELL);
                            matrix[pointY][pointX + k].setHead(true);
                            matrix[pointY][pointX + k].setRotation(rotation);
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY - 1][pointX + k - 1].setType(Constants.NEARBY_CELL);
                            matrix[pointY - 1][pointX + k - 1].setYCoordinate(pointY - 1);
                            matrix[pointY - 1][pointX + k - 1].setXCoordinate(pointX + k - 1);
                            surroundCells.add(matrix[pointY - 1][pointX + k - 1]);
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY][pointX + k - 1].setType(Constants.NEARBY_CELL);
                            matrix[pointY][pointX + k - 1].setXCoordinate(pointX + k - 1);
                            matrix[pointY][pointX + k - 1].setYCoordinate(pointY);
                            surroundCells.add(matrix[pointY][pointX + k - 1]);
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY + 1][pointX + k - 1].setType(Constants.NEARBY_CELL);
                            matrix[pointY + 1][pointX + k - 1].setXCoordinate(pointX + k - 1);
                            matrix[pointY + 1][pointX + k - 1].setYCoordinate(pointY + 1);
                            surroundCells.add(matrix[pointY + 1][pointX + k - 1]);
                        } catch (Exception ignored) {
                        }
                    }
                    try {
                        matrix[pointY + 1][pointX + k].setType(Constants.NEARBY_CELL);
                        matrix[pointY + 1][pointX + k].setXCoordinate(pointX + k);
                        matrix[pointY + 1][pointX + k].setYCoordinate(pointY + 1);
                        surroundCells.add(matrix[pointY + 1][pointX + k]);
                    } catch (Exception ignored) {
                    }

                    try {
                        matrix[pointY - 1][pointX + k].setType( Constants.NEARBY_CELL);
                        matrix[pointY - 1][pointX + k].setXCoordinate(pointX + k);
                        matrix[pointY - 1][pointX + k].setYCoordinate(pointY - 1);
                        surroundCells.add(matrix[pointY - 1][pointX + k]);
                    } catch (Exception ignored) {
                    }

                    if (k == length - 1) {
                        try {
                            matrix[pointY - 1][pointX + k + 1].setType( Constants.NEARBY_CELL);
                            matrix[pointY - 1][pointX + k + 1].setXCoordinate(pointX + k + 1);
                            matrix[pointY - 1][pointX + k + 1].setYCoordinate(pointY - 1);
                            surroundCells.add(matrix[pointY - 1][pointX + k + 1]);
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY][pointX + k + 1].setType(Constants.NEARBY_CELL);
                            matrix[pointY][pointX + k + 1].setXCoordinate(pointX + k + 1);
                            matrix[pointY][pointX + k + 1].setYCoordinate(pointY);
                            surroundCells.add(matrix[pointY][pointX + k + 1]);
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY + 1][pointX + k + 1].setType( Constants.NEARBY_CELL);
                            matrix[pointY + 1][pointX + k + 1].setXCoordinate(pointX + k + 1);
                            matrix[pointY + 1][pointX + k + 1].setYCoordinate(pointY + 1);
                            surroundCells.add(matrix[pointY + 1][pointX + k + 1]);
                        } catch (Exception ignored) {
                        }

                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
            this.ships.add(new Ship(shipCells, surroundCells));

        }
        if (rotation == Constants.VERTICAL){
            for (int k = 0; k < length; k++) {
                try {
                    if (matrix[pointY + k][pointX].getType() != Constants.EMPTY_CELL) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }

            }

            for (int k = 0; k < length; k++) {
                matrix[pointY + k][pointX].setType(Constants.SHIP_CELL);
                matrix[pointY + k][pointX].setYCoordinate(pointY + k);
                matrix[pointY + k][pointX].setXCoordinate(pointX);
                shipCells.add(matrix[pointY + k][pointX]);
                try {
                    if (k == 0) {
                        matrix[pointY][pointX].setType( Constants.SHIP_CELL);
                        matrix[pointY][pointX].setHead(true);
                        matrix[pointY][pointX].setRotation(rotation);
                        try {
                            matrix[pointY - 1][pointX - 1].setType(Constants.NEARBY_CELL);
                            matrix[pointY - 1][pointX - 1].setYCoordinate(pointY - 1);
                            matrix[pointY - 1][pointX - 1].setXCoordinate(pointX - 1);
                            surroundCells.add(matrix[pointY - 1][pointX - 1]);
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY - 1][pointX].setType(Constants.NEARBY_CELL);
                            matrix[pointY - 1][pointX].setYCoordinate(pointY - 1);
                            matrix[pointY - 1][pointX].setXCoordinate(pointX);
                            surroundCells.add(matrix[pointY - 1][pointX]);
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY - 1][pointX + 1].setType(Constants.NEARBY_CELL);
                            matrix[pointY - 1][pointX + 1].setYCoordinate(pointY - 1);
                            matrix[pointY - 1][pointX + 1].setXCoordinate(pointX + 1);
                            surroundCells.add(matrix[pointY - 1][pointX + 1]);
                        } catch (Exception ignored) {
                        }
                    }


                    try {
                        matrix[pointY + k][pointX -1].setType(Constants.NEARBY_CELL);
                        matrix[pointY + k][pointX -1].setYCoordinate(pointY + k);
                        matrix[pointY + k][pointX -1].setXCoordinate(pointX - 1);
                        surroundCells.add(matrix[pointY + k][pointX - 1]);
                    } catch (Exception ignored) {
                    }

                    try {
                        matrix[pointY + k][pointX + 1].setType(Constants.NEARBY_CELL);
                        matrix[pointY + k][pointX + 1].setYCoordinate(pointY + k);
                        matrix[pointY + k][pointX + 1].setXCoordinate(pointX + 1);
                        surroundCells.add(matrix[pointY + k][pointX + 1]);
                    } catch (Exception ignored) {
                    }

                    if (k == length - 1) {
                        try {
                            matrix[pointY + k + 1][pointX - 1].setType(Constants.NEARBY_CELL);
                            matrix[pointY + k + 1][pointX - 1].setYCoordinate(pointY + k + 1);
                            matrix[pointY + k + 1][pointX - 1].setXCoordinate(pointX - 1);
                            surroundCells.add( matrix[pointY + k + 1][pointX - 1]);
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY + k + 1][pointX].setType(Constants.NEARBY_CELL);
                            matrix[pointY + k + 1][pointX].setYCoordinate(pointY + k + 1);
                            matrix[pointY + k + 1][pointX].setXCoordinate(pointX);
                            surroundCells.add(matrix[pointY + k + 1][pointX]);
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY + k + 1][pointX + 1].setType(Constants.NEARBY_CELL);
                            matrix[pointY + k + 1][pointX + 1].setYCoordinate(pointY + k + 1);
                            matrix[pointY + k + 1][pointX + 1].setXCoordinate(pointX + 1);
                            surroundCells.add(matrix[pointY + k + 1][pointX + 1]);
                        } catch (Exception ignored) {
                        }

                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
            this.ships.add(new Ship(shipCells, surroundCells));
        }
        return true;
    }
    public void CheckShip(int row, int column) {
        for(Ship ship:ships){
            for(Cell cell:ship.shipCells){
                if(cell.getYCoordinate() == row && cell.getXCoordinate() == column){
                    ship.cellsRemain--;
                    if (ship.cellsRemain == 0){
                        SurroundShip(ship);
                        return;
                    }
                }
            }
        }
    }
    private void SurroundShip(Ship ship) {
        for(Cell cell:ship.surroundCells){
            this.matrix[cell.getYCoordinate()][cell.getXCoordinate()].setType(Constants.CHECKED_CELL);
        }
    }
}
