package com.example.battleship.Models;
import com.example.battleship.Utils.Constants;
import java.util.Random;

public class Matrix {
    private final int[] array = {4, 3, 3, 2, 2, 2, 1, 1, 1,1};
    public Cell[][] matrix;

    public Matrix() {
        matrix = new Cell[10][10];
    }

    public void GenerateMatrix() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = new Cell();
            }
        }

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
