package com.example.battleship.Models;

public class Matrix {
    public int[][] matrix;

    public Matrix() {
        matrix = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = 1;
            }
        }
    }

}
