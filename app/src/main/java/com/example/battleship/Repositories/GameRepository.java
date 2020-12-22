package com.example.battleship.Repositories;

import android.annotation.SuppressLint;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.battleship.Models.Cell;
import com.example.battleship.Models.Ship;
import com.example.battleship.Models.Statistic;
import com.example.battleship.Utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameRepository {
    DatabaseReference statisticsDatabaseReference;
    DatabaseReference gameDatabaseReference;
    ValueEventListener matrixValueEventListener;
    ValueEventListener shipsValueEventListener;
    ValueEventListener hostStepValueEventListener;
    ValueEventListener gameStateListener;
    ValueEventListener readinessListener;

    public GameRepository(String gameId){
        this.gameDatabaseReference = FirebaseDatabase.getInstance().getReference("games").child(gameId);
        this.statisticsDatabaseReference = FirebaseDatabase.getInstance().getReference("statistics").child(gameId);
    }

    public void ObserveOpponentShips(String shipsPath, FirebaseCallback firebaseCallback){
        shipsValueEventListener = gameDatabaseReference.child(shipsPath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null ){
                    firebaseCallback.onSuccess(snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void ObserveStep(FirebaseCallback firebaseCallback){
        hostStepValueEventListener = gameDatabaseReference.child("hostStep").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firebaseCallback.onSuccess(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
    public void RemoveOpponentShipsListener() {
        this.gameDatabaseReference.removeEventListener(shipsValueEventListener);
    }
    public void FinishGame(boolean isHost) {
        gameDatabaseReference.child("hostWin").setValue(isHost);
        gameDatabaseReference.child("gameState").setValue(Constants.FINISHED_STATE);
    }
    public void SetStep(boolean isHost) {
        gameDatabaseReference.child("hostStep").setValue(!isHost);
    }
    public void UpdateOpponentMatrix(String opponentMatrixPath, List<List<Cell>>  matrixList) {
        gameDatabaseReference.child(opponentMatrixPath).setValue(matrixList);
    }
    public void SaveResult(Statistic statistic) {
        statisticsDatabaseReference.setValue(statistic);
    }
    public void ObserveReadiness(String path, FirebaseCallback firebaseCallback) {
        readinessListener = gameDatabaseReference.child(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firebaseCallback.onSuccess(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void SetReadiness(String path, boolean isReady) {
        gameDatabaseReference.child(path).setValue(isReady);
    }
    public void ObserveGameState(FirebaseCallback firebaseCallback) {
        gameStateListener = gameDatabaseReference.child("gameState").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firebaseCallback.onSuccess(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void StartGame(boolean isHost, List<Ship> ships, List<List<Cell>> matrix) {
        String matrixReference;
        String shipsReference;
        if (isHost){
            matrixReference = "hostMatrix";
            shipsReference = "hostShips";
        }
        else {
            matrixReference = "connectedMatrix";
            shipsReference = "connectedShips";
        }

        gameDatabaseReference.removeEventListener(gameStateListener);
        gameDatabaseReference.removeEventListener(readinessListener);

        gameDatabaseReference.child(matrixReference).setValue(matrix);

        gameDatabaseReference.child(shipsReference).setValue(ships);
        gameDatabaseReference.child("hostStep").setValue(true);
    }
    public void SetGameStep(String gameState) {
        gameDatabaseReference.child("gameState").setValue(gameState);
    }
}
