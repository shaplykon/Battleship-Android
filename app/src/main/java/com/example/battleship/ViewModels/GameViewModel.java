package com.example.battleship.ViewModels;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.battleship.Models.Cell;
import com.example.battleship.Models.Game;
import com.example.battleship.Models.Matrix;
import com.example.battleship.Models.Ship;
import com.example.battleship.Models.Statistic;
import com.example.battleship.Models.User;
import com.example.battleship.Repositories.FirebaseCallback;
import com.example.battleship.Repositories.GameRepository;
import com.example.battleship.Utils.Constants;
import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class GameViewModel extends ViewModel {
    public MutableLiveData<User> hostUser;
    public MutableLiveData<User> guestUser;
    public MutableLiveData<Matrix> playerMatrix;
    public MutableLiveData<Matrix> opponentMatrix;
    public MutableLiveData<Boolean> hostIsReady;
    public MutableLiveData<Boolean> guestIsReady;
    public MutableLiveData<String> gameId;
    public MutableLiveData<String> gameState;
    public MutableLiveData<Boolean> hostStep;
    public MutableLiveData<Integer> hitsToWin;
    public MutableLiveData<Boolean> hostWin;
    private GameRepository gameRepository;

    public GameViewModel(Game game) {
        this.hostUser = new MutableLiveData<>();
        this.guestUser = new MutableLiveData<>();
        this.hostIsReady = new MutableLiveData<>();
        this.guestIsReady = new MutableLiveData<>();
        this.gameId = new MutableLiveData<>();
        this.gameState = new MutableLiveData<>();
        this.opponentMatrix = new MutableLiveData<>();
        this.playerMatrix = new MutableLiveData<>();
        this.hostStep = new MutableLiveData<>();
        this.hitsToWin = new MutableLiveData<>();
        this.hostWin = new MutableLiveData<>();

        this.playerMatrix.setValue(game.getPlayerMatrix());
        this.opponentMatrix.setValue(game.getOpponentMatrix());
        this.gameState.setValue(game.getGameState());
        this.gameId.setValue(game.getGameId());
        this.hostUser.setValue(game.getHostUser());
        this.guestUser.setValue(game.getConnectedUser());
        this.hitsToWin.setValue(20);
        this.hostIsReady.setValue(false);
        this.guestIsReady.setValue(false);
        this.hostStep.setValue(true);
        gameRepository = new GameRepository(gameId.getValue());
    }

    public void ObserveOpponentMatrix(String opponentMatrixPath){
        GameRepository gameRepository = new GameRepository(this.gameId.getValue());
        gameRepository.ObserveMatrix(opponentMatrixPath, this::SetOpponentMatrix);
        gameRepository.RemoveOpponentMatrixListener();
    }

    public void SetOpponentMatrix(Cell[][] matrix) {
        Matrix bufferMatrix = this.opponentMatrix.getValue();
        bufferMatrix.matrix = matrix;
        this.opponentMatrix.setValue(bufferMatrix);
    }

    public void SetOpponentMatrix() {
        Matrix matrix = new Matrix();
        this.opponentMatrix.setValue(matrix);
    }

    public void SetPlayerMatrix(Matrix matrix) {
        this.playerMatrix.setValue(matrix);
    }

    public void SetPlayerMatrix(Cell[][] matrix) {
        Matrix bufferMatrix = this.playerMatrix.getValue();
        bufferMatrix.matrix = matrix;
        this.playerMatrix.setValue(bufferMatrix);
    }

    public Game GetGameInstance() {
        return new Game(
                hostUser.getValue(),
                guestUser.getValue(),
                playerMatrix.getValue(),
                opponentMatrix.getValue(),
                gameState.getValue(),
                gameId.getValue(),
                hostIsReady.getValue(),
                guestIsReady.getValue());
    }

    public void ObservePlayerMatrix(String playerMatrixPath) {
        GameRepository gameRepository = new GameRepository(this.gameId.getValue());
        gameRepository.ObserveMatrix(playerMatrixPath, this::SetPlayerMatrix);
    }

    public void ObserveWin(){
        GameRepository gameRepository = new GameRepository(this.gameId.getValue());
        gameRepository.ObserveWin(snapshot -> hostWin.setValue(snapshot.getValue(Boolean.class)));
    }

    public void ObserveStep(){
        GameRepository gameRepository = new GameRepository(this.gameId.getValue());
        gameRepository.ObserveStep(snapshot -> {
            boolean isHostStep = snapshot.getValue(Boolean.class);
            hostStep.setValue(isHostStep);
        });

    }

    public void ObserveOpponentShips(String opponentShipsPath) {
        GameRepository gameRepository = new GameRepository(gameId.getValue());
        gameRepository.ObserveOpponentShips(opponentShipsPath, snapshot -> {
            List<Ship> ships = new ArrayList<>();
            for(DataSnapshot dataSnapshot:snapshot.getChildren() ){
                ships.add(dataSnapshot.getValue(Ship.class));
            }
            Matrix bufferMatrix = opponentMatrix.getValue();
            bufferMatrix.ships = ships;
            opponentMatrix.setValue(bufferMatrix);
            gameRepository.RemoveOpponentShipsListener();
        });
    }

    public void FinishGame(boolean isHost){
        GameRepository gameRepository = new GameRepository(gameId.getValue());
        gameRepository.FinishGame(isHost);

    }

    public void SetStep(boolean isHost){
        GameRepository gameRepository = new GameRepository(gameId.getValue());
        gameRepository.SetStep(isHost);

    }

    public void UpdateOpponentMatrix(String opponentMatrixPath) {
        List<List<Cell>> matrixList =
                Objects.requireNonNull(opponentMatrix.getValue()).GetList();
        GameRepository gameRepository = new GameRepository(gameId.getValue());
        gameRepository.UpdateOpponentMatrix(opponentMatrixPath, matrixList);

    }

    public void SaveResult(Boolean hostWin) {
        User winner;
        User loser;
        if(hostWin){
            winner = hostUser.getValue();
            loser = guestUser.getValue();
        }
        else{
            winner = guestUser.getValue();
            loser = hostUser.getValue();
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm:ss yyyy.MM.dd ");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatForTimeNow = new SimpleDateFormat("HH:mm:ss");
        Statistic statistic = new Statistic(winner, loser, formatForDateNow.format(new Date()), formatForTimeNow.format(new Date()));
        GameRepository gameRepository = new GameRepository(gameId.getValue());
        gameRepository.SaveResult(statistic);
    }

    public void ObserveReadiness(String path) {
        gameRepository.ObserveReadiness(path, snapshot -> {
            if(path.equals("guestReady"))
                guestIsReady.setValue(snapshot.getValue(Boolean.class));
            else
                hostIsReady.setValue(snapshot.getValue(Boolean.class));
        });
    }

    public void SetReadiness(String path, boolean isReady) {
        gameRepository.SetReadiness(path, isReady);
        if(path.equals("hostReady"))
            hostIsReady.setValue(isReady);
        else
            guestIsReady.setValue(isReady);
    }

    public void ObserveGameState() {
        gameRepository.ObserveGameState(snapshot -> gameState.setValue(snapshot.getValue(String.class)));
    }

    public void StartGame(boolean isHost) {
        gameRepository.StartGame(isHost, playerMatrix.getValue().ships, playerMatrix.getValue().GetList());
    }

    public void SetGameState(String gameState) {
        gameRepository.SetGameStep(gameState);
    }
}
