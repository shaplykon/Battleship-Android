package com.example.battleship.Repositories;

import com.example.battleship.Models.Cell;

public interface FirebaseMatrixCallback{
    void onSuccess(Cell[][] matrix);
}
