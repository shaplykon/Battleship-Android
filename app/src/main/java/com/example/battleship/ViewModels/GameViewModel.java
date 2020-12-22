package com.example.battleship.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.battleship.Models.Cell;
import com.example.battleship.Models.Game;
import com.example.battleship.Models.Matrix;
import com.example.battleship.Models.User;
import com.example.battleship.Repositories.FirebaseCallback;
import com.example.battleship.Repositories.GameRepository;
import com.google.firebase.database.DataSnapshot;


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
}
