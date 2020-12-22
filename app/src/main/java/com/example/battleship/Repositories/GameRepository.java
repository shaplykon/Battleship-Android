package com.example.battleship.Repositories;

import android.annotation.SuppressLint;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.battleship.Models.Cell;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class GameRepository {
    DatabaseReference statisticsDatabaseReference;
    DatabaseReference gameDatabaseReference;
    ValueEventListener matrixValueEventListener;
    public GameRepository(String gameId){
        this.gameDatabaseReference = FirebaseDatabase.getInstance().getReference("games").child(gameId);
        this.statisticsDatabaseReference = FirebaseDatabase.getInstance().getReference("statistics").child(gameId);
    }

    public void ObserveWin(FirebaseCallback firebaseCallback){
        gameDatabaseReference.child("hostWin").addValueEventListener(new ValueEventListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean hostWin = (Boolean) snapshot.getValue();
                if (hostWin != null) {
                   firebaseCallback.onSuccess(snapshot);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void ObserveMatrix(String matrixPath, FirebaseMatrixCallback firebaseCallback){
        matrixValueEventListener = gameDatabaseReference.child(matrixPath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Cell[][] matrix = new Cell[10][10];
                    int row = 0;
                    int column = 0;
                    for (DataSnapshot rowSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot columnSnapshot : rowSnapshot.getChildren()) {
                            matrix[row][column]  = columnSnapshot.getValue(Cell.class);
                            column++;
                        }
                        row++;
                        column = 0;
                    }
                    firebaseCallback.onSuccess(matrix);
                    //gameDatabaseReference.child(opponentMatrixPath).removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void RemoveOpponentMatrixListener() {
        this.gameDatabaseReference.removeEventListener(matrixValueEventListener);
    }
}
