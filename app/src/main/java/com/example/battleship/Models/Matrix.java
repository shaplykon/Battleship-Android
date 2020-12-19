package com.example.battleship.Models;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.battleship.Utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Matrix implements Serializable {
    private final int[] array = {4, 3, 3, 2, 2, 2, 1, 1, 1,1};
    public Cell[][] matrix;

    public Matrix() {
        this.matrix = new Cell[10][10];

        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 10; column++) {
                this.matrix[row][column] = new Cell();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Matrix(List<List<HashMap<Object, Object>>> fieldList){
        this.matrix = new Cell[10][10];

        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 10; column++) {
                this.matrix[row][column] = new Cell((boolean)fieldList.get(row).get(column).getOrDefault("isHead", false),
                        ((Long) fieldList.get(row).get(column).getOrDefault("type", 0)).intValue());

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
        if (rotation == Constants.HORIZONTAL) {
            for (int k = 0; k < length; k++) {
                try {
                    if (matrix[pointY][pointX + k].type != Constants.EMPTY_CELL) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }

            }

            for (int k = 0; k < length; k++) {
                matrix[pointY][pointX + k].type = Constants.SHIP_CELL;
                try {
                    if (k == 0) {
                        try {
                            matrix[pointY][pointX + k].type = Constants.SHIP_CELL;
                            matrix[pointY][pointX + k].isHead = true;
                            matrix[pointY][pointX + k].rotation = rotation;
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY - 1][pointX + k - 1].type = Constants.NEARBY_CELL;
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY][pointX + k - 1].type = Constants.NEARBY_CELL;
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY + 1][pointX + k - 1].type = Constants.NEARBY_CELL;
                        } catch (Exception ignored) {
                        }
                    }
                    try {
                        matrix[pointY + 1][pointX + k].type = Constants.NEARBY_CELL;
                    } catch (Exception ignored) {
                    }

                    try {
                        matrix[pointY - 1][pointX + k].type = Constants.NEARBY_CELL;
                    } catch (Exception ignored) {
                    }

                    if (k == length - 1) {
                        try {
                            matrix[pointY - 1][pointX + k + 1].type = Constants.NEARBY_CELL;
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY][pointX + k + 1].type = Constants.NEARBY_CELL;
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY + 1][pointX + k + 1].type = Constants.NEARBY_CELL;
                        } catch (Exception ignored) {
                        }

                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
            }

        }
        if (rotation == Constants.VERTICAL){
            for (int k = 0; k < length; k++) {
                try {
                    if (matrix[pointY + k][pointX].type != Constants.EMPTY_CELL) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }

            }

            for (int k = 0; k < length; k++) {
                matrix[pointY + k][pointX].type = Constants.SHIP_CELL;
                try {
                    if (k == 0) {
                        matrix[pointY][pointX].type = Constants.SHIP_CELL;
                        matrix[pointY][pointX].isHead = true;
                        matrix[pointY][pointX].rotation = rotation;
                        try {
                            matrix[pointY - 1][pointX - 1].type = Constants.NEARBY_CELL;
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY - 1][pointX].type = Constants.NEARBY_CELL;
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY - 1][pointX + 1].type = Constants.NEARBY_CELL;
                        } catch (Exception ignored) {
                        }
                    }


                    try {
                        matrix[pointY + k][pointX -1].type = Constants.NEARBY_CELL;
                    } catch (Exception ignored) {
                    }

                    try {
                        matrix[pointY + k][pointX + 1].type = Constants.NEARBY_CELL;
                    } catch (Exception ignored) {
                    }

                    if (k == length - 1) {
                        try {
                            matrix[pointY + k + 1][pointX - 1].type = Constants.NEARBY_CELL;
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY + k + 1][pointX].type = Constants.NEARBY_CELL;
                        } catch (Exception ignored) {
                        }

                        try {
                            matrix[pointY + k + 1][pointX + 1].type = Constants.NEARBY_CELL;
                        } catch (Exception ignored) {
                        }

                    }
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
        }
        return true;
    }
}
